package com.edse.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This class stores event details, and populates and event row, either a detailed view from event_detailed_row.xml, 
 * or basic view from event_row.xml(for upcoming and recent tabs)
 * @author Obinna Ngini
 *
 */
public class EventAdapter extends BaseAdapter
{
	Context context;
	String[] eventTitles;
	String[] eventDates;
	String[] eventdateTimes;
	String[] eventDescriptions;
	//ViewHolder holder;
	
	private static LayoutInflater inflater = null;
	
	public EventAdapter(Context context, String[] eventTitles, String[] eventDates, String[] eventdateTimes)
	{
		this.context = context;
		this.eventTitles = eventTitles;
		this.eventDates = eventDates;
		this.eventdateTimes = eventdateTimes;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public EventAdapter(Context context, String[] eventTitles, String[] eventDescs,String[] eventDates, String[] eventdateTimes, String[] eventLocations)
	{
		this.context = context;
		this.eventTitles = eventTitles;
		this.eventDescriptions = eventDescs;
		this.eventdateTimes= eventdateTimes;
		this.eventDates = eventDates;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return eventTitles.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return eventTitles[position];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//		// Sets a view with the article's information and returns it.
//		View view = convertView;
//		 
//		if(view == null)
//		{
//			view = inflater.inflate(R.layout.event_row, null);
//			TextView title = (TextView) view.findViewById(R.id.eventtitle);
//			TextView date_time = (TextView) view.findViewById(R.id.event_date_time);
//			
//			
//			//image.setImageResource(artImages[position]);
//			title.setText(eventTitles[position]);
//			date_time.setText(eventDates[position] + " - " + eventTimes[position]);
//		
//		}
//		
//		return view;
//	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Sets a view with the article's information and returns it.
			View view = convertView;
			//ViewHolder holder = new ViewHolder(); 
			if(view == null)
			{
				view= inflater.inflate(R.layout.event_detailed_row, null);
				TextView title = (TextView) view.findViewById(R.id.event_detailed_title);
				TextView date_time = (TextView) view.findViewById(R.id.event_detailed_date_time);
//				TextView desc = (TextView) view.findViewById(R.id.event_detailed_desc);
//				TextView location = (TextView) view.findViewById(R.id.event_detailed_location);
				
				title.setText(eventTitles[position]);
				date_time.setText(eventdateTimes[position]);
//				desc.setText(eventDescriptions[position]);
//				location.setText(eventLocations[position]);
	
			}
			return view;
	}

}

