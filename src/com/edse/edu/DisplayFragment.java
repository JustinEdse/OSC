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
 
public class DisplayFragment extends SherlockFragment {
 
	    //declare variables for custom list view and article previews.
		static int selectedFrag = 0;
	
		
		ListView displayListView;

		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	((MainActivity) getActivity()).setTitle(FragmentTab1.categoryChosen);
    	
        View view = inflater.inflate(R.layout.article_display, container, false);
        // Locate the ViewPager in viewpager_main.xml
       // ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        // Set the ViewPagerAdapter into ViewPager
        //mViewPager.setBackgroundColor(getResources().getColor(R.color.gray));
    
       // mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
     // Get the Title
     	
        //SET LIST LAYOUT WITH MENU LIST ADAPTER. PUT ARTICLE OBJECT INFORMATION INTO THE LIST.

        	//in the future an arraylist of article objects will be passed to this class. It should be iterated
        	//through and the individual attributes need to be put into arrays so they can be passed as parameters
        	//to the ArticleAdapter class. This arrays below were when I hard coded with arrays but I thought it
        	//if I actually worked with an array list of Articles it would be closer to what we're actually going
        	// to experience.
            
        	int[] hardCodedImages = new int[]{R.drawable.articcyclones, R.drawable.nioshlogo};
        	
        	String[] hardCodedTitles = new String[]{"Arctic cyclones more common than previously thought", "UC's Sousa to leverage OSC to simulate neutrino behavior"};
        	String[] hardCodedDesc = new String[]{"Weather data at the Ohio Supercomputer Center reveals in new study hundreds of smaller storms that had previously escaped detection","DoE experiment to detect particles; provide clues to universe evolution"};
        	
        	ArrayList<Article> test = new ArrayList<Article>();
            //Article one = new Article("Arctic cyclones more common than previously thought",
            	//	"Weather data at the Ohio Supercomputer Center reveals in new study hundreds of smaller storms that had previously escaped detection", "supercomputer", R.drawable.articcyclones, "10/14/2013");
            
           // Article two = new Article("UC's Sousa to leverage OSC to simulate neutrino behavior",
            //		"DoE experiment to detect particles; provide clues to universe evolution", "supercomputer", R.drawable.novalogo, "01/10/2014");
            
            //since we don't actually have a list of Articles retrieved from the server I added these to an arraylist
            //myself to simulate what we might have...
            //test.add(one);
            //test.add(two);
            ArrayList<String> testTitle = new ArrayList<String>();
            ArrayList<String> testDesc = new ArrayList<String>();
            ArrayList<Bitmap> img = new ArrayList<Bitmap>();
            
            for(Article art : test)
            {
            	testTitle.add(art.getTitle());
            	testDesc.add(art.getsubDesc());
            	img.add(art.getPreviewImage());
            }
            
            String[] specTitle = testTitle.toArray(new String[testTitle.size()]);
            String[] specDesc = testDesc.toArray(new String[testDesc.size()]);
            Bitmap[] specImg = img.toArray(new Bitmap[img.size()]);
            
            
        	
        	
     		displayListView = (ListView) view.findViewById(R.id.listview);
     		displayListView.setAdapter(new ArticleAdapter(getActivity().getApplicationContext(), 
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
					switch(position)
					{
					case 0:
						
						
						url = "https://www.osc.edu/press/arctic_cyclones_more_common_than_previously_thought";
					    break;
					case 1:
						url = "https://www.osc.edu/press/ucs_sousa_to_leverage_osc_to_simulate_neutrino_behavior";
						break;
					default:
						url = "http://www.google.com";
					    break;
						
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