package ovoto.math.unifi.it.server.urna;

import javax.persistence.Id;

class UrnaToken {
	
	
	@Id Long Id;
	String ballotId;
	String token;
	boolean used;
	String vote;
	
	
	protected UrnaToken() {}
	
	public UrnaToken(String ballotId, String token) {
		this.ballotId = ballotId;
		this.token=token;
		this.used=false;
		this.vote="";
	}

	public void setUsed() {
		this.used=true;
	}

	public String getBallotId() {
		return ballotId;
	}

	public boolean isFree() {
		return !used;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	

}
