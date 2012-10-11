package ovoto.math.unifi.it.server.admin;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Id;

import org.antlr.stringtemplate.StringTemplate;

import ovoto.math.unifi.it.server.ProfileUtils;
import ovoto.math.unifi.it.shared.Utente;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Unindexed;


@Unindexed
public class EmailsSequence {
	@Id Long sequenceId;
	ArrayList<String> toBeDone;
	ArrayList<String> done;
	ArrayList<Date> timestamps;

	String messageTemplate;
	String messageSubject;
	String baseUrl;
	String ballotId;

	String endOfLifeMessage;

	public  EmailsSequence() {}

	public EmailsSequence(ArrayList<String> ids, String subj, String template, String baseUrl, String ballotId) {
		toBeDone = ids;
		endOfLifeMessage = "created";
		this.messageTemplate = template;
		this.messageSubject = subj;
		this.baseUrl = baseUrl;
		this.ballotId = ballotId;
	}

	public String getNext() {
		String elem =  toBeDone.get(0);
		endOfLifeMessage = "returned " + elem;
		return elem;
	}

	public boolean hasNext() {
		return toBeDone.size() != 0;
	}

	public void done(String elem) {
		//controlla che sia l'ultimo
		String m_elem = getNext();
		assert(m_elem.equals(elem));

		if(done == null)
			done = new ArrayList<String>();
		if(timestamps == null) 
			timestamps = new ArrayList<Date>();

		toBeDone.remove(0);
		done.add(m_elem);
		timestamps.add(new Date());		
		endOfLifeMessage = "done " + elem;
	}

	public void setStatusMessage(String s) {
		endOfLifeMessage = s;
	}
	public String getStatusMessage() {
		return endOfLifeMessage;
	}

	
	public boolean sendOneEmail() throws Exception {
		if(!hasNext()) {
			//errore ... no nci dovrebbe essere arrivato
			//TODO
			return false;
		} else {
			String elem = getNext();
			Key<Utente> k = new Key<Utente>(Utente.class,elem);
			Utente u = ProfileUtils.getProfileByKey(k);

			String cred = ProfileUtils.credentialsUrl(baseUrl, u);
			String dirLink = cred + "&ballotId="+ ballotId;
			String fullName = ProfileUtils.getFullName(u);
			String qualifiedName = ProfileUtils.getQualifiedName(u);

			StringTemplate st = new StringTemplate(messageTemplate);
			st.setAttribute("credentials", cred );
			st.setAttribute("directlink", dirLink );
			st.setAttribute("fullname", fullName );
			st.setAttribute("qualifiedname", qualifiedName );

			String mailbody = st.toString();
			try {
				ProfileUtils.sendEmail(u, messageSubject, mailbody);					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatusMessage("Died: " + e.getMessage());
				throw e;
			}
			//st.reset();
			done(elem);
			return hasNext();
		}
	}
	
	//ritorna true se ha altre email da spedire
	public boolean sendEmail(int atMost) throws Exception {
		for(int i=0; i<atMost; i++) {
			if(!sendOneEmail()) 
				return false;
		}
		return hasNext();
	}

	public  ArrayList<String> getDone() {
		return done;
	}
	public  ArrayList<Date> getTimestamps() {
		return timestamps;
	}
	public  ArrayList<String> getToBeDone() {
		return toBeDone;
	}
	

}