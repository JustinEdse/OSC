package com.edse.edu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.edse.database.Database;
import com.edse.network.ArticleRSSReader;
import com.edse.network.KnownIssueRSSReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class KnownIssueAsync extends AsyncTask<Object, Void, ArrayList<KnownIssue>>
{
	KnownIssueResultsListener listener;
	private com.edse.network.KnownIssueRSSReader issueReaderObj;
	private String urlKnownIssue = "https://osc.edu/feeds/known-issues.xml";
	private Context context;
	public static Database db;

	public KnownIssueAsync(Context context)
	{
		this.context = context;
	}

	ProgressDialog dialog;

	public void setOnResultsListener(KnownIssueResultsListener listener)
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
	protected ArrayList<KnownIssue> doInBackground(Object... params)
	{

		// TODO Auto-generated method stub
		ArrayList<KnownIssue> modifiedList = new ArrayList<KnownIssue>();
		ArrayList<KnownIssue> retIssueList = new ArrayList<KnownIssue>();
		issueReaderObj = new KnownIssueRSSReader(urlKnownIssue);
		MainActivity.db = new Database(MainActivity.globalTHIS);
		

		try
		{

		issueReaderObj.fetchXML();
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

		while (issueReaderObj.parsingComplete);
		
		retIssueList = issueReaderObj.getKnownIssue();
		// right now just doing articles. in the future will worry about
		// events....
		
		if (retIssueList.size() == 0)
		{
			// If list size is zero then that means no new articles were
			// published. get articles from
			// the sqlite database.
			ArrayList<KnownIssue> cacheInfo = null;
			try
			{
				cacheInfo = MainActivity.db.getAllIssues();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modifiedList = cacheInfo;

		}
		else if(retIssueList.size() > 0)
		{
			
			//int newArtSizeAdded = retIssueLogList.size() - db.getKnownIssueCount();
			
			// while the size of number of articles is greater than zero, add
			// those entries to our sqlite table.
			// Nothing needs to be done with retArtList since it should have the
			// updated article entries already.
			
			//MUST FIGURE OUT WHAT TO DO WHEN CACHE IS FULL OR AT A DESIRABLE LIMIT.
			for(KnownIssue issue : retIssueList)
			{
				try
				{
					MainActivity.db.addIssue(issue);
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
				modifiedList = MainActivity.db.getAllIssues();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// get articles from cache
		}
		MainActivity.db.close();
		return retIssueList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<KnownIssue> result)
	{
		dialog.dismiss();

		if (result.size() > 0)
		{
			if (listener != null)
			{
				listener.onResultSuccess(result);
				MainActivity.networkStatus = true;
			}
			//Toast.makeText(context, "Ok KnownIssue", Toast.LENGTH_LONG).show();
		}
		else
		{
			if (listener != null || result.size() == 0)
			{

				listener.onResultFail(1, "No Internet Connection");
				// set xml layout to recent fragment to indicate something.
			}
			Toast.makeText(context, "No Network Connection.", Toast.LENGTH_LONG)
					.show();
		}

	}

	

}
