package com.edse.edu;
 
import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

 
public class FragmentTab3 extends SherlockFragment {
 
	ListView upcomingList = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
    
    	View view = inflater.inflate(R.layout.article_display, container, false);
		
		if(MainActivity.selectedFrag == 0)
		{
		  //should call a method to handle News fragment upcoming 3rd tab
		  
		  NewsTabUpcoming(view, inflater, container);
		}
		else if(MainActivity.selectedFrag == 1)
		{
			//call method to handle actions when Calendar fragment 1st tab
			
			view = inflater.inflate(R.layout.fragmenttab1, container, false);
			
		}
		return view;
      
    }
    
    public void NewsTabUpcoming(View view, LayoutInflater inflater, ViewGroup container)
	{
		//Most of this section will be different in the final version of the app. Right now this is
		//hard coded somewhat like the other section for client UI viewing purposes.
	
    	
    	ArrayList<Article> upcomingTest = new ArrayList<Article>();
        Article upcomingArtOne = new Article("XSEDE HPC Monthly Workshop - Big Data",
        		"Tuesday, February 4, 2014 - 11:00am to 5:00pm", "upcoming", R.drawable.osclogo, "01/20/2014");

        //since we don't actually have a list of Articles retrieved from the server I added these to an arraylist
        //myself to simulate what we might have...
        upcomingTest.add(upcomingArtOne);
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
				switch(position)
				{
				  
				}
				
			}
 			
 		});
   
	}
	
}