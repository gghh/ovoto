package ovoto.math.unifi.it.server.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ovoto.math.unifi.it.shared.Utente;

import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class EmailsWorker extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void init() throws ServletException {
		super.init();
		ObjectifyService.register(Utente.class);
		ObjectifyService.register(EmailsSequence.class);		
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		String seqId = req.getParameter("emailSequence");

		if(seqId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"mancano parametri");
			return;
		} else {
			try {
				long id = Long.parseLong(seqId);
				Objectify ofy = ObjectifyService.begin();
				EmailsSequence es = ofy.get(new Key<EmailsSequence>(EmailsSequence.class,id));

				PrintWriter pw = resp.getWriter();
				pw.println("<html>");
				pw.println("<head>");
				pw.println("<title>"+es.getStatusMessage()+"</title>");
				pw.println("</head>");
				pw.println("<body>");
				pw.println("<h2>"+es.getStatusMessage()+"</h2>");


				ArrayList<Date> timestamps = es.getTimestamps();
				ArrayList<String> done = es.getDone();
				ArrayList<String> toBeDone = es.getToBeDone();


				pw.println("<ol>");	
				pw.println("<h3>Done Jobs:</h2>");

				if(done != null) {

					assert(done.size() == timestamps.size());

					for(int i = done.size()-1; i>=0; i--) {
						pw.println("<li>" + done.get(i)+ " DONE at " + timestamps.get(i).toString() + "</li>");
					}
					pw.println("</ol>");
				}


				if(toBeDone != null) {
					pw.println("<ol>");	
					pw.println("<h3>Remaining Jobs:</h2>");
					for(int i = toBeDone.size()-1; i>=0; i--) {
						pw.println("<li>" + done.get(i)+"</li>");
					}
					pw.println("</ol>");
				}

				pw.println("</body>");
				pw.println("</html>");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {


		String seqId = req.getParameter("emailSequence");

		if(seqId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"mancano parametri");
			System.err.println("mancano parametri");
			return;
		} else {
			System.err.println("Chiamato:" + seqId);
		}

		try {
			long id = Long.parseLong(seqId);
			Objectify ofy = ObjectifyService.begin();
			EmailsSequence es = ofy.get(new Key<EmailsSequence>(EmailsSequence.class,id));

			boolean cont = es.sendEmail(3);

			if(cont) { 
				ofy.put(es);
				QueueFactory.getDefaultQueue().add(
						TaskOptions.Builder.url("/workers/sendEmails")
						.param("emailSequence",seqId.getBytes()).countdownMillis(35000)
				); 
			} else { 
				es.setStatusMessage("ended successfully: " + (new Date()));
				ofy.put(es);
			}

		} catch(Exception e) {	
			e.printStackTrace();
		}




	}


}
