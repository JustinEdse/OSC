package com.edse.edu;
 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.widget.TabWidget;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    // Declare the number of ViewPager pages or tabs.
    final int ARTICLE_PAGE_COUNT = 2;
    final int EVENT_PAGE_COUNT = 2;
    final int SYSTEM_INFO_COUNT = 2;
    private String newstitles[] = new String[] { "CATEGORIES", "RECENT"};
    private String calendartitles[] = new String[] {"FULL CALENDAR", "ALL EVENTS"};
    private String systemTitles[] = new String[]{"GLENN", "OAKLEY"};
    
   
    public ViewPagerAdapter(FragmentManager fm) 
    {
    	
        super(fm);
    }
 
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
 
            // Open FragmentTab1.java
        case 0:
        	
            FragmentTab1 fragmenttab1 = new FragmentTab1();
            return fragmenttab1;
 
            // Open FragmentTab2.java
        case 1:
            FragmentTab2 fragmenttab2 = new FragmentTab2();
            return fragmenttab2;
            
        
        }
        
        
        
        return null;
    }
 
    public CharSequence getPageTitle(int position) {
    	CharSequence temp = null;
    	if (MainActivity.selectedFrag == 0)
    	{
    		temp = (CharSequence) newstitles[position];
    	}
    	else if (MainActivity.selectedFrag == 1)
    	{
    		temp = (CharSequence)calendartitles[position];
    	}
    	else if(MainActivity.selectedFrag == 2)
    	{
    		temp = (CharSequence)systemTitles[position];
    	}
        
    	return temp;
    }
 
    @Override
    public int getCount() {
    	int count = 0;
    	if (MainActivity.selectedFrag == 0)
    	{
    		count = ARTICLE_PAGE_COUNT;
    	}
    	else if (MainActivity.selectedFrag == 1)
    	{
    		count = EVENT_PAGE_COUNT;
    	}
    	else if(MainActivity.selectedFrag == 2)
    	{
    		count = SYSTEM_INFO_COUNT;
    	}
    	return count;
    }
    


    
 
}