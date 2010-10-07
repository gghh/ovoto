package ovoto.math.unifi.it.client.admin;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.client.UserProfileForm;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

public class AdminUserProfileControl implements UserProfileControl {

	private final UserServiceAsync userService = GWT.create(UserService.class);

	
	@Override
	public void changeCode(Utente utente) {
		
		String url = GWT.getHostPageBaseURL();
				
		userService.changeCodeAndNotify(url,utente, new AsyncCallback<Void>() {

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
				
		userService.sendCredentials(url,utente, new AsyncCallback<Void>() {

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
	public void store(Utente utente) {

		Ovoto.getUi().setMessage("Saving entry....",true);


		userService.writeUser(utente, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				//msg.setText("progress...");
				Ovoto.getUi().setMessage("Saving DONE (id:"+result+")");

				loadSavedProfile(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Ovoto.getUi().setErrorMessage("ahi ahi ahi " + caught.getMessage());
			}
		});

	}

	
	private void loadSavedProfile(String id) {
		Key<Utente> key = new Key<Utente>(Utente.class, id);
		userService.getUtente(key, new AsyncCallback<Utente>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error: " + caught.getMessage());
			}
			@Override
			public void onSuccess(Utente result) {
				VotingData vd = new VotingData(result);
				
				UserProfileForm a = new UserProfileForm(vd,AdminUserProfileControl.this);
				Ovoto.getUi().setContent(a);
				//			a.show();
				//			a.center();
			}
		});
	}



	@Override
	public void load(String id, AsyncCallback<Utente> c) {
		Key<Utente> k = new Key<Utente>(Utente.class, id);
		userService.getUtente(k, c);	
	}
	

}
