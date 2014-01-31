package com.edse.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.edse.edu.Article;

public class ClientCompnt
{
    static ArrayList<Article> articles;
	//Need IP and Port number to connect to server?..
	
	public static ArrayList<Article> getArticlesByType(String type)
	{
		articles =  new ArrayList<Article>();
		
		return articles;
	}
	
	public static ArrayList<Article> getArticlesByDate(Date date)
	{
		articles =  new ArrayList<Article>();
		
		return articles;
	}
	
	public static void connectToServer(String IP, int port)
	{
		//open a socket to start communication with the OSC server...
		// not filling this in yet because we don't have enough
		//information or server layout/architecture.
		
		//CHECK INTERNET CONNECTIVITY//////////////////////////
		try
		{
			Socket socket = new Socket(IP, port);
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
