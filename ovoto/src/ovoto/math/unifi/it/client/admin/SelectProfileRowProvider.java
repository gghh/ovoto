package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SelectProfileRowProvider implements ProfileRowProvider {


	private final static UserProfileControl  upc = new AdminUserProfileControl(); //the only one available so it is not needed to pass

	//a list of ckeckboxes 
	ArrayList<SelectableUserRow> entries = new ArrayList<SelectableUserRow>();

	ArrayList<String> already_selected_entries = new ArrayList<String>();
	
	
	//inner class
	class SelectableUserRow extends HorizontalPanel implements ClickHandler, AsyncCallback<Utente> {
		private Utente u;
		//private Anchor a;
		private CheckBox c;
		public SelectableUserRow(Utente u, boolean checked) {
			this.u = u;	
			//a = new Anchor();
			c = new CheckBox(u.getNome() + " " + u.getCognome() + " " + u.getEmail());
			c.setValue(checked);
			//addClickHandler(this);
			this.add(c);
			entries.add(this);
		}

		public boolean isSelected() {
			return c.getValue();
		}

		public Utente getPRofile() {
			return u;
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
		return new SelectableUserRow(u,already_selected_entries.contains(u.getId()));
	}


	public SelectProfileRowProvider(ArrayList<String> already_selected_voters) {
		this.already_selected_entries = already_selected_voters;
	}



	public ArrayList<String> getSelectedList() {
		ArrayList<String> a = new  ArrayList<String>(); 

		for(SelectableUserRow e : entries) {
			if(e.isSelected())
				a.add(e.getPRofile().getId());
		}

		return a;
	}

}
