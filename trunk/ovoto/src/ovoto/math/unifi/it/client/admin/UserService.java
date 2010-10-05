package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Vector;

import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("admin/users")
public interface UserService extends RemoteService {
	String writeUser(Utente utente) throws IllegalArgumentException;
	ArrayList<Utente> listUtenti();
	Utente getUtente(Key<Utente> key);
	Long writeBallot(Ballot b);
	Ballot getBallot(Key<Ballot> key);
	ArrayList<Ballot> listBallots();
	Ballot storeTokens(Ballot ballot, Vector<String> scrambled);
	void changeCodeAndNotify(String url, Utente u);
	void sendCredentials(String url, Utente u);
}
