package com.edse.edu;

import java.util.ArrayList;
import java.util.List;

import com.edse.network.*;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.AdapterView.OnItemClickListener;
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

		
		
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment 1st tab
		  
		  view = inflater.inflate(R.layout.fragmenttab1cat, container, false);
		  //display a list view of news article categories.
		
		  NewsTabCategories(view, inflater, container);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			//call method to handle actions when Calendar fragment 1st tab
			
			view = inflater.inflate(R.layout.fragmenttab1, container, false);
			
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
					type = "supercomputing";
					break;
					
				case 1:
				//research
					type = "research";
					break;
				case 2:
				// computational science
					type = "computational scicence";
					break;
				case 3:
				// outreach
					type = "outreach";
					break;
				case 4:
				// education and training
					type = "education and training";
					break;
				case 5:
				// achievements
					type = "achievements";
					break;
				case 6:
				// cyber infrastructure
					type = "infrastructure";
					break;
				case 7:
				//blue collar computing
					type = "blue collar computing";
					break;
				case 8:
				// summer educational programs
					type = "summer educational programs";
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