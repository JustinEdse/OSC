package com.edse.edu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.edse.network.*;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView.OnDateChangeListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;

import com.actionbarsherlock.app.SherlockFragment;


public class FragmentTab1 extends SherlockFragment
{
	public static ArrayList<Article> articles = new ArrayList<Article>();
	public static ArrayList<Event> events = new ArrayList<Event>();
	
	private ListView listView = null;
	static String categoryChosen = "";
    View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		
		//get lists from main activity...
		
		events = MainActivity.eventsReturned;
		
		// Get the view from fragmenttab1.xml
		PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.scarlet));
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment 1st tab
		  
		  view = inflater.inflate(R.layout.fragmenttab1cat, container, false);
		  //display a list view of news article categories.
		
		  NewsTabCategories(view, inflater, container);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			
			//we can find out some way to highlight days on the caldendar by using the event list 
			//that we got from the MainActivity. We can iterate through it and get the dates for each event
			//and then go from there.. Just an idea...Who would have thought getting the dates of the articles
			//would have been easier than highlighting days on the calendar.
			
			
			
			
			getActivity().setTitle("Calendar");
			//call method to handle actions when Calendar fragment 1st tab
			view = inflater.inflate(R.layout.activity_calendar_view_fragment, container, false);
			CalendarView calendar;
			calendar = (CalendarView)view.findViewById(R.id.calendar);
			
			
			//calendar.setWeekNumberColor(R.color.black);
			calendar.setOnDateChangeListener(new OnDateChangeListener() {
			
				@Override
				public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) 
				{
					
					String key = Integer.toString(month+1) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year);
					if (MainActivity.calendarMap.containsKey(key))
					{
						//Log.d("Selected Day", "Map isnt empty");
						//DO Fragment transaction to to show detailed list of events
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						Bundle bunds = new Bundle();
						bunds.putString("date", key);
						EventDisplayFragment newFragment = new EventDisplayFragment();
						newFragment.setArguments(bunds);
						ft.replace(R.id.calendar, newFragment);
						ft.addToBackStack(null);
						
						
						MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
						MainActivity.movesCount++;
						//getActivity().setTitle("Content");
						ft.commit();
					}
					else 
					{
						Toast.makeText(getActivity().getApplicationContext(), "There is no event on that day", Toast.LENGTH_SHORT).show();
					}
					//Toast.makeText(getActivity().getApplicationContext(), (month+1)+ "/"+dayOfMonth+"/"+year, Toast.LENGTH_SHORT).show();
					
				}
			});
			
		}
		
		return view;
	}
	  
	public void NewsTabCategories(View viewOuter, LayoutInflater inflater, ViewGroup container)
	{
		//articles = MainActivity.articlesReturned;
		
		ArrayAdapter<CharSequence> catListAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
				R.array.categories, R.layout.custom_list_view);
		
	
		listView = (ListView) view.findViewById(R.id.listViewCategories);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	
		
		listView.setAdapter(catListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				//One list should be used to display all news article items.
				//when each category is selected the article objects within the list will
				//change.
				
				//During this switch the type of article wanted should be set for each choice.
				//Then at the end of the switch statement we make one call to the client component
				//class in the networking package to interact with the server.
				String type = "";
				switch(position)
				{
				
				case 0:
				//supercomputing
					type = "Supercomputing";
					break;
					
				case 1:
				//research
					type = "Research";
					break;
				case 2:
				// computational science
					type = "Computational Scicence";
					break;
				case 3:
				// outreach
					type = "Outreach";
					break;
				case 4:
				// education and training
					type = "Education and Training";
					break;
				case 5:
				// achievements
					type = "Achievements";
					break;
				case 6:
				// cyber infrastructure
					type = "Cyberinfrastructure";
					break;
				case 7:
				//blue collar computing
					type = "Blue Collar Computing";
					break;
				case 8:
				// summer educational programs
					type = "Summer Educational Programs";
					break;
				default:
					break;
				}
				
				
				
				
				//we now have articles in the list that are the same category the user chose.
				 
				categoryChosen = type;
				//iterate through the list to see which type of article type is needed...
				
				
				
				
				
				
				////////////CHANGES HERE////////////////////////////////
				MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				// Locate Position
				// new fragment replaces older material.
				
				MainActivity.movesCount++;
				DisplayFragment newFrag = new DisplayFragment();
				
					ft.replace(R.id.content_frame, newFrag);
				    ft.addToBackStack(null);
				
				ft.commit();
			    
				
			}

		});

}
	
	 
	 
	
}