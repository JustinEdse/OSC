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
			Bundle savedInstanceState)
	{
		// "url" is the key for receiving the string from the bundle.
		Bundle bundle = getArguments();
		String url = bundle.getString("url");
		
		View view = (View) inflater.inflate(R.layout.details_fragment,
				container, false);

		webView = (WebView) view.findViewById(R.id.webview);

		webView.loadUrl(url);

		return view;
	}

}
