package com.example.homework311nchanda;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

public class ArtcileXMLPullParser {

	static final String KEY_ITEM = "item";
	static final String KEY_TITLE = "title";
	static final String KEY_DESCRIPTION = "description";
	static final String KEY_LINK = "link";
	static final String KEY_DATE = "pubDate";
	String _ignoretag = null;


	public List<ArticleData> getArticlesFromRSS(Context cxt, String URL1)
			throws Exception {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		Log.d("At XML Parsingclass", "getArticleFromRSS");

		List<ArticleData> articleData;
		articleData = new ArrayList<ArticleData>();

		// temp holder for current ArticleData while parsing
		ArticleData curStackSite = null;
		// temp holder for current text value while parsing
		String curText = "";
		URL url = new URL(URL1);

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		// We will get the XML from an input stream
		try {
			xpp.setInput(getInputStream(url), "UTF_8");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		// get initial eventType
		int eventType = xpp.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			// Get the current tag

			String tagname = xpp.getName();

			if (tagname != null) {
				if (tagname.equalsIgnoreCase("channel")) {
					_ignoretag = tagname;
				}
			}
			// React to different event types appropriately
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagname.equalsIgnoreCase(KEY_ITEM)) {
					// If we are starting a new <site> block we need
					// a new StackSite object to represent it
					_ignoretag = KEY_ITEM;
					curStackSite = new ArticleData();
				}
				break;

			case XmlPullParser.TEXT:
				// grab the current text so we can use it in END_TAG event
				curText = xpp.getText();
				Log.d("At Parser3", curText);
				break;

			case XmlPullParser.END_TAG:
				if (_ignoretag.equalsIgnoreCase(KEY_ITEM)) {

					if (tagname.equalsIgnoreCase(KEY_ITEM)) {
						// if </item> then we are done with current item
						// add it to the list.
						articleData.add(curStackSite);
					} else if (tagname.equalsIgnoreCase(KEY_TITLE)) {
						// if </title> use setTitle() on curItem
						curStackSite.setTitle(curText);
					} else if (tagname.equalsIgnoreCase(KEY_DESCRIPTION)) {
						// if </descritpion> use setDesc() on curItem
						curStackSite.setContent(curText);
					} else if (tagname.equalsIgnoreCase(KEY_LINK)) {
						// if </link> use setLink() on curItem
						curStackSite.setLink(curText);
					} else if (tagname.equalsIgnoreCase(KEY_DATE)) {
						// if </pubDate> use setDate() on curItem
						curStackSite.setDate(curText);
					}
					break;

				}

			default:
				break;

			}

			eventType = xpp.next();
		}
		// stream.close();
		return articleData;

	}

	public InputStream getInputStream(URL url) {
		try {
			Log.d("At getInputStream", "ReadingStream");
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

}
