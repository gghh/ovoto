package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("utente")
public interface UserService extends RemoteService {
	String writeUser(Utente utente) throws IllegalArgumentException;
}
