package com.edse.edu;
 
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

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
 
/**
 * This fragment is used to display a list of events on a certain date.
 * An item click listener is used to launch a web page showing complete event information
 * from the OSC website
 * @author Obinna Ngini
 */
public class EventDisplayFragment extends SherlockFragment {
 
	    //Declare list view to store the event rows
		static int selectedFrag = 0;
		
		ListView displayListView;
		// @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		    	
		    	//getActivity().setTitle("Content");
		    	//GET THE STRING KEY FROM THE BUNDLE passed 
		    	Bundle b = getArguments();
		    	String date = b.getString("date");
		    	Date evDate = new Date();
				try {
					evDate = MainActivity.date_timeFormat.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        View view = inflater.inflate(R.layout.article_display, container, false);
		        ArrayList<String> eventTitles = new ArrayList<String>();
	            ArrayList<String> eventDateTimes = new ArrayList<String>();
	            ArrayList<String> eventLinks = new ArrayList<String>();
				for(Event ev : MainActivity.calendarMap.get(evDate))
	            {
	            	eventTitles.add(ev.getEventName());
	            	eventDateTimes.add(ev.getDateAndTime());
	            	eventLinks.add(ev.getEventLink());
	            	//eventDescs.add(ev.getEventDetails());
	            	//eventLocations.add(ev.getLocation());
	            }
				final String[] evdispTitles = eventTitles.toArray(new String[eventTitles.size()]);
	            final String[] evdispDateTimes = eventDateTimes.toArray(new String [eventDateTimes.size()]);
	            final String[] evdispLinks = eventLinks.toArray(new String [eventLinks.size()]);
//				EventListAdapter evAdapter = new EventListAdapter(getActivity().getApplicationContext(), evdispTitles, evdispDateTimes);
//				ListView listView = (ListView) view.findViewById(R.id.listview);
//				listView.setAdapter(evAdapter);
//				listView.setOnItemClickListener(new OnItemClickListener() {
				ListView displayListView = (ListView) view.findViewById(R.id.listview);
	     		displayListView.setAdapter(new EventListAdapter(getActivity().getApplicationContext(), evdispTitles, evdispDateTimes));
	     		displayListView.setOnItemClickListener(new OnItemClickListener(){

					@Override
	    			public void onItemClick(AdapterView<?> parent, View view,
	    					int position, long id)
	    			{
	    				//The idea is to show a more detailed view of event in this onclick event. 	
	    				
	    				//Launch Web Fragment to show link
	    				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    				WebFragment webFrag = new WebFragment();
	    				String url = "";
	    				
	    				//get the url from each article
	    					url = evdispLinks[position];
	    				
	    				// Pass URL in BUndle
	    				 MainActivity.movesCount++;
	    				Bundle urlExtras = new Bundle();
	    				urlExtras.putString("url", url);
	    				webFrag.setArguments(urlExtras);
	    				
	    				ft.replace(R.id.content_frame, webFrag);
	    				ft.addToBackStack(null);
	    				//MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
	    				ft.commit();
	    				
	    			}
				
				});
		     		
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