package com.edse.edu;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.edse.database.Database;
import com.edse.network.ArticleRSSReader;
import com.edse.network.EventRSSReader;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
/***
 * this class is instantiated when the startActivity method corresponding to the splash activity is called.<br>
 * This class extends class SherlocFragmnetActivity and implements the results listener interface. <br>
 * 
 * This class is responsible to initializing urls for Article and Event RSS feeds, initializing the database for caching,
 * setting the Table Size limits for Article_Table, Events_Table, ChangeLogs_Table and KnowIssues_Table.<br>
 * 
 * This is where the fragment instances corresponding to news,Calendar,ChangeLogs and Known Issues are created.<br>
 *
 * This has the following attributes: <br>
 * boolean network Status <br>
 * Map calendarMap <br>
 * ArrayList<Event> events <br>
 * int selectedFrag <br>
 * int movesCount <br>
 * static ActionBarDrawerToggle mDrawerToggle <br>
 * MenuListAdapter mMenuAdapter <br>
 * String[] title <br>
 * 
 * @author kaushikvelindla
 *
 */
public class MainActivity extends SherlockFragmentActivity implements
		ResultsListener
{

	// Declare Variables
	public static ArrayList<Article> articlesReturned = new ArrayList<Article>();
	public static ArrayList<Event> eventsReturned = new ArrayList<Event>();
	public static FragmentTransaction ft = null;

	public static boolean networkStatus = false;
	static Map<Date, ArrayList<Event>> calendarMap = new HashMap<Date, ArrayList<Event>>();
	static ArrayList<Event> events = new ArrayList<Event>();
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
	Fragment fragment3 = new ChangeLgsFragment();
	Fragment fragment4 = new KnownIssFragment();
	Fragment fragment5 = new StatusFragment();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int backCount = 0;
	private static final String NEWSFRAG = "News";
	private static final String CALFRAG = "Calendar";
	private static final String STATUSFRAG = "System Status";
	private static final String ISSUESFRAG = "Known Issues";
	private static final String CHANGES = "Change Logs";
	// public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy", Locale.US);
	// public static final String EXTENDED_DATE_FORMAT =
	// "EEEEEE, MMMMM dd, yyyy";
	public static final SimpleDateFormat extendeddateFormat = new SimpleDateFormat(
			"EEEEEE, MMMMM dd, yyyy", Locale.US);
	public static SimpleDateFormat date_timeFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy hh:mm:ss", Locale.US);
	// for getting rss article feed.
	static String urlArticles = "https://www.osc.edu/press-feed";
	static String urlEvents = "https://osc.edu/feeds/events/all";
	private com.edse.network.ArticleRSSReader artReaderObj;
	private com.edse.network.EventRSSReader eventReaderObj;
	public static Context globalTHIS = null;

	// Maximum size of tables
	public static Database db;
	public static final int ARTICLE_TABLE_SIZE = 30;
	public static final int EVENT_TABLE_SIZE = 30;
	// action bar
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		globalTHIS = this;
		super.onCreate(savedInstanceState);

		// Get the view from drawer_main.xml

		// this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		FontsOverride.setDefaultFont(this, "MONOSPACE", "Roboto-Light.ttf");

		setContentView(R.layout.drawer_main);

		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// Generate title
		title = new String[] { "News", "Calendar", "Change Log", "Known Issues", "System Status" };

		// Generate icon
		icon = new int[] { R.drawable.doc_lines_stright, R.drawable.calendar_2,
				R.drawable.comp, R.drawable.attention,R.drawable.info };

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
				// getSupportActionBar().setTitle(R.string.app_name);
				super.onDrawerOpened(drawerView);

			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{

			selectItem(0);
			getSupportActionBar().setTitle(R.string.app_name);

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

		if (MainActivity.movesCount == 0 && selectedFrag == 1)
		{
			setTitle("Calendar");
		}

		if (MainActivity.movesCount == 0 && selectedFrag == 4)
		{
			setTitle("System Status");
		}

		if (MainActivity.movesCount == 0 && selectedFrag == 0)
		{
			setTitle("News");
		}
		
		if(MainActivity.movesCount == 0 && selectedFrag == 2)
		{
			setTitle("Change Log");
		}
		if(MainActivity.movesCount == 0 && selectedFrag == 3)
		{
			setTitle("Known Issues");
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

		ft = getSupportFragmentManager().beginTransaction();
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
			ft.replace(R.id.content_frame, fragment1, NEWSFRAG);

			break;
		case 1:// calendar
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment2, CALFRAG);
			break;
		case 2:// calendar
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment3, CHANGES);
			break;
		case 3:// calendar
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment4, ISSUESFRAG);
			break;
		case 4:// calendar
			manager = this.getSupportFragmentManager();
			for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
			{
				manager.popBackStack();
			}
			ft.replace(R.id.content_frame, fragment5, STATUSFRAG);
			break;

		}

		ft.commit();

		mDrawerList.setItemChecked(position, true);

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

		FragmentManager manager = this.getSupportFragmentManager();
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

			if (MainActivity.movesCount == 0)
			{
				mDrawerToggle.setDrawerIndicatorEnabled(true);
			}

			if (MainActivity.movesCount == 0 && selectedFrag == 1)
			{
				setTitle("Calendar");
			}

			if (MainActivity.movesCount == 0 && selectedFrag == 2)
			{
				setTitle("Change Log");
			}

			if (MainActivity.movesCount == 0 && selectedFrag == 0)
			{
				setTitle("News");
			}
			
			if(MainActivity.movesCount == 0 && selectedFrag == 3)
			{
				setTitle("Known Issues");
			}
			if(MainActivity.movesCount == 0 && selectedFrag == 4)
			{
				setTitle("System Status");
			}

		}

	}

	@Override
	public void onStart()
	{
		// This just makes the navigation drawer appear open by default when the
		// app starts.
		// This is similar to many apps already on the market.

		super.onStart();

		// executing tasks/calls to the article and event RSS readers.
		// possibility of moving these task calls to splash screen activity?
		// Would be weird to call async methods from
		// fragments to the splash screen though. I also guess that splash
		// activity's life would be over after the 3 seconds...
		// task1 = new GetArticlesFromRSS();
		// task2 = new GetEventsFromRSS();

		// task1.execute();

		// task2.execute();
		// MainActivity.db.ResetEventTable();
		UsableAsync task = new UsableAsync(MainActivity.globalTHIS);
		task.setOnResultsListener(this);
		task.execute();

		AsyncEvent task2 = new AsyncEvent(MainActivity.globalTHIS);
		
		
		task2.setOnResultsListener(new EventsResultsListener()
		{

			@Override
			public void onResultSuccess(ArrayList<Event> result)
			{
				// TODO Auto-generated method stub
				// This first for loop creates the necessary multiple event
				// instances for events that span multiple days
				ArrayList<ArrayList<Event>> lists = new ArrayList<ArrayList<Event>>();
				if (result.size() > 0)
				{

					lists = generateLists(result);
					Log.d("List1",
							"Size of list returned is "
									+ Integer.toString(lists.get(1).size()));
					for (Event ev : lists.get(1))// No duplicates List
					{
						try
						{
							MainActivity.db.addEvent(ev);
						}
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				try
				{
					events = MainActivity.db.getAllEvents();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("List1",
						"Size of db is "
								+ Integer.toString(MainActivity.db
										.getEventsCount()));
				ArrayList<Event> eventinstances = new ArrayList<Event>();
				lists = generateLists(events);
				eventinstances = lists.get(0);
				// events = lists.get(1);

				/*
				 * THis loop then adds the elements from the newly created
				 * arraylist containing multiple instances for events that span
				 * multiple days to the calendar Map,
				 */
				// Clear the map first
				calendarMap.clear();
				for (Event ev : eventinstances)
				{
					ArrayList<Event> events = new ArrayList<Event>();
					if (calendarMap.containsKey(ev.getDate()))
					{
						events = calendarMap.remove((ev.getDate()));
						events.add(ev);
						calendarMap.put(ev.getDate(), events);
					}
					else
					{
						events.add(ev);
						calendarMap.put(ev.getDate(), events);
					}
				}
				
				
				Fragment currentFragment = getSupportFragmentManager()
						.findFragmentByTag(CALFRAG);
				FragmentTransaction fragTransaction = getSupportFragmentManager()
						.beginTransaction();
				
				
				
				if(currentFragment != null)
				{
				fragTransaction.detach(currentFragment);
				}
				
				if(currentFragment!= null)
				{
				fragTransaction.attach(currentFragment);
				}
				fragTransaction.commit();
				
				MainActivity.db.close();
			}

			@Override
			public void onResultFail(int resultCode, String errorMessage)
			{
				// TODO Auto-generated method stub

			}

		});

		task2.execute();
		ListView lv = (ListView) findViewById(R.id.listview_drawer);
		lv.setItemChecked(0, false);

		mDrawerLayout.openDrawer(mDrawerList);

		mDrawerLayout.setFocusableInTouchMode(false);

	}

	@Override
	public void onResume()
	{
		super.onResume();

		// UsableAsync taskResumeArt = new UsableAsync(MainActivity.globalTHIS);
		// taskResumeArt.setOnResultsListener(this);
		// taskResumeArt.execute();

		// check articles and events returned. compare with entries already in
		// SQLite.
		// Delete the X oldest things in the table if there are new updates from
		// either RSS feed.....
		FragmentManager manager = this.getSupportFragmentManager();
		for (int i = 0; i < manager.getBackStackEntryCount(); i++)
		{
			manager.popBackStack();
		}
		MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
		MainActivity.movesCount = 0;
	}

	@Override
	public void onResultSuccess(ArrayList<Article> result)
	{
		// TODO Auto-generated method stub

		// HERE WE NEED TO COMPARE THE SIZE OF THE SQLITE TABLE WITH SIZE
		// OF THE ARTICES RETURNED. IF THERE IS NO DIFFERENCE THEN NOTHING HAS
		// TO BE DONE.
		// IF THE SIZE RETURNED FROM THE RSS FEED IS GREATER WE KNOW A NEW
		// ARTICLE HAS
		// BEEN PUBLISHED. SO, NEW SIZE - OLD SIZE IS THE NUMER WE NEED TO KNOW.
		// ALSO
		// DEPENDING ON THE CURRENT SIZE OF THE SQLITE TABLE, WE MAY NEED TO
		// DELETE A ROW OR TWO.

		MainActivity.articlesReturned = result;
		ArticleAdapter.done = true;
		ArticleAdapter.artCount = result.size();
		ArticleAdapter.savedCount = result.size();

		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentByTag(NEWSFRAG);
		FragmentTransaction fragTransaction = getSupportFragmentManager()
				.beginTransaction();
		
		
		
		if(currentFragment != null)
		{
		fragTransaction.detach(currentFragment);
		}
		
		if(currentFragment!= null)
		{
		fragTransaction.attach(currentFragment);
		}
		fragTransaction.commit();

	}

	@Override
	public void onResultFail(int resultCode, String errorMessage)
	{
		// TODO Auto-generated method stub

	}

	private static ArrayList<Date> parseDate(String input)
	{

		ArrayList<Date> dates = new ArrayList<Date>();
		
		/*
		 * Since date always has "to" in it, we can tell if it is a single date if following the "to" 
		 * the next non whitespace character is a number
		 * For example: Thursday, 20th March 2pm to 5pm
		 * For a multiple date event, the next non whitespace character following the "to" will be a letter
		 * For example: Thursday, 5th March(All Day) to Friday, 6th March(All Day)
		 * OR Thursday, 5th March - 5:00 pm to Friday, 6th March - 12:00pm
		 */
		String cleanedHTML = input;
		String[] tempsplit = cleanedHTML.split("to");
		if(tempsplit.length >1)
		{
			if(Character.isLetter((tempsplit[1].trim().charAt(0))))//Multiple date instances
			{
				String[] splits = cleanedHTML.split("to");
				for (int count = 0; count < splits.length; count++)
				{
					String date = null;
					if(splits[count].contains("("))
					{
					date = splits[count].substring(0,
							splits[count].indexOf('(')).trim();
					}
					else if (splits[count].contains("-")) {
						date = splits[count].substring(0,
								splits[count].indexOf('-')).trim();
					}
					try
					{
						Date tempDate = MainActivity.extendeddateFormat.parse(date);
						dates.add(tempDate);
					}
					catch (ParseException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}
	
			}
			else if (Character.isDigit((tempsplit[1].trim().charAt(0)))) 
			{
				String date = null;
				if(cleanedHTML.contains("("))
				{
					date = cleanedHTML.substring(0, cleanedHTML.indexOf('('))
							.trim();
				}
				else if (cleanedHTML.contains("-")) {
					date = cleanedHTML.substring(0, cleanedHTML.indexOf('-'))
							.trim();
				}
				try
				{
					Date tempDate = MainActivity.extendeddateFormat.parse(date);
					dates.add(tempDate);
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		}
		else if(tempsplit.length == 1)
		{
			if(cleanedHTML.length() > 0)
			{
				String date = null;
				if(cleanedHTML.contains("("))
				{
					date = cleanedHTML.substring(0, cleanedHTML.indexOf('('))
							.trim();
				}
				else if (cleanedHTML.contains("-")) {
					date = cleanedHTML.substring(0, cleanedHTML.indexOf('-'))
							.trim();
				}
				try
				{
					Date tempDate = MainActivity.extendeddateFormat.parse(date);
					dates.add(tempDate);
					
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		if (cleanedHTML.contains("to") && !(cleanedHTML.contains("-")))
//			//Multiple dates to get
//		{
//			String[] splits = cleanedHTML.split("to");
//			int start = 0;
//			for (int count = 0; count < splits.length; count++)
//			{
//				String date = splits[count].substring(0,
//						splits[count].indexOf('(')).trim();
//				// Log.d("Date", date);
//				String date2 = date;
//				try
//				{
//					Date tempDate = MainActivity.extendeddateFormat.parse(date);
//					dates.add(tempDate);
//					// Log.d("Date", tempDate.toString());
//				}
//				catch (ParseException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					// Log.d("Date", "Error");
//				}
//
//			}
//
//		}
//		// If its just one date
//		else if (cleanedHTML.contains("-"))
//		{
//			// Log.d("Obinna", "Else case, one date");
//			String date1 = cleanedHTML.substring(0, cleanedHTML.indexOf('-'))
//					.trim();
//			// Log.d("Date", date1);
//			try
//			{
//				Date tempDate = MainActivity.extendeddateFormat.parse(date1);
//				dates.add(tempDate);
//				// Log.d("Date", tempDate.toString());
//			}
//			catch (ParseException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		else if (cleanedHTML.contains("("))
//		{
//			// Log.d("Obinna", "Else case, one date");
//			String date1 = cleanedHTML.substring(0, cleanedHTML.indexOf('('))
//					.trim();
//			// Log.d("Date", date1);
//			try
//			{
//				Date tempDate = MainActivity.extendeddateFormat.parse(date1);
//				dates.add(tempDate);
//				// Log.d("Date", tempDate.toString());
//			}
//			catch (ParseException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
		return dates;

	}

	public ArrayList<ArrayList<Event>> generateLists(ArrayList<Event> result)
	{
		ArrayList<Event> eventinstances = new ArrayList<Event>();
		ArrayList<Event> noduplicatesList = new ArrayList<Event>();
		for (Event ev : result)
		{
			ArrayList<Date> eventDates = parseDate(ev.getDateAndTime());
			// No date field
			if (eventDates.size() == 0)
			{
				Event createdEvent = new Event(ev.getEventName(), null,
						ev.getDateAndTime(), ev.getEventLink(), ev.getPubDate());
				noduplicatesList.add(createdEvent);
			}
			// If there is a date field
			if (eventDates.size() > 0)
			{
				// Add it to the no duplicates list
				Event createdEvent = new Event(ev.getEventName(),
						eventDates.get(0), ev.getDateAndTime(),
						ev.getEventLink(), ev.getPubDate());
				noduplicatesList.add(createdEvent);
				// Then generate more event instances if needed
				if (eventDates.size() > 1)
				{
					// Event runs on multiple dates
					Date temp1 = eventDates.get(0);
					Date temp2 = eventDates.get(1);
					if (temp1.before(temp2))
					{

						int diff = temp2.getDate() - temp1.getDate();
						for (int y = 0; y <= diff; y++)
						{
							Date tempDate = new Date();
							Calendar cal = Calendar.getInstance();
							cal.setTime(temp1);
							cal.add(Calendar.DATE, y);
							createdEvent = new Event(ev.getEventName(),
									cal.getTime(), ev.getDateAndTime(),
									ev.getEventLink(), ev.getPubDate());
							eventinstances.add(createdEvent);
						}
					}
					else
					{
						int diff = temp1.getDate() - temp2.getDate();
						for (int y = 0; y <= diff; y++)
						{
							Date tempDate = new Date();
							Calendar cal = Calendar.getInstance();
							cal.setTime(temp2);
							cal.add(Calendar.DATE, y);
							createdEvent = new Event(ev.getEventName(),
									cal.getTime(), ev.getDateAndTime(),
									ev.getEventLink(), ev.getPubDate());
									eventinstances.add(createdEvent);

						}
					}

				}
				else
				{
					createdEvent = new Event(ev.getEventName(),
							eventDates.get(0), ev.getDateAndTime(),
							ev.getEventLink(), ev.getPubDate());
					eventinstances.add(createdEvent);
				}
			}

		}

		ArrayList<ArrayList<Event>> lists = new ArrayList<ArrayList<Event>>();
		lists.add(eventinstances);
		lists.add(noduplicatesList);
		return lists;
	}

}