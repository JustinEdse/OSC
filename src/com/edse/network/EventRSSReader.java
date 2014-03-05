package com.edse.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class EventRSSReader
{
	ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
	//private ArrayList<ArrayList<String>> collectArticles = new ArrayList<ArrayList<String>>();
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> links = new ArrayList<String>();
	private ArrayList<String> descriptions = new ArrayList<String>();
	private ArrayList<String> pubs = new ArrayList<String>();
	//private String title = "title";
	//private String link = "link";
	//private String description = "description";

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

	
	public ArrayList<String> getTitle(){
	 return titles;
	 }
	 public ArrayList<String> getLink(){
	 return links;
	 }
	 public ArrayList<String> getDescription(){
	 return descriptions;
	 }
	 
	 public ArrayList<String> getPub(){
			return pubs;
		}
	public void parseXMLAndStoreIt(XmlPullParser myParser)
	{
		

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
						if(countTitle > 1)
						{
						titles.add(text);
						}
					}
					else if (name.equals("link"))
					{
						// link = text;
						countLink++;
						if(countLink > 1)
						{
						links.add(text);
						}
					}
					else if (name.equals("description"))
					{
						// description = text;
						countDesc++;
						if(countDesc > 1)
						{
						descriptions.add(text);
						}
					}
					else if(name.equals("pubDate"))
					{
						pubs.add(text);
					}
					else
					{
					}
					break;
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
					stream.close();
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
	
}