package ovoto.math.unifi.it.server;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailHandlerServlet extends HttpServlet { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)  throws IOException { 
		Properties props = new Properties(); 
		Session session = Session.getDefaultInstance(props, null); 
		try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			
			System.err.println("Received EMAIL");
			System.err.println("From: "+ message.getFrom()[0] + " To:" + message.getRecipients(RecipientType.TO)[0]);
			System.err.println("Subject: " + message.getSubject());
			Object content = message.getContent();  
			if (content instanceof String) {  
			    String body = (String)content;  
				System.err.println("----");
				System.err.println(body);
				System.err.println("----");
			}  else if (content instanceof Multipart ){
				 Multipart mp = (Multipart)content; 
				 BodyPart bp = mp.getBodyPart(0);
				 
				 if( bp != null) {
					 Object bpc = bp.getContent();
					 if(bpc instanceof String) {
						    String body = (String)bpc;  
							System.err.println("----");
							System.err.println(body);
							System.err.println("----");
					 } else
						 System.err.println("message structure too complex: spam ?");				
				 }
				 
			} else {
				System.err.println("multipart message: spam ?");				
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}