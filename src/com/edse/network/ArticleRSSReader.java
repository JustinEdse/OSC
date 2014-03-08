package com.edse.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.edse.edu.Article;

import android.util.Log;
import android.widget.Toast;

public class ArticleRSSReader
{
	ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
	
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> links = new ArrayList<String>();
	private ArrayList<String> descriptions = new ArrayList<String>();
	private ArrayList<String> pubs = new ArrayList<String>();

	private ArrayList<Article> articles = new ArrayList<Article>();

	private int countTitle = 0;
	private int countDesc = 0;
	private int countLink = 0;
	
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;

	public ArticleRSSReader(String url)
	{
		this.urlString = url;
	}

	public ArrayList<Article> getArticles(){
		return articles;
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
						if(countTitle > 1)
						{
						titles.add(text);
						title = text;
						}
					}
					else if (name.equals("link"))
					{
						// link = text;
						countLink++;
						if(countLink > 1)
						{
						links.add(text);
						link = text;
						}
					}
					else if (name.equals("description"))
					{
						// description = text;
						countDesc++;
						if(countDesc > 1)
						{
						descriptions.add(text);
						description = text;
						}
					}
					else if(name.equals("pubDate"))
					{
						pubs.add(text);
						pubDate = text;
					}
					else
					{
					}
					break;
				}
				
				
				//MAX SIZE OF THESE ARRAYLISTS WILL ALWAYS BE 1.
				if(titles.size() > 0 && links.size() > 0 && descriptions.size() > 0 && pubs.size() > 0)
				{
				if(titles.size() == links.size() && titles.size() == descriptions.size() && titles.size() == pubs.size())
				{
					String subDesc = ArticleRSSReader.parseSubDesc(descriptions.get(0));
					String type = "unknown";
					int image = 0;
					Article createdArt = new Article(title, subDesc, type, image, link, pubDate);
					articles.add(createdArt);
					
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
		}
		
		
	
	}

	public void fetchXML() throws InterruptedException, IOException
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
					parsingComplete = false;
					e.printStackTrace();
					throw new IOException();
				}
				
					
				
			}
	
	public static String parseSubDesc(String desc)
	{
		
		
		String temp = "";
		String arr[] = new String[]{};
		
		
		arr = desc.split("<p>");
		temp = arr[1];
//		modified = temp.split("</p>");
		String finalStr = temp.replaceAll("</p>"," ").trim();
		
		
		return finalStr;
		
		
	}
	
}