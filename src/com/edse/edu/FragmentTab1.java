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
	private ListView listView = null;
	static String categoryChosen = "";
    View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
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
			//Hardcoded events for calendar testing
	        Event test1 = new Event();   
	        test1.addTitle("Monthly HPC Tech Talk");
	        test1.addEventDetails("Monthly HPC Tech Talk, conducted via WebEX. This call is intended for researchers actively using our systems to interact with OSC staff to learn about recent changes to our environment, ask questions, raise concerns, and learn about an advanced topic. This month's advanced topic will be the utilization of NVIDIA GPUs found on OSC's production HPC clusters for computational chemistry work.We are soliciting feedback on the format, topics, and suggestions for future advanced topics.Please register for the WebEX session here; a reminder email will be sent in advance of the event.");
			test1.addDate("2/18/2014");
			test1.addLocation("WebEX");
			test1.addTime("4:00pm to 5:00pm");
			Event test2 = new Event();
			test2.addTitle("HPC System Downtime");
			test2.addDate("2/11/2014");
			test2.addTime("(All Day)");
			Event test3 = new Event();
			test3.addTitle("XSEDE HPC Monthly Workshop - Big Data");
			test3.addTime("11:00am to 5:00pm");
			test3.addDate("2/4/2014");
			test3.addEventDetails("XSEDE along with the Pittsburgh Supercomputing Center are pleased to announce a one day Big Data workshop, to be held February 4, 2014.This workshop will focus on topics such as Hadoop and SPARQL.Due to demand, this workshop will be telecast to several satellite sites.This workshop is NOT available via a webcast.The site list, registration pages and agenda will be available soon.Register by following the link to View Session Details of your preferred location.Please address any questions to Tom Maiden at tmaiden@psc.edu\nVisit https://portal.xsede.org/course-calendar/-/training-user/class/161 for more information");
			test3.addLocation("Ohio Supercomputer Center- Bale Conference Room");
			
			/**In the future, we check to see if the date of the event already exists in the map.
			 * If so, we add the event to the list of events for the date.
			 * If not, we create a new array list, throw the event into the list and then add the list to the map.
			 */
			ArrayList<Event>temp = new ArrayList<Event>();
			temp.add(test1);
			MainActivity.calendarMap.put(test1.getDate(), temp);
			temp = new ArrayList<Event>();
			temp.add(test2);
			MainActivity.calendarMap.put(test2.getDate(), temp);
			temp = new ArrayList<Event>();
			temp.add(test3);
			MainActivity.calendarMap.put(test3.getDate(), temp);
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
				
				//make call to client component to interaction with network.
				ArrayList<Article> articlesInList = new ArrayList<Article>();
				articlesInList = ClientCompnt.getArticlesByType(type);
				
				//we now have articles in the list that are the same category the user chose.
				 
				categoryChosen = type;
				

				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				// Locate Position
				// new fragment replaces older material.
				DisplayFragment newFrag = new DisplayFragment();
				
					ft.replace(R.id.content_frame, newFrag);
				    ft.addToBackStack(null);
				
				ft.commit();
			    
				
			}

		});

}
	
	 
	 
	
}