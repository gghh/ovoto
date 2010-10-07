package ovoto.math.unifi.it.client.admin;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.client.UserProfileForm;
import ovoto.math.unifi.it.client.admin.ListaUtenti.ProfileRowProvider;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

public class LoadProfileRowProvider implements ProfileRowProvider {

	
	UserProfileControl  upc = new AdminUserProfileControl(); //the only one available so it is not needed to pass
	
	
	//inner class
	class GetUserAnchor extends Anchor implements ClickHandler, AsyncCallback<Utente> {
		private Utente u;
		public GetUserAnchor(Utente u) {
			this.u = u;	

			setText(u.getNome() + " " + u.getCognome() + " " + u.getEmail());
			addClickHandler(this);
		}
		@Override
		public void onClick(ClickEvent event) {
			upc.load(u.getId(), this);
		}
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("error: " + caught.getMessage());
		}
		@Override
		public void onSuccess(Utente result) {
			VotingData vd = new VotingData(result);
			UserProfileForm a = new UserProfileForm(vd,upc);
			Ovoto.getUi().setContent(a);
			//			a.show();
			//			a.center();
		}
	}


	@Override
	public Widget getUserRow(Utente u) {
		return new UserAnchor(u);
	}
	
	
	class UserAnchor extends Anchor implements ClickHandler, AsyncCallback<Utente> {
		private Utente u;
		public UserAnchor(Utente u) {
			this.u = u;	

			setText(u.getNome() + " " + u.getCognome() + " " + u.getEmail());
			addClickHandler(this);
		}
		@Override
		public void onClick(ClickEvent event) {
			upc.load(u.getId(), this);
		}
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("error: " + caught.getMessage());
		}
		@Override
		public void onSuccess(Utente result) {
			VotingData vd = new VotingData(result);
			UserProfileForm a = new UserProfileForm(vd,upc);
			Ovoto.getUi().setContent(a);
			//			a.show();
			//			a.center();
		}
	}


}
