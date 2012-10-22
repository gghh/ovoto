package ovoto.math.unifi.it.server.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ovoto.math.unifi.it.server.Util;
import ovoto.math.unifi.it.shared.Ballot;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class GenerateSetupForm extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void init() throws ServletException {
		super.init();
		ObjectifyService.register(Ballot.class);
	}


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		//legge il ballotid
		String val = req.getParameter("ballotId");
		if(val == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Manca Parametro");
			return;
		}

		try {
			long bid = Long.valueOf(val);
			Key<Ballot> key = new Key<Ballot>(Ballot.class,bid);
		Objectify ofy = ObjectifyService.begin();
		Ballot b = ofy.get(key);
		
		PrintWriter p = resp.getWriter();
		
		resp.setContentType("text/html");
		p.println("<html><body>");
		p.println("<h3>after submit <br>you MUST copy the resulting token into access Id and SAVE</h3>");
		
		p.println("<form method='POST' action='" + b.getServiceUrl() + "'>");
			
		p.println("<ul>");
		p.println("<li>mode <input type='text' name='mode' value='SETUP'/>");
		p.println("<li>id <input type='text' name='id' value='"+ b.getBallotId() + "'/>");
		p.println("<li>token <input type='text' name='accessToken' value='" + b.getServiceAccessToken() + "'/>");
		p.println("<li>num <input type='text' name='numOfChoices' value='" + b.getNumOfChoices() + "'/>");
		p.println("<li>start <input type='text' name='startDate' value='" + b.getStartDate().getTime() + "'/>");
		p.println("<li>end <input type='text' name='endDate' value='" + b.getEndDate().getTime() + "'/>");
		p.println("<li>txt <input type='text' name='ballotText' value='" + Util.xmlSingleQuotedEscape(b.getBallotText()) + "'/>");

		int i = 0;
		for(String l : b.getLabels() ) {
			p.println("<li>label<input type='text' name='label"+ i++ +"' value='" + Util.xmlSingleQuotedEscape(l) +"'/>");
		}
				
		
		p.println("</ul>");
		
		p.println("<input type='submit' name='submit' value='submit'/>");

		p.println("</form>");
		p.println("</body></html>");
	
		
		
		} catch(Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Errato Parametro");
			return;
		}

	}


}
