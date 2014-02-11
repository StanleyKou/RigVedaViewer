package com.kou.android.RigVedaViewer.base;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.Logger;
import com.kou.android.RigVedaViewer.utils.Utils;

/**
 * BaseActivity
 * 
 * */
public class BaseActivity extends SlidingFragmentActivity {
	private final String TAG = BaseActivity.class.getSimpleName();
	private int mTitleRes;

	// protected ListFragment mFrag;

	/**
	 * Create BaseActivity.
	 */
	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
		Logger.d(TAG, "Contructor::BaseActivity()");
	}

	/**
	 * Initialize Activity. Initialize SlideMenu.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		// setBehindContentView(R.layout.menu_frame);
		// if (savedInstanceState == null) {
		// FragmentTransaction t =
		// this.getSupportFragmentManager().beginTransaction();
		// mFrag = new SampleListFragment();
		// t.replace(R.id.menu_frame, mFrag);
		// t.commit();
		// } else {
		// mFrag =
		// (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		// }

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);

		// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindOffset(Utils.getScreenWidthPercent(this, 10)); // 20%

		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
}
