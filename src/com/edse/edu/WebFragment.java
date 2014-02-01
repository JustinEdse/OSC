package com.edse.edu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebFragment extends Fragment
{
	View view = null;
	private WebView webView;
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                            Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.details_fragment, container, false);
		 
		 String url = "http://www.google.com";
		 loadUrl(url);
		 
		 return view;
	  }

	  public void loadUrl(String url) {
		  
		  webView = (WebView) view.findViewById(R.id.webview);
		  webView.getSettings().setJavaScriptEnabled(true);
		  webView.loadUrl(url);
		  
	  }
}
