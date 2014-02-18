package com.edse.edu;
 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    // Declare the number of ViewPager pages or tabs.
    final int PAGE_COUNT = 3;
    private String newstitles[] = new String[] { "CATEGORIES", "RECENT", "UPCOMING" };
    private String calendartitles[] = new String[] {"FULL CALENDAR", "RECENT", "UPCOMING"};
 
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
            
        case 2:
        	FragmentTab3 fragmenttab3 = new FragmentTab3();
        	return fragmenttab3;
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
        
    	return temp;
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
}