package com.edse.edu;

import java.util.ArrayList;

public interface ResultsListener
{
	public abstract void onResultSuccess(ArrayList<Article> result);
	public abstract void onResultFail(int resultCode, String errorMessage);
}
