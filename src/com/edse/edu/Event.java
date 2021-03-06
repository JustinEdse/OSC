package com.edse.edu;

import java.util.Date;
import java.util.Set;

import android.R.integer;
/***
 * This class includes metadata associated with an instance of type Event
 * {@code
 * id of the event;
 * String title;
 * Date date;
 * String link;
 * Date pubDate (published date);
 * String dateTime (in-case of multi-day events);
 * String description;
 * String location;
 * }
 * @author kaushikvelindla
 *
 */
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
	/***
	* default constructor for the class Event
	*/
	public Event()
	{
		
	}
	/***
	 * This Constructor of the class, Event creates an instance of type Event and initializes the following fields of the class<br>
	 * String eventname <br>
	 * Date date <br> 
	 * String dateTime <br>
	 * String url <br> 
	 * @param eventname
	 * @param date
	 * @param dateTime
	 * @param url
	 */
	public Event(String eventname, Date date, String dateTime,  String url)
	{
		this.title = eventname;
		this.link = url;
		this.dateTime = dateTime;
		this.date = date;
	}
	/***
	 * 2nd overloaded Constructor of the class Event, creates an instance of type Event and initializes the following fields <br>
	 * String eventname <br>
	 * Date date <br>
	 * String dateTime <br>
	 * String url <br>
	 * Date pubDate <br>
	 * @param eventname
	 * @param date
	 * @param dateTime
	 * @param url
	 * @param pubDate
	 */
	public Event(String eventname, Date date,String dateTime,  String url, Date pubDate)
	{
		this.title = eventname;
		this.link = url;
		this.dateTime = dateTime;
		this.date = date;
		this.pubDate = pubDate;
	}
	/***
	 * 3rd overloaded constructor of the Event class <br>
	 * @param id
	 * @param eventname
	 * @param description
	 * @param date
	 * @param location
	 * @param dateTime
	 * @param url
	 * @param pubDate
	 */
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
	/***
	 * compareTo method compares events and orders them by date.
	 */
	public int compareTo(Event rhs) {
		// TODO Auto-generated method stub
		int val = 10000;
		//Set to use date because of the event 
		if(this.date != null && rhs.date!= null)
		{
			if (this.date.before(rhs.date))
			{
				val = -1;
			}
			else if (this.date.after(rhs.date))
			{
				val = 1;
			}
			else
			{
				 val = 0;
			}
		}
		else 
		{
			if (this.date == null)
			{
				val = 1;
			}
			else if(rhs.date == null)
			{
				val = -1;
			}
		}
		return val;
	}

}
	