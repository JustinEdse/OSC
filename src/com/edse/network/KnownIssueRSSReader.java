package com.edse.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.edse.database.Database;
import com.edse.edu.Article;
import com.edse.edu.ChangeLog;
import com.edse.edu.KnownIssue;
import com.edse.edu.MainActivity;
import com.edse.edu.ArticleAsync;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class KnownIssueRSSReader {
	
	ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
    private boolean done = false;
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> links = new ArrayList<String>();
	private ArrayList<String> descriptions = new ArrayList<String>();
	private ArrayList<String> pubs = new ArrayList<String>();
    
	private ArrayList<Date> checkPubs = new ArrayList<Date>();
	private ArrayList<KnownIssue> knownIssue = new ArrayList<KnownIssue>();
	private static SimpleDateFormat format = new SimpleDateFormat(
			"EEE, dd MMM yyyy hh:mm:ss");

	private int countTitle = 0;
	private int countDesc = 0;
	private int countLink = 0;

	
	private static ArrayList<KnownIssue> issueList = new ArrayList<KnownIssue>();
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;

	public KnownIssueRSSReader(String url)
	{
		this.urlString = url;
	}
	
	public ArrayList<KnownIssue> getKnownIssue()
	{
		return knownIssue;
	}
	
	public int checkKnownIssueStatus(XmlPullParser parser)
	{

		int newIssues = 0;
		int event;
		String text = null;
		try
		{
			event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT && done == false)
			{
				String name = parser.getName();
				switch (event)
				{
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = parser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equals("pubDate"))
					{
						// title = text;
						Date date = format.parse(text);
						if (KnownIssueRSSReader.IsIssueNew(date))
						{
							newIssues++;
						}
						else
						{
							done = true;
						}

					}

					else
					{
					}
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return newIssues;

	}
	public void parseXMLAndStoreIt(XmlPullParser myParser, int numNeeded)
	{

		int issuesFetched = 0;
		String title = null, description = null, link = null, pubDate = null;
		Date realDate = null;
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
							String cleanedTitle = android.text.Html.fromHtml(text).toString();
							title = cleanedTitle;
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
						//problems encountering the new date format
						pubs.add(text);
						pubDate = text;
					}
					else
					{
					}
					break;
				}

				// MAX SIZE OF THESE ARRAYLISTS WILL ALWAYS BE 1.
				if (titles.size() > 0 && links.size() > 0
						&& descriptions.size() > 0 && pubs.size() > 0)
				{
					if (titles.size() == links.size()
							&& titles.size() == descriptions.size()
							&& titles.size() == pubs.size())
					{
						String subDesc = KnownIssueRSSReader
								.parseSubDesc(descriptions.get(0));
						Log.d("Parse Check", subDesc);
						ArrayList<String> type = KnownIssueRSSReader
								.parseForType(descriptions.get(0));
						//Bitmap bitmap = ArticleRSSReader
						//		.parseForImg(descriptions.get(0));

						// String type = "unknown";
						realDate = format.parse(pubDate);
						Log.d("Parse Check", title);
						//Log.d("Parse Check", type);
						Log.d("Parse Check", link);
						Log.d("Parse Check", pubDate);
						KnownIssue createdIssue = new KnownIssue(title, subDesc, link, realDate);
						
						//Inserting check to see if knownIssue already exists in DB.
						// code is similar to the one used in EventRSSReader
						if (isIssueNewVersion2(createdIssue)) {
							knownIssue.add(createdIssue);
							issuesFetched++;	
						}

						if (numNeeded > 0)
						{
							if (issuesFetched == numNeeded)
							{
								// only fetch new articles added...after this
								// condition is met break the while loop.
								// the returned list should only have a small
								// number
								// of article objects in it.
								break;
							}
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
			parsingComplete = false;
		}

	}
	
	private boolean isIssueNewVersion2(KnownIssue createdIssue) {
		Log.d("KnownIssueRSSReader", "Entering into isIssueNewVersion2 method");
		boolean ans = true;
		if (createdIssue.getDate().toString() == "")
		{
			ans =  false;
		}
		else 
		{
			Set<Date> pubdateSet = new TreeSet<Date>();
			Set<String> titlesetSet = new TreeSet<String>();
			try {
				ArrayList<KnownIssue> allIssues = MainActivity.db.getAllIssues();
				for (KnownIssue issue1 : allIssues)
				{
					pubdateSet.add(issue1.getDate());
					titlesetSet.add(issue1.getTitle());
				}
				Log.d("KnownIssueRSSReader", "Value of set containing dates of all known issues from DB: " + pubdateSet);
				Log.d("KnownIssueRSSReader", "Value of set containing titles of all known issues from DB: " + titlesetSet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ans = pubdateSet.contains(createdIssue.getDate()) && titlesetSet.contains(createdIssue.getTitle());
			if (ans == true) {
				Log.d("KnownIssueRSSReader", "The known issue entry: " + createdIssue.getTitle() + " is already present in DB");
			}
		}
		Log.d("KnownIssueRSSReader", "Exiting from isIssueNewVersion2 method");
		return !ans;
	}

	public void fetchXML() throws InterruptedException, IOException
	{

		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			InputStream stream = conn.getInputStream();
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser myparser = xmlFactoryObject.newPullParser();
			myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			myparser.setInput(stream, null);

			// get pub date tags. It's necessary to get all pub date tags. If we
			// just checked for the most recent pub date and it happened
			// after any dates that were in cache there was a new article
			// published. However, there might be more than one new article or
			// a lot of material in the feed could be completely
			// modified/updated. So because of this thought, if any of the
			// publication dates
			// are newer than what's in cache then it's a new article to the
			// feed. I figure the way I did this below is sort of a
			// "catch all scenarios" type
			// way.

		//	issueList = UsableAsync.db.getAllArticles();

			// going through pub dates that were returned. For each pub date, is
			// it new than any of the dates already in cache?
			// I tested this in an external program and it worked.
			if (issueList.size() > 0)
			{
				int checkResult = checkKnownIssueStatus(myparser);
				if(checkResult > 0)
				{
				parseXMLAndStoreIt(myparser, checkResult);
				}
				else
				{
					parsingComplete = false;
				}
			}

			else
			{

				URL urlTwo = new URL(urlString);
				HttpURLConnection connTwo = (HttpURLConnection) urlTwo
						.openConnection();
				connTwo.setReadTimeout(10000 /* milliseconds */);
				connTwo.setConnectTimeout(15000 /* milliseconds */);
				connTwo.setRequestMethod("GET");
				connTwo.setDoInput(true);
				// Starts the query
				connTwo.connect();
				InputStream streamTwo = connTwo.getInputStream();
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser noCacheInfo = xmlFactoryObject.newPullParser();
				noCacheInfo.setFeature(
						XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
				noCacheInfo.setInput(streamTwo, null);

				// want the full load of articles. There is nothing in the
				// sqlite table at the moment.
				parseXMLAndStoreIt(noCacheInfo, 0);

			}
			// no new publication dates were detected...

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
		String arr[] = new String[] {};

		arr = desc.split("<p>");
		temp = arr[1];

		String finalStr = temp.replaceAll("</p>", " ").trim();
		String cleanedHTML = android.text.Html.fromHtml(finalStr).toString();

		return cleanedHTML;

	}
	public static ArrayList<String> parseForType(String desc)
			throws IOException
	{

		// TODO Auto-generated method stub

		ArrayList<String> cats = new ArrayList<String>();

		String[] rr = new String[] {};

		rr = desc.split("datatype=\"\">");

		String[] adjustedArr = Arrays.copyOfRange(rr, 1, rr.length);

		for (String individual : adjustedArr)
		{
			String[] fixedInd = individual.split("</a>");

			cats.add(fixedInd[0].trim());

		}

		return cats;
	}

	public static boolean IsIssueNew(Date pubDate) throws IOException
	{
		
		boolean isNew = false;
		

		ArrayList<Date> dates = new ArrayList<Date>();

		for (KnownIssue issue : issueList)
		{
			dates.add(issue.getDate());
		}

		Collections.sort(dates);

		Date retDate = dates.get(dates.size() - 1);

		if (pubDate.after(retDate))
		{
			isNew = true;
		}

		return isNew;

	}


	}


