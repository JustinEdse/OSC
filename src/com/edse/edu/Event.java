package com.edse.edu;

import java.util.Date;

public class Event implements Comparable<Event>
{
	private String title;
	private Date date;
	private String link;
	private Date pubDate;
	//Date and Time for display purposes, in the case of multi-day events
	private String dateTime;

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
	public void addTitle(String eventname)
	{
		this.title = eventname;
	}
	public void addDate(Date date)
	{
		this.date = date;
	}
	public void addPubDate(Date pubDate)
	{
		this.pubDate = pubDate;
	}
	public void addDateAndTime(String dateTime)
	{
		this.dateTime = dateTime;
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
	
	@Override
	public int compareTo(Event rhs) {
		// TODO Auto-generated method stub
		int val = 10000;
		if (this.date.before(rhs.getDate()))
		{
			val = -1;
		}
		else if (this.date.after(rhs.getDate()))
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
	
	
