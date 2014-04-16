package com.edse.edu;

import java.util.ArrayList;
/***
 * Events results Listener interface implements event handler methods corresponding to a success/failure  
 * @author kaushikvelindla
 *
 */
public interface EventsResultsListener
{
	public abstract void onResultSuccess(ArrayList<Event> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
