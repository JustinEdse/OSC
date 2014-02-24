package com.edse.edu;

import java.lang.reflect.Method;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;

public class MainActivity extends SherlockFragmentActivity 
{
	
	// Declare Variables
	static Map<String , Event> calendarMap = new HashMap<String, Event>();
	static int selectedFrag = 0;
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	int[] icon;
	Fragment fragment1 = new NewsFragment();
	Fragment fragment2 = new CalendarFragment();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	
	//action bar
	ActionBar actionBar;
	
	
	
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
	
		
		//this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		
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
		mMenuAdapter = new MenuListAdapter(MainActivity.this, title,icon);

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
			}

			public void onDrawerOpened(View drawerView)
			{
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle(R.string.app_name);
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
			}
			else
			{
				mDrawerLayout.openDrawer(mDrawerList);
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

		//setting of a global variable here to carry the selection into other fragments easily.
		selectedFrag = position;

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		FragmentManager manager = getSupportFragmentManager();
		// new fragment replaces older material.
		switch (position)
		{
		
		   // Each time there is a change to another selection in the navigation drawer remaining
		   // fragments need to be popped off the backstack. This solves the problem of a fragment
		   // already existing on the backstack and trying to be added again when the back button is
		   // pressed.
		
		case 0: // news
			manager = this.getSupportFragmentManager();
			for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
			    manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment1);
			
			break;
		case 1:// calendar
			manager = this.getSupportFragmentManager();
			for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
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
		
		else
		{

			// If already in a fragment then the back button should be able to be
			// pressed freely and work as expected provided a change in the navigation
			// drawer doesn't take place.
			super.onBackPressed();
			
		}
	}
	
	@Override
	public void onStart()
	{
		// This just makes the navigation drawer appear open by default when the app starts.
		// This is similar to many apps already on the market.
		 
		super.onStart();
		mDrawerLayout.openDrawer(mDrawerList);
	}
	
	
	
	
	
	
	
}