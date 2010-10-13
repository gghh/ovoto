package ovoto.math.unifi.it.client.admin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Ballot.Status;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class BallotForm extends TabLayoutPanel {



	private Button save;
	private TextArea ballotText = new TextArea();
	private TextBox ballotUrl = new TextBox();
	private TextBox serviceUrl = new TextBox();
	private TextBox accessToken = new TextBox();
	private TextBox accessId = new TextBox();
	private DateBox startDate = new DateBox();
	private DateBox endDate = new DateBox();

	//Setup
	private LabelsArea labelsArea = new LabelsArea();
	private TextBox numOfChoices = new TextBox();




	private Ballot ballot = null;

	private BallotControl cntrl;

	//general ballot data ...
	class GeneralInfo extends FlowPanel {

		public GeneralInfo() {

			//			if(ballot!= null) {
			//				Window.alert(ballot.getStatus() + " & " + Status.READY);
			//				if(ballot.getStatus() == Status.READY) {
			//					accessToken.setReadOnly(true);
			//					serviceUrl.setReadOnly(true);
			//					this.add(new Label("Ballot setup done"));
			//				} 
			//			}


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
					String desc = ballotUrl.getText();
					String  svc = serviceUrl.getText();

					Date start = startDate.getValue();
					Date end = endDate.getValue();

					if(ballot==null) {
						ballot = new Ballot(ballotText.getText(),desc,start,end,svc,accessToken.getText());
						//ballot.setDate_creation(d); //now
					} else {
						ballot.setBallotText(ballotText.getText());
						ballot.setBallotUrl(desc);
						ballot.setServiceUrl(svc);
						ballot.setStartDate(start);
						ballot.setEndDate(end);
						ballot.setAccessId(accessId.getText());
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

			ft.setHTML(row, 0, "data inizio");
			ft.setWidget(row++, 1, startDate);
			ft.setHTML(row, 0, "data fine");
			ft.setWidget(row++, 1, endDate);

			ft.setHTML(row, 0, "service url");
			ft.setWidget(row++, 1, serviceUrl);
			ft.setHTML(row, 0, "access Token");
			ft.setWidget(row++, 1, accessToken);

			//accessId.setReadOnly(true);
			ft.setHTML(row, 0, "access Id");
			ft.setWidget(row++, 1, accessId);

			//ft.setHTML(row, 0, "generator url");
			//ft.setWidget(row++, 1, generatorUrl);



			ft.setWidget(row,0,save);

			this.add(ft);
			add(save);


			if(ballot!= null) {

				DecoratorPanel dp = new DecoratorPanel();
				//dp.setWidth("90%");
				//dp.add(l);

				VerticalPanel vp = new VerticalPanel();

				if(ballot.getStatus() == Status.TO_BE_CONTINUED) {
					Label l = new Label(" (numOfChoices: " + ballot.getNumOfChoices() + " labels: " + ballot.getLabels().size()+")");
					Anchor setup = new Anchor("Setup");
					HorizontalPanel hp = new HorizontalPanel();
					hp.add(setup);
					hp.add(l);

					setup.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Ovoto.getUi().setMessage("Setting Up",true);
							cntrl.setup(ballot);
						}
					});
					vp.add(hp);
				} else {
					vp.add(new Label("setup already done"));
				} 



				if(ballot.getStatus() == Status.READY) {
					Label l = new Label(" (numOfChoices: " + ballot.getNumOfChoices() + " labels: " + ballot.getLabels().size()+" voters: " + ballot.getVoters().size()+")");
					Anchor activate = new Anchor("Activate");
					HorizontalPanel hp = new HorizontalPanel();
					hp.add(activate);
					hp.add(l);
					activate.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Ovoto.getUi().setMessage("Activating",true);
							try {
								cntrl.activate(ballot);
							} catch (UnsupportedEncodingException e) {
								Ovoto.getUi().setErrorMessage("Error: " + e.getMessage());
							}
						}
					});
					vp.add(hp);
				} else if(ballot.getStatus() == Status.TO_BE_CONTINUED) {
					vp.add(new Label("not ready for activation"));
				} else if(ballot.getStatus() == Status.FINALIZED) {
					vp.add(new Label("activation already done"));
				}
				dp.add(vp);
				this.add(dp);
			}

		}
	}



	class LabelsArea extends TextArea {
		public LabelsArea() {
			setSize("200px", "300px");	
		}

		public void setData(ArrayList<String> a) {
			String sum ="";
			for(String s: a) 
				sum+= s + "\n";
			setText(sum);
		}

		public ArrayList<String> getData() {
			String[] lines = getValue().split("\n");
			return new ArrayList<String>(Arrays.asList(lines));
		}

	}


	class Configs extends FlowPanel {

		Anchor saveLabels = new Anchor("Save Labels");

		private void setDisabled() {
			labelsArea.setReadOnly(true);
			numOfChoices.setReadOnly(true);
		}

		public Configs() {

			labelsArea.setData(ballot.getLabels());
			numOfChoices.setText(""+ballot.getNumOfChoices());
			numOfChoices.setWidth("20px");

			if(ballot.getStatus() != Status.TO_BE_CONTINUED) {
				setDisabled();
				this.add(new Label("Ballot setup done."));
			} else {
				saveLabels.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						try {
							Ovoto.getUi().setMessage("Saving Labels",true);
							ballot.setLabels(labelsArea.getData());
							ballot.setNumOfChoices(Integer.parseInt(numOfChoices.getText()));
							cntrl.store(ballot); 
						} catch(Exception e) {
							Ovoto.getUi().setErrorMessage("ERROR: " + e.getMessage());
						}
					}
				});
			}



			this.add(new HTML("<hr/>"));

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new Label("Numbero of Choices: "));
			hp.add(numOfChoices);
			this.add(hp);
			this.add(labelsArea);
			this.add(saveLabels);


		}
	}



	class Voters extends FlowPanel {

		public Voters() {


			//this is for update

			if(ballot.getStatus() != Status.FINALIZED) {

				Anchor chooseVoters  = new Anchor("Select Voters (selected: " + ballot.getVoters().size()+")"); 
				chooseVoters.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Ovoto.getUi().setMessage("Selecting Voters");
						VoterSelectDialog vsd = new VoterSelectDialog(ballot);
						vsd.center();
						vsd.show();
					}
				});

				this.add(chooseVoters);
			} else
				this.add(new Label("Ballot is Finalized"));

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
		}
	}




	class Emails extends FlowPanel {
		Anchor send = new Anchor("Send");
		TextBox emailSubj = new TextBox();
		TextArea emailText = new TextArea();

		public Emails() {
			add(new Label("#" + ballot.getVoters().size() + " voters."));
			emailSubj.setWidth("350px");
			emailText.setSize("350px", "220px");

			add(new Label("Subject:"));
			add(emailSubj);
			add(new Label("Body:"));
			add(emailText);
			add(send);
			add(new Label("$credentials$, $directlink$, $fullname$, $qualifiedname$"));

			send.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					cntrl.sendEmails(ballot,emailSubj.getText(),emailText.getText());
				}
			});


		}
	}





	public BallotForm(BallotControl cntrl0) {
		super(2,Unit.EM);
		this.cntrl = cntrl0;
		setSize("400px", "400px");

		this.add(new GeneralInfo(), "General");

	}

	public BallotForm(Ballot u, BallotControl cntrl0) {
		super(2,Unit.EM);
		this.ballot = u;
		this.cntrl = cntrl0;
		setSize("400px", "400px");

		this.add(new GeneralInfo(), "General");

		ballotText.setText(ballot.getBallotText());
		ballotUrl.setText(ballot.getBallotUrl());
		serviceUrl.setText(ballot.getServiceUrl());
		accessToken.setText(ballot.getServiceAccessToken());
		accessId.setText(ballot.getServiceAccessId());

		startDate.setValue(ballot.getStartDate());
		endDate.setValue(ballot.getEndDate());


		this.add(new Configs(), "Configs");
		this.add(new Voters(), "Voters");
		this.add(new Emails(), "Emails");



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

			hp.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
			Anchor all = new Anchor("Select All");
			all.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					sprp.selectAll();
				}
			});
			hp.add(all);


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
