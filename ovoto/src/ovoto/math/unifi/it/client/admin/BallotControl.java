package ovoto.math.unifi.it.client.admin;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;

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


		String url = ballot.getServiceUrl().getValue();
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


	public void setup(Ballot ballot) {


		//System.err.println(scrambled);
		ballotService.setupService(ballot, new AsyncCallback<Ballot>() {

			@Override
			public void onSuccess(Ballot result) {
				//dialog.hide();
				BallotForm a = new BallotForm(result,BallotControl.this);
				Ovoto.getUi().setContent(a);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errror during ballot setup: " + caught.getMessage());
				Ovoto.getUi().setErrorMessage(caught.getMessage());
			}
		});
	}


	public void sendEmails(Ballot ballot, String subj, String body) {
		
		ballotService.sendEmails(ballot,subj,body, new AsyncCallback<Ballot>() {

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
