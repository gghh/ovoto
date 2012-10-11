package ovoto.math.unifi.it.server.voter;

import java.util.ArrayList;
import java.util.Date;

import ovoto.math.unifi.it.client.voter.InvalidCredentialsException;
import ovoto.math.unifi.it.client.voter.VotingService;
import ovoto.math.unifi.it.server.ProfileUtils;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;
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
public class VotingServiceImpl extends RemoteServiceServlet implements VotingService {



	public void init() {		
		ObjectifyService.register(Utente.class);
		ObjectifyService.register(VotingToken.class);
		ObjectifyService.register(Ballot.class);
	}


	@Override
	public VotingData loadVotingData(String id, String code) throws InvalidCredentialsException {
		Utente  u = ProfileUtils.getProfile( id,  code);
		VotingData vd = new VotingData(u);
		
		ArrayList<VotingToken> l = loadTokens(u.getId());
		
		for(VotingToken v : l)
			vd.addToken(getBallotById(v.getBallotId()), v);
		return vd;
	}

	
	private Ballot getBallotById(Long ballotId) {
		Objectify ofy = ObjectifyService.begin();
		Key<Ballot> key = new Key<Ballot>(Ballot.class,ballotId);
		
		return ofy.get(key);
	}


	private  ArrayList<VotingToken> loadTokens(String id) {
		
		Objectify ofy = ObjectifyService.begin();
		
		Query<VotingToken> l = ofy.query(VotingToken.class).filter("voterId", id);

	
		ArrayList<VotingToken> lt = new ArrayList<VotingToken>();
		for( VotingToken vt : l) 
			lt.add(vt);

		return lt;
				
		
	}


	@Override
	public void updateProfile(Utente u, String code) throws InvalidCredentialsException {

		Utente su = ProfileUtils.getProfile(u.getId(), code);

		//si cambiano solo le cose che possono essere cambbiate dall'utente
		su.setEmail(u.getEmail());
		su.setAffiliazione(u.getAffiliazione());
		su.setIndirizzo(u.getIndirizzo());
		su.setTitolo(u.getTitolo());
		su.setUrl(u.getUrl());
		su.setData_modifica(new Date());

		ProfileUtils.writeProfile(su);
	}

	@Override
	public void changeCode(String url, String id,String code) throws InvalidCredentialsException {
		 ProfileUtils.changeCodeAndNotifyUserAuthenticated(url,id, code);
	}

	
	@Override
	public void sendCredentials(String url, String id,String code) throws InvalidCredentialsException {
		 ProfileUtils.reNotifyCredentialsUserAuthenticated(url,id, code);
	}



}
