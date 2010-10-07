package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;
import java.util.Vector;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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


	public void activate(final Ballot ballot) {

		final DialogBox dialog = new DialogBox(false, true);

		dialog.setHTML("Activating Ballot");

		VerticalPanel vp = new VerticalPanel();
		Button close = new Button("Close");
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//refresh
				dialog.hide();
				BallotForm a = new BallotForm(ballot,BallotControl.this);
				Ovoto.getUi().setContent(a);
			}
		});
		vp.add(close);

		dialog.add(vp);
		// open a popup (modal)
		dialog.center();
		dialog.show();
		//ask to the token generator url the codes (as many as the number of users)
		String url = "OOO";//ballot.getGeneratorUrl();
		makeTokensRequest(ballot,url+"?num=4&ballotId=" + ballot.getBallotId());
		Ovoto.getUi().setMessage("Requesting Tokens");
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

	private void makeTokensRequest(final Ballot ballot, String n_url) {

		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();

		// String url = "http://www.google.com/calendar/feeds/developer-calendar@google.com/public/full?alt=json-in-script";

		jsonp.requestObject(n_url,	new AsyncCallback<TokenList>() {
			public void onFailure(Throwable throwable) {
				Window.alert("Errroorrrreeee " + throwable.getMessage());
			}

			public void onSuccess(TokenList tokens) {
				Ovoto.getUi().setMessage("Reading Tokens");

				JsArray<TokenText> entries = tokens.getEntries();

				Vector<String> orig = new Vector<String>();

				for (int i = 0; i < entries.length(); i++) {
					TokenText tt = entries.get(i);
					orig.add(tt.getToken());
				}

				//System.err.println(orig);

				Ovoto.getUi().setMessage("mixingUp Tokens");
				Vector<String>scrambled = mixUp(orig);

				//System.err.println(scrambled);
				ballotService.storeTokens(ballot,scrambled, new AsyncCallback<Ballot>() {
					
					@Override
					public void onSuccess(Ballot result) {
						//dialog.hide();
						BallotForm a = new BallotForm(ballot,BallotControl.this);
						Ovoto.getUi().setContent(a);

					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errror sotring tokens " + caught.getMessage());
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
		
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("Ciro de Cirris");
		labels.add("Amedeo Minghi");
		labels.add("Lalella Lupis");
			
		
		ballot.setLabels(labels);
		
		
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




}
