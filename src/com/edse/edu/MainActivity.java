package com.edse.edu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;

public class MainActivity extends SherlockFragmentActivity
{

	// Declare Variables
	static Map<String, ArrayList<Event>> calendarMap = new HashMap<String, ArrayList<Event>>();
	static int selectedFrag = 0;
	static int movesCount = 0;

	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	static ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	int[] icon;
	Fragment fragment1 = new NewsFragment();
	Fragment fragment2 = new CalendarFragment();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int backCount = 0;

	// action bar
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// Hardcoded events for calendar testing
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

		/**
		 * In the future, we check to see if the date of the event already
		 * exists in the map. If so, we add the event to the list of events for
		 * the date. If not, we create a new array list, throw the event into
		 * the list and then add the list to the map.
		 */
		ArrayList<Event> temp = new ArrayList<Event>();
		temp.add(test1);
		MainActivity.calendarMap.put(test1.getDate(), temp);
		temp = new ArrayList<Event>();
		temp.add(test2);
		MainActivity.calendarMap.put(test2.getDate(), temp);
		temp = new ArrayList<Event>();
		temp.add(test3);
		MainActivity.calendarMap.put(test3.getDate(), temp);
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml

		// this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		FontsOverride.setDefaultFont(this, "MONOSPACE", "Roboto-Light.ttf");
		;

		setContentView(R.layout.drawer_main);

		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// Generate title
		title = new String[] { "News", "Calendar" };

		// Generate icon
		icon = new int[] { R.drawable.doc_lines_stright, R.drawable.calendar_2 };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass string arrays to MenuListAdapter
		mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close)
		{

			public void onDrawerClosed(View view)
			{
				// TODO Auto-generated method stub
				setTitle(title[selectedFrag]);
				super.onDrawerClosed(view);
				backCount = 0;
			}

			public void onDrawerOpened(View drawerView)
			{
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle(mDrawerTitle);
				//getSupportActionBar().setTitle(R.string.app_name);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{
			selectItem(0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getItemId() == android.R.id.home)
		{

			if (mDrawerLayout.isDrawerOpen(mDrawerList))
			{
				mDrawerLayout.closeDrawer(mDrawerList);
				
				
				backCount = 0;
			}
			else
			{

				if (mDrawerToggle.isDrawerIndicatorEnabled())
				{
					mDrawerLayout.openDrawer(mDrawerList);
				}
				else
				{

					MainActivity.movesCount--;
					// Toast.makeText(getApplicationContext(), movesCount,
					// Toast.LENGTH_SHORT).show();

					if (MainActivity.movesCount == 0)
					{
						mDrawerToggle.setDrawerIndicatorEnabled(true);
					}

					super.onBackPressed();
				}

			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			selectItem(position);
		}
	}

	private void selectItem(int position)
	{

		// setting of a global variable here to carry the selection into other
		// fragments easily.
		selectedFrag = position;

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		FragmentManager manager = getSupportFragmentManager();
		// new fragment replaces older material.
		switch (position)
		{

		// Each time there is a change to another selection in the navigation
		// drawer remaining
		// fragments need to be popped off the backstack. This solves the
		// problem of a fragment
		// already existing on the backstack and trying to be added again when
		// the back button is
		// pressed.

		case 0: // news
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment1);

			break;
		case 1:// calendar
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment2);
			break;
		}

		ft.commit();

		mDrawerList.setItemChecked(position, true);

		// Get the title followed by the position
		setTitle(title[position]);

		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public void onBackPressed()
	{
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0)
		{
			// If there are back-stack entries, leave the FragmentActivity
			// implementation take care of them.

			manager.popBackStack();

		}
		
		

		if (mDrawerToggle.isDrawerIndicatorEnabled())
		{

			if (MainActivity.movesCount == 0
					&& mDrawerLayout.isDrawerOpen(mDrawerList) == false)
			{
				mDrawerLayout.openDrawer(mDrawerList);

			}

			if (MainActivity.movesCount == 0
					&& mDrawerLayout.isDrawerOpen(mDrawerList))
			{
				backCount++;

				// I had to write these IF statements in kind of an odd way.
				// Originally I just had the check if the backCount was equal to
				// 2 and call finish
				// before the toast. However when I did this the toast ran
				// anyway even after calling finish() on the app.
				if (backCount == 1)
				{
					Toast.makeText(getApplicationContext(),
							"Press back again to exit OSC.", Toast.LENGTH_SHORT)
							.show();
				}
				else if (backCount == 2)
				{
					finish();
				}

			}

			// If already in a fragment then the back button should be able to
			// be
			// pressed freely and work as expected provided a change in the
			// navigation
			// drawer doesn't take place.
		}
		else 
		{
			
			MainActivity.movesCount--;
			
			if(MainActivity.movesCount == 0)
			{
				mDrawerToggle.setDrawerIndicatorEnabled(true);
			}
			
		}
		// CHANGES!!!!!!!!/////////////////////////////////

	}

	@Override
	public void onStart()
	{
		// This just makes the navigation drawer appear open by default when the
		// app starts.
		// This is similar to many apps already on the market.

		super.onStart();
		ListView lv = (ListView) findViewById(R.id.listview_drawer);
		lv.setItemChecked(0, false);
		mDrawerLayout.openDrawer(mDrawerList);
		mDrawerLayout.setFocusableInTouchMode(false);

	}

}