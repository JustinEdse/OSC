package com.edse.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.edse.edu.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.util.Log;

/**
  * Database class offers an SQLite Database, 
  * which is used by the application to cache articles coming via RSS feeds.
  * @author kaushikvelindla
  *
  */
 
public class Database extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 24;
 
    // Database Name
    private static final String DATABASE_NAME = "articlesManager";
 
    // Articles table name
    private static final String TABLE_ARTICLES = "articles";
    //Events table name
    private static final String TABLE_EVENTS = "events";
    //ChangeLog Table name
    private static final String TABLE_CHANGELOG = "changelog";
    
    private static final String TABLE_ISSUES = "knownissues";
 
    // Articles Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LINK = "link";
    private static final String KEY_DATE = "date";
    private static final String KEY_IMG = "image";
    
  //Events Table Columns names
    private static final String EVENT_ID = "id";
    private static final String EVENT_TITLE = "title";
    private static final String EVENT_DESC = "desc";
    private static final String EVENT_DATE = "date";
    private static final String EVENT_LOCATION = "loc";
    private static final String EVENT_DATE_TIME = "dateTime";
    private static final String EVENT_LINK = "link";
    private static final String EVENT_PUBDATE = "pubdate";
    
 // ChangeLog Table Columns names
    private static final String LOG_ID = "id";
    private static final String LOG_TITLE = "title";
    private static final String LOG_DESC = "desc";
    private static final String LOG_TYPE = "type";
    private static final String LOG_LINK = "link";
    private static final String LOG_DATE = "date";
    
    private static final String ISSUE_ID = "id";
    private static final String ISSUE_TITLE = "title";
    private static final String ISSUE_DESC = "desc";
    private static final String ISSUE_TYPE = "type";
    private static final String ISSUE_LINK = "link";
    private static final String ISSUE_DATE = "date";
    
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d("table issues", "Entering");
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_TYPE + " BLOB," + KEY_LINK + " TEXT," + KEY_DATE + " TEXT," + KEY_IMG + " BLOB" + ")";
        db.execSQL(CREATE_ARTICLES_TABLE);
      //Create Events Table
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + EVENT_ID+ " INTEGER PRIMARY KEY," + EVENT_TITLE + " TEXT,"
                + EVENT_DESC + " TEXT," + EVENT_DATE + " TEXT," + EVENT_LOCATION + " TEXT," + EVENT_DATE_TIME+ " TEXT," + EVENT_LINK + " TEXT," + EVENT_PUBDATE + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
        String CREATE_CHANGELOG_TABLE = "CREATE TABLE " + TABLE_CHANGELOG + "("
                + LOG_ID + " INTEGER PRIMARY KEY," + LOG_TITLE + " TEXT,"
                + LOG_DESC + " TEXT," + LOG_DATE + " TEXT,"  +  LOG_LINK + " TEXT" + ")";
        db.execSQL(CREATE_CHANGELOG_TABLE);
        String CREATE_ISSUES_TABLE = "CREATE TABLE " + TABLE_ISSUES + "("
                + ISSUE_ID + " INTEGER PRIMARY KEY," + ISSUE_TITLE + " TEXT,"
                + ISSUE_DESC + " TEXT," + ISSUE_DATE + " TEXT,"  +  ISSUE_LINK + " TEXT" + ")";
        db.execSQL(CREATE_ISSUES_TABLE);
        Log.d("table issues", "table check " );
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANGELOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISSUES);
        // Create tables again
        onCreate(db);
    }
 
    /**
* All CRUD(Create, Read, Update, Delete) Operations
* @throws IOException
*/
 
    // Adding new contact
	/***
     * Adds a new article to the database. 
     * @param Article article
     * @throws IOException
     */
    public void addArticle(Article article) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, article.getTitle());
        values.put(KEY_DESC, article.getsubDesc());
        values.put(KEY_TYPE, arrLStringToByteArr(article.getType()));//type
        values.put(KEY_LINK, article.getLink());//link
        values.put(KEY_DATE, article.getDate().toString());
        values.put(KEY_IMG, convertToByteArr(article.getPreviewImage()));
        // Inserting Row
        db.insert(TABLE_ARTICLES, null, values);
        db.close(); // Closing database connection
    }
    /***
     * Adds a new event to the database.Checks for the EVENT_DATE, if it is null, the date is set to the empty string "".
     * @param Event event
     * @throws IOException
     */
    public void addEvent(Event event) throws IOException{
     SQLiteDatabase db = this.getWritableDatabase();
    
     ContentValues values = new ContentValues();
     values.put(EVENT_TITLE, event.getEventName());
     values.put(EVENT_DESC, event.getDescription());
     Date temp = event.getDate();
     if (temp != null)
     {
     values.put(EVENT_DATE, event.getDate().toString());	
     }
     else
     {
     values.put(EVENT_DATE, "");	
}
     values.put(EVENT_LOCATION, event.getLocation());
     values.put(EVENT_DATE_TIME, event.getDateAndTime());
     values.put(EVENT_LINK, event.getEventLink());
     values.put(EVENT_PUBDATE, event.getPubDate().toString());
     db.insert(TABLE_EVENTS,null, values);
     db.close();
    }
     /***
     * Adds a new changelog to the database
     * @param Changelog log
     * @throws IOException
     */
    public void addLog(ChangeLog log) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(LOG_TITLE, log.getTitle()); 
        values.put(LOG_DESC, log.getsubDesc()); 
        values.put(LOG_DATE, log.getDate().toString());
        values.put(LOG_LINK, log.getLink());//link
        // Inserting Row
        db.insert(TABLE_CHANGELOG, null, values);
        db.close(); // Closing database connection
    }
     /***
     * Adds a new issue to the database
     * @param KnownIssue issue
     * @throws IOException
     */
    public void addIssue(KnownIssue issue) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ISSUE_TITLE, issue.getTitle()); 
        values.put(ISSUE_DESC, issue.getsubDesc()); 
        values.put(ISSUE_DATE, issue.getDate().toString());
        values.put(ISSUE_LINK, issue.getLink());//link
        // Inserting Row
        db.insert(TABLE_ISSUES, null, values);
        db.close(); // Closing database connection
    }
	   /***
     * Finds and returns an Article instance by its id (Integer)
     * @param int id
     * @return Article 
     * @throws NumberFormatException
     * @throws IOException
     */
    // Getting single article
    Article getArticle(int id) throws NumberFormatException, IOException {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_ARTICLES, new String[] { KEY_ID,
                KEY_TITLE, KEY_DESC, KEY_TYPE, KEY_LINK, KEY_DATE, KEY_IMG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        //id, title, desc, types, link, img
       Article article = new Article(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), byteArrToALOS(cursor.getBlob(3)), cursor.getString(4), convertDate(cursor.getString(5)), convertToBitmap(cursor.getBlob(6)));
        // return article
        return article;
    }
     /***
     * Finds and returns an Event instance by its id(Integer)
     * @param id
     * @return Event event
     * @throws NumberFormatException
     * @throws IOException
     * @throws ParseException
     */
    public Event getEvent(int id) throws NumberFormatException, IOException, ParseException {
     SQLiteDatabase db = this.getReadableDatabase();
    
     Cursor cursor = db.query(TABLE_EVENTS, new String[] {EVENT_TITLE, EVENT_DESC, EVENT_DATE,
     EVENT_LOCATION, EVENT_DATE_TIME, EVENT_LINK, EVENT_PUBDATE},
     EVENT_ID + "=?", new String [] {String.valueOf(id)}, null, null, null, null);
if (cursor != null)
{
cursor.moveToFirst();
}
//public Event(int id, String eventname, String description,Date date,String location, String dateTime, String url, Date pubDate)
// Event event = new Event(cursor.getInt(0), cursor.getString(1),cursor.getString(2),
// MainActivity.extendeddateFormat.parse(cursor.getString(3)),
// cursor.getString(4), cursor.getString(5), cursor.getString(6),
// MainActivity.date_timeFormat.parse(cursor.getString(7)));
SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);
Event event = new Event();
event.addID(cursor.getInt(0));
event.addTitle(cursor.getString(1));
event.addDescription(cursor.getString(2));
String datetext = cursor.getString(3);
if (datetext.length() > 0)
{
event.addDate(format.parse(datetext));
}
event.addLocation(cursor.getString(4));
event.addDateAndTime(cursor.getString(5));
event.addLink(cursor.getString(6));
event.addPubDate(format.parse(cursor.getString(7)));
return event;
    }
	 /***
     * Finds and returns a ChangeLog instance by its id (Integer)
     * @param int id
     * @return ChangeLog
     * @throws NumberFormatException
     * @throws IOException
     */
    ChangeLog getLog(int id) throws NumberFormatException, IOException {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CHANGELOG, new String[] { LOG_ID,
                LOG_TITLE, LOG_DESC, LOG_DATE, LOG_LINK}, LOG_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        //id, title, desc, types, link, img
       ChangeLog log = new ChangeLog(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), convertDate(cursor.getString(4)));
        // return article
        return log;
    }
     /***
     * Finds and returns an KnownIssue instance by its id (Integer)
     * @param int id
     * @return KnownIssue
     * @throws NumberFormatException
     * @throws IOException
     */
    KnownIssue getIssue(int id) throws NumberFormatException, IOException {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_ISSUES, new String[] { ISSUE_ID,
                ISSUE_TITLE, ISSUE_DESC, ISSUE_DATE, ISSUE_LINK}, ISSUE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        //id, title, desc, types, link, img
       KnownIssue issue = new KnownIssue(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), convertDate(cursor.getString(4)));
        // return article
        return issue;
    }
     /***
     * Returns an array list of all the events in the TABLE_ARTICLES
     * @return ArrayList<Article>
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Article> getAllArticles() throws IOException {
        ArrayList<Article> artList = new ArrayList<Article>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setArtID(Integer.parseInt(cursor.getString(0))); //id
                article.setTitle(cursor.getString(1)); //title
                article.setDesc(cursor.getString(2)); //desc
                article.setType(byteArrToALOS(cursor.getBlob(3))); //type
                article.setLink(cursor.getString(4));//link
                article.setDate(convertDate(cursor.getString(5)));
                article.setPreviewImage(convertToBitmap(cursor.getBlob(6))); //image
                
                // Adding article to list
                artList.add(article);
            } while (cursor.moveToNext());
        }
 
        // return article list
        return artList;
    }
    
     /***
     * Returns an array list of all the events in the TABLE_EVENTS
     * @return ArrayList<Event>
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Event> getAllEvents() throws IOException, ParseException
    {
     ArrayList<Event> events = new ArrayList<Event>();
     String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
     SQLiteDatabase dbDatabase = this.getWritableDatabase();
     Cursor cursor = dbDatabase.rawQuery(selectQuery,null);
    
     // looping through all rows and adding to list
	// values.put(EVENT_TITLE, event.getEventName());
	// values.put(EVENT_DESC, event.getDescription());
	// values.put(EVENT_DATE, event.getDate().toString());
	// values.put(EVENT_LOCATION, event.getLocation());
	// values.put(EVENT_DATE_TIME, event.getDateAndTime());
	// values.put(EVENT_LINK, event.getEventLink());
	// values.put(EVENT_PUBDATE, event.getPubDate().toString());
     SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);
     if(cursor.moveToFirst())
     {
     do {
		Event event = new Event();
		event.addID(cursor.getInt(0));
		event.addTitle(cursor.getString(1));
		event.addDescription(cursor.getString(2));
		String datetext = cursor.getString(3);
		if (datetext.length() > 0)
		{
			event.addDate(format.parse(datetext));
	}
		event.addLocation(cursor.getString(4));
		event.addDateAndTime(cursor.getString(5));
		event.addLink(cursor.getString(6));
		event.addPubDate(format.parse(cursor.getString(7)));
		events.add(event);
	} while (cursor.moveToNext());
     }
    
	return events;
    }
    
	 /***
     * Returns an array list of all the events in the TABLE_LOGS
     * @return ArrayList<ChangeLog>
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<ChangeLog> getAllLogs() throws IOException  {
        ArrayList<ChangeLog> logList = new ArrayList<ChangeLog>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHANGELOG;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChangeLog log = new ChangeLog();
                log.setLogID(Integer.parseInt(cursor.getString(0))); //id
                log.setTitle(cursor.getString(1)); //title
                log.setDesc(cursor.getString(2)); //desc
                log.setDate(convertDate(cursor.getString(3)));
                log.setLink(cursor.getString(4));//link
                
                // Adding article to list
                if(!logList.contains(log))
                	logList.add(log);
            } while (cursor.moveToNext());
        }
        return logList;
    }
    
	 /***
     * Returns an array list of all the events in the TABLE_ISSUES
     * @return ArrayList<KnownIssue>
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<KnownIssue> getAllIssues() throws IOException  {
        ArrayList<KnownIssue> issueList = new ArrayList<KnownIssue>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ISSUES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                KnownIssue issue = new KnownIssue();
                issue.setIssueID(Integer.parseInt(cursor.getString(0))); //id
                issue.setTitle(cursor.getString(1)); //title
                issue.setDesc(cursor.getString(2)); //desc
                issue.setDate(convertDate(cursor.getString(3)));
                issue.setLink(cursor.getString(4));//link
                
                // Adding article to list
                if(!issueList.contains(issue))
                	issueList.add(issue);
            } while (cursor.moveToNext());
        }
        return issueList;
    }
 
    /***
     * Updates a single article
     * @param Article
     * @return
     */
    public int updateArticle(Article article) {
    
     //haven't called update yet.........
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, article.getTitle());
        values.put(KEY_DESC, article.getsubDesc());
        //type
        //link
        values.put(KEY_IMG, article.getByteImg());
 
        // updating row
        return db.update(TABLE_ARTICLES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(article.getArtID()) });
    }
 
    /***
     * Deletes a single article
     * @param Article
     * @return
     */
    public void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, KEY_ID + " = ?",
                new String[] { String.valueOf(article.getArtID()) });
        db.close();
    }
 
    /***
     * Deletes a single Event
     * @param Event
     * @return
     */
    public void deleteEvent(Event event)
    {
     SQLiteDatabase db = this.getWritableDatabase();
     db.delete(TABLE_EVENTS, EVENT_ID + " = ?", new String [] {String.valueOf(event.getID())});
     db.close();
    }
	/***
     * Updates a single ChangeLog
     * @param ChangeLog
     * @return
     */
	public int updateLog(ChangeLog log) {
    	
    	//haven't called update yet.........
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(LOG_TITLE, log.getTitle());
        values.put(LOG_DESC, log.getsubDesc());
        //type
        //link
        // updating row
        return db.update(TABLE_CHANGELOG, values, LOG_ID + " = ?",
                new String[] { String.valueOf(log.getLogID()) });
    }
 
    /***
     * Deletes a single ChangeLog
     * @param ChangeLog
     * @return
     */
    public void deleteLog(ChangeLog log) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHANGELOG, LOG_ID + " = ?",
                new String[] { String.valueOf(log.getLogID()) });
        db.close();
    }
    /***
     * Updates a single issue
     * @param KnownIssue
     * @return
     */
	public int updateIssue(KnownIssue issue) {
    	
    	//haven't called update yet.........
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ISSUE_TITLE, issue.getTitle());
        values.put(ISSUE_DESC, issue.getsubDesc());
        //type
        //link
        // updating row
        return db.update(TABLE_ISSUES, values, ISSUE_ID + " = ?",
                new String[] { String.valueOf(issue.getIssueID()) });
    }
 
    /***
     * Deletes a single article
     * @param Article
     * @return
     */
    public void deleteIssue(KnownIssue issue) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ISSUES, ISSUE_ID + " = ?",
                new String[] { String.valueOf(issue.getIssueID()) });
        db.close();
    }
    /***
     * Keeps the size of the Article Table at ARTCILE_TABLE_SIZE. When the articles count exceeds ARTCILE_TABLE_SIZE,
     * articles are deleted from the table from the bottom until Size of the table = ARTCILE_TABLE_SIZE   
     */
    public void trimArticleTable()
    {
     int initialcount = getArticlesCount();
     SQLiteDatabase db = this.getWritableDatabase();
     while (initialcount > MainActivity.ARTICLE_TABLE_SIZE)
     {
db.delete(TABLE_ARTICLES, KEY_ID + " = ?", new String [] {String.valueOf(initialcount)});
initialcount--;
     }
     db.close();
    }
    
    /***
     * Keeps the size of the Event Table at EVENT_TABLE_SIZE. When the articles count exceeds EVENT_TABLE_SIZE,
     * articles are deleted from the table from the bottom until Size of the table = EVENT_TABLE_SIZE   
     */
    public void trimEventTable()
    {
     int initialcount = getEventsCount();
     SQLiteDatabase db = this.getWritableDatabase();
     while (initialcount > MainActivity.EVENT_TABLE_SIZE)
     {
db.delete(TABLE_EVENTS, EVENT_ID + " = ?", new String [] {String.valueOf(initialcount)});
initialcount--;
     }
     db.close();
    }
    /***
     * Keeps the size of the ChangeLog Table at CHANGELOG_TABLE_SIZE. When the articles count exceeds CHANGELOG_TABLE_SIZE,
     * articles are deleted from the table from the bottom until Size of the table = CHANGELOG_TABLE_SIZE   
     */
    public void trimChangeLogTable()
    {
    	int initialcount = getChangeLogCount();
    	SQLiteDatabase db = this.getWritableDatabase();
    	while (initialcount > MainActivity.CHANGELOG_TABLE_SIZE)
    	{
	    	db.delete(TABLE_CHANGELOG, LOG_ID + " = ?", new String [] {String.valueOf(initialcount)});
	    	initialcount--;
    	}
    	db.close();
    }
    /***
     * Keeps the size of the Issue Table at ISSUE_TABLE_SIZE. When the articles count exceeds ISSUE_TABLE_SIZE,
     * articles are deleted from the table from the bottom until Size of the table = ISSUE_TABLE_SIZE   
     */
    public void trimIssueTable()
    {
    	int initialcount = getIssueCount();
    	SQLiteDatabase db = this.getWritableDatabase();
    	while (initialcount > MainActivity.ISSUE_TABLE_SIZE)
    	{
	    	db.delete(TABLE_ISSUES, ISSUE_ID + " = ?", new String [] {String.valueOf(initialcount)});
	    	initialcount--;
    	}
    	db.close();
    }
 
    /***
     * returns the current number of articles in the table TABLE_ARTICLES.
     * @return
     */
    public int getArticlesCount() {
    
     int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_ARTICLES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if(cursor != null && !cursor.isClosed())
        {
         count = cursor.getCount();
         cursor.close();
        }
        
        return count;
    }
    
    /***
     * Returns the current number of events in the table TABLE_EVENTS.
     * @return
     */
    public int getEventsCount() {
    
     int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if(cursor != null && !cursor.isClosed())
        {
         count = cursor.getCount();
         cursor.close();
        }
        
        return count;
    }
    /***
     * Returns the current number of issues in the table TABLE_CHANGELOG.
     * @return
     */
	public int getChangeLogCount() {
    	
    	int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_CHANGELOG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if(cursor != null && !cursor.isClosed())
        {
        	count = cursor.getCount();
        	cursor.close();
        }
        
        return count;
    }
	/***
     * Returns the current number of issues in the table TABLE_ISSUES.
     * @return
     */
public int getIssueCount() {
	
	int count = 0;
    String countQuery = "SELECT  * FROM " + TABLE_ISSUES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    
    if(cursor != null && !cursor.isClosed())
    {
    	count = cursor.getCount();
    	cursor.close();
    }
    
    return count;
}
     /***
     * Resets the Events Table by dropping all the entries and recreating it.
     */
    public void ResetEventTable()
    {
     SQLiteDatabase db = this.getWritableDatabase();
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + EVENT_ID+ " INTEGER PRIMARY KEY," + EVENT_TITLE + " TEXT,"
                + EVENT_DESC + " TEXT," + EVENT_DATE + " TEXT," + EVENT_LOCATION + " TEXT," + EVENT_DATE_TIME+ " TEXT," + EVENT_LINK + " TEXT," + EVENT_PUBDATE + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }
	/***
     * Converts a bitmap image to a byte array.
     * @param bitmap
     * @return
     */
    public byte[] convertToByteArr(Bitmap bitmap)
    {
     ByteArrayOutputStream stream = new ByteArrayOutputStream();
    
     bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
     byte[] retArr = stream.toByteArray();
    
     return retArr;
    }
     /***
     * Converts a byte array to a Bitmap image
     * @param arr
     * @return
     */
    public Bitmap convertToBitmap(byte[] arr)
    {
     Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
    
     return bitmap;
    
    }
        /***
     * Converts an arrayList of Strings into a byte array.
     * @param listOfString
     * @return
     * @throws IOException
     */
    public byte[] arrLStringToByteArr(ArrayList<String> listOfString) throws IOException
    {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
     DataOutputStream out = new DataOutputStream(baos);
    
     for(String entry : listOfString)
     {
     out.writeUTF(entry);
     }
    
     byte[] bytes = baos.toByteArray();
    
     return bytes;
    }
     /***
     * Converts a byte array to an array list of strings
     * @param barr
     * @return
     * @throws IOException
     */
    public ArrayList<String> byteArrToALOS(byte[] barr) throws IOException
    {
     ArrayList<String> retList = new ArrayList<String>();
     ByteArrayInputStream bais = new ByteArrayInputStream(barr);
    
     DataInputStream in = new DataInputStream(bais);
    
     while(in.available() > 0)
     {
     String element = in.readUTF();
     retList.add(element);
     }
    
     return retList;
    }
     /***
     * Converts a string into a corresponding date instance using SimpleDateFormat.parse
     * @param str
     * @return
     */
    public Date convertDate(String str)
    {
     SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);
     Date retDate = null;
	try
	{
		retDate = format.parse(str);
	}
	catch (ParseException e)
	{
	// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return retDate;
    }
    
    
 
}