package ovoto.math.unifi.it.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ovoto.math.unifi.it.client.UserService;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {


	
	public void init() {
		ObjectifyService.register(Utente.class);
	}
	
	public String writeUser(Utente utente) throws IllegalArgumentException {
		
		
		
		// Verify that the input is valid. 

		//static ?
	
		utente.setId(UUID.randomUUID().toString());

		Objectify ofy = ObjectifyService.begin();
		ofy.put(utente);

		System.err.println(utente);
		
		return utente.getId();
	}

	@Override
	public ArrayList<Key<Utente>> listUtenti() {
		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();
		Iterable<Key<Utente>> allKeys = ofy.query(Utente.class).fetchKeys();
		
		ArrayList<Key<Utente>> lu = new ArrayList<Key<Utente>>();
		for( Key<Utente> u : allKeys) 
			lu.add(u);
		
		return lu;
	}
}
