package com.edse.edu;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleAdapter extends BaseAdapter
{

	Context context;
	Integer[] artImages;
	String[] artTitles;
	String[] artDesc;
	public static boolean done = false;
	UsableAsync taskI = new UsableAsync(context);
	public static int artCount = 0;
	public static int savedCount = 0;
	
	
	
	private static LayoutInflater inflater = null;
	
	// this ArticleAdapter constructor contains pieces of an Article object.
	public ArticleAdapter(Context context, Integer[] specImg, String[] artTitles, String[] artDesc)
	{
		
		this.context = context;
		this.artImages = specImg;
		this.artTitles = artTitles;
		this.artDesc = artDesc;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return artTitles.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return artTitles[position];
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
		 
		if(done == true && artCount != 0)
		{
			view = inflater.inflate(R.layout.article_row, null);
			ImageView image = (ImageView) view.findViewById(R.id.list_image);
			TextView title = (TextView) view.findViewById(R.id.articletitle);
			TextView desc = (TextView) view.findViewById(R.id.articldesc);
			
			image.setImageResource(artImages[position]);
			title.setText(artTitles[position]);
			desc.setText(artDesc[position]);
		
			artCount--;
		}
		
		if(artCount == 0 && done == true)
		{
			artCount = savedCount;
		}
		
		return view;
	}
	
}
