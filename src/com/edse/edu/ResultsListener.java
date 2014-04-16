package com.edse.edu;

import java.util.ArrayList;
/***
 * This interface defines event handler methods, which are implemented by Main activity class.
 * @author kaushikvelindla
 *
 */
public interface ResultsListener
{
	public abstract void onResultSuccess(ArrayList<Article> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
