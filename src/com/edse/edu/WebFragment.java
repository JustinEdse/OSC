package com.edse.edu;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.edse.database.Database;
import com.edse.network.ArticleRSSReader;
import com.edse.network.EventRSSReader;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;

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
		//getActivity().setProgressBarIndeterminateVisibility(true);
		View view = (View) inflater.inflate(R.layout.details_fragment,
				container, false);

		// getting the web view XML layout. The XML mostly has nothing in it
		// just a blank page with full parent so the web browser content can
		// be displayed.
		webView = (WebView) view.findViewById(R.id.webview);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);
		
		
        getActivity().setTitle("Content");
        
		setHasOptionsMenu(true);
        
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		return view;
	}
	
	 @Override
	    public void onDetach() {
		 if(MainActivity.selectedFrag == 1)
	    	{
	    		getActivity().setTitle("Calendar");
	    	}
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
