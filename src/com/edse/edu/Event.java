package com.edse.edu;

import java.util.Date;

import android.R.integer;

public class Event implements Comparable<Event>
{
	private int id;
	private String title;
	private Date date;
	private String link;
	private Date pubDate;
	//Date and Time for display purposes, in the case of multi-day events
	private String dateTime;
	
	private String description;
	private String location;

	public Event()
	{
		
	}
	public Event(String eventname, Date date, String dateTime,  String url)
	{
		this.title = eventname;
		this.link = url;
		this.dateTime = dateTime;
		this.date = date;
	}
	public Event(String eventname, Date date,String dateTime,  String url, Date pubDate)
	{
		this.title = eventname;
		this.link = url;
		this.dateTime = dateTime;
		this.date = date;
		this.pubDate = pubDate;
	}
	public Event(int id, String eventname, String description,Date date,String location, String dateTime, String url, Date pubDate)
	{
		this.id = id;
		this.title = eventname;
		this.link = url;
		this.description = description;
		this.dateTime = dateTime;
		this.date = date;
		this.location = location;
		this.pubDate = pubDate;
	}
	//Setters
	public void addID(int id)
	{
		this.id = id;
	}
	public void addTitle(String eventname)
	{
		this.title = eventname;
	}
	public void addDate(Date date)
	{
		this.date = date;
	}
	public void addLink(String link)
	{
		this.link = link;
	}
	public void addPubDate(Date pubDate)
	{
		this.pubDate = pubDate;
	}
	public void addDateAndTime(String dateTime)
	{
		this.dateTime = dateTime;
	}
	public void addDescription(String description) 
	{
		this.description = description;
	}
	public void addLocation(String location) 
	{
		this.location = location;
	}
	
	//Getters
	public int getID()
	{
		return this.id;
	}
	public String getEventName()
	{
		return this.title;
	}
	public String getEventLink()
	{
		return this.link;
	}
	public Date getDate()
	{
		return this.date;
	}
	public Date getPubDate()
	{
		return this.pubDate;
	}
	public String getDateAndTime()
	{
		return this.dateTime;
	}
	public String getDescription() 
	{
		return this.description;
	}
	public String getLocation() 
	{
		return this.location;
	}

	
	
	@Override
	public int compareTo(Event rhs) {
		// TODO Auto-generated method stub
		int val = 10000;
		if (this.pubDate.before(rhs.getPubDate()))
		{
			val = -1;
		}
		else if (this.pubDate.after(rhs.getPubDate()))
		{
			val = 1;
		}
		else
		{
			 val = 0;
		}
		return val;
	}

}
	
	
