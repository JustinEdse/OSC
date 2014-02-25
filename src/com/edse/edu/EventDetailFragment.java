
package com.edse.edu;
 
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class EventDetailFragment extends SherlockFragment {
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 	Bundle b = getArguments();
		 	//Get the details of event from bundle
	    	String evtitle = b.getString("title");
	    	String evdesc = b.getString("desc");
	    	String evdate = b.getString("date");
	    	String evtime = b.getString("time");
	    	String evlocation = b.getString("loc");
	    	View view = inflater.inflate(R.layout.event_details, null);
			TextView title = (TextView) view.findViewById(R.id.event_detail_title);
			TextView date_time = (TextView) view.findViewById(R.id.event_detail_date_time);
			TextView desc = (TextView) view.findViewById(R.id.event_detail_desc);
			TextView location = (TextView) view.findViewById(R.id.event_detail_location);
			
			title.setText(evtitle);
			date_time.setText(evdate + " - " + evtime);
			desc.setText(evdesc);
			location.setText(evlocation);  
			
		return view;
	 }
	 
}