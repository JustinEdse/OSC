package com.edse.edu;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalendarViewFragment extends SherlockFragment {
	

	CalendarView calendar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.activity_calendar_view_fragment, container, false);
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_calendar_view_fragment);
		
		calendar = (CalendarView)view.findViewById(R.id.calendar);
		
		getActivity().setTitle("Calendar");
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
	
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) 
			{
				Toast.makeText(getActivity().getApplicationContext(), dayOfMonth+ "/"+month+"/"+year, Toast.LENGTH_LONG).show();
				
			}
		});
		
		setHasOptionsMenu(true);
        
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
       
       
		return view;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.calendar_view, menu);
//		return true;
//	}

}
