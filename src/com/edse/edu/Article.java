package com.edse.edu;

import java.util.Date;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import android.os.Parcel;
import android.os.Parcelable;


public class Article implements Parcelable
{
	private String title;
	private String subDesc;
	private String type;
	private byte[] previewImage;
	//these images will be small and so we don't have to worry about them taking up
	//huge amounts of memory/space. They will be to the left of the article title and description on
	//the article previews.
	private String date;
	
	//don't want to store the actual text of articles. This could take up a lot of space.
	//
	
	public Article(String title, String subDesc, String type, String text, String date)
	{
		this.title = title;
		this.subDesc = subDesc;
		this.type = type;
		
		
		//format of date coming from article???
		this.date = date;
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
	
	public String getType()
	{
		return this.type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	public byte[] getPreviewImage()
	{
		return this.previewImage;
	}
	public void setPreviewImage(byte[] previewImage)
	{
		this.previewImage = previewImage;
	}
	
	public String getDate()
	{
		return this.date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	

	//This method may be handy depending on the format in which images
	//are stored on the server or in code. Again since we will be working
	//with small images I don't think memory should be an issue.
	
	public BufferedImage convertToBuffImage(byte[] image)
	{
		//declaring variables to work with.
		BufferedImage buffImage = null;
		InputStream input = new ByteArrayInputStream(image);
		try
		{
			buffImage = ImageIO.read(input);
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffImage;
	
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag)
	{
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(subDesc);
		dest.writeString(type);
		dest.writeByteArray(previewImage);
		dest.writeString(date);
		
	}
	public Article(Parcel in)
	{
		this.title = in.readString();
		this.subDesc = in.readString();
		this.type = in.readString();
		in.readByteArray(this.previewImage);
		this.date = in.readString();
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
		public Article createFromParcel(Parcel in)
		{
			return new Article(in);
		}
		public Article[] newArray(int size)
		{
			return new Article[size];
		}

	};
}
