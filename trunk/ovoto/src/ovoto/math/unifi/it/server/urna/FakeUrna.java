package ovoto.math.unifi.it.server.urna;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class FakeUrna extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	@Override
	public void init() throws ServletException {
		super.init();
		ObjectifyService.register(UrnaToken.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)	throws IOException {


//		String response="";
//		BufferedReader reader =	new BufferedReader(new InputStreamReader(req.getInputStream()));
//		String line;
//		while ((line = reader.readLine()) != null) {
//			response += line;
//		}
//		reader.close();
//		System.err.println(response);
//		
		
		for (Enumeration<String>e = req.getParameterNames(); e.hasMoreElements();) {
			String pname = e.nextElement();
			String pvalue = req.getParameter(pname);
			System.err.println(pname + " = " + pvalue);
		}
		
		//The ballot reference number
		String bid=UUID.randomUUID().toString();
		resp.getWriter().print(bid);
		resp.setStatus(HttpServletResponse.SC_OK);
		
	}


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {



		String pi = req.getPathInfo();


		if(pi != null) {
			if( "/vote".equals(pi)) {
				letVoterVote(req,resp);
				return;
			} else if("/registerVote".equals(pi)) {
				registerVote(req,resp);				
				return;
			}

			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"unknown pathinfo");
			return;
		}
		resp.setContentType("text/javascript; charset=UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		ServletOutputStream os = resp.getOutputStream();



		//num of tokens
		String n_s = req.getParameter("num");
		String callback = req.getParameter("callback");
		String ballotId = req.getParameter("ballotId");


		if(n_s == null || callback == null || ballotId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"mancano parametri");
			return;
		}

		int n; 
		try {
			n = Integer.parseInt(n_s);
		} catch(Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"numtokens unparsable");
			return;
		}



		//genera i token


		Vector<String> tokens = new Vector<String>();

		for(int i=0;i<n;i++) {
			tokens.add(generateToken());
		}

		// store the tokens
		storeTokens(tokens, ballotId);


		String tokList =  jsonToken(tokens.get(0));
		for(int i=1;i<n;i++) {
			tokList += "," + jsonToken(tokens.get(i));
		}

		String response=callback+"({\"version\":\"1.0\",\"encoding\":\"UTF-8\",\"tokenList\":[" + tokList + "]});";

		System.err.println(response);


		os.println(response);
		os.flush();



		resp.flushBuffer();



	}


	private void registerVote(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		UrnaToken theToken = getTokenFromRequestPArams(req);

		if(theToken == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"invalid data");
			return;	
		}

		if(theToken.isFree()) {
			System.err.println(req.getQueryString());

		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Token already used ....");
		}


		String v1 = req.getParameter("v1");
		String v2 = req.getParameter("v2");

		theToken.setUsed();

		if(v1 == null) 
			if(v2==null) 
				theToken.setVote("");
			else
				theToken.setVote(v2);
		else 
			if(v2==null) 
				theToken.setVote(v1);
			else
				theToken.setVote(v1 + "," + v2);

		store(theToken);	

		resp.getWriter().println("Voto registrato correttmanete: " + theToken.getVote());
		return;

	}


	private void store(UrnaToken theToken) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(theToken);
	}


	private void letVoterVote(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//bisogna recuparare il token e vedere se e' valido

		UrnaToken theToken = getTokenFromRequestPArams(req);

		if(theToken == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"invalid data");
			return;	
		}

		if(theToken.isFree()) {
			//resp.getWriter().print("TokenValid");
			generateForm(req,resp);
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Token already used ....");
		}

	}



	private void generateForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// questo punto siamo sicuri che ci sono ...
		String token = req.getParameter("token");	
		String ballotId = req.getParameter("ballotId");

		StringBuffer me = req.getRequestURL();
		//che schifezza
		me.delete(me.lastIndexOf("/vote"),me.length());

		String storeDest = me + "/registerVote" + "?" + req.getQueryString();

		PrintWriter p = resp.getWriter();


		p.println("<html>");
		p.println("<body>");
		p.println("<form method=\"GET\" action=\""+ storeDest +"\">");
		p.println("<input type=\"checkbox\" name=\"v1\" value=\"one\"/>One<br/>");
		p.println("<input type=\"checkbox\" name=\"v2\" value=\"two\"/>Two</br>");

		p.println("<input type=\"text\" name=\"ballotId\" value=\""+ballotId+"\"/></br>");
		p.println("<input type=\"text\" name=\"token\" value=\""+token+"\"/></br>");
		p.println("<input type=\"submit\" name=\"submit\" value=\"submit\"/>");

		p.println("</form>");
		p.println("</body>");
		p.println("</html>");


	}


	private UrnaToken getTokenFromRequestPArams(HttpServletRequest req) {
		String token = req.getParameter("token");	
		String ballotId = req.getParameter("ballotId");

		if(token == null || ballotId == null) {
			//resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"mancano parametri essenziali");
			return null;
		}


		Objectify ofy = ObjectifyService.begin();

		Query<UrnaToken> l = ofy.query(UrnaToken.class).filter("token", token);

		UrnaToken theToken = null;

		for( UrnaToken ut : l) 
			if(ballotId.equals(ut.getBallotId())) {
				theToken = ut;
				break;
			} else {
				System.out.println(ballotId +","+ ut.getBallotId());
			}

		return theToken;

	}


	private String generateToken() {
		return UUID.randomUUID().toString() ;
	}

	private String jsonToken(String token) {
		return "{\"token\":" + "\"" + token + "\"}";
	}


	private void storeTokens(Vector<String> tokens, String ballotId) {
		Objectify ofy = ObjectifyService.begin();
		for(String t: tokens) 
			ofy.put(new UrnaToken(ballotId, t));
	}



}
