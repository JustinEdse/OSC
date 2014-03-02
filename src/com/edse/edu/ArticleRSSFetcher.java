package com.edse.edu;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.*;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;




public class ArticleRSSFetcher	
{

	//ArrayList<String> headlines = new ArrayList<String>();
	//ArrayList<String> links = new ArrayList<String>();
	//ArrayList<String> descriptions = new ArrayList<String>();
	
	public void getArticleFeeds(ArrayList<String> headlines, ArrayList<String> links, ArrayList<String> descriptions)
		{
		try {
			URL url;
			url = new URL("https://osc.edu/press-feed");
			XmlPullParserFactory factory;
			factory = XmlPullParserFactory.newInstance();			
			factory.setNamespaceAware(false);
			XmlPullParser xpp;
			xpp = factory.newPullParser();
			xpp.setInput(getInputStream(url), "UTF_8");
			boolean insideItem = false;
			 
	        // Returns the type of current event: START_TAG, END_TAG, etc..
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
		        if (eventType == XmlPullParser.START_TAG) {
		 
		            if (xpp.getName().equalsIgnoreCase("item")) {
		                insideItem = true;
		            } else if (xpp.getName().equalsIgnoreCase("title")) {
		                if (insideItem)
		                    //headlines.add(xpp.nextText()); //extract the headline
		                	Log.d("info",xpp.nextText());
		            } else if (xpp.getName().equalsIgnoreCase("link")) {
		                if (insideItem)
		                    //links.add(xpp.nextText()); //extract the link of article
		                	Log.d("info",xpp.nextText());
		            } else if (xpp.getName().equalsIgnoreCase("description")) {
		                if (insideItem)
		                    //descriptions.add(xpp.nextText()); //extract the description of article
		                	Log.d("info",xpp.nextText());
		            }
		            
		        }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
		            insideItem=false;
		        }
		 
		        eventType = xpp.next(); //move to next element
		} }
		
		catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		catch (XmlPullParserException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	
	private InputStream getInputStream(URL url) {
		   try {
		       return url.openConnection().getInputStream();
		   } catch (IOException e) {
		       return null;
		     }
		}
		
}
		