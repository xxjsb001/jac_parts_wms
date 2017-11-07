package com.vtradex.wms.client.inventoryviewUI.companent;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.inventoryviewUI.page.Sub_Warehouse_Image_Panel;
import com.vtradex.wms.client.ui.javabean.JB_IV;

public final class DraggableUiColumnChartWrapper extends UiColumnChart
       implements SourcesMouseEvents,EventPreview 
{
    private MouseListenerCollection mouseListeners = new MouseListenerCollection();
    public void addMouseListener(MouseListener l){
    	mouseListeners.add(l);
    }
    public void removeMouseListener(MouseListener l){
    	mouseListeners.remove(l);
    }
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEDOWN:
          case Event.ONMOUSEUP:
          case Event.ONMOUSEMOVE:
          case Event.ONMOUSEOVER:
          case Event.ONMOUSEOUT:
          case Event.MOUSEEVENTS:
            mouseListeners.fireMouseEvent(this, event);
            break;
        }
      }

	public DraggableUiColumnChartWrapper(final JB_IV iv, final Sub_Warehouse_Image_Panel wPanel){
		super(iv,wPanel);
		DOM.setStyleAttribute(getElement(),"position","absolute");
		//We don't want to lose anything already sunk,
		//so just ORing it to what's already there.
		DOM.sinkEvents(getElement(),DOM.getEventsSunk(getElement())|Event.MOUSEEVENTS);
		addMouseListener(new DraggableMouseListener(iv,wPanel));
	}
	    
	public boolean onEventPreview(Event event) {
	    if (DOM.eventGetType(event) == Event.ONMOUSEDOWN &&
	        DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))) {
	      DOM.eventPreventDefault(event);
	    }
	    return true;
	  }
	
	private class DraggableMouseListener
	    extends MouseListenerAdapter {

	    private boolean dragging = false;
	    private int dragStartX;
	    private int dragStartY;
	    
	    Sub_Warehouse_Image_Panel wPanel;
	    JB_IV iv;
	    
	    public DraggableMouseListener(JB_IV iv,Sub_Warehouse_Image_Panel w){
	    	this.wPanel = w;
	    	this.iv = iv;
	    }
	    
	    public void onMouseDown(Widget sender, int x, int y) {
	      dragging = true;
	      // capturing the mouse to the dragged widget.
	      DOM.setCapture(getElement());
	      dragStartX = x;
	      dragStartY = y;	
	    }

	    public void onMouseUp(Widget sender, int x, int y) {
	      dragging = false;
	      DOM.releaseCapture(getElement());
	      Page_IV_DataAccessor pid = new   Page_IV_DataAccessor(wPanel);
	      pid.setCur_JB_IV(iv);
		  pid.setCur_JB_IV_POS(sender.getAbsoluteLeft()-CT_IV.DEFAULT_OFF_WIDTH,
				  sender.getAbsoluteTop()-CT_IV.DEFAULT_OFF_HEIGHT);
		  pid.updateIVData();
	    }

	    public void onMouseMove(Widget sender, int x, int y) {
	      if (dragging) {
	        // we don't want the widget to go off-screen, so the top/left
	        // values should always remain be positive.
	        int newX = Math.max(0, x + getAbsoluteLeft() - dragStartX);
	        int newY = Math.max(0, y + getAbsoluteTop() - dragStartY);
	        DOM.setStyleAttribute(getElement(), "left", ""+newX);
	        DOM.setStyleAttribute(getElement(), "top", ""+newY);
	        
	      }
	    }
	    
	    public void onMouseEnter(Widget sender){
	    	// applyToolTips(iv);
	    }
	  
	  }
	/* factory method:
	  public static DraggableWidgetWrapper makeDraggable(UiColumnChart widget) {
		    return new DraggableWidgetWrapper(widget);
		  }

	*/
	
}
