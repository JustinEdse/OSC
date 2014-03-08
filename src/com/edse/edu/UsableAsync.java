package com.edse.edu;

import java.io.IOException;
import java.util.ArrayList;

import com.edse.network.ArticleRSSReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
public class UsableAsync extends AsyncTask<Object, Void, ArrayList<Article>>
{
	ResultsListener listener;
	private com.edse.network.ArticleRSSReader artReaderObj;
	private String urlArticles = "https://www.osc.edu/press-feed";
	private Context context;
	
	public UsableAsync(Context context)
	{
		this.context = context;
	}
	
	ProgressDialog dialog;
	
	public void setOnResultsListener(ResultsListener listener)
	{
		this.listener = listener;
	}
	

	@Override
	protected void onPreExecute() {
	        dialog = new ProgressDialog(context);
	        dialog.setMessage("Loading...");
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(false);
	        dialog.show();
	    }
	@Override
	protected ArrayList<Article> doInBackground(Object... params)
	{
		

		// TODO Auto-generated method stub
		ArrayList<Article> retArtList = new ArrayList<Article>();
		artReaderObj = new ArticleRSSReader(urlArticles);

		try
		{
			artReaderObj.fetchXML();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException ioe)
		{
			
			cancel(true);
			
		}

		while (artReaderObj.parsingComplete)
			;
		retArtList = artReaderObj.getArticles();
		

		return retArtList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Article> result)
	{
		dialog.dismiss();
		
		
		
		if (result.size() > 0){
			if(listener != null)
			{
				listener.onResultSuccess(result);
			}
			
			Toast.makeText(context, "Ok", Toast.LENGTH_LONG).show();
		}
		else{
			if(listener != null)
			{
				listener.onResultFail(1, "Error internet connection.");
			}
			Toast.makeText(context, "Error, Check Network Connection.", Toast.LENGTH_LONG).show();
		}
		
	}

}
