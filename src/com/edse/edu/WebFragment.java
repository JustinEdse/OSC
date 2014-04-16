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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.graphics.Bitmap;
/***
 * Web Fragment class represents the View Fragment rendering the web-page corresponding to the url set for this particular view. 
 * @author kaushikvelindla
 *
 */
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
		
		final ProgressBar proBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		webView = (WebView) view.findViewById(R.id.webview);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		
		
		//added progress loading circle to web view when waiting on webpage to display. This
		//is a simple little concept but I figured it's better than having nothing for the user
		//to see.
		webView.setWebViewClient(new WebViewClient(){
			
			
			@Override 
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				//when waiting the loading circle is visible
				proBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				//when page has been loaded it's invisible
				proBar.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
			}
		});
		
		
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
