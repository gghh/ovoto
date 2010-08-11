package ovoto.math.unifi.it.server;

import java.util.UUID;

import ovoto.math.unifi.it.client.GreetingService;
import ovoto.math.unifi.it.client.UserService;
import ovoto.math.unifi.it.shared.FieldVerifier;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
UserService {

	public String writeUser(Utente utente) throws IllegalArgumentException {
		// Verify that the input is valid. 

		//static ?
		ObjectifyService.register(Utente.class);

		utente.setId(UUID.randomUUID().toString());

		Objectify ofy = ObjectifyService.begin();
		ofy.put(utente);

		System.err.println(utente);
		
		return utente.getId();
	}
}
