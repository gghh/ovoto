package ovoto.math.unifi.it.server.urna;

import java.util.Date;

import javax.persistence.Id;

class UrnaToken {
	
	@Id Long Id;
	String ballotId;
	String publicBallotId;
	String token;
	boolean used;
	String vote;
	Date validFrom;
	Date validUntil;
	
	protected UrnaToken() {}
	
	public UrnaToken(String ballotId,String publicBallotId, String token, Date validFrom, Date validUntil) {
		this.ballotId = ballotId;
		this.publicBallotId = publicBallotId;
		this.token=token;
		this.used=false;
		this.vote="";
		this.validFrom = validFrom;
		this.validUntil = validUntil;
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

	public Date getvalidFrom() {
		return validFrom;
	}

	public Date getvalidUntil() {
		return validUntil;
	}

	public String getTokenText() {
		return token;
	}

	public String getPublicBallotId() {
		return publicBallotId;
	}


}
