package com.kou.android.RigVedaViewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.base.BaseActivity;
import com.kou.android.RigVedaViewer.fragment.CustomWebViewFragment;
import com.kou.android.RigVedaViewer.fragment.FootNoteMenuFragment;
import com.kou.android.RigVedaViewer.utils.Logger;

/**
 * WebViewFragmentHolderActivity
 * 
 * Core part of this app. Holding WebView fragment and SlideMenu.
 * 
 * 
 * */
public class WebViewFragmentHolderActivity extends BaseActivity {
	private static String TAG = WebViewFragmentHolderActivity.class.getSimpleName();

	private Fragment mContent;
	private Fragment mMenu;

	final int MENU_PREV = 0;
	final int MENU_NEXT = 1;
	final int MENU_OPTN = 2;
	final int MENU_RAND = 3;

	private final int REQUEST_OPTION = 1000;

	private boolean bIsBackKeyPressed = false;
	private final int BACK_AGAIN_TO_EXIT_TIME = 2000; // Millisecond

	public WebViewFragmentHolderActivity() {
		super(R.string.app_name_eng);
		Logger.d(TAG, TAG, "Constructor::CenterFragmentChangeActivity()");
	}

	/**
	 * Initialize Activity.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "onCreate()");

		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new CustomWebViewFragment();
		}

		if (savedInstanceState != null) {
			mMenu = getSupportFragmentManager().getFragment(savedInstanceState, "mMenu");
		}

		if (mMenu == null) {
			mMenu = new FootNoteMenuFragment();
		}

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, mMenu).commit();

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	/**
	 * Override function, to restore screen.
	 */

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Logger.d(TAG, "onSaveInstanceState()");
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
		getSupportFragmentManager().putFragment(outState, "mMenu", mMenu);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Logger.d(TAG, "onRestoreInstanceState()");

		mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

		// It Works! FIXME: 앱 백그라운드 시 메모리 가비지 컬렉션 후 되살아날 때 이렇게 해주어야 우측메뉴가 보임
		mMenu = new FootNoteMenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, mMenu).commit();

		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_PREV, android.view.Menu.NONE, getString(R.string.menu_prev));
		menu.add(0, MENU_NEXT, android.view.Menu.NONE, getString(R.string.menu_next));
		menu.add(0, MENU_OPTN, android.view.Menu.NONE, getString(R.string.menu_optn));
		menu.add(0, MENU_RAND, android.view.Menu.NONE, getString(R.string.menu_rand));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case MENU_PREV:
			((CustomWebViewFragment) mContent).processNaviPrev();
			break;

		case MENU_NEXT:
			((CustomWebViewFragment) mContent).processNaviNext();
			break;

		case MENU_OPTN:
			Intent intent = new Intent(WebViewFragmentHolderActivity.this, OptionActivity.class);
			startActivityForResult(intent, REQUEST_OPTION);
			break;

		case MENU_RAND:
			((CustomWebViewFragment) mContent).processRandomPage();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Override function, to close menu / History back / "Double-back exit"
	 */

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isMenuShowing()) {
				toggle();
				return true;
			} else if (((CustomWebViewFragment) mContent).canGoBack()) {
				((CustomWebViewFragment) mContent).goBack();
				return true;
			} else {
				if (false == bIsBackKeyPressed) { // show toast
					Toast.makeText(this, R.string.backbutton_message, Toast.LENGTH_SHORT).show();
					bIsBackKeyPressed = true;

					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(BACK_AGAIN_TO_EXIT_TIME);
								bIsBackKeyPressed = false;
							} catch (InterruptedException e) {
								Logger.e(Logger.TAG, "", e);
							}
						}

					}).start();
					return false;
				} else {
					return super.onKeyUp(keyCode, event);
				}
			}
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	/**
	 * Wrapper function of loadUrl
	 * */
	public void loadUrl(int urlStringId) {
		((CustomWebViewFragment) mContent).loadUrl(urlStringId);
	}

	/**
	 * Wrapper function of loadUrl
	 * */
	public void loadUrl(String urlString) {
		((CustomWebViewFragment) mContent).loadUrl(urlString);
	}

	/**
	 * Wrapper function of clearHistory
	 * */
	public void clearHistory() {
		((CustomWebViewFragment) mContent).clearHistory();
	}

	public void setFootNote(final String footNote) {
		Logger.d(TAG, "setFootNote()");
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				((FootNoteMenuFragment) mMenu).setFootNote(footNote);
			}
		});

	}

	public void setFootNoteSSB(final SpannableStringBuilder ssb) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((FootNoteMenuFragment) mMenu).setFootNoteSSB(ssb);
			}
		});

	}

	public String getmCurrentUrl() {
		return ((CustomWebViewFragment) mContent).getCurrentURL();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_OPTION:
			((CustomWebViewFragment) mContent).reload();
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
