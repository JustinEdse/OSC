package com.edse.edu;


import java.util.ArrayList;


public interface ChangeLogResultsListener
{
	public abstract void onResultSuccess(ArrayList<ChangeLog> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
