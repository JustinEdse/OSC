package com.edse.edu;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.edse.network.EventRSSReader;

public class FragmentTab2 extends SherlockFragment
{
	public static ArrayList<Article> articles = new ArrayList<Article>();
	public static ArrayList<Event> events = new ArrayList<Event>();
	public static View savedView;
	public static LayoutInflater savedInflater;
	public static ViewGroup savedContainer;
	public static boolean done = false;
	public static int count = 0;
	ListView listViewRecent;
	ArticleAdapter artAdapter;
	
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		articles = MainActivity.articlesReturned;
		savedInflater = inflater;
		savedContainer = container;
		
		
		PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.scarlet));
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.article_display, container, false);
		
		savedView = view;
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment recent 2nd tab
		  
		  NewsTabRecent(view, inflater, container);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			//call method to handle actions when Calendar fragment 1st tab
			getActivity().setTitle("Calendar");
			view = inflater.inflate(R.layout.article_display, container, false);
			 ArrayList<String> eventTitles = new ArrayList<String>();
	            ArrayList<String> eventDates = new ArrayList<String>();
	            ArrayList<String> eventDescs = new ArrayList<String>();
	            ArrayList<String> eventTimes = new ArrayList<String>();
	            ArrayList<String> eventLocations = new ArrayList<String>();
	            
	            //Get all events stored in all arraylists in the map
	            for (String date : MainActivity.calendarMap.keySet())
	            {
		            for(Event ev : MainActivity.calendarMap.get(date))
		            {
		            	eventTitles.add(ev.getEventName());
		            	eventDates.add(ev.getDate());
		            	eventTimes.add(ev.getTime());
		            	eventDescs.add(ev.getEventDetails());
		            	eventLocations.add(ev.getLocation());
		            }
	            }
	            
	            final String[] evdispTitles = eventTitles.toArray(new String[eventTitles.size()]);
	            final String[] evdispDescs = eventDescs.toArray(new String[eventDescs.size()]);
	            final String[] evdispDates = eventDates.toArray(new String [eventDates.size()]);
	            final String[] evdispTimes = eventTimes.toArray(new String [eventTimes.size()]);
	            final String[] evdispLocs = eventLocations.toArray(new String[eventLocations.size()]);
	            
	            
	        	
	        	
	     		ListView displayListView = (ListView) view.findViewById(R.id.listview);
	     		displayListView.setAdapter(new EventListAdapter(getActivity().getApplicationContext(), 
	     				evdispTitles, evdispDates, evdispTimes));
	     		displayListView.setOnItemClickListener(new OnItemClickListener(){

	    			@Override
	    			public void onItemClick(AdapterView<?> parent, View view,
	    					int position, long id)
	    			{
	    				//The idea is to show a more detailed view of event in this onclick event. 	
	    				
	    				//Create a fragment to show more detailed event information
	    				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    				EventDetailFragment fragment = new EventDetailFragment();
	    				Bundle bunds = new Bundle();
	    				//Pass title, description, date, time and location to the fragment
	    				bunds.putString("title", evdispTitles[position]);
	    				bunds.putString("desc", evdispDescs[position]);
	    				bunds.putString("date", evdispDates[position]);
	    				bunds.putString("time", evdispTimes[position]);
	    				bunds.putString("loc", evdispLocs[position]);
	    				fragment.setArguments(bunds);
	    				
	    				
	    				MainActivity.movesCount++;
	    				ft.replace(R.id.content_frame, fragment);
	    				ft.addToBackStack(null);
	   
	    				MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
	    				ft.commit();
	    				
	    			}
	     			
	     		});
			
		}
		
		
		
		
		
		return view;
		
		
	}
	
	public void NewsTabRecent(View view, LayoutInflater inflater, ViewGroup container)
	{
		//Most of this section will be different in the final version of the app. Right now this is
		//hard coded somewhat like the other section for client UI viewing purposes.
	    
	    
		articles = MainActivity.articlesReturned;
		
		getActivity().setTitle("News");
		
		
		
		//CHECK IF NOT DONE!!!!!!!!!!!!!!!!!!!
        ArrayList<String> testTitle = new ArrayList<String>();
        ArrayList<String> testDesc = new ArrayList<String>();
        ArrayList<Bitmap> img = new ArrayList<Bitmap>();
        
        for(Article art : articles)
        {
        	testTitle.add(art.getTitle());
        	testDesc.add(art.getsubDesc());
        	img.add(art.getPreviewImage());
        }
        
        String[] specTitle = testTitle.toArray(new String[testTitle.size()]);
        String[] specDesc = testDesc.toArray(new String[testDesc.size()]);
        Bitmap[] specImg = img.toArray(new Bitmap[img.size()]);
        
        
    	
    	artAdapter = new ArticleAdapter(getActivity().getApplicationContext(),specImg,specTitle,specDesc);
 		listViewRecent = (ListView) view.findViewById(R.id.listview);
 		listViewRecent.setAdapter(artAdapter);
		
		
 		listViewRecent.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				
				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				WebFragment webFrag = new WebFragment();
				String url = "";
				
				//get the url from each article
				for(int i = 0; i < articles.size(); i++)
				{
					url = articles.get(position).getLink();
				}
				
				// making a bundle and passing setting these arguments so another fragment can receive them.
				// This is very similar with making a bundle and passing it to another activity via an
				// intent.
				 MainActivity.movesCount++;
				Bundle urlExtras = new Bundle();
				urlExtras.putString("url", url);
				webFrag.setArguments(urlExtras);
				
				ft.replace(R.id.content_frame, webFrag);
				ft.addToBackStack(null);
				MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
				ft.commit();
				
				
				
			}
 			
 		});
   
	}


	
	
	
}