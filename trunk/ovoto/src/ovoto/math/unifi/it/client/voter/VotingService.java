package ovoto.math.unifi.it.client.voter;

import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("voting")
public interface VotingService extends RemoteService {
	public VotingData loadVotingData(String id, String code) throws InvalidCredentialsException;
	public void updateProfile(Utente u, String code) throws InvalidCredentialsException;
	void changeCode(String url, String id, String code) throws InvalidCredentialsException;
	void sendCredentials(String url, String id, String code)	throws InvalidCredentialsException;
}
