package com.edse.edu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.edse.database.Database;
import com.edse.network.EventRSSReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * This class implements an asynchronous task used to fetch a list of events from the events RSS feed
 */
public class EventAsync extends AsyncTask<Void, Void, ArrayList<Event>>
{
	
	EventsResultsListener listener;
	private com.edse.network.EventRSSReader eventReaderObj;
	private String urlEvents = MainActivity.urlEvents;
	private Context context;
	ArrayList<Event> retEventList= new ArrayList<Event>();
	//public static EventDatabase db;

	public EventAsync(Context context)
	{
		this.context = context;
	}

	ProgressDialog dialog;

	public void setOnResultsListener(EventsResultsListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onPreExecute()
	{
		/*
		 *Show a loading screen while fetching the events 
		 */
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading ...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected ArrayList<Event> doInBackground(Void... v)
	{
		/*
		 * Create an instance of the event rss reader class to fetch the list
		 */
		// TODO Auto-generated method stub
		eventReaderObj = new EventRSSReader(urlEvents);

		
		try
		{
			eventReaderObj.fetchXML();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (eventReaderObj.parsingComplete)
			;
		retEventList = eventReaderObj.getEvents();
		
		MainActivity.db.close();
		return retEventList;

	}

	@Override
	protected void onPostExecute(ArrayList<Event> result)
	{
		//Log.d("testing", "Entering into onPostExecute of AsyncEvent");
		dialog.dismiss();
			if (listener != null)
			{
				listener.onResultSuccess(result);
			}

			//Toast.makeText(context, "Ok Events", Toast.LENGTH_LONG).show();
	}

		

}