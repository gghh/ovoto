package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.shared.Ballot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.objectify.Key;

public class ListaVotazioni extends SimplePanel {

	private final UserServiceAsync userService =	GWT.create(UserService.class);

	private VerticalPanel vp = new VerticalPanel();
	private BallotControl bc;



	public ListaVotazioni(BallotControl bc) {
		this.bc = bc;

		userService.listBallots(new AsyncCallback<ArrayList<Ballot>>() {

			@Override
			public void onSuccess(ArrayList<Ballot> result) {
				insertList(result);
			}



			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail:" + caught.getMessage());	
			}
		});



		add(vp);

	}



	private void insertList(ArrayList<Ballot> result) {
		for(Ballot b: result) {
			//for(int i=0;i<100;i++)
			vp.add(new GetBallotAnchor(b));
		}
	}

	class GetBallotAnchor extends Anchor implements ClickHandler, AsyncCallback<Ballot> {
		private Ballot b;
		public GetBallotAnchor(Ballot b) {
			this.b = b;	

			setText(b.getBallotText());
			addClickHandler(this);
		}
		@Override
		public void onClick(ClickEvent event) {
			Key<Ballot> k = new Key<Ballot>(Ballot.class, b.getBallotId());
			userService.getBallot(k, this);

		}
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("error: " + caught.getMessage());
		}
		@Override
		public void onSuccess(Ballot result) {
			BallotForm a = new BallotForm(result,bc);
			Ovoto.getUi().setContent(a);
		}
	}

}


