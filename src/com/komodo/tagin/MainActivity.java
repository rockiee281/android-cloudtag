package com.komodo.tagin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private TagCloudView mTagCloudView;
	private final String TAG = "cloudtag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout container = (LinearLayout) findViewById(R.id.container);

		mTagCloudView = new TagCloudView(this, 720, 720, createTags(), 0, 40); // passing
		mTagCloudView.requestFocus();
		mTagCloudView.setFocusableInTouchMode(true);
		container.addView(mTagCloudView);

	}

	public boolean dispatchTouchEvent(MotionEvent e) {

		boolean result = mTagCloudView.dispatchTouchEvent(e);
		// boolean result = true;
		Log.d(TAG, getTime() + "super dispatching ... result is [" + result
				+ "] action is [" + e.getAction() + "]");
		return result;
	}

	private String getTime() {

		return "[" + System.currentTimeMillis() + "] ";
	}

	public boolean onTouchEvent(MotionEvent e) {
		Log.d(TAG,
				getTime() + "super movition:x=" + e.getX() + ",y=" + e.getY()
						+ ",action is [" + e.getAction() + "]");
		return mTagCloudView.onTouchEvent(e);
	}

	private List<Tag> createTags() {
		// create the list of tags with popularity values and related url
		List<Tag> tempList = new ArrayList<Tag>();

		tempList.add(new Tag("Google", 7, "http://www.google.com")); // 1,4,7,...
																		// assumed
																		// values
																		// for
																		// popularity
		tempList.add(new Tag("Yahoo", 3, "www.yahoo.com"));
		tempList.add(new Tag("CNN", 4, "www.cnn.com"));
		tempList.add(new Tag("MSNBC", 5, "www.msnbc.com"));
		tempList.add(new Tag("CNBC", 5, "www.CNBC.com"));
		tempList.add(new Tag("Facebook", 7, "www.facebook.com"));
		tempList.add(new Tag("Youtube", 3, "www.youtube.com"));
		tempList.add(new Tag("BlogSpot", 5, "www.blogspot.com"));
		tempList.add(new Tag("Bing", 3, "www.bing.com"));
		tempList.add(new Tag("Wikipedia", 8, "www.wikipedia.com"));
		tempList.add(new Tag("Twitter", 5, "www.twitter.com"));
		tempList.add(new Tag("Msn", 1, "www.msn.com"));
		tempList.add(new Tag("Amazon", 3, "www.amazon.com"));
		tempList.add(new Tag("Ebay", 7, "www.ebay.com"));
		tempList.add(new Tag("LinkedIn", 5, "www.linkedin.com"));
		tempList.add(new Tag("Live", 7, "www.live.com"));
		tempList.add(new Tag("Microsoft", 3, "www.microsoft.com"));
		tempList.add(new Tag("Flicker", 1, "www.flicker.com"));
		tempList.add(new Tag("Apple", 5, "www.apple.com"));
		tempList.add(new Tag("Paypal", 5, "www.paypal.com"));
		tempList.add(new Tag("Craigslist", 7, "www.craigslist.com"));
		tempList.add(new Tag("Imdb", 2, "www.imdb.com"));
		tempList.add(new Tag("Ask", 4, "www.ask.com"));
		tempList.add(new Tag("Weibo", 1, "www.weibo.com"));
		tempList.add(new Tag("Tagin!", 8,
				"http://scyp.idrc.ocad.ca/projects/tagin"));
		tempList.add(new Tag("Shiftehfar", 8, "www.shiftehfar.org"));
		tempList.add(new Tag("Soso", 5, "www.google.com"));
		tempList.add(new Tag("XVideos", 3, "www.xvideos.com"));
		tempList.add(new Tag("BBC", 5, "www.bbc.co.uk"));
		return tempList;
	}
}
