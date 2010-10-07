package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListaUtenti extends VerticalPanel {
	
	public interface ProfileRowProvider {
		Widget getUserRow(Utente u);
	}
	
	
	private final UserServiceAsync userService =	GWT.create(UserService.class);

	private ProfileRowProvider prp;
	

	public ListaUtenti (ProfileRowProvider prp) {
		this.prp=prp;

		userService.listUtenti(new AsyncCallback<ArrayList<Utente>>() {

			@Override
			public void onSuccess(ArrayList<Utente> result) {
				insertList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail:" + caught.getMessage());	
			}
		});

	}
	

	
	//when the list is available.
	public ListaUtenti(ProfileRowProvider prp, ArrayList<String> list) {
		this.prp = prp;
		
		userService.listUtenti(list,new AsyncCallback<ArrayList<Utente>>() {

			@Override
			public void onSuccess(ArrayList<Utente> result) {
				insertList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail:" + caught.getMessage());	
			}
		});
		
	}


	private void insertList(ArrayList<Utente> result) {
		for(Utente u: result) {
			add( prp.getUserRow(u) );
		}
	}

	





}
