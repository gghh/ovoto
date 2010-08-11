package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserProfileForm extends PopupPanel {
	private final UserServiceAsync userService =
		GWT.create(UserService.class);
	
	private TextBox nome = new TextBox();
	private TextBox cognome = new TextBox();
	private Label msg = new Label();
	
	public UserProfileForm() {
		setAutoHideEnabled(false);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		Button invio = new Button("Invio");
		invio.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				store();
			}
		});
		
		Button closePanel = new Button("Chiudi");
		closePanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
				
		HorizontalPanel hpNome = new HorizontalPanel();
		hpNome.add(new Label("Nome"));
		hpNome.add(nome);

		HorizontalPanel hpCognome = new HorizontalPanel();		
		hpCognome.add(new Label("Cognome"));
		hpCognome.add(cognome);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(hpNome);
		vp.add(hpCognome);
		vp.add(invio);
		vp.add(msg);
		vp.add(closePanel);
		
		add(vp);
	}

	private void store() {
		Utente utente = new Utente();
		utente.setNome(nome.getText());
		utente.setCognome(cognome.getText());
		
		msg.setText("fruzziching...");
		
		userService.writeUser(utente, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				hide();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				msg.setText("ahi ahi ahi " + caught.getMessage());
			}
		});
		
	}

}
