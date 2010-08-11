package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface UserServiceAsync {
	void writeUser(Utente input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
