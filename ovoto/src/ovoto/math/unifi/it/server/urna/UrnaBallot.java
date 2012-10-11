package ovoto.math.unifi.it.server.urna;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Unindexed;


@Unindexed
public class UrnaBallot {

	public enum Status { 
		SETUP_DONE,
		TOKENS_GENERATED
	}
	
	
	@Id
	String id;
	String publicId;
	long numOfChoices;
	ArrayList<String> labels;
	Date startDate;
	Date endDate;
	Status status;
	String ballotText; //titolo da mettere sulla form
	
	protected UrnaBallot() {}

	
	
	public UrnaBallot(String id, String publicId, long numOfChoices,
			ArrayList<String> labels, Date startDate, Date endDate, String ballotText) {
		super();
		this.id = id;
		this.publicId= publicId;
		this.numOfChoices = numOfChoices;
		this.labels = labels;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = Status.SETUP_DONE;
		this.ballotText = ballotText;
	}


	public void setStatus(Status s) {
		this.status = s;
	}



	public Status getStatus() {
		return status;
	}



	public ArrayList<String> getLabels() {
		return labels;
	}



	public String getPublicId() {
		return publicId;
	}



	public long getNumOfChoices() {
		return numOfChoices;
	}



	public String getBallotText() {
		return ballotText;
	}

	
	
}
