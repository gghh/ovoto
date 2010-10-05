package ovoto.math.unifi.it.server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ovoto.math.unifi.it.client.voter.InvalidCredentialsException;
import ovoto.math.unifi.it.shared.Utente;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ProfileUtils {



	public static void writeProfile(Utente utente) throws IllegalArgumentException {	
		Objectify ofy = ObjectifyService.begin();
		ofy.put(utente);
	}


	//resend method can work just id the following is invertible
	private static String codeExternal2Internal(String code) throws NoSuchAlgorithmException {
		//		MessageDigest  digest = java.security.MessageDigest.getInstance("MD5");
		//
		//		digest.update(code.getBytes());
		//
		//		String  pw = new String(digest.digest());
		//		return pw;
		return code;
	}

	private static String  codeInternal2External(String pw) {
		return pw;
	}



	//intended for administrators
	public static void reNotifyCredentialsAdminAuthenticated(String url,Utente su)  {
		String code = codeInternal2External(su.getCode());
		try {
			notifyCodeToUser(url,su,code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//intended for voters
	public static void reNotifyCredentialsUserAuthenticated(String url,String id, String code) throws InvalidCredentialsException {
		Utente su = getProfile(id, code);
		reNotifyCredentialsAdminAuthenticated(url,su);
	}


	public static Utente getProfile(String id, String code) throws InvalidCredentialsException {
		Key<Utente>  key = new Key<Utente>(Utente.class, id);

		Objectify ofy = ObjectifyService.begin();
		Utente u = ofy.get(key);

		if(u == null) 
			throw new InvalidCredentialsException("Invalid user");

		try {
			String pw = codeExternal2Internal(code);

			//System.err.println(pw);
			//System.err.println(u.getCode());

			if(! pw.equals(u.getCode())) 
				throw new InvalidCredentialsException("Invalid code");

			return u;

		} catch (NoSuchAlgorithmException e) {
			throw new InvalidCredentialsException("Internal Error");
		}
	}



	//intended for administrators
	public static void changeCodeAndNotifyAdminAuthenticated(String url,Utente su)  {



		//rigenera la pw
		String new_code = UUID.randomUUID().toString();

		try {
			String pw = codeExternal2Internal(new_code);
			su.setCode(pw);
			//e la spedice all'email
			writeProfile(su);

			try {
				notifyCodeToUser(url,su,new_code);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}


	public static void changeCodeAndNotifyUserAuthenticated(String url,String id, String code) throws InvalidCredentialsException {
		Utente su = getProfile(id, code);
		changeCodeAndNotifyAdminAuthenticated(url,su);
	}

	private static void notifyCodeToUser(String url,Utente u, String code) throws UnsupportedEncodingException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);


		String msgBody = url + "?user=" + u.getId() + "&code=" + code;

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("ovoto.anagrafe@gmail.com", "O'voto Administrator"));
		msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(u.getEmail(), u.getTitolo() + " " + u.getNome() + " " + u.getCognome()));
		msg.setSubject("Voting Account");
		msg.setText(msgBody);
		Transport.send(msg);


		System.err.println(msgBody);

	}




}
