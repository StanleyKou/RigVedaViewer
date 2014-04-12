package com.kou.android.RigVedaViewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.Logger;
import com.kou.android.RigVedaViewer.utils.Utils;

/**
 * SplashActivity
 * 
 * */

public class SplashActivity extends Activity {
	private final String TAG = SplashActivity.class.getSimpleName();
	private final int SPLASH_TIME = 800;

	private Handler mHandler = new Handler();
	private Runnable mSplashRunnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent(SplashActivity.this, WebViewFragmentHolderActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
	};

	/**
	 * Initialize Activity.
	 * */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		Utils.installShortcut(this);
		Utils.createDirIfNotExists(this);
		mHandler.postDelayed(mSplashRunnable, SPLASH_TIME);
	}

}
