package com.edse.edu;

import java.util.ArrayList;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class KnownIssue implements Parcelable
{
	private int id;
	private String title;
	private String subDesc;
	private ArrayList<String> type;
	private String link;
	private Date date;
	
	//don't want to store the actual text of changeLogs. This could take up a lot of space.
	//
	
	public KnownIssue()
	{}
	
	public KnownIssue(String title, String subDesc, String link, Date date)
	{
		this.title = title;
		this.subDesc = subDesc;
		this.link = link;
		this.date = date;
	}
	
	public KnownIssue(int id,String title, String desc, String link, Date date)
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
	
	public int getIssueID()
	{
		return this.id;
	}
	public void setIssueID(int id)
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
		dest.writeStringList(type);
		dest.writeString(date.toString());
		
		
	}
	public KnownIssue(Parcel in)
	{
		this.title = in.readString();
		this.subDesc = in.readString();
		this.type = in.readParcelable(ArrayList.class.getClassLoader());
		this.date = in.readParcelable(Date.class.getClassLoader());
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
		public KnownIssue createFromParcel(Parcel in)
		{
			return new KnownIssue(in);
		}
		public KnownIssue[] newArray(int size)
		{
			return new KnownIssue[size];
		}

	};
	
	// standard overriding of the toString() method.
	@Override
	public String toString()
	{
		return this.title;
	}

}
