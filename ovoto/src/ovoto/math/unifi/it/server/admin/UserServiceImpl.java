package ovoto.math.unifi.it.server.admin;

import java.util.ArrayList;
import java.util.Map;

import ovoto.math.unifi.it.client.admin.UserService;
import ovoto.math.unifi.it.server.ProfileUtils;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingToken;

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
		ObjectifyService.register(Ballot.class);
		ObjectifyService.register(VotingToken.class);
	}

	public String writeUser(Utente utente) throws IllegalArgumentException {

		// Verify that the input is valid. 
		//static ?

		String nome = utente.getNome();
		for(int i=0; i< 100 ;i++) {

			//if(utente.getId() == null || "".equals(utente.getId())) {
				String id  = ProfileUtils.getFreshId();
				utente.setId(id);
				//System.err.println("aLV:" + i);
				utente.setNome(nome + ":" + i);
				//we create the code too
				String code = ProfileUtils.getFreshCode();
				String pw = ProfileUtils.codeExternal2Internal(code);
				utente.setCode(pw);
			//}
			ProfileUtils.writeProfile(utente);
		}

		return utente.getId();
	}

	@Override
	public ArrayList<Utente> listUtenti() {
		return ProfileUtils.listProfiles();
	}

	//only the specified users
	@Override
	public ArrayList<Utente> listUtenti(ArrayList<String> list) {

		Objectify ofy = ObjectifyService.begin();

		Map<String, Utente> all = ofy.get(Utente.class,list);

		ArrayList<Utente> lu = new ArrayList<Utente>();

		for( Utente u : all.values()) {
			lu.add(u);
		}
		return lu;
	}


	public ArrayList<Key<Utente>> getUtentiKeys() {

		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();
		Iterable<Key<Utente>> allKeys = ofy.query(Utente.class).fetchKeys();

		ArrayList<Key<Utente>> lu = new ArrayList<Key<Utente>>();
		for( Key<Utente> u : allKeys) 
			lu.add(u);

		return lu;
	}


	@Override
	public Utente getUtente(Key<Utente> key) {
		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();
		Utente u = ofy.get(key);
		return u;
	}


	@Override
	public void changeCodeAndNotify(String url,Utente u) {
		ProfileUtils.changeCodeAndNotifyAdminAuthenticated(url,u);
	}

	@Override
	public void sendCredentials(String url,Utente u) {
		//the given Utente has not all the fields ready
		//so it is better to load from datastore
		Key<Utente> key = new Key<Utente>(Utente.class,u.getId());
		Utente su = getUtente(key);
		ProfileUtils.reNotifyCredentialsAdminAuthenticated(url, su);
	}


}
