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

import com.edse.edu.Article;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
public class Database extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 13;
 
    // Database Name
    private static final String DATABASE_NAME = "articlesManager";
 
    // Articles table name
    private static final String TABLE_ARTICLES = "articles";
 
    // Articles Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LINK = "link";
    private static final String KEY_DATE = "date";
    private static final String KEY_IMG = "image";
    
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_TYPE + " BLOB," + KEY_LINK + " TEXT," +  KEY_DATE + " TEXT," + KEY_IMG + " BLOB" + ")";
        db.execSQL(CREATE_ARTICLES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     * @throws IOException 
     */
 
    // Adding new contact
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
     
    // Getting All Articles
    public ArrayList<Article> getAllArticles() throws IOException  {
        ArrayList<Article> artList = new ArrayList<Article>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLES;
 
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
 
    // Updating single article
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
 
    // Deleting single article
    public void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, KEY_ID + " = ?",
                new String[] { String.valueOf(article.getArtID()) });
        db.close();
    }
 
 
    // Getting articles Count
    public int getArticlesCount() {
    	
    	int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_ARTICLES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if(cursor != null && !cursor.isClosed())
        {
        	count = cursor.getCount();
        	cursor.close();
        }
        
        return count;
    }
    
    public byte[] convertToByteArr(Bitmap bitmap)
    {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	
    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    	byte[] retArr = stream.toByteArray();
    	
    	return retArr;
    }
    
    public Bitmap convertToBitmap(byte[] arr)
    {
    	Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
    	
    	return bitmap;
    	
    }
    
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
    
    public Date convertDate(String str)
    {
    	SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy", Locale.US);
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