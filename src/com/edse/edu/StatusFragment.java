package com.edse.edu;
 
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

public class StatusFragment extends SherlockFragment {
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main, container, false);
        // Locate the ViewPager in viewpager_main.xml
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        // Set the ViewPagerAdapter into ViewPager
        mViewPager.setBackgroundColor(getResources().getColor(R.color.gray));
      ;
		// Capture listview menu item click
    
       //here is where the 2 or 3 tabs associated with news are set.
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        
        setHasOptionsMenu(true);
        
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle("System Status");
        
        
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