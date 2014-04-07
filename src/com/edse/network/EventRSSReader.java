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

	public ArrayList<Event> getEvents(){
	
		//lists.add(events);
		//lists.add(fullevents);
		//Log.d("Obinna", lists.coun)
		return listevents;
	}
	
	public void parseXMLAndStoreIt(XmlPullParser myParser)
	{
	

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
					//Log.d("Obinna", "One event fouund");
					if(titles.size() == links.size() && titles.size() == descriptions.size() && titles.size() == pubs.size())
					{
						String type = "unknown";
						//Log.d("Parse Check", description);
					
						
						String fulldesc = parseDescription(description);
						String dateTime = parseDateTime(description);
						int endpoint = trimDateTime(dateTime);
						//Log.d("Link", link);
						String extratext = dateTime.substring(endpoint);
						fulldesc = extratext + fulldesc;
						dateTime = dateTime.substring(0,endpoint);
						//ArrayList<Date> eventDates = new ArrayList<Date>();
						//eventDates = parseDate(dateTime);
						
						Date pubDate2 = MainActivity.date_timeFormat.parse(pubDate);
						String tittextString = Html.fromHtml(title).toString();
						Log.d("Parse Check", "1" + tittextString);
						Log.d("Parse Check", "2" + dateTime);
						Log.d("Parse Check", "3" + fulldesc);
						Log.d("Parse Check", "4" + pubDate);
						//Date object will be created from dateTime String in Main Activity
						Event tempEvent = new Event(tittextString,null, dateTime, link, pubDate2);
						if(isNewEvent(tempEvent))
						{
							Log.d("Obinna", "Event: " + tittextString + " added");
							listevents.add(tempEvent);
							//Log.d("Obinna", Integer.toString(listevents.size()));
						}
						//Log.d("Title", tittextString + " and date is " + dateTime);
//						if(eventDates.size() > 0 )
//						{
//							Event tempEvent = new Event(tittextString,eventDates.get(0), dateTime, link, pubDate2);
//							listevents.add(tempEvent);
//						}
//						if(eventDates.size() > 1)
//						{
//							//Event runs on multiple dates
//							Date temp1 = eventDates.get(0);
//							Date temp2 = eventDates.get(1);
//							if (temp1.before(temp2))
//							{
//								
//								int diff = temp2.getDate() - temp1.getDate();
//								for (int y = 0; y<= diff; y++)
//								{
//									Date tempDate = new Date();
//									Calendar cal = Calendar.getInstance();    
//									cal.setTime(temp1);    
//									cal.add(Calendar.DATE, y);
//									Event createdEvent = new Event(tittextString, cal.getTime(), dateTime, link,pubDate2);
//									//Log.d("Date", cal.getTime().toString());
//									events.add(createdEvent);
//								}
//							}
//							else 
//							{
//								int diff = temp1.getDate() - temp2.getDate();
//								for (int y = 0; y<= diff; y++)
//								{
//									Date tempDate = new Date();
//									Calendar cal = Calendar.getInstance();    
//									cal.setTime(temp2);    
//									cal.add(Calendar.DATE, y);
//									Event createdEvent = new Event(tittextString, cal.getTime(), dateTime, link,pubDate2);
//									//Log.d("Date", cal.getTime().toString());
//									events.add(createdEvent);
//									
//								}
//							}
//						}
//						else if(eventDates.size() == 1)
//						{
//							//Event is only on one day
//							Event createdEvent = new Event(tittextString, eventDates.get(0), dateTime, link,pubDate2);
//							//Log.d("Date", eventDates.get(0).toString());
//							events.add(createdEvent);
//							
//						}
						//Event createdEvent = new Event();
						//createdEvent.addEventDetails(fulldesc);
						//events.add(createdEvent);
						
					titles.clear();
					links.clear();
					descriptions.clear();
					pubs.clear();
						
					}
					
				
				}
				
				
				event = myParser.next();
			}
			parsingComplete = false;

			//collectArticles.add(titles);
			//collectArticles.add(descriptions);
			//collectArticles.add(links);
			
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//anurag. 6 Apr. Added this to avoid getting trapped in infinite loop in AsyncEvent class  
			parsingComplete = false;
		}
		
		
	
	}


	public void fetchXML() throws InterruptedException
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
//			DocumentBuilderFactory dFactory =DocumentBuilderFactory.newInstance();
//			
//			try 
//			{
//				DocumentBuilder builder = dFactory.newDocumentBuilder();
//				Document doc = builder.parse(stream);
//				XPath xPath = XPathFactory.newInstance().newXPath();
//				NodeList ndList  = (NodeList) xPath.evaluate("/item/link", doc,XPathConstants.NODE);
//				if (ndList!= null && ndList.getLength() >0)
//				{
//					for (int y = 0; y<ndList.getLength(); y++)
//					{
//						Log.d("Obinna", ndList.item(y).getNodeValue());
//					}
//				}
//				/*
//				 * Log.d("Obinna", node.getNodeValue());
//				 */
//			}
//			catch (Exception e) 
//			{
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
			stream.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//anurag. 6 Apr. Added this to avoid getting trapped in infinite loop in AsyncEvent class 
			parsingComplete = false;
		}
	}
	private static String parseDateTime(String input)
	{
		String temp = "";
		String arr[] = new String[] {};
		arr = input.split("<p>");
		temp = arr[0];
		ArrayList<Date> dates = new ArrayList<Date>();
		String finalStr = temp.replaceAll("</p>", " ").trim();
		String cleanedHTML = android.text.Html.fromHtml(finalStr).toString();
		return cleanedHTML;
	}
	private int trimDateTime(String input) 
	{
		int end = 0;
		if (input.contains("to")  && !(input.contains("-")))
		{
			//If there are two events
//			for (int x = 0; x < input.length(); x++)
//			{
//				if (input.charAt(x) == ')')
//				{
//					timeCount++;
//				}
//				if(timeCount == 2)
//				{
//					end = x;
//				}
//			}
			end = input.indexOf(')', input.indexOf(')') + 1);
			end = end + 1;
		}
		else if(input.contains("-"))
		{
			//end = input.indexOf("pm", input.indexOf("pm") + 1);
			end = input.length();
		}
		else if(input.contains("("))
		{
			end = input.indexOf(')');
			end = end + 1;
		}
		//String t2 = input.substring(0, end);
//		Log.d("Trim", input);
//		Log.d("Trim", t2);
		return end;
		
	}
	private static ArrayList<Date> parseDate(String input)
	{
		
		ArrayList<Date> dates = new ArrayList<Date>();
		
		String cleanedHTML =input;
		if (cleanedHTML.contains("to")  && !(cleanedHTML.contains("-")))//More than one date to get
		{
			String [] splits = cleanedHTML.split("to");
			int start = 0;
			for (int count = 0; count < splits.length; count++)
			{
				String date = splits[count].substring(0, splits[count].indexOf('(')).trim();
				//Log.d("Date", date);
				String date2 = date;
				try 
				{
					Date tempDate = MainActivity.extendeddateFormat.parse(date);		
					dates.add(tempDate);
					//Log.d("Date", tempDate.toString());
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					//Log.d("Date", "Error");
				}
				
			}
			
		}
		//If its just one date
		else if(cleanedHTML.contains("-"))
		{
			//Log.d("Obinna", "Else case, one date");
			String date1 = cleanedHTML.substring(0, cleanedHTML.indexOf('-')).trim();
			//Log.d("Date", date1);
			try 
			{
				Date tempDate = MainActivity.extendeddateFormat.parse(date1);
				dates.add(tempDate);
				//Log.d("Date", tempDate.toString());
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else if(cleanedHTML.contains("("))
		{
			//Log.d("Obinna", "Else case, one date");
			String date1 = cleanedHTML.substring(0, cleanedHTML.indexOf('(')).trim();
		//	Log.d("Date", date1);
			try 
			{
				Date tempDate = MainActivity.extendeddateFormat.parse(date1);
				dates.add(tempDate);
				//Log.d("Date", tempDate.toString());
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return dates;
	
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
	   //Log.d("Obinna", Boolean.toString(ans));
	   return !ans;
   }
}