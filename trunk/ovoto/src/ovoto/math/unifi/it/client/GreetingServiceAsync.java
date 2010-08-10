package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(Utente input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
