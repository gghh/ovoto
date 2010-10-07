package ovoto.math.unifi.it.shared;

import java.util.ArrayList;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Ballot implements IsSerializable{

	public enum Status {
		FINALIZED, //actually read only, tokens assigned to users
		READY, //ready to be initialized, BALLOT created into the service, descriptive fields are writable
		TO_BE_CONTINUED //not yet created on remote services.
	}
	
	
	@Id Long ballotId;
		
	
	private String ballotText;
	private Link ballotUrl;  //descriptive page
	private Link serviceUrl; //the external voting service
	
	private Status status = Status.TO_BE_CONTINUED;
	
	
	private String serviceAccessToken = ""; //has to be sent to the service to create the ballot (a password)
	private String serviceAccessId = ""; //has to be sent to the service to generate tokens it is sent by the service upon creation
	
	
	
	//private HashMap<String,String> emailsToUsers;
	//private HashMap<Date,String> events;

	//voters, per id
	private ArrayList<String> voters = new ArrayList<String>();
	
	
	private ArrayList<String> labels = new ArrayList<String>(); //le possibili scelte da votare
	private int numOfChoices =0; //how many choices per voter
	
	
	
	
	
	protected Ballot() {}

	public Ballot(String ballotText, Link ballotUrl, Link serviceUrl, String accessToken) {
		this.ballotText = ballotText;
		this.setBallotUrl(ballotUrl);
		this.setServiceUrl(serviceUrl);
		this.setServiceAccessToken(accessToken);
	}
	
	
	public Long getBallotId() {
		return ballotId;
	}

	
	public String getBallotText() {
		return ballotText;
	}

	public void setBallotText(String ballotText) {
		this.ballotText = ballotText;
	}

	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setBallotUrl(Link ballotUrl) {
		this.ballotUrl = ballotUrl;
	}

	public Link getBallotUrl() {
		return ballotUrl;
	}

	public void setServiceUrl(Link serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Link getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceAccessToken(String serviceAccessToken) {
		this.serviceAccessToken = serviceAccessToken;
	}

	public String getServiceAccessToken() {
		return serviceAccessToken;
	}

	public void setServiceAccessId(String serviceAccessId) {
		this.serviceAccessId = serviceAccessId;
	}

	public String getServiceAccessId() {
		return serviceAccessId;
	}

	public void setVoters(ArrayList<String> voters) {
		this.voters = voters;
	}

	public ArrayList<String> getVoters() {
		return voters;
	}

	
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public void setNumOfChoices(int numOfChoices) {
		this.numOfChoices = numOfChoices;
	}

	public int getNumOfChoices() {
		return numOfChoices;
	}


}
