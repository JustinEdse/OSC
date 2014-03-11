package com.edse.edu;
 
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
 
public class DisplayFragment extends SherlockFragment{
 
	    //declare variables for custom list view and article previews.
		static int selectedFrag = 0;
	
		public static String catChosen = FragmentTab1.categoryChosen;
		ListView displayListView;
		ArrayList<Article> disArticles = new ArrayList<Article>();
		public static ArrayList<Article> modifiedListArt = new ArrayList<Article>();

		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	((MainActivity) getActivity()).setTitle(FragmentTab1.categoryChosen);
    	
    	
    	if(MainActivity.networkStatus == false)
 	   {
 		   //get arraylist of articles from cache/SQLite and rely upon that.
 	   }
 	   else
 	   {
 		   disArticles = MainActivity.articlesReturned;
 	   }
    	
    	
        View view = inflater.inflate(R.layout.article_display, container, false);
        // Locate the ViewPager in viewpager_main.xml
       // ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        // Set the ViewPagerAdapter into ViewPager
        //mViewPager.setBackgroundColor(getResources().getColor(R.color.gray));
    
       // mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
     // Get the Title
     	
        
            ArrayList<String> testTitle = new ArrayList<String>();
            ArrayList<String> testDesc = new ArrayList<String>();
            ArrayList<Bitmap> img = new ArrayList<Bitmap>();
            //ArrayList<ArrayList<String>> type= new ArrayList<ArrayList<String>>();
            
            
            for(Article art : disArticles)
            {
            	
            	
            	
            	//type.add(art.getType());
            	
            	
            	if(art.getType().contains(catChosen))
            	{
            		modifiedListArt.add(art);
            		testTitle.add(art.getTitle());
                	testDesc.add(art.getsubDesc());
                	img.add(art.getPreviewImage());
            	}
            	
            	
            	
            	
            	
            }
            
            //after this testTitle, testDesc, and img should have all the articles according to the category chosen.
            
            
            
            
            String[] specTitle = testTitle.toArray(new String[testTitle.size()]);
            String[] specDesc = testDesc.toArray(new String[testDesc.size()]);
            Bitmap[] specImg = img.toArray(new Bitmap[img.size()]);
            
            
        	
        	
     		displayListView = (ListView) view.findViewById(R.id.listview);
     		//set size of article count in Article Adapter now that we now how many articles are supposed
        	//to be displayed. 
        	ArticleAdapter.artCount = modifiedListArt.size();
     		displayListView.setAdapter(new ArticleAdapter(0, getActivity().getApplicationContext(), 
     				specImg,specTitle, specDesc));
        
     		
     		displayListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					
					
					// Replace whatever is in the fragment_container view with this fragment,
					// and add the transaction to the back stack
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					WebFragment webFrag = new WebFragment();
					String url = "";
					
					for(int i = 0; i < modifiedListArt.size(); i++)
					{
						url = modifiedListArt.get(position).getLink();
					}
					
					 
					//getActivity().setTitle(FragmentTab1.categoryChosen);
					Bundle urlExtras = new Bundle();
					urlExtras.putString("url", url);
					webFrag.setArguments(urlExtras);

					
					
					
					//setContentView(R.layout.drawer_main);
					
					
			        
					MainActivity.movesCount++;
					
					ft.replace(R.id.content_frame, webFrag);
					ft.addToBackStack(null);
					
					ft.commit();
					
				}
     			
     		});
     		
     		setHasOptionsMenu(true);
            
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            
           
        //this may will be needed for first time start up of the app. Right now it looks better than just a blank screen.
        if(MainActivity.networkStatus == false)
        {
        	Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        
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