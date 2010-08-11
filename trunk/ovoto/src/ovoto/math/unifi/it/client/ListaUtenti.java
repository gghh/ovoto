package ovoto.math.unifi.it.client;

import java.util.ArrayList;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.objectify.Key;

public class ListaUtenti extends PopupPanel {
	private final UserServiceAsync userService =
		GWT.create(UserService.class);

	private VerticalPanel vp = new VerticalPanel();

	public ListaUtenti () {
		setAutoHideEnabled(false);
		setAnimationEnabled(true);
		setGlassEnabled(true);

		Button closePanel = new Button("Chiudi");
		closePanel.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		vp.add(closePanel);
		
		
		userService.listUtenti(new AsyncCallback<ArrayList<Key<Utente>>>() {

			@Override
			public void onSuccess(ArrayList<Key<Utente>> result) {
				insertList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());	
			}
		});
		
		
		
		add(vp);
		
	}

	private void insertList(ArrayList<Key<Utente>> result) {
		for(Key<Utente> a: result) {
			vp.add(new Label(a.toString()));
		}
	}
}
