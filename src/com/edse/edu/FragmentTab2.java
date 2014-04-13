package com.edse.edu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
			getActivity().setTitle("Calendar");
			//call method to handle actions when Calendar fragment 1st tab
			DisplayAllEvents(view, inflater, container);
			
		}
		else if(MainActivity.selectedFrag == 4)
		{
			view = inflater.inflate(R.layout.status_matrix_oakley, container, false);
		}
		
		
		if(MainActivity.networkStatus == false)
		{
			//return view = inflater.inflate(R.layout.nointernet_view, container, false);
			//Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
		}
		
			return view;

	}
	
	public void NewsTabRecent(View view, LayoutInflater inflater, ViewGroup container)
	{
		
	    
	   if(MainActivity.networkStatus == false)
	   {
		   //get arraylist of articles from cache/SQLite and rely upon that.
	   }
	   else
	   {
		   articles = MainActivity.articlesReturned;
	   }
		
		
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
        
        
    	//calling article adapter for list of articles in fragment 2.
    	artAdapter = new ArticleAdapter(1,getActivity().getApplicationContext(),specImg,specTitle,specDesc);
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
	
	public void DisplayAllEvents(View view, LayoutInflater inflater, ViewGroup container)
	{
		 ArrayList<String> eventTitles = new ArrayList<String>();
            ArrayList<String> eventDateTimes = new ArrayList<String>();
            //ArrayList<String> eventDescs = new ArrayList<String>();
            ArrayList<String> eventLinks = new ArrayList<String>();
           // ArrayList<String> eventLocations = new ArrayList<String>();
            
            Collections.sort(MainActivity.events);
    		Collections.reverse(MainActivity.events);
            
    		for (Event ev: MainActivity.events)
    		{
    			eventTitles.add(ev.getEventName());
    			eventDateTimes.add(ev.getDateAndTime());
    			eventLinks.add(ev.getEventLink());
    		}
            final String[] evdispTitles = eventTitles.toArray(new String[eventTitles.size()]);
           // final String[] evdispDescs = eventDescs.toArray(new String[eventDescs.size()]);
            final String[] evdispDateTimes = eventDateTimes.toArray(new String [eventDateTimes.size()]);
            final String[] evdispLinks = eventLinks.toArray(new String [eventLinks.size()]);
           // final String[] evdispLocs = eventLocations.toArray(new String[eventLocations.size()]);
            
            
        	
        	
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
    				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    				MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
    				ft.commit();
    				
    			}
     			
     		});
	}
	
}
	
	
