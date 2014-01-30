package com.edse.edu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;

import com.actionbarsherlock.app.SherlockFragment;


public class FragmentTab1 extends SherlockFragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Get the view from fragmenttab1.xml

		View view = null;
		
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment 1st tab
		  
		  view = inflater.inflate(R.layout.fragmenttab1, container, false);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			//call method to handle actions when Calendar fragment 1st tab
			
			view = inflater.inflate(R.layout.fragmenttab7, container, false);
			CalendarTab1(view);
		}
		return view;
	}
	
	public void CalendarTab1(View view)
	{
		Button myButton = (Button)view.findViewById(R.id.button1);
		
		myButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				Toast.makeText(getActivity(), "I was just clicked!", Toast.LENGTH_LONG).show();
				
			}
		});
		
	}
}