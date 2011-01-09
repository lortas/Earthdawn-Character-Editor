package de.earthdawn.event;

public class CharChangeRefresh {

	 // Create the listener list
	    protected javax.swing.event.EventListenerList listenerList =
	        new javax.swing.event.EventListenerList();
	    	
	
	    // This methods allows classes to register for MyEvents
	    public void addCharChangeEventListener(CharChangeEventListener listener) {
	        listenerList.add(CharChangeEventListener.class, listener);
	    }
	
	    // This methods allows classes to unregister for MyEvents
	    public void removeCharChangeEventListener(CharChangeEventListener listener) {
	        listenerList.remove(CharChangeEventListener.class, listener);
	    }
	
	    // This methode  is used to fire the event
	    void fireEvent(CharChangeEvent evt) {
	        Object[] listeners = listenerList.getListenerList();
	        // Each listener occupies two elements - the first is the listener class
	        // and the second is the listener instance
	        for (int i=0; i<listeners.length; i+=2) {
	            if (listeners[i]==CharChangeEventListener.class) {
	                ((CharChangeEventListener)listeners[i+1]).CharChanged(evt);
	            }
	        }
	    }
	    
	    public void refesh(){

	    	fireEvent(null);
	    	
	    }
	

}
