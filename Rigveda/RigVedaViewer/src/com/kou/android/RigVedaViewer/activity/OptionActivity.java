package com.kou.android.RigVedaViewer.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.Logger;

/**
 * SplashActivity
 * 
 * */

@SuppressWarnings("deprecation")
public class OptionActivity extends Activity implements OnCheckedChangeListener, OnClickListener {
	private final String TAG = OptionActivity.class.getSimpleName();

	private CheckBox cbAlias;
	private CheckBox cbModifyYouTubeWidth;
	private CheckBox cbShowPrev;
	private CheckBox cbShowNext;
	private CheckBox cbShowRandom;
	private CheckBox cbShowFootNote;
	private CheckBox cbShowMenuLeft;
	private CheckBox cbShowMenuRight;

	private View btnAbout;
	private View tvAbout;

	/**
	 * Initialize Activity.
	 * */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_activity);

		cbAlias = (CheckBox) findViewById(R.id.cbAlias);
		cbModifyYouTubeWidth = (CheckBox) findViewById(R.id.cbModifyYouTubeWidth);
		cbShowPrev = (CheckBox) findViewById(R.id.cbShowPrev);
		cbShowNext = (CheckBox) findViewById(R.id.cbShowNext);
		cbShowRandom = (CheckBox) findViewById(R.id.cbShowRandom);
		cbShowFootNote = (CheckBox) findViewById(R.id.cbShowFootNote);
		cbShowMenuLeft = (CheckBox) findViewById(R.id.cbShowMenuLeft);
		cbShowMenuRight = (CheckBox) findViewById(R.id.cbShowMenuRight);

		btnAbout = findViewById(R.id.btnAbout);
		btnAbout.setOnClickListener(this);

		tvAbout = findViewById(R.id.tvAbout);
		tvAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAbout:

			if (tvAbout.getVisibility() == View.GONE) {
				tvAbout.setVisibility(View.VISIBLE);
			} else {
				tvAbout.setVisibility(View.GONE);
			}

			break;

		case R.id.tvAbout:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(getString(R.string.http_stanleykou_tistory_com_));
			Toast.makeText(this, R.string.option_clipboard_copied, Toast.LENGTH_SHORT).show();
			break;
		}
	}

	protected void onResume() {
		super.onResume();

		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

		boolean valuecbAlias = pref.getBoolean("cbAlias", true);
		cbAlias.setChecked(valuecbAlias);
		cbAlias.setOnCheckedChangeListener(this);

		boolean valuecbMobileImageHide = pref.getBoolean("cbModifyYouTubeWidth", true);
		cbModifyYouTubeWidth.setChecked(valuecbMobileImageHide);
		cbModifyYouTubeWidth.setOnCheckedChangeListener(this);

		boolean valuecbShowPrev = pref.getBoolean("cbShowPrev", false);
		cbShowPrev.setChecked(valuecbShowPrev);
		cbShowPrev.setOnCheckedChangeListener(this);

		boolean valuecbShowNext = pref.getBoolean("cbShowNext", false);
		cbShowNext.setChecked(valuecbShowNext);
		cbShowNext.setOnCheckedChangeListener(this);

		boolean valuecbShowRandom = pref.getBoolean("cbShowRandom", true);
		cbShowRandom.setChecked(valuecbShowRandom);
		cbShowRandom.setOnCheckedChangeListener(this);

		boolean valuecbShowFootNote = pref.getBoolean("cbShowFootNote", true);
		cbShowFootNote.setChecked(valuecbShowFootNote);
		cbShowFootNote.setOnCheckedChangeListener(this);

		boolean valuecbShowMenuLeft = pref.getBoolean("cbShowMenuLeft", true);
		cbShowMenuLeft.setChecked(valuecbShowMenuLeft);
		cbShowMenuLeft.setOnCheckedChangeListener(this);

		boolean valuecbShowMenuRight = pref.getBoolean("cbShowMenuRight", false);
		cbShowMenuRight.setChecked(valuecbShowMenuRight);
		cbShowMenuRight.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean("cbAlias", cbAlias.isChecked());
		editor.putBoolean("cbMobileImageHide", cbModifyYouTubeWidth.isChecked());
		editor.putBoolean("cbShowPrev", cbShowPrev.isChecked());
		editor.putBoolean("cbShowNext", cbShowNext.isChecked());
		editor.putBoolean("cbShowRandom", cbShowRandom.isChecked());
		editor.putBoolean("cbShowFootNote", cbShowFootNote.isChecked());
		editor.putBoolean("cbShowMenuLeft", cbShowMenuLeft.isChecked());
		editor.putBoolean("cbShowMenuRight", cbShowMenuRight.isChecked());

		editor.commit();
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {

		case R.id.cbAlias: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbAlias", cbAlias.isChecked());
			editor.commit();
		}
			break;

		case R.id.cbModifyYouTubeWidth: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbModifyYouTubeWidth", cbModifyYouTubeWidth.isChecked());
			editor.commit();
		}
			break;

		case R.id.cbShowPrev: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbShowPrev", cbShowPrev.isChecked());
			editor.commit();
		}
			break;

		case R.id.cbShowNext: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbShowNext", isChecked);
			editor.commit();
		}
			break;

		case R.id.cbShowRandom: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbShowRandom", isChecked);
			editor.commit();
		}
			break;

		case R.id.cbShowFootNote: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbShowFootNote", isChecked);
			editor.commit();
		}
			break;

		case R.id.cbShowMenuLeft: {
			if (isChecked == true) {
				cbShowMenuRight.setChecked(false);
				SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("cbShowMenuLeft", cbShowMenuLeft.isChecked());
				editor.putBoolean("cbShowMenuRight", cbShowMenuRight.isChecked());
				editor.commit();
			} else {
				cbShowMenuRight.setChecked(true);
				SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("cbShowMenuLeft", cbShowMenuLeft.isChecked());
				editor.putBoolean("cbShowMenuRight", cbShowMenuRight.isChecked());
				editor.commit();
			}

		}
			break;
		case R.id.cbShowMenuRight: {
			if (isChecked == true) {
				cbShowMenuLeft.setChecked(false);
				SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("cbShowMenuLeft", cbShowMenuLeft.isChecked());
				editor.putBoolean("cbShowMenuRight", cbShowMenuRight.isChecked());
				editor.commit();
			} else {
				cbShowMenuLeft.setChecked(true);
				SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("cbShowMenuLeft", cbShowMenuLeft.isChecked());
				editor.putBoolean("cbShowMenuRight", cbShowMenuRight.isChecked());
				editor.commit();
			}

		}
			break;
		}

	}

}
