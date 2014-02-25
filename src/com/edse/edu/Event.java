package com.edse.edu;

public class Event 
{
	private String title;
	private String description;
	private String date;
	private String time;
	private String location;
	//private String link;
	

	public Event()
	{
		
	}
	public Event(String eventname, String desc, String date, String location)//, String url)
	{
		this.title = eventname;
		this.description = desc;
		//this.link = url;
		this.date = date;
		this.location = location;
	}
	public void addTitle(String eventname)
	{
		this.title = eventname;
	}
	public void addEventDetails(String desc)
	{
		this.description = desc;
	}
	public void addDate(String date)
	{
		this.date = date;
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
	public String getDate()
	{
		return this.date;
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
	
	
