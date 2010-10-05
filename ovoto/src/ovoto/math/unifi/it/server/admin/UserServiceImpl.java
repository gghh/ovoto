package ovoto.math.unifi.it.server.admin;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import ovoto.math.unifi.it.client.admin.UserService;
import ovoto.math.unifi.it.server.ProfileUtils;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingToken;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

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
		if(utente.getId() == null || "".equals(utente.getId()))
			utente.setId(UUID.randomUUID().toString());

		ProfileUtils.writeProfile(utente);

		return utente.getId();
	}

	@Override
	public ArrayList<Utente> listUtenti() {

		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();

		Query<Utente> all = ofy.query(Utente.class);

		ArrayList<Utente> lu = new ArrayList<Utente>();

		for( Utente u : all) 
			lu.add(u);

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


	///Votazioni
	@Override
	public Long writeBallot(Ballot b) {

		//TODO Verify that the input is valid. 
		Objectify ofy = ObjectifyService.begin();
		ofy.put(b);
		return b.getBallotId();
	}





	@Override
	public Ballot getBallot(Key<Ballot> key) {
		Objectify ofy = ObjectifyService.begin();
		Ballot b = ofy.get(key);
		return b;

	}

	@Override
	public ArrayList<Ballot> listBallots() {
		// You can query for just keys, which will return Key objects much more efficiently than fetching whole objects
		Objectify ofy = ObjectifyService.begin();

		Query<Ballot> all = ofy.query(Ballot.class);

		ArrayList<Ballot> lb = new ArrayList<Ballot>();

		for( Ballot b : all) 
			lb.add(b);

		return lb;

	}

	@Override
	public Ballot storeTokens(Ballot ballot, Vector<String> scrambled) {
		ArrayList<Utente> l = listUtenti();

		Objectify ofy = ObjectifyService.begin();

		//c;e; da controllare che le iste siano della stessa lunghezza :O
		int i=0;
		for(Utente u: l) {
			VotingToken vt = new VotingToken(scrambled.get(i++), null, null, ballot.getBallotId(), u.getId());
			ofy.put(vt);
		}

		return ballot;
	}




}
