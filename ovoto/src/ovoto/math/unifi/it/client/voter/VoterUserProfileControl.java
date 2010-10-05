package ovoto.math.unifi.it.client.voter;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.client.UserProfileForm;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class VoterUserProfileControl implements UserProfileControl {

	private final VotingServiceAsync votingService = GWT.create(VotingService.class);

	private String code;
	public VoterUserProfileControl(String code) {
		this.code = code;
	}
	
	@Override
	public void changeCode(Utente utente) {
		
		String url = GWT.getHostPageBaseURL();
				
		votingService.changeCode(url,utente.getId(),code, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Ovoto.getUi().setMessage("ChangeCode DONE");	
			}

			@Override
			public void onFailure(Throwable caught) {
				Ovoto.getUi().setErrorMessage("ahi ahi ahi " + caught.getMessage());
			}
		});	 
	}

	
	
	
	@Override
	public void sendCredentials(Utente utente) {
		
		String url = GWT.getHostPageBaseURL();
				
		votingService.sendCredentials(url,utente.getId(),code, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Ovoto.getUi().setMessage("SendCred DONE");	
			}

			@Override
			public void onFailure(Throwable caught) {
				Ovoto.getUi().setErrorMessage("ahi ahi ahi " + caught.getMessage());
			}
		});	 
	}
	
	

	@Override
	public void store(final Utente utente) {

		Ovoto.getUi().setMessage("Saving entry....",true);


		votingService.updateProfile(utente, code, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				//msg.setText("progress...");
				Ovoto.getUi().setMessage("Saving DONE (id:"+result+")");

				loadSavedProfile(utente.getId());
			}

			@Override
			public void onFailure(Throwable caught) {
				Ovoto.getUi().setErrorMessage("ahi ahi ahi " + caught.getMessage());
			}
		});

	}

	
	private void loadSavedProfile(String id) {
		votingService.loadVotingData(id,code, new AsyncCallback<VotingData>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error: " + caught.getMessage());
			}
			@Override
			public void onSuccess(VotingData result) {
				
				VotingData vd = new VotingData(result.getProfile());
				
				UserProfileForm a = new UserProfileForm(vd,VoterUserProfileControl.this);
				Ovoto.getUi().setContent(a);
				//			a.show();
				//			a.center();
			}
		});
	}
	

}
