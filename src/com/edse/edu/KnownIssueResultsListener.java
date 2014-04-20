package com.edse.edu;


import java.util.ArrayList;


public interface KnownIssueResultsListener
{
	public abstract void onResultSuccess(ArrayList<KnownIssue> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
