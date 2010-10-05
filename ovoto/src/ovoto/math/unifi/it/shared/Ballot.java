package ovoto.math.unifi.it.shared;

import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Ballot implements IsSerializable{

	@Id Long ballotId;
	private String ballotText;
	private String ballotUrl; //descriptive page
	private String votingUrl;//where to vote !!
	private String generatorUrl;//service used to generate tokens
	
	private Date date_creation;
	private Date date_activation;
	
	
	public Ballot() {}


	public String getBallotText() {
		return ballotText;
	}


	public void setBallotText(String ballotText) {
		this.ballotText = ballotText;
	}


	public String getBallotUrl() {
		return ballotUrl;
	}


	public void setBallotUrl(String ballotUrl) {
		this.ballotUrl = ballotUrl;
	}


	public String getVotingUrl() {
		return votingUrl;
	}


	public void setVotingUrl(String votingUrl) {
		this.votingUrl = votingUrl;
	}


	public String getGeneratorUrl() {
		return generatorUrl;
	}


	public void setGeneratorUrl(String generatorUrl) {
		this.generatorUrl = generatorUrl;
	}


	public Date getDate_creation() {
		return date_creation;
	}


	public void setDate_creation(Date dateCreation) {
		date_creation = dateCreation;
	}


	public Date getDate_activation() {
		return date_activation;
	}


	public void setDate_activation(Date dateActivation) {
		date_activation = dateActivation;
	}


	public Long getBallotId() {
		
		return ballotId;
	}
	
	
	

}
