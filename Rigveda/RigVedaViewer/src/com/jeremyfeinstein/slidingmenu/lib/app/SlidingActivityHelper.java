package com.jeremyfeinstein.slidingmenu.lib.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.Logger;

public class SlidingActivityHelper {

	private static final String TAG = SlidingActivityHelper.class.getSimpleName();
	
	private Activity mActivity;

	private SlidingMenu mSlidingMenu;

	private View mViewAbove;

	private View mViewBehind;

	private boolean mBroadcasting = false;

	private boolean mOnPostCreateCalled = false;

	private boolean mEnableSlide = true;

	/**
	 * Instantiates a new SlidingActivityHelper.
	 *
	 * @param activity the associated activity
	 */
	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
		Logger.d(TAG, "SlidingActivityHelper()");
	}

	/**
	 * Sets mSlidingMenu as a newly inflated SlidingMenu. Should be called within the activitiy's onCreate()
	 *
	 * @param savedInstanceState the saved instance state (unused)
	 */
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "onCreate()");
		mSlidingMenu = (SlidingMenu) LayoutInflater.from(mActivity).inflate(R.layout.slidingmenumain, null);
	}

	/**
	 * Further SlidingMenu initialization. Should be called within the activitiy's onPostCreate()
	 *
	 * @param savedInstanceState the saved instance state (unused)
	 */
	public void onPostCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "onPostCreate()");
		if (mViewBehind == null || mViewAbove == null) {
			throw new IllegalStateException("Both setBehindContentView must be called " + "in onCreate in addition to setContentView.");
		}

		mOnPostCreateCalled = true;
		mSlidingMenu.attachToActivity(mActivity, mEnableSlide ? SlidingMenu.SLIDING_WINDOW : SlidingMenu.SLIDING_CONTENT);
		
		final boolean open;
		final boolean secondary;
		if (savedInstanceState != null) {
			open = savedInstanceState.getBoolean("SlidingActivityHelper.open");
			secondary = savedInstanceState.getBoolean("SlidingActivityHelper.secondary");
		} else {
			open = false;
			secondary = false;
		}
		
		new Handler().post(new Runnable() {
			public void run() {
				if (open) {
					if (secondary) {
						mSlidingMenu.showSecondaryMenu(false);
					} else {
						mSlidingMenu.showMenu(false);
					}
				} else {
					mSlidingMenu.showContent(false);					
				}
			}
		});
	}

	/**
	 * Controls whether the ActionBar slides along with the above view when the menu is opened,
	 * or if it stays in place.
	 *
	 * @param slidingActionBarEnabled True if you want the ActionBar to slide along with the SlidingMenu,
	 * false if you want the ActionBar to stay in place
	 */
	public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled) {
		Logger.d(TAG, "setSlidingActionBarEnabled()");
		if (mOnPostCreateCalled) {
			throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
		}
		mEnableSlide = slidingActionBarEnabled;
	}

	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in onCreate(Bundle).
	 * 
	 * @param id the resource id of the desired view
	 * @return The view if found or null otherwise.
	 */
	public View findViewById(int id) {
		Logger.d(TAG, "findViewById()");
		
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null) {
				return v;
			}
		}
		return null;
	}

	/**
	 * Called to retrieve per-instance state from an activity before being killed so that the state can be
	 * restored in onCreate(Bundle) or onRestoreInstanceState(Bundle) (the Bundle populated by this method
	 * will be passed to both). 
	 *
	 * @param outState Bundle in which to place your saved state.
	 */
	public void onSaveInstanceState(Bundle outState) {
		Logger.d(TAG, "onSaveInstanceState()");
		outState.putBoolean("SlidingActivityHelper.open", mSlidingMenu.isMenuShowing());
		outState.putBoolean("SlidingActivityHelper.secondary", mSlidingMenu.isSecondaryMenuShowing());
	}

	/**
	 * Register the above content view.
	 *
	 * @param v the above content view to register
	 * @param params LayoutParams for that view (unused)
	 */
	public void registerAboveContentView(View v, LayoutParams params) {
		Logger.d(TAG, "registerAboveContentView()");
		if (!mBroadcasting) {
			mViewAbove = v;
		}
	}

	/**
	 * Set the activity content to an explicit view. This view is placed directly into the activity's view
	 * hierarchy. It can itself be a complex view hierarchy. When calling this method, the layout parameters
	 * of the specified view are ignored. Both the width and the height of the view are set by default to
	 * MATCH_PARENT. To use your own layout parameters, invoke setContentView(android.view.View,
	 * android.view.ViewGroup.LayoutParams) instead.
	 *
	 * @param v The desired content to display.
	 */
	public void setContentView(View v) {
		Logger.d(TAG, "setContentView()");
		mBroadcasting = true;
		mActivity.setContentView(v);
	}

	/**
	 * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
	 * It can itself be a complex view hierarchy.
	 *
	 * @param view The desired content to display.
	 * @param layoutParams Layout parameters for the view. (unused)
	 */
	public void setBehindContentView(View view, LayoutParams layoutParams) {
		Logger.d(TAG, "setBehindContentView()");
		mViewBehind = view;
		mSlidingMenu.setMenu(mViewBehind);
	}

	/**
	 * Gets the SlidingMenu associated with this activity.
	 *
	 * @return the SlidingMenu associated with this activity.
	 */
	public SlidingMenu getSlidingMenu() {
		Logger.d(TAG, "getSlidingMenu()");
		return mSlidingMenu;
	}

	/**
	 * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
	 */
	public void toggle() {
		Logger.d(TAG, "toggle()");
		mSlidingMenu.toggle();
	}

	/**
	 * Close the SlidingMenu and show the content view.
	 */
	public void showContent() {
		Logger.d(TAG, "showContent()");
		mSlidingMenu.showContent();
	}

	/**
	 * Open the SlidingMenu and show the menu view.
	 */
	public void showMenu() {
		Logger.d(TAG, "showMenu()");
		mSlidingMenu.showMenu();
	}

	/**
	 * Open the SlidingMenu and show the secondary menu view. Will default to the regular menu
	 * if there is only one.
	 */
	public void showSecondaryMenu() {
		Logger.d(TAG, "showSecondaryMenu()");
		mSlidingMenu.showSecondaryMenu();
	}

	/**
	 * On key up.
	 *
	 * @param keyCode the key code
	 * @param event the event
	 * @return true, if successful
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Logger.d(TAG, "onKeyUp(), keyCode(%s)", keyCode);
		
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isMenuShowing()) {
			showContent();
			return true;
		}
		return false;
	}

}
