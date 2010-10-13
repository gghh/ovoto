package ovoto.math.unifi.it.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;

import ovoto.math.unifi.it.client.voter.InvalidCredentialsException;
import ovoto.math.unifi.it.shared.Utente;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ProfileUtils {



	public static void writeProfile(Utente utente) throws IllegalArgumentException {	
		Objectify ofy = ObjectifyService.begin();
		ofy.put(utente);
	}


	//resend method can work just id the following is invertible
	public static String codeExternal2Internal(String code) /*throws NoSuchAlgorithmException*/ {
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

		//try {
		String pw = codeExternal2Internal(code);

		//System.err.println(pw);
		//System.err.println(u.getCode());

		if(! pw.equals(u.getCode())) 
			throw new InvalidCredentialsException("Invalid code");

		return u;

		//} catch (NoSuchAlgorithmException e) {
		//	throw new InvalidCredentialsException("Internal Error");
		//}
	}



	//intended for administrators
	public static void changeCodeAndNotifyAdminAuthenticated(String url,Utente su)  {



		//rigenera la pw
		String new_code = getFreshCode();
		//try {
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
		//} catch (NoSuchAlgorithmException e1) {
		//	// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}

	}


	public static void changeCodeAndNotifyUserAuthenticated(String url,String id, String code) throws InvalidCredentialsException {
		Utente su = getProfile(id, code);
		changeCodeAndNotifyAdminAuthenticated(url,su);
	}

	private static void notifyCodeToUser(String url,Utente u, String code) throws UnsupportedEncodingException, MessagingException {

		String msgBody = credentialsUrl(url,u,code);
		String msgSubj = "Voting Account";
		sendEmail(u,msgSubj,msgBody);

		System.err.println(msgBody);

	}




	public static String getFreshCode() {
		return  RandomStringUtils.randomAlphabetic(16);
	}

	public static String getFreshId() {
		return UUID.randomUUID().toString();
	}


	public static ArrayList<Utente> listProfiles() {

		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();

		Query<Utente> all = ofy.query(Utente.class);

		ArrayList<Utente> lu = new ArrayList<Utente>();

		for( Utente u : all) 
			lu.add(u);

		Collections.sort(lu, new Comparator<Utente>() {
			@Override
			public int compare(Utente o1, Utente o2) {
				String n1 = o1.getCognome()+o1.getNome();
				String n2 = o2.getCognome()+o2.getNome();
				return (String.CASE_INSENSITIVE_ORDER.compare(n1, n2)); 
			}
		});
		return lu;
	}


	public static ArrayList<Utente> listProfiles(ArrayList<String> list) {

		Objectify ofy = ObjectifyService.begin();

		Map<String, Utente> all = ofy.get(Utente.class,list);

		ArrayList<Utente> lu = new ArrayList<Utente>();

		for( Utente u : all.values()) {
			lu.add(u);
		}
		return lu;
	}



	public static String credentialsUrl(String baseUrl,Utente u, String code) {
		return  baseUrl + "?user=" + u.getId() + "&code=" + code;
	}

	public static String credentialsUrl(String baseUrl, Utente u) {
		return credentialsUrl(baseUrl, u, codeInternal2External(u.getCode()));
	}


	public static String getFullName(Utente u) {
		return u.getNome() + " " + u.getCognome();
	}

	public static String getQualifiedName(Utente u) {
		if(null == u.getTitolo())
			return getFullName(u);
		else
			return u.getTitolo() + " " +getFullName(u);
	}


	public static void sendEmail(Utente u, String subj, String mailbody) throws UnsupportedEncodingException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("ovoto.anagrafe@gmail.com", "Sistema di voto elettronico"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail(), getQualifiedName(u)));
		msg.setSubject(subj);
		msg.setText(mailbody);
		Transport.send(msg);

		System.err.println("sent email :" + mailbody);

	}


	public static Utente getProfileByKey(Key<Utente> k) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(k);
	}


}
