package ovoto.math.unifi.it.client;

import ovoto.math.unifi.it.client.admin.AdminUserProfileControl;
import ovoto.math.unifi.it.client.admin.BallotControl;
import ovoto.math.unifi.it.client.admin.BallotForm;
import ovoto.math.unifi.it.client.admin.ListaUtenti;
import ovoto.math.unifi.it.client.admin.ListaVotazioni;
import ovoto.math.unifi.it.client.admin.LoadProfileRowProvider;
import ovoto.math.unifi.it.client.voter.VoterUserProfileControl;
import ovoto.math.unifi.it.client.voter.VotingService;
import ovoto.math.unifi.it.client.voter.VotingServiceAsync;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.appengine.api.users.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ovoto implements EntryPoint {

	private final AuthenticationServiceAsync authenticationService =	GWT.create(AuthenticationService.class);

	private final VotingServiceAsync votingService =	GWT.create(VotingService.class);


	//ignobile ma funzionale !!!
	public static Ui getUi() {
		return ui;
	}

	private static Ui ui = new Ui();


	private String code;
	
	public void onModuleLoad() {

		//setup basic app layout
		RootLayoutPanel.get().add(ui);

		
				

		//entrypoint for voters
		String id = Window.Location.getParameter("user");
		if(id != null) {
			code = Window.Location.getParameter("code");
			if(code == null)
				code = Window.prompt("Password Required: ","");

			votingService.loadVotingData(id, code, new AsyncCallback<VotingData>() {

				@Override
				public void onSuccess(VotingData result) {
					codeOk(result, code);
				}

				@Override
				public void onFailure(Throwable caught) {	
					Window.alert("Error: " + caught.getMessage());
				}
			});

			return;
		}



		// check for authentiction
		authenticationService.getCurrentUser(Window.Location.getHref(),new AsyncCallback<User>() {

			@Override
			public void onSuccess(User user) {
				authOk(user);
			}
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof NotAuthenticatedException) 
					Window.Location.replace(((NotAuthenticatedException)caught).getAuthUrl());
				else if(caught instanceof NotAuthorizedException) {
					NotAuthorizedException e = (NotAuthorizedException)caught;
					VerticalPanel vp = new VerticalPanel();
					vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					vp.add(new Label(e.getMessage()));
					vp.add(new Anchor("reLogin",e.getLogoutUrl()));
					ui.setContent(vp);
				} else
					Window.alert(caught.getMessage());
			}
		});
	}


	
	private void authOk(User user) {

		//TODO
		// inserire AUTORIZZAZIONE !!!
		//tipo user.getEmail() == qualcosa

		
		
		//entrypoint for administration ...
		//[will] require googleAccounts auth 
		//RootPanel.get().add(vp);
	
		
		final UserProfileControl upc = new AdminUserProfileControl();
		final BallotControl bc = new BallotControl();


		Anchor createUser = new Anchor("Create User");

		createUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UserProfileForm a = new UserProfileForm(upc);
				ui.setContent(a);
				//				a.show();
				//				a.center();
			}
		});

		ui.addSideControl(createUser);



		Anchor listaUtenti = new Anchor("Lista utenti");

		listaUtenti.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ListaUtenti a = new ListaUtenti(new LoadProfileRowProvider());
				ui.setContent(a);
				//				a.show();
				//				a.center();
			}
		});
		ui.addSideControl(listaUtenti);


		
		
		Anchor creaVotazione = new Anchor("Crea Votazione");

		creaVotazione.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BallotForm a = new BallotForm(bc);
				ui.setContent(a);
			}
		});
		ui.addSideControl(creaVotazione);

		
		
		Anchor listaVotazioni = new Anchor("Lista Votazioni");

		listaVotazioni.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ListaVotazioni a = new ListaVotazioni(bc);
				ui.setContent(a);
				//				a.show();
				//				a.center();
			}
		});
		ui.addSideControl(listaVotazioni);

		
		

		Anchor logout = new Anchor("Logout (dev)");

		logout.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("http://127.0.0.1:8888/_ah/login?continue=http://127.0.0.1:8888/Ovoto.html?gwt.codesvr=127.0.0.1:9997");
			}
		});
		ui.addSideControl(logout);
	}


	//the voter can proceed.
	private void codeOk(VotingData vd, String code) {
		
		UserProfileControl upc = new VoterUserProfileControl(code);

		UserProfileForm a = new UserProfileForm(vd,upc);
		ui.setContent(a);
	
	}
	
	
	

}
