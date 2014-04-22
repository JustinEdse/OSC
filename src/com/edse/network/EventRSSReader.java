package com.edse.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.edse.edu.Article;
import com.edse.edu.Event;
import com.edse.edu.MainActivity;

import android.R.bool;
import android.R.integer;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class fetches date from the RSS feed using a HTTP GET and then parses that data into a list of events.
 * Only events not in the database will be returned.
 * @author Obinna Ngini
 *
 */
public class EventRSSReader
{
	ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> links = new ArrayList<String>();
	private ArrayList<Event> listevents = new ArrayList<Event>();
//	private ArrayList<String> locs = new ArrayList<String>();
	private ArrayList<String> descriptions = new ArrayList<String>();
	private ArrayList<String> pubs = new ArrayList<String>();
	
	ArrayList<ArrayList<Event>> lists = new ArrayList<ArrayList<Event>>();
	private ArrayList<Event> events = new ArrayList<Event>();

	private int countTitle = 0;
	private int countDesc = 0;
	private int countLink = 0;
	
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;

	public EventRSSReader(String url)
	{
		this.urlString = url;
	}

	public ArrayList<Event> getEvents()
	{
		return listevents;
	}
	
	public void parseXMLAndStoreIt(XmlPullParser myParser)
	{
		/*
		 *Fetch all items from the list using the XML Pull parser and build an event  
		 */
		String title = null, description = null, link = null, pubDate = null;
		int event;
		String text = null;
		try
		{
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT)
			{
				String name = myParser.getName();
				switch (event)
				{
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("title"))
					{
						// title = text;
						countTitle++;
						if (countTitle > 1)
						{
							titles.add(text);
							title = text;
						}
					}
					else if (name.equals("link"))
					{
						// link = text;
						countLink++;
						if (countLink > 1)
						{
							links.add(text);
							link = text;
						}
					}
					else if (name.equals("description"))
					{
						// description = text;
						countDesc++;
						if (countDesc > 1)
						{
							descriptions.add(text);
							description = text;
						}
					}
					else if (name.equals("pubDate"))
					{
						pubs.add(text);
						pubDate = text;
					}
					else
					{
					}
					break;
				}
				
				if(titles.size() > 0  && links.size() > 0 && descriptions.size() > 0 && pubs.size() > 0)
				{
					if(titles.size() == links.size() && titles.size() == descriptions.size() && titles.size() == pubs.size())
					{
						//String type = "unknown";
						String fulldesc = parseDescription(description);
						String dateTime = parseDateTime(description);
						int endpoint = trimDateTime(dateTime);
						String extratext = dateTime.substring(endpoint);
						fulldesc = extratext + fulldesc;
						dateTime = dateTime.substring(0,endpoint);
						Date pubDate2 = MainActivity.date_timeFormat.parse(pubDate);
						String tittextString = Html.fromHtml(title).toString();
						//Date object will be created from dateTime String in Main Activity
						Event tempEvent = new Event(tittextString,null, dateTime, link, pubDate2);
						if(isNewEvent(tempEvent))
						{
							listevents.add(tempEvent);
						}
					titles.clear();
					links.clear();
					descriptions.clear();
					pubs.clear();
						
					}
				}
				event = myParser.next();
			}
			parsingComplete = false;
			
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//anurag. 6 Apr. Added this to avoid getting trapped in infinite loop in AsyncEvent class  
			parsingComplete = false;
		}
		
		
	
	}


	public void fetchXML() throws InterruptedException//, IOException
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			InputStream stream = conn.getInputStream();
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myparser = xmlFactoryObject.newPullParser();
			myparser.setFeature(
					XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myparser.setInput(stream, null);
			parseXMLAndStoreIt(myparser);
			stream.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//anurag. 6 Apr. Added this to avoid getting trapped in infinite loop in AsyncEvent class 
			parsingComplete = false;
			//throw new IOException();
		}
	}
	private static String parseDateTime(String input)
	{
		String temp = "";
		String arr[] = new String[] {};
		arr = input.split("<p>");
		temp = arr[0];
		String finalStr = temp.replaceAll("</p>", " ").trim();
		String cleanedHTML = android.text.Html.fromHtml(finalStr).toString();
		return cleanedHTML;
	}
	private int trimDateTime(String input) 
	{
		int end = 0;
		if (input.contains("to")  && !(input.contains("-")))
		{

			end = input.indexOf(')', input.indexOf(')') + 1);
			end = end + 1;
		}
		else if(input.contains("-"))
		{
			end = input.length();
		}
		else if(input.contains("("))
		{
			end = input.indexOf(')');
			end = end + 1;
		}
		return end;
		
	}
	private static String parseDescription(String input)
	{

		String temp = "";
		String arr[] = new String[] {};
		String fulltext = "";
		arr = input.split("<p>");
		for (int x = 1; x < arr.length; x++)
		{
			temp = arr[x];
	
			String finalStr = temp.replaceAll("</p>", " ").trim();
			String cleanedHTML = android.text.Html.fromHtml(finalStr).toString();
			fulltext = fulltext + cleanedHTML;
		}
		return fulltext;
		
	}
	/*
	 * Check if an event with the same title and publication date already exists in the Database.
	 * Return true or false 
	 */
   private static boolean isNewEvent(Event ev)
   {
	   boolean ans;
	   if (ev.getPubDate().toString() == "")
	   {
		  ans =  false;
	   }
	   else 
	   {
		   Set<Date> pubdateSet = new TreeSet<Date>();
		   Set<String> titlesetSet = new TreeSet<String>();
		   try {
			ArrayList<Event> evs = MainActivity.db.getAllEvents();
			for (Event ev1 : evs)
			{
				pubdateSet.add(ev1.getPubDate());
				titlesetSet.add(ev1.getEventName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    ans = pubdateSet.contains(ev.getPubDate()) && titlesetSet.contains(ev.getEventName());
	   }
	   return !ans;
   }
}