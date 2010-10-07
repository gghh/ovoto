package ovoto.math.unifi.it.client;

import java.util.Date;
import java.util.Map;

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

public class UserProfileForm extends SimplePanel {



	private Button save;
	private TextBox nome = new TextBox();
	private TextBox cognome = new TextBox();
	private TextBox titolo = new TextBox();
	private TextArea affiliazione = new TextArea();
	private TextArea indirizzo = new TextArea();
	private TextBox url = new TextBox();

	private TextBox email = new TextBox();

	VerticalPanel vp;

	private Utente utente = null;

	private UserProfileControl cntrl;
	//private VotingData votingData;


	public UserProfileForm(UserProfileControl cntrl0) {
		this.cntrl = cntrl0;

		save= new Button("Save");
		//save.setEnabled(false);
		save.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {

				//verify 
				if("".equals(nome.getText()) || "".equals(cognome.getText())) {
					Window.alert("Nome e cognome sono campi required");
					return;
				}
				if("".equals(email.getText()) ) {
					Window.alert("email e' campo required");
					return;
				}



				Date d = new Date();

				if(utente==null) {
					utente = new Utente();
					utente.setData_creazione(d); //now
					utente.setData_ultimo_accesso(new Date(0)); //never
				}

				utente.setNome(nome.getText());
				utente.setCognome(cognome.getText());
				utente.setTitolo(titolo.getText());
				utente.setUrl(url.getText());
				utente.setEmail(email.getText());
				utente.setAffiliazione(affiliazione.getText());
				utente.setIndirizzo(indirizzo.getText());



				utente.setData_modifica(d);  //now


				//msg.setText("progress...");


				cntrl.store(utente);
			}
		});





		FlexTable ft = new FlexTable();
		int row =0;

		ft.setHTML(row, 0, "Nome");
		ft.setWidget(row++, 1, nome);

		ft.setHTML(row, 0, "Cognome");
		ft.setWidget(row++, 1, cognome);

		ft.setHTML(row, 0, "Titolo");
		ft.setWidget(row++, 1, titolo);

		ft.setHTML(row, 0, "URL");
		ft.setWidget(row++, 1, url);

		ft.setHTML(row, 0, "email");
		ft.setWidget(row++, 1, email);

		ft.setHTML(row, 0, "affiliazione");
		ft.setWidget(row++, 1, affiliazione);

		ft.setHTML(row, 0, "indirizzo");
		ft.setWidget(row++, 1, indirizzo);



		vp = new VerticalPanel();


		ft.setWidget(row,0,save);
		//ft.setWidget(row++,1,closePanel);


		vp.add(ft);
		//vp.add(msg);

		add(vp);
	}

	public UserProfileForm(VotingData vd, UserProfileControl cntrl0) {
		this(cntrl0);
		this.utente = vd.getProfile();
		//this.votingData = vd;
		nome.setText(utente.getNome());
		cognome.setText(utente.getCognome());

		//on update nome & cognome are not editable
		nome.setEnabled(false);
		cognome.setEnabled(false);
		//must be enforced in the service 


		titolo.setText(utente.getTitolo());
		url.setText(utente.getUrl());
		email.setText(utente.getEmail());
		affiliazione.setText(utente.getAffiliazione());
		indirizzo.setText(utente.getIndirizzo());


		//this is for update
		//so we can add the changecode anchor
		Anchor changeCode = new Anchor("Regenerate Credentials");

		changeCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm("This will regenerate the credentials !!")) {
					//msg.setText("progress...")
					Ovoto.getUi().setMessage("Regenerating Credentials",true);
					cntrl.changeCode(utente);
				}
			}
		});
		vp.add(changeCode);



		//this is for update
		//so we can add the changecode anchor
		Anchor sendCode = new Anchor("(re)Send Credentials");

		sendCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//msg.setText("progress...");
				Ovoto.getUi().setMessage("(re)Sending credentials",true);
				cntrl.sendCredentials(utente);

			}
		});


		vp.add(sendCode);

		vp.add(new HTML("<HR/>"));

		vp.add(new TokensDisplayList(vd));



	}



	class TokensDisplayList extends FlexTable {

		public TokensDisplayList(VotingData vd) {
			int row =0;


			for( Map.Entry<Ballot, VotingToken> e : vd.getTokens().entrySet()) {

				Ballot b =  e.getKey();
				VotingToken vt = e.getValue();

				//this.setWidget(row, 0,new Anchor(b.getBallotText(), b.getBallotUrl(),"_blank"));

				//String dest = b.getVotingUrl() + "/vote?ballotId="+b.getBallotId() + "&token=" + vt.getTokenText();

				//this.setWidget(row++, 1, new Anchor("VoteNow", dest, "_blank"));
			}

		}


	}




}
