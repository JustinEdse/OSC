package com.edse.edu;

import java.lang.reflect.Field;

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

		// getting the web view XML layout. The XML mostly has nothing in it
		// just a blank page with full parent so the web browser content can
		// be displayed.
		webView = (WebView) view.findViewById(R.id.webview);

		webView.loadUrl(url);

		return view;
	}
	
	 @Override
	    public void onDetach() {
	        super.onDetach();
	        try {
	            Field childFragmentManager = Fragment.class
	                    .getDeclaredField("mChildFragmentManager");
	            childFragmentManager.setAccessible(true);
	            childFragmentManager.set(this, null);
	            
	        } catch (NoSuchFieldException e) {
	            throw new RuntimeException(e);
	        } catch (IllegalAccessException e) {
	            throw new RuntimeException(e);
	        }
	    }

}
