package com.edse.edu;

public class Event 
{
	private String title;
	private String description;
	private String date;
	private String location;
	private String link;

	public Event(String eventname, String desc, String date, String location, String url)
	{
		this.title = eventname;
		this.description = desc;
		this.link = url;
		this.date = date;
		this.location = location;
	}
	public String getEventName()
	{
		return this.title;
	}
	public String getEventDetails()
	{
		return this.description;
	}
	public String getEventCalendarLink()
	{
		return this.link;
	}
	public String getDate()
	{
		return this.date;
	}
	public String getPlace()
	{
		return this.location;
	}
}
	
	
