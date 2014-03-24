//package com.edse.edu;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import com.edse.database.Database;
//import com.edse.network.ArticleRSSReader;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//public class AsyncEvent extends AsyncTask<Object, Void, ArrayList<Article>>
//{
//	ResultsListener listener;
//	private com.edse.network.EventRSSReader eventReaderObj;
//	private String urlEvents = MainActivity.urlEvents;
//	private Context context;
//	public static Database db;
//
//	public AsyncEvent(Context context)
//	{
//		this.context = context;
//	}
//
//	ProgressDialog dialog;
//
//	public void setOnResultsListener(ResultsListener listener)
//	{
//		this.listener = listener;
//	}
//
//	@Override
//	protected void onPreExecute()
//	{
//		dialog = new ProgressDialog(context);
//		dialog.setMessage("Loading...");
//		dialog.setIndeterminate(true);
//		dialog.setCancelable(false);
//		dialog.show();
//	}
//
//	@Override
//	protected ArrayList<Event> doInBackground(Object... params)
//	{
//
//		// TODO Auto-generated method stub
//		ArrayList<Event> modifiedList = new ArrayList<Event>();
//		ArrayList<Event> retEventList = new ArrayList<Event>();
//		eventReaderObj = new EventRSSReader(urlEvents);
//		db = new Database(MainActivity.globalTHIS);
//
//		try
//		{
//
//			eventReaderObj.fetchXML();
//		}
//		catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException ioe)
//		{
//			ioe.printStackTrace();
//
//		}
//
//		while (eventReaderObj.parsingComplete)
//			;
//		retEventList = eventReaderObj.getEvents();
//		//Get events
//
//		if (retEventList.size() == 0)
//		{
//			// If list size is zero then that means no new events were
//			// published. get events from
//			// the sqlite database.
//			ArrayList<Event> cacheInfo = null;
//			try
//			{
//				cacheInfo = db.getAllEvents();
//			}
//			catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			modifiedList = cacheInfo;
//
//		}
//		else if(retEventList.size() > 0)
//		{
//			
//			
//			// while the size of number of events is greater than zero, add
//			// those entries to our sqlite table.
//			// Nothing needs to be done with retEventList since it should have the
//			// updated article entries already.
//			
//			//MUST FIGURE OUT WHAT TO DO WHEN CACHE IS FULL OR AT A DESIRABLE LIMIT.
//			for(Event ev : retEventList)
//			{
//				try
//				{
//					db.addEvent(ev);
//				}
//				catch (IOException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				// at some point establish a max number of articles that are
//				// allowed in the table/cache.
//			}
//			try
//			{
//				//Database afterAdd = new Database(MainActivity.globalTHIS);
//				modifiedList = db.getAllEvents();
//			}
//			catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}// get articles from cache
//
//		}
//
//		return modifiedList;
//	}
//
//	@Override
//	protected void onPostExecute(ArrayList<Article> result)
//	{
//		dialog.dismiss();
//
//		if (result.size() > 0)
//		{
//			if (listener != null)
//			{
//				listener.onResultSuccess(result);
//				MainActivity.networkStatus = true;
//			}
//
//			Toast.makeText(context, "Ok Events", Toast.LENGTH_LONG).show();
//		}
//		else
//		{
//			if (listener != null || result.size() == 0)
//			{
//
//				listener.onResultFail(1, "No Internet Connection");
//				// set xml layout to recent fragment to indicate something.
//			}
//			Toast.makeText(context, "No Network Connection.", Toast.LENGTH_LONG)
//					.show();
//		}
//
//	}
//
//}