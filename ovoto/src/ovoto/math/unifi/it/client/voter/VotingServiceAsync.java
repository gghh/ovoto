package ovoto.math.unifi.it.client.voter;

import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VotingServiceAsync {
	
	void loadVotingData(String id, String code,	AsyncCallback<VotingData> callback);

	void updateProfile(Utente u, String code, AsyncCallback<Void> callback);

	void changeCode(String url, String id, String code, AsyncCallback<Void> callback);

	void sendCredentials(String url, String id, String code, AsyncCallback<Void> callback);

}
