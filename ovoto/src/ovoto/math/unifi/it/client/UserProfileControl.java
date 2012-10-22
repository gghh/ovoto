package ovoto.math.unifi.it.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ovoto.math.unifi.it.shared.Utente;

public interface UserProfileControl {

	void changeCode(Utente utente);
	void store(Utente utente);
	void sendCredentials(Utente utente);
	void load(String id, AsyncCallback<Utente> c);
	void store(List<Utente> utenti);

}
