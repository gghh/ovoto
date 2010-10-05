package ovoto.math.unifi.it.client.admin;

import java.util.Date;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BallotForm extends SimplePanel {



	private Button save;
	private TextArea ballotText = new TextArea();
	private TextBox ballotUrl = new TextBox();
	private TextBox votingUrl = new TextBox();
	private TextBox generatorUrl = new TextBox();


	VerticalPanel vp;

	private Ballot ballot = null;

	private BallotControl cntrl;


	public BallotForm(BallotControl cntrl0) {
		this.cntrl = cntrl0;

		save= new Button("Save");
		//save.setEnabled(false);
		save.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {

				//verify 
				if("".equals(ballotText.getText())) {
					Window.alert("la descrizione e' obbligatoria");
					return;
				}
				if("".equals(generatorUrl.getText())  || "".equals(votingUrl.getText()) ) {
					Window.alert("generator & voting urls are mandatory");
					return;
				}



				Date d = new Date();

				if(ballot==null) {
					ballot = new Ballot();
					ballot.setDate_creation(d); //now
				}

				ballot.setBallotText(ballotText.getText());
				ballot.setBallotUrl(ballotUrl.getText());
				ballot.setVotingUrl(votingUrl.getText());
				ballot.setGeneratorUrl(generatorUrl.getText());

				cntrl.store(ballot);
			}
		});





		FlexTable ft = new FlexTable();
		int row =0;

		ft.setHTML(row, 0, "Descrizione");
		ft.setWidget(row++, 1, ballotText);

		ft.setHTML(row, 0, "url descrizione");
		ft.setWidget(row++, 1, ballotUrl);

		ft.setHTML(row, 0, "voting url");
		ft.setWidget(row++, 1, votingUrl);

		ft.setHTML(row, 0, "generator url");
		ft.setWidget(row++, 1, generatorUrl);



		vp = new VerticalPanel();


		ft.setWidget(row,0,save);


		vp.add(ft);

		add(vp);
	}

	public BallotForm(Ballot u, BallotControl cntrl0) {
		this(cntrl0);
		this.ballot = u;
		ballotText.setText(ballot.getBallotText());
		ballotUrl.setText(ballot.getBallotUrl());
		votingUrl.setText(ballot.getVotingUrl());
		generatorUrl.setText(ballot.getGeneratorUrl());	
		
		
		
		
		//this is for update
		//so we can add the activate anchor
		Anchor changeCode = new Anchor("Activate");

		changeCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Ovoto.getUi().setMessage("Activating",true);
				cntrl.activate(ballot);
			}
		});

		vp.add(changeCode);

	}






}
