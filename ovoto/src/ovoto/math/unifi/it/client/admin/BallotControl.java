package ovoto.math.unifi.it.client.admin;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Ballot.Status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

public class BallotControl {

	//private final UserServiceAsync userService = GWT.create(UserService.class);
	private final BallotServiceAsync ballotService =	GWT.create(BallotService.class);

	public void store(Ballot b) {

		if(b.getServiceAccessId() != null && !("".equals(b.getServiceAccessId())) && b.getStatus() == Status.TO_BE_CONTINUED) {
			b.setStatus(Status.READY);
		}
		Ovoto.getUi().setMessage("Saving entry....",true);


		ballotService.writeBallot(b, new AsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				//msg.setText("progress...");
				Ovoto.getUi().setMessage("Saving DONE (id:"+result+")");

				loadSavedBallot(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Ovoto.getUi().setErrorMessage("ahi ahi ahi " + caught.getMessage());
			}
		});
	}

	private void loadSavedBallot(Long id) {
		Key<Ballot> key = new Key<Ballot>(Ballot.class, id);
		ballotService.getBallot(key, new AsyncCallback<Ballot>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error: " + caught.getMessage());
			}
			@Override
			public void onSuccess(Ballot result) {
				BallotForm a = new BallotForm(result,BallotControl.this);
				Ovoto.getUi().setContent(a);
			}
		});
	}


	public void activate(final Ballot ballot) throws UnsupportedEncodingException {

		//		final DialogBox dialog = new DialogBox(false, true);
		//
		//		dialog.setHTML("Activating Ballot");
		//
		//		VerticalPanel vp = new VerticalPanel();
		//		
		//		Button close = new Button("Close");
		//		close.addClickHandler(new ClickHandler() {
		//
		//			@Override
		//			public void onClick(ClickEvent event) {
		//				//refresh
		//				dialog.hide();
		//				BallotForm a = new BallotForm(ballot,BallotControl.this);
		//				Ovoto.getUi().setContent(a);
		//			}
		//		});
		//		vp.add(close);
		//
		//		dialog.add(vp);
		//		// open a popup (modal)
		//		dialog.center();
		//		dialog.show();
		//ask to the token generator url the codes (as many as the number of users)



		makeTokensRequest(ballot);

		//mix the tokens
		//push back to the server 

	}




	static class TokenText extends JavaScriptObject {
		protected TokenText() {}

		public final native String getToken() /*-{
	     return this.token;
	   }-*/;
	}

	static class TokenList extends JavaScriptObject {
		protected TokenList() {}

		public final native JsArray<TokenText> getEntries() /*-{
	     return this.tokenList;
	   }-*/;

	}

	private void makeTokensRequest(final Ballot ballot) throws UnsupportedEncodingException {

		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		jsonp.setTimeout(100000);
		

		String url = ballot.getServiceUrl();
		url += "?mode=ACTIVATE";
		url += "&accessToken=" + URL.encode(ballot.getServiceAccessToken());
		url += "&accessId=" + URL.encode(ballot.getServiceAccessId());
		url += "&numTokens=" + ballot.getVoters().size();

		Ovoto.getUi().setMessage("Requesting Tokens...",true);


		jsonp.requestObject(url,	new AsyncCallback<TokenList>() {
			public void onFailure(Throwable throwable) {
				Window.alert("Error " + throwable.getMessage());
				Ovoto.getUi().setErrorMessage(throwable.getMessage());
			}

			public void onSuccess(TokenList tokens) {
				Ovoto.getUi().setMessage("Reading Tokens...", true);

				JsArray<TokenText> entries = tokens.getEntries();

				Vector<String> orig = new Vector<String>();

				for (int i = 0; i < entries.length(); i++) {
					TokenText tt = entries.get(i);
					orig.add(tt.getToken());
				}

				System.err.println("==================");
				System.err.println(orig);
				System.err.println("==================");

				Ovoto.getUi().setMessage("mixingUp Tokens...",true);
				Vector<String>scrambled = mixUp(orig);

				System.err.println(scrambled);
				System.err.println("==================");

				ballotService.storeTokens(ballot,scrambled, new AsyncCallback<Ballot>() {

					@Override
					public void onSuccess(Ballot result) {
						//dialog.hide();
						BallotForm a = new BallotForm(result,BallotControl.this);
						Ovoto.getUi().setContent(a);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errror storing tokens " + caught.getMessage());
						Ovoto.getUi().setErrorMessage(caught.getMessage());
					}
				});


			}


		});


	}


	private Vector<String> mixUp(Vector<String> orig) {
		int iSize = orig.size();
		Vector<String> out = new Vector<String>();
		for ( int i=0; i < iSize; i++ ) {
			// Choose a random Vector item
			int iRandom = (int)(Math.random() * orig.size());
			// store the chosen vector item in the array
			out.add(orig.get(iRandom));
			// remove the item from the vector
			orig.remove(iRandom);
		}

		return out;
	}


	//	public void setup(Ballot ballot) {
	//
	//
	//		//System.err.println(scrambled);
	//		
	//		ballotService.setupService(ballot, new AsyncCallback<Ballot>() {
	//
	//			@Override
	//			public void onSuccess(Ballot result) {
	//				//dialog.hide();
	//				BallotForm a = new BallotForm(result,BallotControl.this);
	//				Ovoto.getUi().setContent(a);
	//			}
	//
	//			@Override
	//			public void onFailure(Throwable caught) {
	//				Window.alert("Errror during ballot setup: " + caught.getMessage());
	//				Ovoto.getUi().setErrorMessage(caught.getMessage());
	//			}
	//		});
	//	}


	//metodo alternativo.

	//	public void setup(final Ballot ballot) {
	//
	//
	//		//System.err.println(scrambled);
	//
	//		//prepara una form in un popup perl'utente e glie la fa spedire.
	//		final DialogBox pp = new DialogBox();
	//
	//		FormPanel f = new FormPanel();
	//
	//
	//		f.addSubmitCompleteHandler(new SubmitCompleteHandler() {
	//
	//			@Override
	//			public void onSubmitComplete(SubmitCompleteEvent event) {
	//				ballot.setServiceAccessId(event.getResults());
	//				ballot.setStatus(Status.READY);
	//				pp.hide();
	//				BallotForm a = new BallotForm(ballot,BallotControl.this);
	//				Ovoto.getUi().setContent(a);
	//				Window.alert("AccessToken "+event.getResults()+" REMEMBER TO SAVE THE BALLOT BEFORE PROCEEDING");
	//				//Window.alert(event.getResults());
	//			}
	//		});
	//
	//		VerticalPanel vp = new VerticalPanel();
	//		vp.setSize("400px", "300px");
	//		ScrollPanel sp = new ScrollPanel();
	//
	//		SubmitButton submit = new SubmitButton("Store");
	//
	//		Button cancel = new Button("Cancel");
	//		cancel.addClickHandler(new ClickHandler() {		
	//			@Override
	//			public void onClick(ClickEvent event) {
	//				pp.hide();
	//				Ovoto.getUi().setMessage("Setup cancelled");
	//			}
	//		});
	//
	//
	//
	//		VerticalPanel vp2 = new VerticalPanel();
	//
	//		//i fields sono quelli in ovoto.math.unifi.it.server.admin.BallotUtils.setupBallot(Ballot)
	//
	//		f.setAction(ballot.getServiceUrl());
	//		f.setMethod(FormPanel.METHOD_POST);
	//
	//		TextBox fld = new TextBox();
	//		fld.setName("mode");
	//		fld.setValue("SETUP");
	//		vp2.add(fld);
	//
	//		fld = new TextBox();
	//		fld.setName("ballotText");
	//		fld.setValue("" + ballot.getBallotText());
	//		vp2.add(fld);
	//
	//		
	//		fld = new TextBox();
	//		fld.setName("id");
	//		fld.setValue("" + ballot.getBallotId());
	//		vp2.add(fld);
	//
	//		fld = new TextBox();
	//		fld.setName("accessToken");
	//		fld.setValue( ballot.getServiceAccessToken());
	//		vp2.add(fld);
	//
	//		fld = new TextBox();
	//		fld.setName("numOfChoices");
	//		fld.setValue("" + ballot.getNumOfChoices());
	//		vp2.add(fld);
	//
	//		fld = new TextBox();
	//		fld.setName("startDate");
	//		fld.setValue( "" +ballot.getStartDate().getTime());
	//		vp2.add(fld);
	//
	//		fld = new TextBox();
	//		fld.setName("endDate");
	//		fld.setValue( "" +ballot.getEndDate().getTime());
	//		vp2.add(fld);
	//
	//
	//		int i=0;
	//		for(String l : ballot.getLabels()) {
	//			TextBox tb = new TextBox();
	//
	//			tb.setName("label" +i);
	//			tb.setValue(l);
	//			tb.setReadOnly(true);
	//			vp2.add(tb);
	//			i++;
	//		}
	//		sp.add(vp2);
	//		vp.add(sp);
	//		HorizontalPanel hp = new HorizontalPanel();
	//		hp.add(submit);
	//		hp.add(cancel);
	//
	//		vp.add(hp);
	//		f.add(vp);
	//
	//
	//		pp.add(f);
	//		pp.center();
	//		pp.show();
	//
	//		//				ballotService.setupService(ballot, new AsyncCallback<Ballot>() {
	//		//	
	//		//					@Override
	//		//					public void onSuccess(Ballot result) {
	//		//						//dialog.hide();
	//		//						BallotForm a = new BallotForm(result,BallotControl.this);
	//		//						Ovoto.getUi().setContent(a);
	//		//					}
	//		//	
	//		//					@Override
	//		//					public void onFailure(Throwable caught) {
	//		//						Window.alert("Errror during ballot setup: " + caught.getMessage());
	//		//						Ovoto.getUi().setErrorMessage(caught.getMessage());
	//		//					}
	//		//				});
	//		//			}
	//	}

	//metodo ancora + alternativo

	public void setup(Ballot b) {

		//chiama un servizio che genera la form da submittare
		//url del servizio che genra la form
		String url = GWT.getModuleBaseURL() + "admin/generateSetupForm?ballotId=" + b.getBallotId();
		Window.open(url, "Setup", "location=1,status=1,scrollbars=1,width=600,height=450");

		//e rimette il ballot alla pagina iniziale che non fa mai male
		BallotForm a = new BallotForm(b,BallotControl.this);
		Ovoto.getUi().setContent(a);
		

	}



	public void sendEmails(Ballot ballot, String subj, String body) {
		String url = GWT.getHostPageBaseURL();

		ballotService.sendEmails(ballot,subj,body, url, new AsyncCallback<Ballot>() {

			@Override
			public void onSuccess(Ballot result) {
				//dialog.hide();
				BallotForm a = new BallotForm(result,BallotControl.this);
				Ovoto.getUi().setContent(a);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errror during mail delivery: " + caught.getMessage());
				Ovoto.getUi().setErrorMessage(caught.getMessage());
			}
		});


	}




}
