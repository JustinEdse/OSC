package com.edse.edu;

import java.util.ArrayList;

public interface EventsResultsListener
{
	public abstract void onResultSuccess(ArrayList<Event> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
