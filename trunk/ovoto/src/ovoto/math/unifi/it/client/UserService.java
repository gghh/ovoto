package ovoto.math.unifi.it.client;

import java.util.ArrayList;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("utente")
public interface UserService extends RemoteService {
	String writeUser(Utente utente) throws IllegalArgumentException;
	ArrayList<Key<Utente>> listUtenti();
}
