package ovoto.math.unifi.it.shared;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Ballot implements IsSerializable{

	public enum Status {
		FINALIZED, //actually read only, tokens assigned to users
		READY, //ready to be initialized, BALLOT created into the service, descriptive fields are writable
		TO_BE_CONTINUED //not yet created on remote services.
	}
	
	
	@Id Long ballotId;
		
	
	private String ballotText;
	private String ballotUrl;  //descriptive page
	private String serviceUrl; //the external voting service
	
	private Status status = Status.TO_BE_CONTINUED;
	
	
	private String serviceAccessToken = ""; //has to be sent to the service to create the ballot (a password)
	private String serviceAccessId = ""; //has to be sent to the service to generate tokens it is sent by the service upon creation
	
	
	private Date startDate, endDate;
	//private HashMap<String,String> emailsToUsers = new HashMap<String,String>();
	//private HashMap<Date,String> events;

	//voters, per id
	private ArrayList<String> voters = new ArrayList<String>();
	
	
	private ArrayList<String> labels = new ArrayList<String>(); //le possibili scelte da votare
	private int numOfChoices =0; //how many choices per voter
	
	
	
	
	
	protected Ballot() {}

	public Ballot(String ballotText, String ballotUrl, Date startDate, Date endDate, String serviceUrl, String accessToken) {
		this.ballotText = ballotText;
		this.setBallotUrl(ballotUrl);
		this.setStartDate(startDate);
		this.endDate  = endDate;
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

	public void setBallotUrl(String ballotUrl) {
		this.ballotUrl = ballotUrl;
	}

	public String getBallotUrl() {
		return ballotUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getServiceUrl() {
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

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setAccessId(String aid) {
		this.serviceAccessId = aid;
	}

//	public void setEmailsToUsers(HashMap<String,String> emailsToUsers) {
//		this.emailsToUsers = emailsToUsers;
//	}
//
//	public HashMap<String,String> getEmailsToUsers() {
//		return emailsToUsers;
//	}


}
