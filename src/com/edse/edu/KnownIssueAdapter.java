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


public class KnownIssueAdapter extends BaseAdapter
{

	private int varyingLogNum;
	private Context context;
	private String[] issueTitles;
	private String[] issueDesc;
	
	public static boolean done = false;
	ArticleAsync taskI = new ArticleAsync(context);
	public static int issueCount = 0;
	public static int savedCount = 0;
	
	
	
	private static LayoutInflater inflater = null;
	
	// this ArticleAdapter constructor contains pieces of an Article object.
	public KnownIssueAdapter(int varyingLogNum, Context context, String[] issueTitles, String[] issueDesc)
	{
		this.varyingLogNum = varyingLogNum;
		this.context = context;
		this.issueTitles = issueTitles;
		this.issueDesc = issueDesc;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return issueTitles.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return issueTitles[position];
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
			issueCount = KnownIssueDisplayFragment.modifiedListIssue.size();
		}
		
		if(done == true && issueCount != 0)
		{
			view = inflater.inflate(R.layout.known_issue_row, null);
			TextView title = (TextView) view.findViewById(R.id.articletitle);
			TextView desc = (TextView) view.findViewById(R.id.articldesc);
			
			title.setText(issueTitles[position]);
			desc.setText(issueDesc[position]);
		
			issueCount--;
		}
		
		if(issueCount == 0 && done == true)
		{
			if(this.varyingLogNum == 0)
			{
				issueCount = KnownIssueDisplayFragment.modifiedListIssue.size();
			}
			else if(this.varyingLogNum == 1)
			{
			issueCount = savedCount;
			}
		}
		
		
		return view;
	}
	
}
