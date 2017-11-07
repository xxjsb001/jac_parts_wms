package com.vtradex.wms.client.inventoryviewUI.companent;


import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UiClickedPanel extends VerticalPanel implements SourcesClickEvents{

	protected ClickListenerCollection clickedCollections;
	
	protected static final String DEFAULT_CLICKEDPANEL_STYLE = "iv_clicked_vertical_panel";

	public UiClickedPanel(){
		this.sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONMOUSEWHEEL|Event.ONDBLCLICK);
		this.setStyleName(DEFAULT_CLICKEDPANEL_STYLE);
	}
	
	public void addClickListener(ClickListener listener) {
		if(clickedCollections == null) {
			clickedCollections = new ClickListenerCollection();
		}
		clickedCollections.add(listener);
	}
	public void removeClickListener(ClickListener listener) {
		if(clickedCollections != null) {
			clickedCollections.remove(listener);
		}
	}

	public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
	        case Event.ONDBLCLICK:
	        	if(clickedCollections != null) {
					clickedCollections.fireClick(this);
					
				}
				break;
		}
	}
	
}
