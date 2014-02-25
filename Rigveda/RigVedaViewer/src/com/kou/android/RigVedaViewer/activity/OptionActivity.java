package com.kou.android.RigVedaViewer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.Logger;
import com.kou.android.RigVedaViewer.utils.Utils;

import de.devmil.common.ui.color.ColorSelectorDialog;
import de.devmil.common.ui.color.ColorSelectorDialog.OnColorChangedListener;

/**
 * SplashActivity
 * 
 * */

// https://code.google.com/p/devmil-android-color-picker/
public class OptionActivity extends Activity implements OnCheckedChangeListener, OnClickListener {
	private final static String TAG = OptionActivity.class.getSimpleName();

	public final static String textColorPrefKey = "textColor";
	public final static String backgroundColorPrefKey = "backgroundColor";
	public final static String linkColorPrefKey = "linkColor";

	private final int TEXTCOLOR_TYPE1 = 0xFF000000;
	private final int TEXTCOLOR_TYPE2 = 0xFF999999;
	private final int TEXTCOLOR_TYPE3 = 0xFF121255;

	private final int BACKGROUND_TYPE1 = 0xFFFFFFFF;
	private final int BACKGROUND_TYPE2 = 0xFF000000;
	private final int BACKGROUND_TYPE3 = 0xFFBBBBBB;

	private final int LINKCOLOR_TYPE1 = 0xFF002EE2;
	private final int LINKCOLOR_TYPE2 = 0xFFCC6600;
	private final int LINKCOLOR_TYPE3 = 0xFF0072FF;

	private ScrollView svHolder;

	private Button btnClearCache;
	private CheckBox cbAlias;
	private CheckBox cbModifyYouTubeWidth;
	private CheckBox cbTextColor;
	private CheckBox cbShowPrev;
	private CheckBox cbShowNext;
	private CheckBox cbShowRandom;
	private CheckBox cbShowFootNote;
	private CheckBox cbShowMenuLeft;
	private CheckBox cbShowMenuRight;

	private View btnAbout;
	private View tvAbout;

	private View llColor;

	private ColorSelectorDialog textColorDialog;
	private ColorSelectorDialog backgroundColorDialog;
	private ColorSelectorDialog linkColorDialog;

	private OnColorChangedListener textColorListener;
	private OnColorChangedListener backgroundColorListener;
	private OnColorChangedListener linkColorListener;

	private Button btnTextColorPicker;
	private Button btnBackgroundColorPicker;
	private Button btnLinkColorPicker;

	private Button btnType1;
	private Button btnType2;
	private Button btnType3;

	private Button btnType1Link;
	private Button btnType2Link;
	private Button btnType3Link;

	private TextView tvColorSample;
	private TextView tvColorSampleLink;

	private int textColor;
	private int backgroundColor;
	private int linkColor;

	/**
	 * Initialize Activity.
	 * */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_activity);

		btnClearCache = (Button) findViewById(R.id.btnClearCache);
		btnClearCache.setOnClickListener(this);

		svHolder = (ScrollView) findViewById(R.id.svHolder);
		cbAlias = (CheckBox) findViewById(R.id.cbAlias);
		cbModifyYouTubeWidth = (CheckBox) findViewById(R.id.cbModifyYouTubeWidth);
		cbTextColor = (CheckBox) findViewById(R.id.cbTextColor);
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

		llColor = findViewById(R.id.llColor);

		btnType1 = (Button) findViewById(R.id.btnType1);
		btnType1.setOnClickListener(this);

		btnType2 = (Button) findViewById(R.id.btnType2);
		btnType2.setOnClickListener(this);

		btnType3 = (Button) findViewById(R.id.btnType3);
		btnType3.setOnClickListener(this);

		btnType1Link = (Button) findViewById(R.id.btnType1Link);
		btnType1Link.setOnClickListener(this);

		btnType2Link = (Button) findViewById(R.id.btnType2Link);
		btnType2Link.setOnClickListener(this);

		btnType3Link = (Button) findViewById(R.id.btnType3Link);
		btnType3Link.setOnClickListener(this);

		btnTextColorPicker = (Button) findViewById(R.id.btnTextColorPicker);
		btnTextColorPicker.setOnClickListener(this);

		btnBackgroundColorPicker = (Button) findViewById(R.id.btnBackgroundColorPicker);
		btnBackgroundColorPicker.setOnClickListener(this);

		btnLinkColorPicker = (Button) findViewById(R.id.btnLinkColorPicker);
		btnLinkColorPicker.setOnClickListener(this);

		tvColorSample = (TextView) findViewById(R.id.tvColorSample);
		tvColorSampleLink = (TextView) findViewById(R.id.tvColorSampleLink);

		textColorListener = new OnColorChangedListener() {

			@Override
			public void colorChanged(int color) {
				tvColorSample.setTextColor(color);
				textColor = color;

				commitTextBackgroundColor();
			}
		};

		backgroundColorListener = new OnColorChangedListener() {

			@Override
			public void colorChanged(int color) {
				tvColorSample.setBackgroundColor(color);
				tvColorSampleLink.setBackgroundColor(color);
				backgroundColor = color;

				commitTextBackgroundColor();
			}
		};

		linkColorListener = new OnColorChangedListener() {

			@Override
			public void colorChanged(int color) {
				tvColorSampleLink.setTextColor(color);
				linkColor = color;

				commitTextBackgroundColor();
			}
		};

		textColorDialog = new ColorSelectorDialog(this, textColorListener, TEXTCOLOR_TYPE1);
		backgroundColorDialog = new ColorSelectorDialog(this, backgroundColorListener, BACKGROUND_TYPE1);
		linkColorDialog = new ColorSelectorDialog(this, linkColorListener, LINKCOLOR_TYPE1);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnClearCache:
			Toast.makeText(this, R.string.option_clear_cache_complete, Toast.LENGTH_SHORT).show();
			clearCache(this, 0);
			break;

		case R.id.btnAbout:

			if (tvAbout.getVisibility() == View.GONE) {
				tvAbout.setVisibility(View.VISIBLE);
				svHolder.post(new Runnable() {
					@Override
					public void run() {
						svHolder.fullScroll(View.FOCUS_DOWN);
					}
				});
			} else {
				tvAbout.setVisibility(View.GONE);
			}

			break;

		case R.id.tvAbout:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(getString(R.string.http_stanleykou_tistory_com_));
			Toast.makeText(this, R.string.option_clipboard_copied, Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnTextColorPicker:
			textColorDialog.show();
			break;

		case R.id.btnBackgroundColorPicker:
			backgroundColorDialog.show();
			break;

		case R.id.btnLinkColorPicker:
			linkColorDialog.show();
			break;

		case R.id.btnType1:
		case R.id.btnType1Link:
			textColor = TEXTCOLOR_TYPE1;
			backgroundColor = BACKGROUND_TYPE1;
			linkColor = LINKCOLOR_TYPE1;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		case R.id.btnType2:
		case R.id.btnType2Link:
			textColor = TEXTCOLOR_TYPE2;
			backgroundColor = BACKGROUND_TYPE2;
			linkColor = LINKCOLOR_TYPE2;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		case R.id.btnType3:
		case R.id.btnType3Link:
			textColor = TEXTCOLOR_TYPE3;
			backgroundColor = BACKGROUND_TYPE3;
			linkColor = LINKCOLOR_TYPE3;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		}
	}

	private void setSampleTextColor() {
		tvColorSample.setTextColor((int) textColor);
		tvColorSample.setBackgroundColor((int) backgroundColor);
		tvColorSampleLink.setTextColor((int) linkColor);
		tvColorSampleLink.setBackgroundColor((int) backgroundColor);
	}

	private void commitTextBackgroundColor() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(textColorPrefKey, textColor);
		editor.putInt(backgroundColorPrefKey, backgroundColor);
		editor.putInt(linkColorPrefKey, linkColor);

		editor.commit();
	}

	protected void onResume() {
		super.onResume();

		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

		boolean valuecbAlias = pref.getBoolean("cbAlias", true);
		cbAlias.setChecked(valuecbAlias);
		cbAlias.setOnCheckedChangeListener(this);

		boolean valuecbModifyYouTubeWidth = pref.getBoolean("cbModifyYouTubeWidth", true);
		cbModifyYouTubeWidth.setChecked(valuecbModifyYouTubeWidth);
		cbModifyYouTubeWidth.setOnCheckedChangeListener(this);

		boolean valuecbTextColor = pref.getBoolean("cbTextColor", false);
		cbTextColor.setChecked(valuecbTextColor);
		cbTextColor.setOnCheckedChangeListener(this);

		if (cbTextColor.isChecked() == true) {
			llColor.setVisibility(View.VISIBLE);
		} else {
			llColor.setVisibility(View.GONE);
		}

		btnTextColorPicker.setEnabled(cbTextColor.isChecked());
		btnBackgroundColorPicker.setEnabled(cbTextColor.isChecked());
		btnLinkColorPicker.setEnabled(cbTextColor.isChecked());
		btnType1.setEnabled(cbTextColor.isChecked());
		btnType2.setEnabled(cbTextColor.isChecked());
		btnType3.setEnabled(cbTextColor.isChecked());
		btnType1Link.setEnabled(cbTextColor.isChecked());
		btnType2Link.setEnabled(cbTextColor.isChecked());
		btnType3Link.setEnabled(cbTextColor.isChecked());

		textColor = pref.getInt(textColorPrefKey, TEXTCOLOR_TYPE1);
		backgroundColor = pref.getInt(backgroundColorPrefKey, BACKGROUND_TYPE1);
		linkColor = pref.getInt(linkColorPrefKey, LINKCOLOR_TYPE1);

		tvColorSample.setTextColor(textColor);
		tvColorSample.setBackgroundColor(backgroundColor);
		tvColorSampleLink.setTextColor(linkColor);
		tvColorSampleLink.setBackgroundColor(backgroundColor);

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
		case R.id.cbTextColor: {
			SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("cbTextColor", cbTextColor.isChecked());
			editor.commit();

			if (cbTextColor.isChecked() == true) {
				llColor.setVisibility(View.VISIBLE);
			} else {
				llColor.setVisibility(View.GONE);
			}

			btnTextColorPicker.setEnabled(cbTextColor.isChecked());
			btnBackgroundColorPicker.setEnabled(cbTextColor.isChecked());
			btnLinkColorPicker.setEnabled(cbTextColor.isChecked());
			btnType1.setEnabled(cbTextColor.isChecked());
			btnType2.setEnabled(cbTextColor.isChecked());
			btnType3.setEnabled(cbTextColor.isChecked());
			btnType1Link.setEnabled(cbTextColor.isChecked());
			btnType2Link.setEnabled(cbTextColor.isChecked());
			btnType3Link.setEnabled(cbTextColor.isChecked());
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

	/*
	 * Delete the files older than numDays days from the application cache 0 means all files.
	 */
	public void clearCache(final Context context, final int numDays) {
		Logger.i(TAG, String.format("Starting cache prune, deleting files older than %d days", numDays));
		int numDeletedFiles = Utils.clearCacheFolder(context.getCacheDir(), numDays);
		Logger.i(TAG, String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
	}

}
