package com.edse.edu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.edse.database.Database;
import com.edse.network.ArticleRSSReader;
import com.edse.network.ChangeLogRSSReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ChangeLogAsync extends AsyncTask<Object, Void, ArrayList<ChangeLog>>
{
	ChangeLogResultsListener listener;
	private com.edse.network.ChangeLogRSSReader logReaderObj;
	private String urlChangeLog = "https://osc.edu/feeds/hpc-changelog.xml";
	private Context context;
	public static Database db;

	public ChangeLogAsync(Context context)
	{
		this.context = context;
	}

	ProgressDialog dialog;

	public void setOnResultsListener(ChangeLogResultsListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute()
	{
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected ArrayList<ChangeLog> doInBackground(Object... params)
	{
		
		// TODO Auto-generated method stub
		ArrayList<ChangeLog> modifiedList = new ArrayList<ChangeLog>();
		ArrayList<ChangeLog> retLogList = new ArrayList<ChangeLog>();
		logReaderObj = new ChangeLogRSSReader(urlChangeLog);
		MainActivity.db = new Database(MainActivity.globalTHIS);
		

		try
		{

			logReaderObj.fetchXML();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();

		}

		while (logReaderObj.parsingComplete);
		
		retLogList = logReaderObj.getChangeLog();
		// right now just doing articles. in the future will worry about
		// events....
		
		if (retLogList.size() == 0)
		{
			//Log.d("Obinna", "No new chenge logs, ");
			// If list size is zero then that means no new articles were
			// published. get articles from
			// the sqlite database.
			ArrayList<ChangeLog> cacheInfo = null;
			try
			{
				cacheInfo = MainActivity.db.getAllLogs();
				//Log.d("Obinna", "Size of changelog db is " + Integer.toString(cacheInfo.size()));
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modifiedList = cacheInfo;

		}
		else if(retLogList.size() > 0)
		{
			
			//int newArtSizeAdded = retLogList.size() - db.getChangeLogCount();
			
			// while the size of number of articles is greater than zero, add
			// those entries to our sqlite table.
			// Nothing needs to be done with retArtList since it should have the
			// updated article entries already.
			
			//MUST FIGURE OUT WHAT TO DO WHEN CACHE IS FULL OR AT A DESIRABLE LIMIT.
			for(ChangeLog log : retLogList)
			{
				try
				{
					MainActivity.db.addLog(log);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// at some point establish a max number of articles that are
				// allowed in the table/cache.
			}
			try
			{
				//Database afterAdd = new Database(MainActivity.globalTHIS);
				modifiedList = MainActivity.db.getAllLogs();
				if(modifiedList != null)
					Log.d("List Check", "" + modifiedList.size());
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// get articles from cache
		}
		MainActivity.db.close();
//		return retLogList;
		return modifiedList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<ChangeLog> result)
	{
		dialog.dismiss();

		if (result.size() > 0)
		{
			if (listener != null)
			{
				listener.onResultSuccess(result);
				MainActivity.networkStatus = true;
			}
			//Toast.makeText(context, "Ok changeLog", Toast.LENGTH_LONG).show();
		}
		else
		{
			if (listener != null || result.size() == 0)
			{

				listener.onResultFail(1, "No Internet Connection");
				// set xml layout to recent fragment to indicate something.
			}
			/*Toast.makeText(context, "No Network Connection.", Toast.LENGTH_LONG)
					.show();*/
		}

	}

	

}
