package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Vector;

import ovoto.math.unifi.it.shared.Ballot;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

public interface BallotServiceAsync {

	void getBallot(Key<Ballot> key, AsyncCallback<Ballot> callback);

	void listBallots(AsyncCallback<ArrayList<Ballot>> callback);

	void storeTokens(Ballot ballot, Vector<String> scrambled,
			AsyncCallback<Ballot> callback);

	void writeBallot(Ballot b, AsyncCallback<Long> callback);

	void setupService(Ballot b, AsyncCallback<Ballot> callback);

	void sendEmails(Ballot ballot, String subj, String body, String baseUrl, AsyncCallback<Ballot> callback);

}
