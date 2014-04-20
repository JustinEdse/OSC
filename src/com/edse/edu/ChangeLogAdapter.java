package com.edse.edu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ChangeLogAdapter extends BaseAdapter
{

	private int varyingLogNum;
	private Context context;
	private String[] logTitles;
	private String[] logDesc;
	
	public static boolean done = false;
	ChangeLogAsync taskI = new ChangeLogAsync(context);
	public static int logCount = 0;
	public static int savedCount = 0;
	
	
	
	private static LayoutInflater inflater = null;
	
	// this ArticleAdapter constructor contains pieces of an Article object.
	public ChangeLogAdapter(int varyingLogNum, Context context, String[] logTitles, String[] logDesc)
	{
		this.varyingLogNum = varyingLogNum;
		this.context = context;
		this.logTitles = logTitles;
		this.logDesc = logDesc;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return logTitles.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return logTitles[position];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Sets a view with the article's information and returns it.
		View view = convertView;
		 
		if(varyingLogNum == 0)
		{
			logCount = ChangeLogDisplayFragment.modifiedListLog.size();
		}
		
		if(done == true && logCount != 0)
		{
			view = inflater.inflate(R.layout.changelog_row, null);
			TextView title = (TextView) view.findViewById(R.id.articletitle);
			TextView desc = (TextView) view.findViewById(R.id.articldesc);
			
			title.setText(logTitles[position]);
			desc.setText(logDesc[position]);
		
			logCount--;
		}
		
		if(logCount == 0 && done == true)
		{
			if(this.varyingLogNum == 0)
			{
				logCount = ChangeLogDisplayFragment.modifiedListLog.size();
			}
			else if(this.varyingLogNum == 1)
			{
			logCount = savedCount;
			}
		}
		
		
		return view;
	}
	
}
