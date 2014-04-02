package com.edse.edu;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.edse.network.*;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
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

	private FragmentTransaction t = null;
	private CaldroidFragment caldroidFragment = null;
	private FragmentTransaction eventFragmentTransaction = null;

	private ListView listView = null;
	static String categoryChosen = "";
	View view = null;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("testing", "Entering into onCreateView");

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
		else if(MainActivity.selectedFrag == 1) {
			/*
			 * Anurag. Trying out the Caldroid Calendar 
			 */
			view = inflater.inflate(R.layout.calendar_view_host, container, false);
			CalendarFragmentSetup(view, inflater, container);

		}

		else if(MainActivity.selectedFrag == 2)
		{
			view = inflater.inflate(R.layout.status_matrix_glenn, container, false);
			GlennStatus(view, inflater, container);
		}

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d("testing", "Entering into onHiddenChanged. hidden: " + hidden);
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
					type = "Computational Science";
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





				DisplayFragment.modifiedListArt.clear();
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
	/*
	 * Note: Later, we can modify the code for optimization, to call caldroidFragment.setBackgroundResourceForDates(backgroundForDateMap);
	 * method. Also we may want to set different colors for events - say to differentiate between past and new events.
	 */

	private void CalendarFragmentSetup (View viewOuter, LayoutInflater inflater, ViewGroup container) {

		getActivity().setTitle("Calendar");
		caldroidFragment = new CaldroidFragment();
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
		caldroidFragment.setArguments(args);

		setColorToEvents(caldroidFragment);

		// Attach to the activity

		t = this.getChildFragmentManager().beginTransaction();
//		t = getActivity().getSupportFragmentManager().beginTransaction();			
		t.replace(R.id.calendar1, caldroidFragment);
		Log.d("testing", "Trying to do the addtobackstack thing");
		t.addToBackStack(null);
		// What does this mDrawerToggle and movesCount do ????????????????

		//MainActivity.movesCount++;
		t.commit();

		// setting up Listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				//String strDate = MainActivity.dateFormat.format(date);
				//Log.d("anurag", "After formatting Date: " + strDate);
				Log.d("anurag", "Keys: " + MainActivity.calendarMap.keySet());
				//strDate = hackFirstZero(strDate);
				//Log.d("What string key is", strDate);
				if (MainActivity.calendarMap.containsKey(date)) {
					Log.d("anurag", "The date has events assiciated with it.");

					//					t.remove(caldroidFragment);
					t.detach(caldroidFragment);
					// I have no idea why I did the decrement thing
					//MainActivity.movesCount--;
					MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					// Launsh fragment showing the list of events on that day.

					MainActivity.movesCount++;
					EventDisplayFragment newFrag= new EventDisplayFragment();
					Bundle bunds = new Bundle();
					bunds.putString("date", MainActivity.date_timeFormat.format(date));
					newFrag.setArguments(bunds);
					//Log.d("Obinna", "Date sent to event display fragement");
					ft.replace(R.id.content_frame, newFrag);
					ft.addToBackStack(null);
					ft.commit();

				}
				else 
				{
					//int id = view.getId();
					Toast.makeText(getActivity().getApplicationContext(), "There is no event on that day", Toast.LENGTH_SHORT).show();
				}

			}

			/**
			 * The String date received from onSelectDate callback method contains date in format: mm/dd/yyyy. For months like Feb, the mm part
			 * contains: 02. But the String key entered by us in test data does not contain 02 (it contains just 2). This causes string mismatch.
			 *  This method will check to see if the Date string starts with a 0, if yes, it will remove it.
			 * @param strDate
			 * @return
			 */
			private String hackFirstZero(String strDate) {
				if (strDate.startsWith("0")) {
					strDate = strDate.substring(1);
				}
				return strDate;
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				onSelectDate(date, view);
			}


		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	private void setColorToEvents(CaldroidFragment caldroidFragment) {
		Date dateObj = null;
		for (Date strDate : MainActivity.calendarMap.keySet()) {
			//			try {
			//				dateObj = MainActivitydateFormat.parse(strDate);
			//			} catch (ParseException e) {
			//				Log.e("anurag", "Error when parsing date: " + strDate, e);
			//				continue;
			//			}
			caldroidFragment.setBackgroundResourceForDate(R.color.green, strDate);
			caldroidFragment.setTextColorForDate(R.color.white, strDate);
		}

	}

	public void GlennStatus(View vGStatus, LayoutInflater inflaterGStatus, ViewGroup containerGStatus)
	{

		//set glenn status images in matrix....
		//top row = cpu report, load report.
		//bottom row = memory report, network report
	}


}