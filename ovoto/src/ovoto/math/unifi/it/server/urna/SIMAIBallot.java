package ovoto.math.unifi.it.server.urna;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.antlr.stringtemplate.StringTemplate;

import ovoto.math.unifi.it.server.urna.UrnaBallot.Status;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class SIMAIBallot extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	private static final String confermaVotoRegistratoTemplate = "ConfermaVotoRegistrato_SIMAIBallot.st";
	private static final String schedaElettoraleTemplate = "SchedaElettorale_SIMAIBallot.st";







	@Override
	public void init() throws ServletException {
		super.init();
		ObjectifyService.register(UrnaToken.class);
		ObjectifyService.register(UrnaBallot.class);
	}


	public class ParametersMismatchException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ParametersMismatchException(String msg) {
			super(msg);
		}

	}

	public String getStringParamOrFail(String param, HttpServletRequest req) throws ParametersMismatchException {
		String val = req.getParameter(param);
		if(val == null)
			throw new ParametersMismatchException("manca parametro");
		return val;
	}


	public String getStringParamOrDefault(String param, String defl,HttpServletRequest req) throws ParametersMismatchException {
		String val = req.getParameter(param);
		if(val == null)
			return defl;
		return val;
	}


	public long getLongParamOrFail(String param, HttpServletRequest req) throws ParametersMismatchException {
		String t = getStringParamOrFail(param, req);
		try {
			long val = Long.parseLong(t);
			return val;
		} catch(Exception e) {
			throw new ParametersMismatchException("parameter parsing problem");
		}
	}

	public Date getDateParamOrFail(String param, HttpServletRequest req) throws ParametersMismatchException {
		long num = getLongParamOrFail(param, req);
		try {
			Date d=new Date(num);
			return d;
		} catch(Exception e) {
			throw new ParametersMismatchException("parameter parsing problem");
		}
	}


	//a list of numbered entries, the firs must be present
	public ArrayList<String> getListOfStringParamOrFail(String tag, HttpServletRequest req) throws ParametersMismatchException {
		ArrayList<String> ls = new ArrayList<String>();
		String l = getStringParamOrFail(tag + "0", req);
		ls.add(l);
		try   {
			for(int i=1;;i++) {
				l = getStringParamOrFail(tag + i, req);
				ls.add(l);
			}
		} catch(ParametersMismatchException pms) {
			return ls;
		} catch(Exception e) {
			throw new ParametersMismatchException(e.getMessage());
		}
	}



	//returns param names
	@SuppressWarnings("unused")
	private ArrayList<Integer> getListOfCheckedParamUntil(String tag,	HttpServletRequest req, int size) {
		ArrayList<Integer> ls = new ArrayList<Integer>();

		for(int i=0;i<size;i++) {
			try {
				String name=tag + i;
				String l = getStringParamOrFail(name, req);
				if(l != null)
					ls.add(i);
			} catch(ParametersMismatchException pme) {
				//does nothing
			}
		}
		return ls;
	}




	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		doPost(req, resp);
	}


	//POST:mode=SETUP: get accessToken,labels,numOfChoices,id, startDate, endDate & return accessId
	//GET: mode=ACTIVATE: get accessToken,accessId,numTokens & return Tokens (in number of numTokens, as JsonArray)
	//GET: mode=VOTE: get token & let user vote
	//GET: mode=REGISTERVOTE: get token & register vote
	//GET: mode=STATUS get accessToken & OK 

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			String mode = getStringParamOrFail("mode", req);
			if("".equals(mode)) {
				//listBallots etc ...
				//TODO
				return;
			} else if("VOTE".equals(mode)) {
				doVoteMethod(req, resp);
				return;
			} else if("REGISTERVOTE".equals(mode)) {
				doRegisterVoteMethod(req, resp);
				return;
			} 

			//tutti gli altri sono autenticati
			String accessToken = getStringParamOrFail("accessToken",req);
			if( !"pippo11".equals(accessToken.trim()))
				throw new ParametersMismatchException("token mismatch");

			if("STATUS".equals(mode)) {
				doStatusMethod(req,resp);	
			} if("SETUP".equals(mode)) {
				doSetupMethod(req,resp);
			} else if("ACTIVATE".equals(mode)) {
				doActivateMethod(req,resp);
			} else if("SPOIL".equals(mode)) {
				doSpoilMethod(req,resp);
			}

		} catch( ParametersMismatchException pme) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,pme.getMessage());
			return;
		} 






	}



	private void doSpoilMethod(HttpServletRequest req, HttpServletResponse resp) throws ParametersMismatchException, IOException {
		//carica il ballot.
		String accessId = getStringParamOrFail("accessId",req);

		HashMap<String,Integer> sums = new HashMap<String, Integer>();

		try {
			UrnaBallot ub = getUrnaBallot(accessId);

			ArrayList<UrnaToken> votes = loadVotesByBallodAccessId(accessId);

			resp.setContentType("text/html");
			resp.getWriter().println("<html>");
			resp.getWriter().println("<body>");
			resp.getWriter().println("<h2>"+ub.getBallotText()+"</h2>");

			resp.getWriter().println("<ol>");
			for(UrnaToken ut: votes) {
				String vote = ut.getVote();
				resp.getWriter().println("<li>" + vote+ "</li>");
				if(vote != null && !ut.isFree() && !"".equals(vote)) {
					System.err.println(vote);
			
//					String[] parts = vote.split(";");
//					String cleanVote = parts[1];
//					String[] choices = cleanVote.split("\n");

//					for(int i=0;i< choices.length;i++) {
						Integer e = sums.get(vote);		
						if(e == null) {
							sums.put(vote, 1);
						} else {
							sums.put(vote, e+1);
						}
					}
//				}

			}
			resp.getWriter().println("</ol>");

			resp.getWriter().println("<ul>");

			for(Map.Entry<String,Integer> e : sums.entrySet()) {
				resp.getWriter().println("<li>" + e.getKey() + ": " + e.getValue() + "</li>");
			}

			resp.getWriter().println("</ul>");


			resp.getWriter().println("</body>");
			resp.getWriter().println("</html>");
		} catch(NotFoundException e) {
			throw new ParametersMismatchException("Invalid Access ID");
		}


	}

	private ArrayList<UrnaToken> loadVotesByBallodAccessId(String accessId) {

		Objectify ofy = ObjectifyService.begin();
		Query<UrnaToken> l = ofy.query(UrnaToken.class).filter("ballotId", accessId);

		ArrayList<UrnaToken> utl = new ArrayList<UrnaToken>();

		for(UrnaToken ut : l) 
			utl.add(ut);

		return utl;



	}

	private void doStatusMethod(HttpServletRequest req, HttpServletResponse resp) throws ParametersMismatchException, IOException {
		resp.setContentType("text/html");
		resp.getWriter().print("OK");
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void doSetupMethod(HttpServletRequest req, HttpServletResponse resp) throws ParametersMismatchException, IOException {
		String id = getStringParamOrFail("id",req);
		long numOfChoices = getLongParamOrFail("numOfChoices", req);
		Date startDate = getDateParamOrFail("startDate", req);
		Date endDate = getDateParamOrFail("endDate", req);

		String ballotText = getStringParamOrFail("ballotText",req);

		ArrayList<String> labels = getListOfStringParamOrFail("label", req);

		//crea il ballot, gli assegna un id, salba il ballot, ritorna l'id
		String bid=UUID.randomUUID().toString();

		UrnaBallot ub = new UrnaBallot(bid,id,numOfChoices,labels,startDate,endDate,ballotText);

		store(ub);

		resp.setContentType("text/html");
		resp.getWriter().print(bid);
		resp.setStatus(HttpServletResponse.SC_OK);
	}





	private void doActivateMethod(HttpServletRequest req,	HttpServletResponse resp) throws ParametersMismatchException, IOException {

		String accessId = getStringParamOrFail("accessId",req);
		long numTokens = getLongParamOrFail("numTokens",req);
		String callback = getStringParamOrFail("callback",req);


		//apre il ballot con key accessid

		try {

			UrnaBallot ub = getUrnaBallot(accessId); 

			if(ub.getStatus() != Status.SETUP_DONE) 
				throw new ParametersMismatchException("wrong status");

			//genera i token
			Vector<String> tokens = new Vector<String>();
			for(int i=0;i<numTokens;i++) {
				tokens.add(generateToken());
			}

			// store the tokens
			storeTokens(tokens, accessId,ub.getPublicId(),ub.startDate,ub.endDate);

			ub.setStatus(Status.SETUP_DONE);

			store(ub);

			resp.setContentType("text/javascript; charset=UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);
			ServletOutputStream os = resp.getOutputStream();


			String tokList =  jsonToken(tokens.get(0));
			for(int i=1;i<numTokens;i++) {
				tokList += "," + jsonToken(tokens.get(i));
			}

			String response=callback+"({\"version\":\"1.0\",\"encoding\":\"UTF-8\",\"tokenList\":[" + tokList + "]});";

			os.println(response);
			os.flush();

			resp.flushBuffer();


		} catch( NotFoundException nfe) {
			throw new ParametersMismatchException("parameters mismatch");
		}


	}



	private void doVoteMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParametersMismatchException {

		//bisogna recuparare il token e vedere se e' valido

		UrnaToken theToken = getTokenFromRequestParamAndVerify(req);

		generateForm(theToken, resp, req);

	}





	private UrnaBallot getUrnaBallot(String accessId) {
		Key<UrnaBallot> k = new Key<UrnaBallot>(UrnaBallot.class,accessId);
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(k);
	}

	private void store(UrnaToken theToken) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(theToken);
	}

	private void store(UrnaBallot ub) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(ub);
	}


	private String generateToken() {
		return UUID.randomUUID().toString() ;
	}

	private String jsonToken(String token) {
		return "{\"token\":" + "\"" + token + "\"}";
	}

	private void storeTokens(Vector<String> tokens, String ballotId, String publicBallotId, Date validFrom, Date validUntil) {
		Objectify ofy = ObjectifyService.begin();
		for(String t: tokens) 
			ofy.put(new UrnaToken(ballotId,publicBallotId, t, validFrom,validUntil));
	}



	private UrnaToken getTokenFromRequestParamAndVerify(HttpServletRequest req) throws ParametersMismatchException {

		String token = getStringParamOrFail("token",req);	
		String ballotId = getStringParamOrFail("ballotId",req);

		Objectify ofy = ObjectifyService.begin();
		Query<UrnaToken> l = ofy.query(UrnaToken.class).filter("token", token);
		UrnaToken theToken = null;

		for( UrnaToken ut : l) {
			//System.err.println(ut.ballotId + " " + ut.getTokenText());

			if(ballotId.equals(ut.getPublicBallotId())) {
				theToken = ut;
				break;
			} else {
				//System.out.println(ballotId +","+ ut.getBallotId());
			}
		}


		if(theToken != null) {
			if(theToken.isFree()) { 
				Date now = new Date();
				if(now.after(theToken.getvalidFrom()) && now.before(theToken.getvalidUntil())) 
					return theToken;
				else 
					throw new ParametersMismatchException("Ballot is open from: " + theToken.getvalidFrom() + " to: " + theToken.getvalidUntil());
			} else 
				throw new ParametersMismatchException("Token already USED");
		}
		throw new ParametersMismatchException("Invalid Token");


	}




	private void doRegisterVoteMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParametersMismatchException {



		UrnaToken token = getTokenFromRequestParamAndVerify(req);

		UrnaBallot ub = getUrnaBallot(token.getBallotId());

		String vote = "";


		//		ArrayList<Integer> choices = getListOfCheckedParamUntil("v", req, ub.getLabels().size());
		//		if(choices.size() > ub.getNumOfChoices()) {
		//			throw new ParametersMismatchException("No more than " + ub.getNumOfChoices() + " choices are allowed.");
		//		}
		//
		//		String textVote = "";
		//		for(int v: choices) {
		//			vote+= ":" + v +":";
		//			textVote += ub.getLabels().get(v) + "\n";		
		//		}

		vote = getStringParamOrDefault("choices","SCHEDA BIANCA",req);
		token.setVote(vote);
		token.setUsed();
		store(token);	


		String s = readFileAsString(confermaVotoRegistratoTemplate);
		StringTemplate st = new StringTemplate(s);
		st.setAttribute("voto", vote );
		st.setAttribute("title", ub.getBallotText() );



		//resp.getWriter().println("Voto registrato correttmanete.");
		//resp.getWriter().println("<pre>");
		//resp.getWriter().println(textVote);
		//resp.getWriter().println("</pre>");

		resp.getWriter().println(st.toString());
		return;

	}






	/** @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be better, and buffer sizes are hardcoded
	 */ 
	private static String readFileAsString(String filePath) throws java.io.IOException{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}




	public class MyLabel {
		private String id;
		private String key;
		private String value;

		public MyLabel(String id, String key, String value) {
			this.id = id;
			this.key = key;
			this.value = value;
		}


		public String getId() {
			return id;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	private void generateForm(UrnaToken token, HttpServletResponse resp, HttpServletRequest req) throws IOException {


		StringBuffer me = req.getRequestURL();

		PrintWriter p = resp.getWriter();

		UrnaBallot ub = getUrnaBallot(token.getBallotId());


		String s = readFileAsString(schedaElettoraleTemplate);
		StringTemplate st = new StringTemplate(s);

		st.setAttribute("num_labels", ub.getLabels().size() );
		st.setAttribute("num_choices", ub.getNumOfChoices() );
		st.setAttribute("title", ub.getBallotText() );
		st.setAttribute("action", me);

		st.setAttribute("ballot_public_id", ub.getPublicId());
		st.setAttribute("token_text", token.getTokenText());


		ArrayList<MyLabel> labels = new ArrayList<SIMAIBallot.MyLabel>();

		int i=0;
		for(String l : ub.getLabels()) {
			String id = "v" + i;
			String key = l;
			String value = l;
			labels.add(new MyLabel(id, key, value));
			i++;
		}

		st.setAttribute("labels", labels);

		//String mailbody = st.toString();


		//		p.println("<html>");
		//
		//
		//		p.println("<script type=\"text/javascript\">");
		//		p.println("function get_check_value()");
		//		p.println("{");
		//		p.println("var c_value = \"\"; var count =0;");
		//		p.println("for (var i=0; i < "+ub.getLabels().size()+"; i++)");
		//		p.println("   { var e = document.vote['v'+i]");
		//		p.println("   if (e.checked)");
		//		p.println("      {count++; if(count >"+ub.getNumOfChoices()+") { alert('Invalid selection: \\nyou cannot select mote than "+ub.getNumOfChoices()+" items.'); return false;}");
		//		p.println("      c_value = c_value + e.value + \"\\n\";");
		//		p.println("      }");
		//		p.println("   }");
		//		p.println(" return confirm('Please confirm your choices:\\n' +c_value);");
		//		p.println("}");
		//		p.println("</script>");
		//
		//
		//		p.println("<body>");
		//
		//
		//
		//		p.println("<h3>"+ ub.getBallotText()+"</h3>");
		//
		//		p.println("<form name=\"vote\" method=\"POST\" action=\""+ me +"\">");
		//
		//		int i=0;
		//		p.println("<ol>");
		//		for(String l : ub.getLabels()) {
		//			p.println("<li><input type=\"checkbox\" name=\"v"+i+"\" value=\""+l+"\"/>"+l+"</li>");
		//			i++;
		//		}
		//		p.println("</ol>");
		//
		//		p.println("<input type=\"submit\"  onClick='return get_check_value();' name=\"submit\" value=\"submit\"/>");
		//
		//		//p.println("<input type=\"button\" onClick='javascript:get_check_value();'/>");
		//
		//		p.println("<input type=\"hidden\" name=\"ballotId\" value=\""+ub.getPublicId()+"\"/></br>");
		//		p.println("<input type=\"hidden\" name=\"token\" value=\""+token.getTokenText()+"\"/></br>");
		//		p.println("<input type=\"hidden\" name=\"mode\" value=\"REGISTERVOTE\"/></br>");
		//
		//		p.println("</form>");
		//		p.println("</body>");
		//		p.println("</html>");


		p.print(st.toString());


	}





}
