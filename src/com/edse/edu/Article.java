package com.edse.edu;

import java.util.ArrayList;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

//import javax.imageio.ImageIO;



import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Article class includes 
 * {@code 
 * id of the article;
 * title;
 * subDesc;
 * type;
 * stored Image (for caching as stored Image);
 * preview Image (in Bitmap format for thumbnails);
 * date; 
 * }
 * @author kaushikvelindla
 *
 */
public class Article implements Parcelable
{
	private int id;
	private String title;
	private String subDesc;
	private ArrayList<String> type;
	private String link;
	private byte[] storedImage;
	private Bitmap previewImage;
	//these images will be small and so we don't have to worry about them taking up
	//huge amounts of memory/space. They will be to the left of the article title and description on
	//the article previews.
	private Date date;
	
	//don't want to store the actual text of articles. This could take up a lot of space.
	//
	/***
	 * Constructor of the article class
	 */
	public Article()
	{
		
	}
	/***
	 * Overloaded Constructor for the article class, initializes the following fields:
	 * @param title
	 * @param subDesc
	 * @param type
	 * @param bitmap
	 * @param link
	 * @param date
	 */
	public Article(String title, String subDesc, ArrayList<String> type, Bitmap bitmap, String link, Date date)
	{
		this.title = title;
		this.subDesc = subDesc;
		this.type = type;
		this.previewImage = bitmap;
		this.link = link;
		
		//format of date coming from article???
		this.date = date;
	}
	/***
	 * Overloaded Constructor which initializes 'id' in addition to rest of the fields with the exception of the byte array for the stored image
	 * @param id
	 * @param title
	 * @param desc
	 * @param types
	 * @param link
	 * @param date
	 * @param bitmap
	 */
	public Article(int id,String title, String desc, ArrayList<String> types, String link, Date date,Bitmap bitmap)
	{
		this.id = id;
		this.title = title;
		this.subDesc = desc;
		this.type = types;
		this.link = link;
		this.date = date;
		this.previewImage = bitmap;
		
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
	
	public ArrayList<String> getType()
	{
		return this.type;
	}
	public void setType(ArrayList<String> type)
	{
		this.type = type;
	}
	
	public Bitmap getPreviewImage()
	{
		return this.previewImage;
	}
	public void setPreviewImage(Bitmap previewImage)
	{
		this.previewImage = previewImage;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public byte[] getByteImg()
	{
		return this.storedImage;
	}
	public void setByteImg(byte[] img)
	{
		this.storedImage = img;
	}
	
	public int getArtID()
	{
		return this.id;
	}
	public void setArtID(int id)
	{
		this.id = id;
	}
	

	//This method may be handy depending on the format in which images
	//are stored on the server or in code. Again since we will be working
	//with small images I don't think memory should be an issue.
	/*
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
*/
	// These methods below help implement the parcelable class. It just allows the 
	// bundling and passing of custom objects to activites or fragments.
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
		dest.writeStringList(type);
		dest.writeValue(previewImage);
		dest.writeString(date.toString());
		
		
	}
	public Article(Parcel in)
	{
		this.title = in.readString();
		this.subDesc = in.readString();
		this.type = in.readParcelable(ArrayList.class.getClassLoader());
		this.previewImage = in.readParcelable(Bitmap.class.getClassLoader());
		this.date = in.readParcelable(Date.class.getClassLoader());
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
	
	/***
	 * toString method of the article class prints out to standard output title of the article 
	 */
	@Override
	public String toString()
	{
		return this.title;
	}
}
