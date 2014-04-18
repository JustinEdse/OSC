package com.edse.edu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.edse.database.Database;
import com.edse.network.ArticleRSSReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ArticleAsync extends AsyncTask<Object, Void, ArrayList<Article>>
{
	ArticleResultsListener listener;
	private com.edse.network.ArticleRSSReader artReaderObj;
	private String urlArticles = "https://www.osc.edu/press-feed";
	private Context context;
	ProgressDialog dialog;

	public ArticleAsync(Context context)
	{
		this.context = context;
	}

	

	public void setOnArticleResultsListener(ArticleResultsListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading ...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();
	}

	@Override
	protected ArrayList<Article> doInBackground(Object... params)
	{

		// TODO Auto-generated method stub
		ArrayList<Article> modifiedList = new ArrayList<Article>();
		ArrayList<Article> retArtList = new ArrayList<Article>();
		artReaderObj = new ArticleRSSReader(urlArticles);
		MainActivity.db = new Database(MainActivity.globalTHIS);

		try
		{

			artReaderObj.fetchXML();
			MainActivity.networkStatus = true;
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			//MainActivity.networkStatus = false;

		}

		while (artReaderObj.parsingComplete)
			;
		
		ArrayList<Article> cacheInfo = null;
		
		if(MainActivity.networkStatus != false)
		{
		retArtList = artReaderObj.getArticles();
		// right now just doing articles. in the future will worry about
		// events....

		if (retArtList.size() == 0)
		{
			// If list size is zero then that means no new articles were
			// published. get articles from
			// the sqlite database.
			
			try
			{
				cacheInfo = MainActivity.db.getAllArticles();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				MainActivity.networkStatus = false;
			}
			modifiedList = cacheInfo;

		}
		else if(retArtList.size() > 0)
		{
			
			//int newArtSizeAdded = retArtList.size() - db.getArticlesCount();
			
			// while the size of number of articles is greater than zero, add
			// those entries to our sqlite table.
			// Nothing needs to be done with retArtList since it should have the
			// updated article entries already.
			
			//MUST FIGURE OUT WHAT TO DO WHEN CACHE IS FULL OR AT A DESIRABLE LIMIT.
			for(Article art : retArtList)
			{
				try
				{
					MainActivity.db.addArticle(art);
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
				modifiedList = MainActivity.db.getAllArticles();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// get articles from cache

		}
		}
		else
		{
			try
			{
				modifiedList = MainActivity.db.getAllArticles();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MainActivity.db.close();
		return modifiedList;
	}

	@Override
	protected void onPostExecute(ArrayList<Article> result)
	{
		super.onPostExecute(result); 
		if(dialog != null && dialog.isShowing())
		{
			dialog.dismiss();
		}

		
		
		if (result.size() > 0)
		{
			if (listener != null)
			{
				listener.onResultSuccess(result);
				
			}

			Toast.makeText(context, "Ok", Toast.LENGTH_LONG).show();
		}
		
		if(MainActivity.networkStatus == false)
		{
			if (listener != null || result.size() == 0)
			{

				//listener.onResultFail(1, "No Internet Connection");
				// set xml layout to recent fragment to indicate something.
				dialog.dismiss();
			}
			Toast.makeText(context, "No Network Connection.", Toast.LENGTH_LONG)
					.show();
		}

	}

}
