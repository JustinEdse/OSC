package com.edse.edu;

import java.util.Date;

public class Event 
{
	private String title;
	private String description;
	private Date date;
	private String time;
	private String location;
	private Date pubDate;
	

	public Event()
	{
		
	}
	public Event(String eventname, String desc, Date date, String location)//, String url)
	{
		this.title = eventname;
		this.description = desc;
		//this.link = url;
		this.date = date;
		this.location = location;
	}
	public Event(String eventname, String desc, Date date, String location, Date pubDate)
	{
		this.title = eventname;
		this.description = desc;
		//this.link = url;
		this.date = date;
		this.location = location;
		this.pubDate = pubDate;
	}
	public void addTitle(String eventname)
	{
		this.title = eventname;
	}
	public void addEventDetails(String desc)
	{
		this.description = desc;
	}
	public void addDate(Date date)
	{
		this.date = date;
	}
	public void addPubDate(Date pubDate)
	{
		this.pubDate = pubDate;
	}
	public void addTime(String time)
	{
		this.time = time;
	}
	public void addLocation(String place)
	{
		this.location = place;
	}
	public String getEventName()
	{
		return this.title;
	}
	public String getEventDetails()
	{
		return this.description;
	}
//	public String getEventCalendarLink()
//	{
//		return this.link;
//	}
	public Date getDate()
	{
		return this.date;
	}
	public Date getPubDate()
	{
		return this.pubDate;
	}
	public String getTime()
	{
		return this.time;
	}
	public String getLocation()
	{
		return this.location;
	}
}
	
	
