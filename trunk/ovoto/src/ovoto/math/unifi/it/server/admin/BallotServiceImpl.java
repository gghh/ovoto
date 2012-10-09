package ovoto.math.unifi.it.server.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import ovoto.math.unifi.it.client.admin.BallotService;
import ovoto.math.unifi.it.client.admin.BallotServiceCommunicationErrorException;
import ovoto.math.unifi.it.client.admin.MailSendingException;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Ballot.Status;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingToken;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BallotServiceImpl extends RemoteServiceServlet implements BallotService {



	public void init() {
		ObjectifyService.register(Utente.class);
		ObjectifyService.register(Ballot.class);
		ObjectifyService.register(VotingToken.class);
		ObjectifyService.register(EmailsSequence.class);
		
	}


	///Votazioni
	@Override
	public Long writeBallot(Ballot b) {
		return BallotUtils.writeBallot(b);

	}



	@Override
	public Ballot getBallot(Key<Ballot> key) {
		Objectify ofy = ObjectifyService.begin();
		Ballot b = ofy.get(key);
		return b;

	}

	@Override
	public ArrayList<Ballot> listBallots() {
		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();

		Query<Ballot> all = ofy.query(Ballot.class);

		ArrayList<Ballot> lb = new ArrayList<Ballot>();

		for( Ballot b : all) 
			lb.add(b);

		return lb;

	}

	@Override
	public Ballot storeTokens(Ballot ballot, Vector<String> scrambled) {

		Objectify ofy = ObjectifyService.begin();

		assert(ballot.getVoters().size() == scrambled.size());

		//ArrayList<Utente> voters = ProfileUtils.listProfiles(ballot.getVoters());

		//una mischiatina in questi cas non fa mai male !!!
		Collections.shuffle(scrambled);
		
		
		int i=0;
		for(String uId: ballot.getVoters()) {
			VotingToken vt = new VotingToken(scrambled.get(i++), ballot.getStartDate(), ballot.getEndDate(), ballot.getBallotId(), uId);
			ofy.put(vt);
		}

		ballot.setStatus(Status.FINALIZED);
		BallotUtils.writeBallot(ballot);

		return ballot;
	}


	@Override
	//setup the service on the remote end
	//and store the serviceAccessId to be used in future communicatiosn with the service
	public Ballot setupService(Ballot b)  throws BallotServiceCommunicationErrorException {


		b = BallotUtils.setupBallot(b);


		return b;

	}


	
	
		
	@Override
	public Ballot sendEmails(Ballot ballot, String subj, String body, String baseUrl) throws MailSendingException {

		
		//VA FATTA UNA TASKQUEUE !!!
		//
		// ALLA QUALE PASSARE la lista di voters e che ne spedisce 5 e poi chiama la queue successiva
		// crea la squenza die email da scrivere e la mette sul db.
		
		
		EmailsSequence e = new EmailsSequence(ballot.getVoters(),subj,body,baseUrl,ballot.getBallotId().toString());
		Objectify ofy = ObjectifyService.begin();
		Key<EmailsSequence> k = ofy.put(e);
		
		QueueFactory.getDefaultQueue().add(
				TaskOptions.Builder.withUrl("/workers/sendEmails")
				.param("emailSequence",Long.toString(k.getId()).getBytes())
				); 
		

		
		return ballot;
	}





}
