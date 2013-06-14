package com.komodo.tagin;

/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagCloudView extends RelativeLayout {
	RelativeLayout navigation_bar;
	TextView mTextView1;

	private final String TAG = "cloudtag";

	public TagCloudView(Context mContext, AttributeSet attrSet) {
		super(mContext, attrSet);
	}

	public TagCloudView(Context mContext, int width, int height,
			List<Tag> tagList, int offsetX, int offsetY) {
		// text size
		this(mContext, width, height, tagList, 6, 34, 1, offsetX, offsetY); // default
																			// for
																			// min/max
	}

	public TagCloudView(Context mContext, int width, int height,
			List<Tag> tagList, int textSizeMin, int textSizeMax, int scrollSpeed) {
		this(mContext, width, height, tagList, textSizeMin, textSizeMax,
				scrollSpeed, 0, 0);
	}

	public TagCloudView(Context mContext, int width, int height,
			List<Tag> tagList, int textSizeMin, int textSizeMax,
			int scrollSpeed, int offsetX, int offsetY) {

		super(mContext);
		this.mContext = mContext;
		this.textSizeMin = textSizeMin;
		this.textSizeMax = textSizeMax;
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		tspeed = scrollSpeed;

		// set the center of the sphere on center of our screen:
		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(centerX * 0.95f, centerY * 0.95f); // use 95% of
																// screen
		// since we set tag margins from left of screen, we shift the whole tags
		// to left so that
		// it looks more realistic and symmetric relative to center of screen in
		// X direction
		shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));

		// initialize the TagCloud from a list of tags
		// Filter() func. screens tagList and ignores Tags with same text (Case
		// Insensitive)
		mTagCloud = new TagCloud(Filter(tagList), (int) radius, textSizeMin,
				textSizeMax);
		float[] tempColor1 = { 0.9412f, 0.7686f, 0.2f, 1 }; // rgb Alpha
		// {1f,0f,0f,1} red {0.3882f,0.21568f,0.0f,1} orange
		// {0.9412f,0.7686f,0.2f,1} light orange
		float[] tempColor2 = { 1f, 0f, 0f, 1 }; // rgb Alpha
												// {0f,0f,1f,1} blue
												// {0.1294f,0.1294f,0.1294f,1}
												// grey
												// {0.9412f,0.7686f,0.2f,1}
												// light orange
		mTagCloud.setTagColor1(tempColor1);// higher color
		mTagCloud.setTagColor2(tempColor2);// lower color
		mTagCloud.setRadius((int) radius);
		mTagCloud.create(true); // to put each Tag at its correct initial
								// location

		// update the transparency/scale of tags
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		mTextView = new ArrayList<TextView>();
		mParams = new ArrayList<RelativeLayout.LayoutParams>();
		// Now Draw the 3D objects: for all the tags in the TagCloud
		Iterator it = mTagCloud.iterator();
		Tag tempTag;
		int i = 0;

		while (it.hasNext()) {
			tempTag = (Tag) it.next();
			tempTag.setParamNo(i); // store the parameter No. related to this
									// tag

			mTextView.add(new TextView(this.mContext));
			mTextView.get(i).setText(tempTag.getText());

			mParams.add(new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
			mParams.get(i)
					.setMargins(
							(int) (centerX - shiftLeft + offsetX + tempTag
									.getLoc2DX()),
							(int) (centerY + offsetY + tempTag.getLoc2DY()), 0,
							0);
			mTextView.get(i).setLayoutParams(mParams.get(i));

			mTextView.get(i).setSingleLine(true);
			int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(i).setTextColor(mergedColor);
			mTextView.get(i).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			addView(mTextView.get(i));
			mTextView.get(i).setOnClickListener(
					OnTagClickListener(tempTag.getUrl()));
			i++;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void addTag(Tag newTag) {
		mTagCloud.add(newTag);

		int i = mTextView.size();
		newTag.setParamNo(i);

		mTextView.add(new TextView(this.mContext));
		mTextView.get(i).setText(newTag.getText());

		mParams.add(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mParams.get(i).setMargins(
				(int) (centerX - shiftLeft + offsetX + newTag.getLoc2DX()),
				(int) (centerY + offsetY + newTag.getLoc2DY()), 0, 0);
		mTextView.get(i).setLayoutParams(mParams.get(i));

		mTextView.get(i).setSingleLine(true);
		int mergedColor = Color.argb((int) (newTag.getAlpha() * 255),
				(int) (newTag.getColorR() * 255),
				(int) (newTag.getColorG() * 255),
				(int) (newTag.getColorB() * 255));
		mTextView.get(i).setTextColor(mergedColor);
		mTextView.get(i).setTextSize(
				(int) (newTag.getTextSize() * newTag.getScale()));
		addView(mTextView.get(i));
		mTextView.get(i)
				.setOnClickListener(OnTagClickListener(newTag.getUrl()));

		mTextView.get(i).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				float x = e.getX();
				float y = e.getY();
				Log.d(TAG, "tv movition:x=" + x + ",y=" + y + ",action is ["
						+ e.getAction() + "]");
				return false;
			}
		});
	}

	public boolean Replace(Tag newTag, String oldTagText) {
		boolean result = false;
		int j = mTagCloud.Replace(newTag, oldTagText);
		if (j >= 0) { // then oldTagText was found and replaced with newTag data
			Iterator it = mTagCloud.iterator();
			Tag tempTag;
			while (it.hasNext()) {
				tempTag = (Tag) it.next();
				mParams.get(tempTag.getParamNo()).setMargins(
						(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
						(int) (centerY + tempTag.getLoc2DY()), 0, 0);
				mTextView.get(tempTag.getParamNo()).setText(tempTag.getText());
				mTextView.get(tempTag.getParamNo()).setTextSize(
						(int) (tempTag.getTextSize() * tempTag.getScale()));
				int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
						(int) (tempTag.getColorR() * 255),
						(int) (tempTag.getColorG() * 255),
						(int) (tempTag.getColorB() * 255));
				mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
				mTextView.get(tempTag.getParamNo()).bringToFront();
			}
			result = true;
		}
		return result;
	}

	public void reset() {
		mTagCloud.reset();

		Iterator it = mTagCloud.iterator();
		Tag tempTag;
		while (it.hasNext()) {
			tempTag = (Tag) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		Log.d(TAG, "using trackball");
		mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
		mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		Iterator it = mTagCloud.iterator();
		Tag tempTag;
		while (it.hasNext()) {
			tempTag = (Tag) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));
			mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		boolean result = true;
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			Log.d(TAG, getTime() + "child movition:x=" + x + ",y=" + y
					+ ",action is [" + e.getAction() + "]");
			onTouchEvent(e);
		} else {
			oldX = x;
			oldY = y;
		}

		result = super.dispatchTouchEvent(e);
		Log.d(TAG, getTime() + "child dispatching ,action is :" + e.getAction()
				+ " result is :" + result);
		return result;
	}

	private String getTime() {

		return "[" + System.currentTimeMillis() + "] ";
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN: // pressed
			oldX = x;
			oldY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// rotate elements depending on how far the selection point is from
			// center of cloud
			float dx = x - oldX;
			float dy = y - oldY;
			oldX = x;
			oldY = y;
			mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
			mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

			mTagCloud.setAngleX(mAngleX);
			mTagCloud.setAngleY(mAngleY);
			mTagCloud.update();

			Iterator it = mTagCloud.iterator();
			Tag tempTag;
			while (it.hasNext()) {
				tempTag = (Tag) it.next();
				mParams.get(tempTag.getParamNo()).setMargins(
						(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
						(int) (centerY + tempTag.getLoc2DY()), 0, 0);
				mTextView.get(tempTag.getParamNo()).setTextSize(
						(int) (tempTag.getTextSize() * tempTag.getScale()));
				int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
						(int) (tempTag.getColorR() * 255),
						(int) (tempTag.getColorG() * 255),
						(int) (tempTag.getColorB() * 255));
				mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
				mTextView.get(tempTag.getParamNo()).bringToFront();
			}

			break;
		/*
		 * case MotionEvent.ACTION_UP: //now it is clicked!!!! dx = x - centerX;
		 * dy = y - centerY; break;
		 */
		}
		
		Log.d(TAG, getTime() + "child movition2:x=" + x + ",y=" + y
				+ ",action is [" + e.getAction() + "]");
		return true;
		// return super.onTouchEvent(e);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return mGestureDetector.onTouchEvent(event);
	// };

	String urlMaker(String url) {
		if ((url.substring(0, 7).equalsIgnoreCase("http://"))
				|| (url.substring(0, 8).equalsIgnoreCase("https://")))
			return url;
		else
			return "http://" + url;
	}

	// the filter function makes sure that there all elements are having unique
	// Text field:
	List<Tag> Filter(List<Tag> tagList) {
		// current implementation is O(n^2) but since the number of tags are not
		// that many,
		// it is acceptable.
		List<Tag> tempTagList = new ArrayList();
		Iterator itr = tagList.iterator();
		Iterator itrInternal;
		Tag tempTag1, tempTag2;
		// for all elements of TagList
		while (itr.hasNext()) {
			tempTag1 = (Tag) (itr.next());
			boolean found = false;
			// go over all elements of temoTagList
			itrInternal = tempTagList.iterator();
			while (itrInternal.hasNext()) {
				tempTag2 = (Tag) (itrInternal.next());
				if (tempTag2.getText().equalsIgnoreCase(tempTag1.getText())) {
					found = true;
					break;
				}
			}
			if (found == false)
				tempTagList.add(tempTag1);
		}
		return tempTagList;
	}

	// for handling the click on the tags
	// onclick open the tag url in a new window. Back button will bring you back
	// to TagCloud
	View.OnClickListener OnTagClickListener(final String url) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// we now have url from main code
				Uri uri = Uri.parse(urlMaker(url));
				// just open a new intent and set the content to search for the
				// url
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		};
	}

	private final float TOUCH_SCALE_FACTOR = 30.8f;
	private final float TRACKBALL_SCALE_FACTOR = 50;
	private float tspeed;
	private TagCloud mTagCloud;
	private float mAngleX = 0;
	private float mAngleY = 0;
	private float centerX, centerY;
	private float offsetX, offsetY;
	private float oldX, oldY;
	private float radius;
	private Context mContext;
	private int textSizeMin, textSizeMax;
	private List<TextView> mTextView;
	private List<RelativeLayout.LayoutParams> mParams;
	private int shiftLeft;

}
