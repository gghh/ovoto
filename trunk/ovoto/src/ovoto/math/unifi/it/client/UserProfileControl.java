package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.shared.Utente;

public interface UserProfileControl {

	void changeCode(Utente utente);
	void store(Utente utente);
	void sendCredentials(Utente utente);

}
