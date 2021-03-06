package com.edse.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * This class extends the class BaseAdapter and represents the listView Corresponding to the <br>
 * main menu that contains the following fields :
 * 
 * 1. News <br>
 * 2. Calendar <br>
 * 3. Change Log <br>
 * 4. KnownIssues <br>
 * 
 * @author kaushikvelindla
 *
 */
public class MenuListAdapter extends BaseAdapter
{

	// Declare Variables
	Context context;
	String[] mTitle;
	int[] mIcon;
	LayoutInflater inflater;

	public MenuListAdapter(Context context, String[] title, int[] icon)
	{
		this.context = context;
		this.mTitle = title;
		this.mIcon = icon;
	}

	@Override
	public int getCount()
	{
		return mTitle.length;
	}

	@Override
	public Object getItem(int position)
	{
		return mTitle[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Declare Variables
		TextView txtTitle;
		ImageView imgIcon;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.drawer_list_item, parent,
				false);

		// Locate the TextViews in drawer_list_item.xml
		txtTitle = (TextView) itemView.findViewById(R.id.title);

		// Locate the ImageView in drawer_list_item.xml
		imgIcon = (ImageView) itemView.findViewById(R.id.icon);

		// Set the results into TextViews
		txtTitle.setText(mTitle[position]);

		// Set the results into ImageView
		imgIcon.setImageResource(mIcon[position]);

		return itemView;
	}

}