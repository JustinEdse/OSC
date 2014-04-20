package com.edse.edu;

import java.util.ArrayList;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ChangeLog implements Parcelable
{
	private int id;
	private String title;
	private String subDesc;
	private String link;
	private Date date;
	
	//don't want to store the actual text of changeLogs. This could take up a lot of space.
	//
	
	public ChangeLog()
	{}
	
	public ChangeLog(String title, String subDesc, String link, Date date)
	{
		this.title = title;
		this.subDesc = subDesc;
		this.link = link;
		this.date = date;
	}
	
	public ChangeLog(int id,String title, String desc, String link, Date date)
	{
		this.id = id;
		this.title = title;
		this.subDesc = desc;
		this.link = link;
		this.date = date;
	}
	
	public String getLink()
	{
		return this.link;
	}
	public void setLink(String link)
	{
		this.link = link;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getsubDesc()
	{
		return this.subDesc;
	}
	public void setDesc(String subDesc)
	{
		this.subDesc = subDesc;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public int getLogID()
	{
		return this.id;
	}
	public void setLogID(int id)
	{
		this.id = id;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag)
	{
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(subDesc);
		dest.writeString(date.toString());
		
		
	}
	public ChangeLog(Parcel in)
	{
		this.title = in.readString();
		this.subDesc = in.readString();
		this.date = in.readParcelable(Date.class.getClassLoader());
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
		public ChangeLog createFromParcel(Parcel in)
		{
			return new ChangeLog(in);
		}
		public ChangeLog[] newArray(int size)
		{
			return new ChangeLog[size];
		}

	};
	
	// standard overriding of the toString() method.
	@Override
	public String toString()
	{
		return this.title;
	}

}
