package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;
import ovoto.math.unifi.it.shared.VotingToken;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BulkUserImport extends SimplePanel {



	class ImportArea extends TextArea {
		public ImportArea() {
			setSize("400px", "300px");	
		}

		public ArrayList<String> getData() {
			String[] lines = getValue().split("\n");
			return new ArrayList<String>(Arrays.asList(lines));
		}
	}





	private Button save;


	VerticalPanel vp;


	private UserProfileControl cntrl;

	public BulkUserImport(UserProfileControl cntrl0) {
		this.cntrl = cntrl0;
		
		final ImportArea ia = new ImportArea();
		
		
		save= new Button("Save");
		save.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {

				
				ArrayList<String> lines = ia.getData();
				
				for(String l:lines) {
					String flds[]  = l.split(",");
					
					Utente u = new Utente();
					
					u.setCognome(flds[0]);
					u.setNome(flds[1]);
					u.setEmail(flds[2]);
					
					if(Window.confirm("Save: " + u.getNome() + " " + u.getCognome() + " email: " + u.getEmail() + " ?")) {
						cntrl.store(u);
						Ovoto.getUi().setMessage("Saved" + u.getCognome());
					} else {
						Ovoto.getUi().setMessage("Skipped" + u.getCognome());
					}	
				}

				//				//verify 
				//				if("".equals(nome.getText()) || "".equals(cognome.getText())) {
				//					Window.alert("Nome e cognome sono campi required");
				//					return;
				//				}
				//				if("".equals(email.getText()) ) {
				//					Window.alert("email e' campo required");
				//					return;
				//				}
				//
				//
				//
				//				Date d = new Date();
				//
				//				if(utente==null) {
				//					utente = new Utente();
				//					utente.setData_creazione(d); //now
				//					utente.setData_ultimo_accesso(new Date(0)); //never
				//				}
				//
				//				utente.setNome(nome.getText());
				//				utente.setCognome(cognome.getText());
				//				utente.setTitolo(titolo.getText());
				//				utente.setUrl(url.getText());
				//				utente.setEmail(email.getText());
				//				utente.setAffiliazione(affiliazione.getText());
				//				utente.setIndirizzo(indirizzo.getText());
				//
				//
				//
				//				utente.setData_modifica(d);  //now
				//
				//
				//				//msg.setText("progress...");
				//
				//
				//				cntrl.store(utente);
			}
		});







		vp = new VerticalPanel();

		vp.add(ia);
		vp.add(save);
		//vp.add(msg);

		add(vp);
	}

}


