package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Vector;

import ovoto.math.unifi.it.shared.Ballot;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("admin/ballots")
public interface BallotService extends RemoteService {
	
	Long writeBallot(Ballot b);
	Ballot getBallot(Key<Ballot> key);
	ArrayList<Ballot> listBallots();
	Ballot setupService(Ballot b) throws BallotServiceCommunicationErrorException;
	Ballot storeTokens(Ballot ballot, Vector<String> scrambled);
	Ballot sendEmails(Ballot ballot, String subj, String body) throws MailSendingException;
}
