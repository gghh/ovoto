package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

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
	void changeCodeAndNotify(String url, Utente u);
	void sendCredentials(String url, Utente u);
	ArrayList<Utente> listUtenti(ArrayList<String> list);
}
