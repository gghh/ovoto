package ovoto.math.unifi.it.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Ui extends Composite {

	interface UiUiBinder extends UiBinder<DockLayoutPanel, Ui> {}
	private static UiUiBinder uiBinder = GWT.create(UiUiBinder.class);

	@UiField 
	ScrollPanel body;
	
	@UiField 
	VerticalPanel sidebar;
	
	@UiField 
	HTML header;
	
	@UiField 
	HTML messages;
	
	
	
	private PopupPanel block;
	
	
	public Ui() {
		initWidget(uiBinder.createAndBindUi(this));
		
		body.setWidget(new HTML("<h1>Il Centro</h1>"));
		
		 block = new PopupPanel(false,true);
		 block.setGlassEnabled(true);
		 block.setAnimationEnabled(true);
	}
	
	
	public void addSideControl(Widget w) {
		sidebar.add(w);
	}
	
	public void removeSideControl(Widget w) {
		sidebar.remove(w);
	}
	
	public void clearSideControls() {
		sidebar.clear();
	}
	
	public void setContent(Widget w) {
		body.setWidget(w);
		
		//cleanup messages
		setMessage("");
		
	}
	
	public void setMessage(String h, boolean blocking) {
		
		//in any case clear the blocking 
		block.hide();
		
		messages.setHTML(h);
		
		if(blocking) {
			block.setWidget(new Label("Saving..."));
			block.center();
			block.show();
		} 
		
	}
	
	public void setMessage(String h) {
		setMessage(h,false);	
	}
	
	public void setErrorMessage(String h) {
		//TODO sets the style to errorMessage 
		setMessage(h,false);	
	}
	
}
