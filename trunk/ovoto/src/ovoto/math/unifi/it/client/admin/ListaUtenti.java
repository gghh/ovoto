package ovoto.math.unifi.it.client.admin;

import java.util.ArrayList;

import ovoto.math.unifi.it.client.Ovoto;
import ovoto.math.unifi.it.client.UserProfileControl;
import ovoto.math.unifi.it.client.UserProfileForm;
import ovoto.math.unifi.it.shared.Utente;
import ovoto.math.unifi.it.shared.VotingData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.objectify.Key;

public class ListaUtenti extends SimplePanel {
	private final UserServiceAsync userService =	GWT.create(UserService.class);

	private VerticalPanel vp = new VerticalPanel();
	private UserProfileControl upc;


	public ListaUtenti ( UserProfileControl upc) {
		this.upc=upc;

		userService.listUtenti(new AsyncCallback<ArrayList<Utente>>() {

			@Override
			public void onSuccess(ArrayList<Utente> result) {
				insertList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail:" + caught.getMessage());	
			}
		});



		add(vp);


	}

	private void insertList(ArrayList<Utente> result) {
		for(Utente u: result) {
			//for(int i=0;i<100;i++)
			vp.add(new GetUserAnchor(u));
		}
	}

	class GetUserAnchor extends Anchor implements ClickHandler, AsyncCallback<Utente> {
		private Utente u;
		public GetUserAnchor(Utente u) {
			this.u = u;	

			setText(u.getNome() + " " + u.getCognome() + " " + u.getEmail());
			addClickHandler(this);
		}
		@Override
		public void onClick(ClickEvent event) {
			Key<Utente> k = new Key<Utente>(Utente.class, u.getId());
			userService.getUtente(k, this);

		}
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("error: " + caught.getMessage());
		}
		@Override
		public void onSuccess(Utente result) {
			VotingData vd = new VotingData(result);
			UserProfileForm a = new UserProfileForm(vd,upc);
			Ovoto.getUi().setContent(a);
			//			a.show();
			//			a.center();
		}
	}






	//
	//			// create some data
	//			ArrayList<String> values = new ArrayList<String>();
	//			values.add("one");
	//			values.add("two");
	//			values.add("three");
	//			values.add("four");
	//			values.add("five");
	//			values.add("six");
	//
	//			// create a ListViewAdapter
	//			ListDataProvider<String> lva = new ListDataProvider<String>();
	//			// give the ListViewAdapter our data
	//			lva.setList(values);


	//			{
	//				// CellList of TextCells with PageSizePager
	//				CellList<String> cl = new CellList<String>(new TextCell());
	//				// set the initial pagesize to 2
	//				cl.setPageSize(2);
	//
	//				// add the CellLists to the adaptor
	//				lva.addDataDisplay(cl);
	//
	//
	//				// create a PageSizePager, giving it a handle to the CellList
	//				PageSizePager psp = new PageSizePager(2);
	//
	//				psp.setDisplay(cl);
	//
	//				// add the CellList to the page
	//				vp.add(cl);
	//
	//				// add the PageSizePager to the page
	//				vp.add(psp);
	//			}
	//
	//			vp.add(new HTML("<hr />"));
	//
	//			{
	//				// CellList of TextCells with a SimplePager
	//				CellList<String> cl = new CellList<String>(new TextCell());
	//				// set the initial pageSize to 2
	//				cl.setPageSize(2);
	//
	//				// add the CellLists to the adaptor
	//				lva.addDataDisplay(cl);
	//
	//
	//
	//				// create a pager, giving it a handle to the CellList
	//				SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER);
	//
	//				pager.setDisplay(cl);
	//				// cl/pager/lva	
	//
	//				//            pager.nextPage();
	//
	//
	//				// add the CellList to the page
	//				vp.add(cl);
	//
	//				// add the Pager to the page
	//				vp.add(pager);
	//			}
	//
	//			vp.add(new HTML("<hr />"));
	//
	//			{
	//				// CellList of TextCells with a SimplePager and PageSizePager
	//				CellList<String> cl = new CellList<String>(new TextCell());
	//				// set the initial pageSize to 2
	//				cl.setPageSize(2);
	//
	//				// add the CellLists to the adaptor
	//				lva.addDataDisplay(cl);
	//
	//				// create a PageSizePager, giving it a handle to the CellList
	//				PageSizePager psp = new PageSizePager( 1);
	//
	//				// create a pager, giving it a handle to the CellList
	//				SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER);
	//
	//				pager.setDisplay(cl);
	//				psp.setDisplay(cl);
	//
	//				// add the CellList to the page
	//				vp.add(cl);
	//
	//				// add the Pager to the page
	//				vp.add(pager);
	//
	//				// add the PageSizePager to the page
	//				vp.add(psp);
	//			}
	//
	//			vp.add(new HTML("<hr />"));

	//			{
	//				// CellTable
	//				CellTable<String> ct = new CellTable<String>();
	//				ct.setPageSize(4);
	//				lva.addDataDisplay(ct);
	//
	//				// add a column with a simple string header
	//				ct.addColumn(new Column<String,String>(new TextCell()) {
	//
	//					@Override
	//					public String getValue(String object) {
	//						return object;
	//					}
	//				}, "String Header");
	//
	//				
	//				
	//				
	//				//add a column with a TextCell header
	//				ct.addColumn(new TextColumn<String>() {
	//
	//					@Override
	//					public String getValue(String object) {
	//						return "[" + object + "]";
	//					}
	//				}, new Header<String>(new TextCell()) {
	//
	//					@Override
	//					public String getValue() {
	//						return "TextCell Header";
	//					}
	//				});



	//add a column with a TextCell header

	//ButtonCell b_cell = new ButtonCell();


	//				ct.addColumn(new Column<Button, Button>(new ButtonCell()) {
	//
	//					@Override
	//					public String getValue(String object) {
	//						return "[" + object + "]";
	//					}
	//				}, new Header<String>(new TextCell()) {
	//
	//					@Override
	//					public String getValue() {
	//						return "TextCell Header";
	//					}
	//				});




	//				// create a pager, giving it a handle to the CellTable
	//				SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER);
	//				pager.setDisplay(ct);
	//
	//
	//				// add the CellList to the page
	//				vp.add(ct);
	//
	//				// add the Pager to the page
	//				vp.add(pager);
	//			}











}
