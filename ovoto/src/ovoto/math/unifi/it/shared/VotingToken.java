package ovoto.math.unifi.it.shared;

import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;



@Unindexed
public class VotingToken implements IsSerializable{

	//representation of a token 
	//used by the voter to ... vote !!
	@Id Long tokenId;
	private String tokenText; 
	private Date validFrom;
	private Date validUntil;
	private Long ballotId;
	
	@Indexed private String voterId;
	

	protected VotingToken() {}
	
	public VotingToken(String tokenText, Date validFrom, Date validUntil, Long ballotId, String voterId) {
		super();
		this.tokenText = tokenText;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.ballotId = ballotId;
		this.setVoterId(voterId);
	}
	
	
	public String getTokenText() {
		return tokenText;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public Long  getBallotId() {
		return ballotId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public String getVoterId() {
		return voterId;
	}

		
}