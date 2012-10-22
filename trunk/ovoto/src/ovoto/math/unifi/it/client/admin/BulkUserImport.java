package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Arrays;

import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.shared.Utente;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
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
				
				ArrayList<Utente> users = new ArrayList<Utente>();
				for(String l:lines) {
					String flds[]  = l.split(",");
					
					Utente u = new Utente();
					
					u.setCognome(flds[0]);
					u.setNome(flds[1]);
					u.setEmail(flds[2]);
					//XXX
					//should validate email address
					users.add(u);
					
//					if(Window.confirm("Save: " + u.getNome() + " " + u.getCognome() + " email: " + u.getEmail() + " ?")) {
//						cntrl.store(u);
//						Ovoto.getUi().setMessage("Saved" + u.getCognome());
//					} else {
//						Ovoto.getUi().setMessage("Skipped" + u.getCognome());
//					}	
				}
				
				cntrl.store(users);
				
			}
		});







		vp = new VerticalPanel();

		vp.add(ia);
		vp.add(save);
		//vp.add(msg);

		add(vp);
	}

}


