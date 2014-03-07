package com.edse.edu;
 
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

 
public class FragmentTab3 extends SherlockFragment {
 
	ListView upcomingList = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
    	
        // Get the view from fragmenttab2.xml
    	PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.scarlet));
    	View view = inflater.inflate(R.layout.article_display, container, false);
		
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment upcoming 3rd tab
		  
		  NewsTabUpcoming(view, inflater, container);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			//call method to handle actions when Calendar fragment 3rd tab: Full calendar fragment
			getActivity().setTitle("Calendar");
			//launchCalendarView(view, inflater, container);
			view = inflater.inflate(R.layout.fragmenttab1, container, false);
		}
		return view;
      
    }
    
    public void NewsTabUpcoming(View view, LayoutInflater inflater, ViewGroup container)
	{
		//Most of this section will be different in the final version of the app. Right now this is
		//hard coded somewhat like the other section for client UI viewing purposes.
	
    	  // PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerTabStrip);
           //pagerTabStrip.setDrawFullUnderline(true);
           //pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.scarlet));
           
    	
    	ArrayList<Article> upcomingTest = new ArrayList<Article>();
        //Article upcomingArtOne = new Article("XSEDE HPC Monthly Workshop - Big Data",
        	//	"Tuesday, February 4, 2014 - 11:00am to 5:00pm", "upcoming", R.drawable.osclogo, "01/20/2014");

        //since we don't actually have a list of Articles retrieved from the server I added these to an arraylist
        //myself to simulate what we might have...
        //upcomingTest.add(upcomingArtOne);
        ArrayList<String> testTitle = new ArrayList<String>();
        ArrayList<String> testDesc = new ArrayList<String>();
        
        //Later we need to check if there is an image when getting from the server. If not then just assign the generic OSC logo.
        ArrayList<Integer> img = new ArrayList<Integer>();
        
        for(Article art : upcomingTest)
        {
        	testTitle.add(art.getTitle());
        	testDesc.add(art.getsubDesc());
        	img.add(art.getPreviewImage());
        }
        
        String[] specTitle = testTitle.toArray(new String[testTitle.size()]);
        String[] specDesc = testDesc.toArray(new String[testDesc.size()]);
        Integer[] specImg = img.toArray(new Integer[img.size()]);
        
        
        
    	
 		upcomingList = (ListView) view.findViewById(R.id.listview);
 		upcomingList.setAdapter(new ArticleAdapter(getActivity().getApplicationContext(), 
 				specImg,specTitle, specDesc));
    
 		upcomingList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				WebFragment webFrag = new WebFragment();
				String url = "";
				switch(position)
				{
				case 0:
					url = "https://www.osc.edu/calendar/events/xsede_hpc_monthly_workshop_big_data";
				    break;
				default:
					url = "http://www.google.com";
					break;
					
				}
				
				
				 MainActivity.movesCount++;
				Bundle urlExtras = new Bundle();
				urlExtras.putString("url", url);
				webFrag.setArguments(urlExtras);
				
				ft.replace(R.id.content_frame, webFrag);
				ft.addToBackStack(null);
				MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
				ft.commit();
				
				
			}
 			
 		});
   
	}
    
	
}