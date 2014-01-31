package com.edse.edu;
 
import java.lang.reflect.Field;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
 
public class DisplayFragment extends SherlockFragment {
 
	    //declare variables for custom list view and article previews.
		static int selectedFrag = 0;
		
		ListView mDrawerList;
		
		MenuListAdapter mMenuAdapter;
		String[] title;
		int[] icon;

		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_display, container, false);
        // Locate the ViewPager in viewpager_main.xml
       // ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        // Set the ViewPagerAdapter into ViewPager
        //mViewPager.setBackgroundColor(getResources().getColor(R.color.gray));
    
       // mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
     // Get the Title
     	
        //SET LIST LAYOUT WITH MENU LIST ADAPTER. PUT ARTICLE OBJECT INFORMATION INTO THE LIST.

     		// Generate title
     		title = new String[] { "News", "Calendar" };

     		// Generate icon
     		icon = new int[] { R.drawable.newsicon, R.drawable.calendaricon };

     		// Locate DrawerLayout in drawer_main.xml
     		//mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

     		// Locate ListView in drawer_main.xml
     		//mDrawerList = (ListView) findViewById(R.id.listview_drawer);

     		// Set a custom shadow that overlays the main content when the drawer
     		// opens
     		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
     			//	GravityCompat.START);

     		// Pass string arrays to MenuListAdapter
     		//mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon);

     		// Set the MenuListAdapter to the ListView
     		//mDrawerList.setAdapter(mMenuAdapter);

        
        return view;
    }
 
    @Override
    public void onDetach() {
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