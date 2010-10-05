package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Vector;

import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface UserServiceAsync {
	void writeUser(Utente input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void listUtenti(AsyncCallback<ArrayList<Utente>> callback);
	void getUtente(Key<Utente> key, AsyncCallback<Utente> callback);
	void writeBallot(Ballot b, AsyncCallback<Long> asyncCallback);
	void getBallot(Key<Ballot> key, AsyncCallback<Ballot> asyncCallback);
	void listBallots(AsyncCallback<ArrayList<Ballot>> asyncCallback);
	void storeTokens(Ballot ballot, Vector<String> scrambled, AsyncCallback<Ballot> asyncCallback);
	void changeCodeAndNotify(String url, Utente u, AsyncCallback<Void> callback);
	void sendCredentials(String url, Utente u, AsyncCallback<Void> callback);
}
