package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

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
	void changeCodeAndNotify(String url, Utente u, AsyncCallback<Void> callback);
	void sendCredentials(String url, Utente u, AsyncCallback<Void> callback);
	void listUtenti(ArrayList<String> list,
			AsyncCallback<ArrayList<Utente>> asyncCallback);
	void sendEmail_TEMPORARY(String from, String to,
			AsyncCallback<Void> callback);
}
