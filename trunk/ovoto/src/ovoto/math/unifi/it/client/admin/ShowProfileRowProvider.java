package ovoto.math.unifi.it.client.admin;

import ovoto.math.unifi.it.client.admin.ListaUtenti.ProfileRowProvider;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ShowProfileRowProvider implements ProfileRowProvider {


	
	//inner class
	class ShowMeUserRow extends HorizontalPanel implements ClickHandler {
		private Utente u;
		//private Anchor a;
		public ShowMeUserRow(Utente u) {
			this.u = u;	
			//a = new Anchor();
			Anchor a = new Anchor(u.getNome() + " " + u.getCognome() + " " + u.getEmail());		
			a.addClickHandler(this);
			this.add(a);
			
		}

		
		@Override
		public void onClick(ClickEvent event) {
			Window.alert("" + u);
		}
	}

	


	@Override
	public Widget getUserRow(Utente u) {
		return new ShowMeUserRow(u);
	}

}
