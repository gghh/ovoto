package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BallotForm extends TabLayoutPanel {



	private Button save;
	private TextArea ballotText = new TextArea();
	private TextBox ballotUrl = new TextBox();
	private TextBox serviceUrl = new TextBox();
	private TextBox accessToken = new TextBox();
	private TextBox accessId = new TextBox();




	private Ballot ballot = null;

	private BallotControl cntrl;

	//general ballot data ...
	class GeneralInfo extends FlowPanel {

		public GeneralInfo() {

			save = new Button("Save");
			//save.setEnabled(false);
			save.addClickHandler(new ClickHandler() {	
				@Override
				public void onClick(ClickEvent event) {

					//verify 
					if("".equals(ballotText.getText())) {
						Window.alert("la descrizione e' obbligatoria");
						return;
					}

					if("".equals(serviceUrl.getText())  ) {
						Window.alert("serviceUrl is mandatory");
						return;
					}


					//Date d = new Date();
					Link desc = new Link(ballotUrl.getText());
					Link svc = new Link(serviceUrl.getText());

					if(ballot==null) {
						ballot = new Ballot(ballotText.getText(),desc,svc,accessToken.getText());
						//ballot.setDate_creation(d); //now
					} else {
						ballot.setBallotText(ballotText.getText());
						ballot.setBallotUrl(desc);
						ballot.setServiceUrl(svc);
					}
					cntrl.store(ballot);

				}
			});



			FlexTable ft = new FlexTable();
			int row =0;

			ft.setHTML(row, 0, "Descrizione");
			ft.setWidget(row++, 1, ballotText);

			ft.setHTML(row, 0, "url descrizione");
			ft.setWidget(row++, 1, ballotUrl);

			ft.setHTML(row, 0, "service url");
			ft.setWidget(row++, 1, serviceUrl);
			ft.setHTML(row, 0, "access Token");
			ft.setWidget(row++, 1, accessToken);

			accessId.setReadOnly(true);
			ft.setHTML(row, 0, "access Id");
			ft.setWidget(row++, 1, accessId);

			//ft.setHTML(row, 0, "generator url");
			//ft.setWidget(row++, 1, generatorUrl);



			ft.setWidget(row,0,save);

			this.add(ft);
			add(save);
		}
	}




	class Configs extends FlowPanel {
		
		public Configs() {


		
			//so we can add the activate anchor
			Anchor setup = new Anchor("Setup");

			setup.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Ovoto.getUi().setMessage("Activating",true);
					cntrl.setup(ballot);
				}
			});

			this.add(setup);
		
			


		

		}
	}

	
	
class Voters extends FlowPanel {
		
		public Voters() {


			//this is for update


			Anchor chooseVoters  = new Anchor("Select Voters (now: " + ballot.getVoters().size()+")"); 
			chooseVoters.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Ovoto.getUi().setMessage("Selecting Voters");
					VoterSelectDialog vsd = new VoterSelectDialog(ballot);
					vsd.center();
					vsd.show();
					//cntrl.activate(ballot);
				}
			});

			this.add(chooseVoters);

			this.add(new HTML("<hr/>"));
			//show selected users
			ScrollPanel sp = new ScrollPanel();
			sp.setHeight("300px");
			ArrayList<String> a = ballot.getVoters();
			if(a != null) {
				ListaUtenti l = new ListaUtenti(new ShowProfileRowProvider(),a);
				sp.add(l);
			}
			this.add(sp);
			
			this.add(new HTML("<hr/>"));
			//so we can add the activate anchor
			Anchor activate = new Anchor("Activate");

			activate.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Ovoto.getUi().setMessage("Activating",true);
					cntrl.activate(ballot);
				}
			});

			this.add(activate);

			
		}
	}

	
	

	public BallotForm(BallotControl cntrl0) {
		super(2,Unit.EM);
		this.cntrl = cntrl0;
		setSize("400px", "400px");



		this.add(new GeneralInfo(), "General");


	}

	public BallotForm(Ballot u, BallotControl cntrl0) {
		this(cntrl0);
		this.ballot = u;
		ballotText.setText(ballot.getBallotText());
		ballotUrl.setText(ballot.getBallotUrl().getValue());
		serviceUrl.setText(ballot.getServiceUrl().getValue());
		accessToken.setText(ballot.getServiceAccessToken());
		accessId.setText(ballot.getServiceAccessId());

		this.add(new Configs(), "Configs");
		this.add(new Voters(), "Voters");

		//



	}



	class VoterSelectDialog extends DialogBox {

		public VoterSelectDialog(final Ballot b) {
			super();
			setGlassEnabled(true);
			setModal(true);
			setAutoHideEnabled(false);

			setHTML("Select Voters");
			
			final SelectProfileRowProvider sprp = new SelectProfileRowProvider(b.getVoters());
			ListaUtenti a = new ListaUtenti(sprp);
			
			VerticalPanel vp = new VerticalPanel();

			HorizontalPanel hp = new HorizontalPanel();
			Anchor close = new Anchor("Close");
			close.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if(Window.confirm("This will discard your changes"))
						hide();
				}
			});
			hp.add(close);

			hp.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
			Anchor save = new Anchor("Save");
			save.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ArrayList<String> voters = sprp.getSelectedList();
					b.setVoters(voters);
					cntrl.store(b);
					BallotForm a = new BallotForm(b,cntrl);
					Ovoto.getUi().setContent(a);
					hide();
				}
			});

			hp.add(save);
			vp.add(hp);
			vp.add(new HTML("<hr/>"));
			
			ScrollPanel sp = new ScrollPanel();
			sp.setSize("400px","300px");
			sp.add(a);
			
			vp.add(sp);

			add(vp);
		}
	}







}
