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
 
public class EventDisplayFragment extends SherlockFragment {
 
	    //Declare list view to store the event rows
		static int selectedFrag = 0;
		
		ListView displayListView;
		// @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		    	
		    	//((MainActivity) getActivity()).setTitle(FragmentTab1.categoryChosen);
		    	getActivity().setTitle("Content");
		    	//GET THE STRING KEY FROM THE BUNDLE passed 
		    	Bundle b = getArguments();
		    	String date = b.getString("date");
		        View view = inflater.inflate(R.layout.article_display, container, false);
		     
		        //Create arrays that can be passed to the event adapter, and so populate the list view
		            ArrayList<String> eventTitles = new ArrayList<String>();
		            ArrayList<String> eventDates = new ArrayList<String>();
		            ArrayList<String> eventDescs = new ArrayList<String>();
		            ArrayList<String> eventTimes = new ArrayList<String>();
		            ArrayList<String> eventLocations = new ArrayList<String>();
		            
		            for(Event ev : MainActivity.calendarMap.get(date))
		            {
		            	eventTitles.add(ev.getEventName());
		            	eventDates.add(ev.getDate());
		            	eventTimes.add(ev.getTime());
		            	eventDescs.add(ev.getEventDetails());
		            	eventLocations.add(ev.getLocation());
		            }
		            
		            String[] evdispTitles = eventTitles.toArray(new String[eventTitles.size()]);
		            String[] evdispDescs = eventDescs.toArray(new String[eventDescs.size()]);
		            String[] evdispDates = eventDates.toArray(new String [eventDates.size()]);
		            String[] evdispTimes = eventTimes.toArray(new String [eventTimes.size()]);
		            String[] evdispLocs = eventLocations.toArray(new String[eventLocations.size()]);
		            
		            
		        	
		        	
		     		displayListView = (ListView) view.findViewById(R.id.listview);
		     		displayListView.setAdapter(new EventAdapter(getActivity().getApplicationContext(), 
		     				evdispTitles, evdispDescs, evdispDates, evdispTimes, evdispLocs));
		     		
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