package ovoto.math.unifi.it.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;


public class VotingData implements IsSerializable{
	
	private Utente profile;
	//Ballot vs VotingToken
	private HashMap<Ballot,VotingToken> tokens;
	

	protected VotingData() {}
	
	public VotingData(Utente profile) {
		this.profile=profile;
		this.tokens  = new HashMap<Ballot, VotingToken>();
	}

	public Utente getProfile() {
		return profile;
	}

	public HashMap<Ballot,VotingToken> getTokens() {
		return tokens;
	}
	
	public void addToken(Ballot el, VotingToken tok) {
		tokens.put(el,tok);
	}
	
	
}
