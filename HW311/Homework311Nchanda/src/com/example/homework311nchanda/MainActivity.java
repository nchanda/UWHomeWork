package com.example.homework311nchanda;

import java.util.ArrayList;

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
	ListView listview;
	ArticleData ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d("OnCreate", "Good Morning");

		// ***Insert Data into DB (One time only). So based on i value
		int i = loadArticlesFromProvider();
		if(i == 1)
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
		//***Not able make this work.
		// mShakeDetector.setOnShakeListener(new OnShakeListener() {
		//
		// public void onShake(int count) {
		// //TDO: code for refresh by querying content provider.
		// handleShakeEvent(count);
		// }
		// });

	}

	//USed to add data into DB
	private void addDataIntoDB() {
		AddArticleData1();
		AddArticleData2();
		AddArticleData3();
		AddArticleData4();
		AddArticleData5();
	}

	//Connecting to Content Provider to Read DB
	private int loadArticlesFromProvider() {
		Log.d("LoadArticlesMtd", "Loading Data...");
		//Used to track record count in DB
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
				int Id = c.getInt(c.getColumnIndex(ArticleDataProvider._ID));
				ArticleData a = new ArticleData(title, content, Id);
				a.setTitle(title);
				a.setContent(content);
				a.setId(Id);
				myArticles.add(a);
			} while (c.moveToNext());
		}
		return i;
	}

	//Below 5 mtds are used to load data in DB using Content provider.
	private void AddArticleData1() {
		Log.d("Article 1", "Adding Data");
		ContentValues values = new ContentValues();

		values.put(ArticleDataProvider.TITLE,
				"Apple application to trademark iPad Mini denied - CNET");
		values.put(
				ArticleDataProvider.CONTENT,
				"The U.S. Patent and Trademark Office has denied Apple's bid to trademark the term iPad Mini, "
						+ "contending that mini is merely descriptive of goods or services sold in miniature form. In a letter sent to Apple in January but"
						+ " only recently published, the USPTO reviewer denied Apple's application because the applied-for mark merely describes a feature or "
						+ "characteristic of applicant's goods. Apple can appeal the decision, but to win a reversal the company will need to address the office's "
						+ "reasons for denial.");
		Uri uri = getContentResolver().insert(ArticleDataProvider.CONTENT_URI,
				values);
	}

	private void AddArticleData2() {
		Log.d("Article 2", "Adding Data");
		ContentValues values = new ContentValues();
		values.put(ArticleDataProvider.TITLE,
				"Facebook's Android OS will be called �Facebook Home� - SlashGear");
		values.put(
				ArticleDataProvider.CONTENT,
				"Last week, we reported that Facebook is planning on revealing its own customized version of the Google Android operating system ."
						+ " They will be debuting their �special� version of the Android OS onto one of HTC�s devices. It�s speculated that it will launch on a new HTC "
						+ "device, however, there�s also reports that the OS will be able to run on HTC�s older handsets, and even on its upcoming flagship handset, "
						+ "the HTC One. Now reports are saying that Facebook�s version of Android OS will be called �Facebook Home�. Sources revealed to 9to5Google "
						+ "that the tagline on Facebook�s invitations, �Come See Our New Home On Android�, is actually a teaser to the new product name."
						+ " Facebook�s version of the OS will feature deep integration into Android. Facebook Messenger, Photos, and Contacts will be set as the "
						+ "default programs, with Facebook Messenger being used for both messaging your Facebook friends, as well as sending out SMS text messages.");
		Uri uri = getContentResolver().insert(ArticleDataProvider.CONTENT_URI,
				values);
	}

	private void AddArticleData3() {
		Log.d("Article 3", "Adding Data");
		ContentValues values = new ContentValues();
		values.put(ArticleDataProvider.TITLE,
				"Spamhaus DDoS attack not to blame for rise in spam - DaniWeb (blog)");
		values.put(
				ArticleDataProvider.CONTENT,
				"The media, online and off, has been full of scare stories about the 'biggest Internet attack ever' and how a "
						+ "distributed denial of service (DDoS) campaign aimed against anti-spam outfit Spamhaus peaked at an attack volume of 300 Gbps (the highest ever"
						+ " recorded by those who record such things) was 'slowing down the global Internet'. DaniWeb didn't join the rush to shout 'the sky is falling' as,"
						+ " frankly, we didn't believe it as there was precious little evidence to be found that the DDoS attack was impacting anyone other than Spamhaus"
						+ " along with it's anti-DDoS protection service CloudFlare and their upstream providers. Sure it was a serious attack, one that could well have"
						+ " implications on the direction such things are heading in, and potentially could be bad news for all of use. However, the Internet did not "
						+ "slow down and for the vast majority of global users there was no noticeable effect at all. The one area that you might think would be"
						+ " impacted is the amount of spam that reaches your mailbox. After all, if one of the main organisations responsible for keeping the lid "
						+ "on spam distribution channels is taken off air then surely we can expect to see spam levels peak. So when a press release arrived following"
						+ " these attacks which proclaimed that spam is twice as likely to be hitting mailboxes than previously, I was concerned. But only for a few"
						+ " moments, as a bit more reading reassured me that it had nothing to do with the Spamhaus attacks at all.");
		Uri uri = getContentResolver().insert(ArticleDataProvider.CONTENT_URI,
				values);
	}

	private void AddArticleData5() {
		Log.d("Article 5", "Adding Data");
		ContentValues values = new ContentValues();
		values.put(
				ArticleDataProvider.TITLE,
				"World's top supercomputer from '09 is now obsolete, will be dismantled - Ars Technica");
		values.put(
				ArticleDataProvider.CONTENT,
				"Five years ago, an IBM-built supercomputer designed to model the decay of the US nuclear weapons arsenal "
						+ "was clocked at speeds no computer in the history of Earth had ever reached. At more than one quadrillion floating point operations "
						+ "per second (that's a million billion, or a petaflop), the aptly-named Roadrunner was so far ahead of the competition that it earned "
						+ "the #1 slot on the Top 500 supercomputer list in June 2008, November 2008, and one last time in June 2009. Today, that computer "
						+ "has been declared obsolete and it's being taken offline. Based at the US Department of Energy's Los Alamos National Laboratory in"
						+ " New Mexico, Roadrunner will be studied for a while and then ultimately dismantled. While the computer is still one of the 22 "
						+ "fastest in the world, it isn't energy-efficient enough to make the power bill worth it. During its five operational years,"
						+ "Roadrunner, part of the National Nuclear Security Administration�s Advanced Simulation and Computing (ASC) program to provide key"
						+ "computer simulations for the Stockpile Stewardship Program, was a workhorse system providing computing power for stewardship of the "
						+ "US nuclear deterrent, and in its early shakedown phase, a wide variety of unclassified science, Los Alamos lab said in an announcement "
						+ "Friday. Costing more than $120 million, Roadrunner's 296 server racks covering 6,000 square feet were connected with InfiniBand and "
						+ "contained 122,400 processor cores. The hybrid architecture used IBM PowerXCell 8i CPUs (an enhanced version of the Sony PlayStation "
						+ "3 processor) and AMD Opteron dual-core processors. The AMD processors handled basic tasks, with the Cell CPUs taking on the most"
						+ " computationally intense parts of a calculation�thus acting as a computational accelerator, Los Alamos wrote.");
		Uri uri = getContentResolver().insert(ArticleDataProvider.CONTENT_URI,
				values);
	}

	private void AddArticleData4() {
		Log.d("Article 4", "Adding Data");
		ContentValues values = new ContentValues();
		values.put(
				ArticleDataProvider.TITLE,
				"Vizio announces pricing for new Windows 8 laptops, all-in-one desktops - ZDNet");
		values.put(
				ArticleDataProvider.CONTENT,
				"With its first round of computers, Vizio tried the approach that made it wildly successful with its HDTVs:"
						+ " decent quality at a very affordable price. But with its latest group of desktops and laptops, Vizio is going upscale. Introduced at this year's"
						+ " CES, the new Windows 8 touchscreen-enabled Vizio PC lineup has just received its pricing, and not one of the four new systems costs less than"
						+ " $1,000. The least expensive model is the CT14T-B0 14-inch laptop, which features an AMD APU processor and a $1,089 price tag. But adding"
						+ " an Intel Core i7 processor instead boosts the price to $1,419.99. The 15.6-inch thin-and-light laptop starts at $1,189.99 with AMD inside,"
						+ " whereas the Core i7 flavor of the same machine runs $1,469.99. Note that while these notebooks include touchscreen capabilities,"
						+ " they are not convertible models with tablet functionality that justify the high price. But they are being marketed as super-svelte"
						+ " competitors designed to take on the MacBook Air, with similar aluminum unibody construction and solid state storage. Pricing for premium "
						+ "all-in-one desktop PCs tends to run a little higher, so the sticker price for Vizio's new desktops appears a bit more in line with the "
						+ "overall marketplace. The 24-inch CA24T-B0 starts at $1,279.99, and includes an AMD A10-4600M processor, 8GB of RAM, 1TB hard drive, "
						+ "and built-in 802.11ac Wi-Fi capabilities. A version using a Core i7 CPU instead will run $1,439.99. There's no AMD version of the "
						+ "27-inch all-in-one, with the CA27T-A5 coming with a Core i7-3630QM processor and similar components as the 24-inch model (albeit with a "
						+ "bigger screen). It will set you back $1,549.99.");
		Uri uri = getContentResolver().insert(ArticleDataProvider.CONTENT_URI,
				values);
	}

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
