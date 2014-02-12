package com.edse.edu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;


/**
 * Checks the state of the external storage of the device.
 * 
 */
public class StorageChecker
{

	private boolean externalStorageAvailable, externalStorageWriteable;
	private String path = "";

	/**
	 * Checks the external storage's state and saves it in member attributes.
	 */
	private void checkStorage()
	{
		// Get the external storage's state
		String state = Environment.getExternalStorageState();

		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			// Storage is available and writeable
			externalStorageAvailable = externalStorageWriteable = true;
		}
		else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			// Storage is only readable
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		}
		else
		{
			// Storage is neither readable nor writeable
			externalStorageAvailable = externalStorageWriteable = false;
		}
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is available, false otherwise.
	 */
	public boolean isExternalStorageAvailable()
	{
		checkStorage();

		return externalStorageAvailable;
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is writeable, false otherwise.
	 */
	public boolean isExternalStorageWriteable()
	{
		checkStorage();

		return externalStorageWriteable;
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is available and writeable, false
	 *         otherwise.
	 */
	public boolean isExternalStorageAvailableAndWriteable()
	{
		checkStorage();

		if (!externalStorageAvailable)
		{
			return false;
		}
		else if (!externalStorageWriteable)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void writeToSDCard(ArrayList<Byte> images)
	{
		// check if SD card is present and writable before attempting
		// to write to external storage.
		FileOutputStream outStream = null;
		try
		{
			File deviceSDCard = Environment.getExternalStorageDirectory();
			File dir = new File(deviceSDCard.getAbsolutePath() + "/sysImages");

			if (!dir.exists())
			{
				dir.mkdir();
			}

			int imgCount = 1;
			for (Byte img : images)
			{
				File sysImgFile = new File(dir, "sysImg" + imgCount + ".txt");
				outStream = new FileOutputStream(sysImgFile);

				// normally the method "getBytes()" would need to be called but
				// since we are
				// already dealing with Bytes then the img variable is left as
				// is.
				outStream.write(img);

				imgCount++;
			}

			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeImages(ArrayList<Byte> images, Context context)
	{
		FileOutputStream outStream = null;
		File dir = null;
		int filesAlreadySaved = 0;
		// 1. When writing of images is wanting to take place we first check if
		// an SD card
		// or external storage is available. If it is then that's where we
		// write. If it is not available
		// then store the images in internal storage.
		//
		// 2. Next we need to check if there are already 4 system status images
		// in place that need to be
		// updated with the new four or there aren't any at all. First we need to read
		// from either the SD card or internal storage and see if we in fact have 4 images
		// already in the directory. If we do then replace them with the fresh images (CHECK THAT WE
		// DEFINITIVELY HAVE THESE IMAGES BEFORE OVERWRITING). If not then just write the four images.
		
		try
		{
			if (isExternalStorageAvailableAndWriteable())
			{
				File deviceSDCard = Environment.getExternalStorageDirectory();
				dir = new File(deviceSDCard.getAbsolutePath() + "/sysImages");
			}
			else
			{
				dir = context.getDir("sysImgInternal", Context.MODE_PRIVATE);
			}

			//count images already in directory of internal storage or the SD card.
		    filesAlreadySaved = StorageChecker.countImagesInDir(dir);
			
		    if(filesAlreadySaved != 0)
		    {
		    	//using apache commons libs to easily delete all previous files from the directory.
		    	FileUtils.cleanDirectory(dir);
		    }
		    
		    // So either way this part of code should be writing images to the directory whether its on the
		    // SD card or internal storage.
			int imgCount = 1;
			for (Byte img : images)
			{
				File imgFile = new File(dir, "sysImg" + imgCount + ".txt");
				outStream = new FileOutputStream(imgFile);

				// normally the method "getBytes()" would need to be called but
				// since we are
				// already dealing with Bytes then the img variable is left as
				// is.
				outStream.write(img);

				imgCount++;
			}

			outStream.flush();
			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static int countImagesInDir(File dirProvided)
	{
		return 0;
		
	}
	public static void readImages()
	{
		//would there be an event where images would be stored on both the sd card and internal memory?
		//Perhaps someone is using are application with an SD initially and then loses their SD card. 
		
	}
}