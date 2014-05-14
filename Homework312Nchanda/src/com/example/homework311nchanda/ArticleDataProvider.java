package com.example.homework311nchanda;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ArticleDataProvider extends ContentProvider {

	// fields for my content provider
	static final String PROVIDER_NAME = "com.example.provider.Data";
	static final String URL = "content://" + PROVIDER_NAME + "/articles";
	static final Uri CONTENT_URI = Uri.parse(URL);

	// DB fields
	static final String _ID = "_id";
	static final String TITLE = "title";
	static final String CONTENT = "content";
	static final String LINK = "link";
	static final String DATE = "date";

	static final int ARICLES = 1;
	static final int ARTICLE_ID = 2;

	private static HashMap<String, String> ARTICLES_PROJECTION_MAP;
	// URI matcher
	static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "articles", ARICLES);
		uriMatcher.addURI(PROVIDER_NAME, "articles/#", ARTICLE_ID);
	}

	/**
	 * Database specific constant declarations
	 */
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "LibraryABC";
	static final String ARTICLES_TABLE_NAME = "articlesABC";
	static final int DATABASE_VERSION = 1;
	//Create Table
	static final String CREATE_DB_TABLE = " CREATE TABLE "
			+ ARTICLES_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " title TEXT NOT NULL, " + " content TEXT NOT NULL, " + " link TEXT NOT NULL, " + " date TEXT NOT NULL);";
	
	
	/**
	 * Helper class that actually creates and manages the provider's underlying
	 * data repository.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("CR ONCREATE", "CreatingDB");
			db.execSQL(CREATE_DB_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		 * Create a write able database which will trigger its creation if it
		 * doesn't already exist.
		 */
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		Log.d("Insert Mtd", "Inserting Data");
		/**
		 * Add a new ARticle record
		 */
		int count = values.size();
		Log.d("Insert Row Id", String.valueOf(count));
		long rowID = db.insert(ARTICLES_TABLE_NAME, "", values);
		/**
		 * If record is added successfully
		 */
		Log.d("Insert Row Id", String.valueOf(rowID));
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ARICLES:
			count = db.delete(ARTICLES_TABLE_NAME, selection, selectionArgs);
			break;
		case ARTICLE_ID:
			String id = uri.getPathSegments().get(1);
			count = db.delete(ARTICLES_TABLE_NAME, _ID
					+ " = "
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		// if uriMatcher returns Articles
		case ARICLES:
			return "vnd.android.cursor.dir/vnd.example.articles";
			// if uriMatcher returns a particular article
		case ARTICLE_ID:
			return "vnd.android.cursor.item/vnd.example.articles";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Log.d("Query (get) Mtd", "Reading Data");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ARTICLES_TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case ARICLES:
			qb.setProjectionMap(ARTICLES_PROJECTION_MAP);
			break;
		case ARTICLE_ID:
			qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == "") {
			/**
			 * By default sort on article titles
			 */
			sortOrder = TITLE;
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		/**
		 * register to watch a content URI for changes
		 */
		c.setNotificationUri(getContext().getContentResolver(), uri);

		Log.d("Cursor Rec Count", String.valueOf(c.getCount()));
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
