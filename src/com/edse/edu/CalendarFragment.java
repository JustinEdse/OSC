package com.edse.edu;
 
import java.lang.reflect.Field;

import com.actionbarsherlock.app.SherlockFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
 
public class CalendarFragment extends SherlockFragment {
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main, container, false);
      
         ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
         // Set the ViewPagerAdapter into ViewPager
         mViewPager.setBackgroundColor(getResources().getColor(R.color.gray));
     
         mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
         
         
         setHasOptionsMenu(true);
        
        
         getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
         getActivity().setTitle("Calendar");
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