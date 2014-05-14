package com.example.homework311nchanda;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class MainActivity extends Activity implements OnItemClickListener {

	// The following are used for the shake detection
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;

	ArrayList<ArticleData> myArticles;
	List<ArticleData> myData;
	ContentValues values;
	ListView listview;
	ArticleData ad;
	ArtcileXMLPullParser _parser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d("OnCreate", "Good Morning");

		// ***Insert Data into DB (One time only). So based on i value
		// int i = loadArticlesFromProvider();
		// if(i == 1)
		addDataIntoDB();

		// Get Data and display in List view format

		myArticles = new ArrayList<ArticleData>();

		// Read from DB using ContentResolver & add to array
		loadArticlesFromProvider();

		// Find list view & Bind to Adapter
		listview = (ListView) findViewById(R.id.list);
		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
				R.layout.list_item, myArticles);
		Log.d("At Adapater", "Back from cutm Adap");
		listview.setAdapter(adapter);
		// Click event for single list row
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this, ListDetail.class);
				ad = myArticles.get(position);
				i.putExtra("title", ad.title);
				i.putExtra("content", ad.content);
				startActivity(i);
			}
		});

		// ShakeDetector initialization
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetector();
		// ***Not able make this work.
		// mShakeDetector.setOnShakeListener(new OnShakeListener() {
		//
		// public void onShake(int count) {
		// //TDO: code for refresh by querying content provider.
		// handleShakeEvent(count);
		// }
		// });

	}

	// USed to add data into DB
	private void addDataIntoDB() {
		Log.d("At Add Data", "Mtd. called add to DB");
		_parser = new ArtcileXMLPullParser();
		try {
			values = new ContentValues();
			myData = _parser.getArticlesFromRSS(MainActivity.this,
					"http://news.yahoo.com/rss/world");
			Log.d("At Add Data1", String.valueOf(myData.size()));
			// List Array to ContentValues
			for (int i = 0; i < myData.size(); i++) {
				// Read each ArticleData object from List and add it to content
				// values
				ad = myData.get(i);
				Log.d("At AddDb forloop", String.valueOf(i));
				values.put(ArticleDataProvider.TITLE, ad.getTitle());
				values.put(ArticleDataProvider.CONTENT, ad.getContent());
				values.put(ArticleDataProvider.LINK, ad.getLink());
				values.put(ArticleDataProvider.DATE, ad.getDate());

				// Insert in DB using ContentProvider
				Log.d("At AddDb3", "Before ContentProvider");
				Uri uri = getContentResolver().insert(
						ArticleDataProvider.CONTENT_URI, values);
				values.clear();
			}
			values.clear();
			myData.clear();
			
			//Second RSS feed parsing and inserting into DB.
			myData = _parser.getArticlesFromRSS(MainActivity.this,
					"https://news.google.com/news/section?topic=w&output=rss");
			Log.d("At Add Data2", String.valueOf(myData.size()));
			for (int i = 0; i < myData.size(); i++) {
				// Read each ArticleData object from List and add it to content
				// values
				ad = myData.get(i);
				Log.d("At AddDb forloop5", String.valueOf(i));
				values.put(ArticleDataProvider.TITLE, ad.getTitle());
				values.put(ArticleDataProvider.CONTENT, ad.getContent());
				values.put(ArticleDataProvider.LINK, ad.getLink());
				values.put(ArticleDataProvider.DATE, ad.getDate());

				// Insert in DB using ContentProvider
				Log.d("At AddDb5", "Before ContentProvider");
				Uri uri = getContentResolver().insert(
						ArticleDataProvider.CONTENT_URI, values);
				values.clear();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Connecting to Content Provider to Read DB
	private int loadArticlesFromProvider() {
		Log.d("LoadArticlesMtd", "Loading Data...");
		// Used to track record count in DB
		int i = 0;
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(ArticleDataProvider.CONTENT_URI, null, null, null,
				null);

		if (c == null)
			i = 1;

		if (c.moveToFirst()) {
			do {
				String title = c.getString(c
						.getColumnIndex(ArticleDataProvider.TITLE));
				String content = c.getString(c
						.getColumnIndex(ArticleDataProvider.CONTENT));
				String link = c.getString(c
						.getColumnIndex(ArticleDataProvider.LINK));
				String date = c.getString(c
						.getColumnIndex(ArticleDataProvider.DATE));
				int Id = c.getInt(c.getColumnIndex(ArticleDataProvider._ID));
				ArticleData a = new ArticleData(title, content, Id, null, date,
						link);
				a.setTitle(title);
				a.setContent(content);
				a.setId(Id);
				a.setLink(link);
				a.setDate(date);
				myArticles.add(a);
			} while (c.moveToNext());
		}
		return i;
	}

	/*
	 * private void AddArticleData4() { Log.d("Article 4", "Adding Data");
	 * ContentValues values = new ContentValues(); values.put(
	 * ArticleDataProvider.TITLE,
	 * "Vizio announces pricing for new Windows 8 laptops, all-in-one desktops - ZDNet"
	 * ); values.put( ArticleDataProvider.CONTENT,
	 * "With its first round of computers, Vizio tried the approach that made it wildly successful with its HDTVs:"
	 * +
	 * " decent quality at a very affordable price. But with its latest group of desktops and laptops, Vizio is going upscale. Introduced at this year's"
	 * +
	 * " CES, the new Windows 8 touchscreen-enabled Vizio PC lineup has just received its pricing, and not one of the four new systems costs less than"
	 * +
	 * " $1,000. The least expensive model is the CT14T-B0 14-inch laptop, which features an AMD APU processor and a $1,089 price tag. But adding"
	 * +
	 * " an Intel Core i7 processor instead boosts the price to $1,419.99. The 15.6-inch thin-and-light laptop starts at $1,189.99 with AMD inside,"
	 * +
	 * " whereas the Core i7 flavor of the same machine runs $1,469.99. Note that while these notebooks include touchscreen capabilities,"
	 * +
	 * " they are not convertible models with tablet functionality that justify the high price. But they are being marketed as super-svelte"
	 * +
	 * " competitors designed to take on the MacBook Air, with similar aluminum unibody construction and solid state storage. Pricing for premium "
	 * +
	 * "all-in-one desktop PCs tends to run a little higher, so the sticker price for Vizio's new desktops appears a bit more in line with the "
	 * +
	 * "overall marketplace. The 24-inch CA24T-B0 starts at $1,279.99, and includes an AMD A10-4600M processor, 8GB of RAM, 1TB hard drive, "
	 * +
	 * "and built-in 802.11ac Wi-Fi capabilities. A version using a Core i7 CPU instead will run $1,439.99. There's no AMD version of the "
	 * +
	 * "27-inch all-in-one, with the CA27T-A5 coming with a Core i7-3630QM processor and similar components as the 24-inch model (albeit with a "
	 * + "bigger screen). It will set you back $1,549.99."); Uri uri =
	 * getContentResolver().insert(ArticleDataProvider.CONTENT_URI, values); }
	 */

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.d("ItemClick", "Clicking Item");
	}

	@Override
	public void onResume() {
		super.onResume();
		// Add the following line to register the Session Manager Listener
		// onResume
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause() {
		// Add the following line to unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);
		super.onPause();
	}
}
