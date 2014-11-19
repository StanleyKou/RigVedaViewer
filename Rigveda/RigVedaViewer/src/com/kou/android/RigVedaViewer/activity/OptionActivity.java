package com.kou.android.RigVedaViewer.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.utils.LogWrapper;
import com.kou.android.RigVedaViewer.utils.PreferenceUtils;
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

	private Button btnClearCache;
	private CheckBox cbAlias;
	private CheckBox cbModifyYouTubeWidth;
	private CheckBox cbExternalImage;
	private CheckBox cbNightEyeProtect;
	private CheckBox cbTextColor;
	private CheckBox cbFontSize;
	private CheckBox cbShowExit;
	private CheckBox cbShowPrev;
	private CheckBox cbShowNext;
	private CheckBox cbShowRandom;
	private CheckBox cbShowReverseLink;
	private CheckBox cbShowFootNote;
	private CheckBox cbShowMenuLeft;
	private CheckBox cbShowMenuRight;

	private View btnAbout;
	private View tvAbout;

	// Font - Background color
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

	// Font size
	private View llFontSize;
	private int inputFontSize;
	private Button btnTextSizeLARGEST;
	private Button btnTextSizeLARGER;
	private Button btnTextSizeNORMAL;
	private Button btnTextSizeSMALLER;
	private Button btnTextSizeSMALLEST;

	/**
	 * Initialize Activity.
	 * */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogWrapper.d(TAG, TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_activity);

		btnClearCache = (Button) findViewById(R.id.btnClearCache);
		btnClearCache.setOnClickListener(this);

		cbAlias = (CheckBox) findViewById(R.id.cbAlias);
		cbModifyYouTubeWidth = (CheckBox) findViewById(R.id.cbModifyYouTubeWidth);
		cbExternalImage = (CheckBox) findViewById(R.id.cbExternalImage);
		cbNightEyeProtect = (CheckBox) findViewById(R.id.cbNightEyeProtect);
		cbTextColor = (CheckBox) findViewById(R.id.cbTextColor);
		cbFontSize = (CheckBox) findViewById(R.id.cbFontSize);
		cbShowExit = (CheckBox) findViewById(R.id.cbShowExit);
		cbShowPrev = (CheckBox) findViewById(R.id.cbShowPrev);
		cbShowNext = (CheckBox) findViewById(R.id.cbShowNext);
		cbShowRandom = (CheckBox) findViewById(R.id.cbShowRandom);
		cbShowReverseLink = (CheckBox) findViewById(R.id.cbShowReverseLink);
		cbShowFootNote = (CheckBox) findViewById(R.id.cbShowFootNote);
		cbShowMenuLeft = (CheckBox) findViewById(R.id.cbShowMenuLeft);
		cbShowMenuRight = (CheckBox) findViewById(R.id.cbShowMenuRight);

		btnAbout = findViewById(R.id.btnAbout);
		btnAbout.setOnClickListener(this);

		tvAbout = findViewById(R.id.tvAbout);
		tvAbout.setOnClickListener(this);

		// Font - background color
		llColor = findViewById(R.id.llColor);
		llFontSize = findViewById(R.id.llFontSize);
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

		textColorDialog = new ColorSelectorDialog(this, textColorListener, PreferenceUtils.TEXTCOLOR_TYPE1);
		backgroundColorDialog = new ColorSelectorDialog(this, backgroundColorListener, PreferenceUtils.BACKGROUND_TYPE1);
		linkColorDialog = new ColorSelectorDialog(this, linkColorListener, PreferenceUtils.LINKCOLOR_TYPE1);

		// Font size
		btnTextSizeLARGEST = (Button) findViewById(R.id.btnTextSizeLARGEST);
		btnTextSizeLARGER = (Button) findViewById(R.id.btnTextSizeLARGER);
		btnTextSizeNORMAL = (Button) findViewById(R.id.btnTextSizeNORMAL);
		btnTextSizeSMALLER = (Button) findViewById(R.id.btnTextSizeSMALLER);
		btnTextSizeSMALLEST = (Button) findViewById(R.id.btnTextSizeSMALLEST);

		btnTextSizeLARGEST.setOnClickListener(this);
		btnTextSizeLARGER.setOnClickListener(this);
		btnTextSizeNORMAL.setOnClickListener(this);
		btnTextSizeSMALLER.setOnClickListener(this);
		btnTextSizeSMALLEST.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnClearCache:
			Toast.makeText(this, R.string.option_clear_cache_complete, Toast.LENGTH_SHORT).show();
			clearCache(this, 0);
			break;

		case R.id.btnAbout:
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
			textColor = PreferenceUtils.TEXTCOLOR_TYPE1;
			backgroundColor = PreferenceUtils.BACKGROUND_TYPE1;
			linkColor = PreferenceUtils.LINKCOLOR_TYPE1;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		case R.id.btnType2:
		case R.id.btnType2Link:
			textColor = PreferenceUtils.TEXTCOLOR_TYPE2;
			backgroundColor = PreferenceUtils.BACKGROUND_TYPE2;
			linkColor = PreferenceUtils.LINKCOLOR_TYPE2;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		case R.id.btnType3:
		case R.id.btnType3Link:
			textColor = PreferenceUtils.TEXTCOLOR_TYPE3;
			backgroundColor = PreferenceUtils.BACKGROUND_TYPE3;
			linkColor = PreferenceUtils.LINKCOLOR_TYPE3;
			setSampleTextColor();
			commitTextBackgroundColor();
			break;

		case R.id.btnTextSizeLARGEST:
			inputFontSize = 0;
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);
			setFontSizeButtons(inputFontSize);
			break;
		case R.id.btnTextSizeLARGER:
			inputFontSize = 1;
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);
			setFontSizeButtons(inputFontSize);
			break;
		case R.id.btnTextSizeNORMAL:
			inputFontSize = 2;
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);
			setFontSizeButtons(inputFontSize);
			break;
		case R.id.btnTextSizeSMALLER:
			inputFontSize = 3;
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);
			setFontSizeButtons(inputFontSize);
			break;
		case R.id.btnTextSizeSMALLEST:
			inputFontSize = 4;
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);
			setFontSizeButtons(inputFontSize);
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
		PreferenceUtils.settextColor(getApplicationContext(), textColor);
		PreferenceUtils.setbackgroundColor(getApplicationContext(), backgroundColor);
		PreferenceUtils.setlinkColorPrefKey(getApplicationContext(), linkColor);
	}

	protected void onResume() {
		super.onResume();

		boolean valuecbAlias = PreferenceUtils.getcbAlias(getApplicationContext());
		cbAlias.setChecked(valuecbAlias);
		cbAlias.setOnCheckedChangeListener(this);

		boolean valuecbModifyYouTubeWidth = PreferenceUtils.getcbModifyYouTubeWidth(getApplicationContext());
		cbModifyYouTubeWidth.setChecked(valuecbModifyYouTubeWidth);
		cbModifyYouTubeWidth.setOnCheckedChangeListener(this);

		boolean valuecbExternalImage = PreferenceUtils.getcbExternalImage(getApplicationContext());
		cbExternalImage.setChecked(valuecbExternalImage);
		cbExternalImage.setOnCheckedChangeListener(this);

		boolean valuecbNightEyeProtect = PreferenceUtils.getcbNightEyeProtect(getApplicationContext());
		cbNightEyeProtect.setChecked(valuecbNightEyeProtect);
		cbNightEyeProtect.setOnCheckedChangeListener(this);

		boolean valuecbTextColor = PreferenceUtils.getcbTextColor(getApplicationContext());
		cbTextColor.setChecked(valuecbTextColor);
		cbTextColor.setOnCheckedChangeListener(this);

		if (true == cbTextColor.isChecked()) {
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

		textColor = PreferenceUtils.gettextColor(getApplicationContext());
		backgroundColor = PreferenceUtils.getbackgroundColor(getApplicationContext());
		linkColor = PreferenceUtils.getlinkColorPrefKey(getApplicationContext());

		tvColorSample.setTextColor(textColor);
		tvColorSample.setBackgroundColor(backgroundColor);
		tvColorSampleLink.setTextColor(linkColor);
		tvColorSampleLink.setBackgroundColor(backgroundColor);

		boolean valuecbFontSize = PreferenceUtils.getcbFontSize(getApplicationContext());
		cbFontSize.setChecked(valuecbFontSize);
		cbFontSize.setOnCheckedChangeListener(this);

		if (true == cbFontSize.isChecked()) {
			llFontSize.setVisibility(View.VISIBLE);
		} else {
			llFontSize.setVisibility(View.GONE);
		}

		int fontSize = PreferenceUtils.getfontSize(getApplicationContext());
		if (fontSize > 4) {
			fontSize = 2;
		}
		setFontSizeButtons(fontSize);

		boolean valuecbShowExit = PreferenceUtils.getcbShowExit(getApplicationContext());
		cbShowExit.setChecked(valuecbShowExit);
		cbShowExit.setOnCheckedChangeListener(this);

		boolean valuecbShowPrev = PreferenceUtils.getcbShowPrev(getApplicationContext());
		cbShowPrev.setChecked(valuecbShowPrev);
		cbShowPrev.setOnCheckedChangeListener(this);

		boolean valuecbShowNext = PreferenceUtils.getcbShowNext(getApplicationContext());
		cbShowNext.setChecked(valuecbShowNext);
		cbShowNext.setOnCheckedChangeListener(this);

		boolean valuecbShowRandom = PreferenceUtils.getcbShowRandom(getApplicationContext());
		cbShowRandom.setChecked(valuecbShowRandom);
		cbShowRandom.setOnCheckedChangeListener(this);

		boolean valuecbShowReverseLink = PreferenceUtils.getcbShowReverseLink(getApplicationContext());
		cbShowReverseLink.setChecked(valuecbShowReverseLink);
		cbShowReverseLink.setOnCheckedChangeListener(this);

		boolean valuecbShowFootNote = PreferenceUtils.getcbShowFootNote(getApplicationContext());
		cbShowFootNote.setChecked(valuecbShowFootNote);
		cbShowFootNote.setOnCheckedChangeListener(this);

		boolean valuecbShowMenuLeft = PreferenceUtils.getcbShowMenuLeft(getApplicationContext());
		cbShowMenuLeft.setChecked(valuecbShowMenuLeft);
		cbShowMenuLeft.setOnCheckedChangeListener(this);

		boolean valuecbShowMenuRight = !PreferenceUtils.getcbShowMenuLeft(getApplicationContext());
		cbShowMenuRight.setChecked(valuecbShowMenuRight);
		cbShowMenuRight.setOnCheckedChangeListener(this);

	}

	private void setFontSizeButtons(int mode) {// 0:LARGER , 1:LARGEST , 2:NORMAL, 3:SMALLER, 4:SMALLEST
		switch (mode) {
		case 0:
			btnTextSizeLARGEST.setBackgroundColor(Color.GREEN);
			btnTextSizeLARGER.setBackgroundColor(Color.GRAY);
			btnTextSizeNORMAL.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLER.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLEST.setBackgroundColor(Color.GRAY);
			break;
		case 1:
			btnTextSizeLARGEST.setBackgroundColor(Color.GRAY);
			btnTextSizeLARGER.setBackgroundColor(Color.GREEN);
			btnTextSizeNORMAL.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLER.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLEST.setBackgroundColor(Color.GRAY);
			break;
		case 2:
			btnTextSizeLARGEST.setBackgroundColor(Color.GRAY);
			btnTextSizeLARGER.setBackgroundColor(Color.GRAY);
			btnTextSizeNORMAL.setBackgroundColor(Color.GREEN);
			btnTextSizeSMALLER.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLEST.setBackgroundColor(Color.GRAY);
			break;
		case 3:
			btnTextSizeLARGEST.setBackgroundColor(Color.GRAY);
			btnTextSizeLARGER.setBackgroundColor(Color.GRAY);
			btnTextSizeNORMAL.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLER.setBackgroundColor(Color.GREEN);
			btnTextSizeSMALLEST.setBackgroundColor(Color.GRAY);
			break;
		case 4:
			btnTextSizeLARGEST.setBackgroundColor(Color.GRAY);
			btnTextSizeLARGER.setBackgroundColor(Color.GRAY);
			btnTextSizeNORMAL.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLER.setBackgroundColor(Color.GRAY);
			btnTextSizeSMALLEST.setBackgroundColor(Color.GREEN);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		PreferenceUtils.setcbAlias(getApplicationContext(), cbAlias.isChecked());
		PreferenceUtils.setcbModifyYouTubeWidth(getApplicationContext(), cbModifyYouTubeWidth.isChecked());
		PreferenceUtils.setcbExternalImage(getApplicationContext(), cbExternalImage.isChecked());
		PreferenceUtils.setcbNightEyeProtect(getApplicationContext(), cbNightEyeProtect.isChecked());
		PreferenceUtils.setcbShowExit(getApplicationContext(), cbShowExit.isChecked());
		PreferenceUtils.setcbShowPrev(getApplicationContext(), cbShowPrev.isChecked());
		PreferenceUtils.setcbShowNext(getApplicationContext(), cbShowNext.isChecked());
		PreferenceUtils.setcbShowRandom(getApplicationContext(), cbShowRandom.isChecked());
		PreferenceUtils.setcbShowReverseLink(getApplicationContext(), cbShowReverseLink.isChecked());
		PreferenceUtils.setcbShowFootNote(getApplicationContext(), cbShowFootNote.isChecked());
		PreferenceUtils.setcbShowMenuLeft(getApplicationContext(), cbShowMenuLeft.isChecked());
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {

		case R.id.cbAlias:
			PreferenceUtils.setcbAlias(getApplicationContext(), cbAlias.isChecked());
			break;

		case R.id.cbModifyYouTubeWidth:
			PreferenceUtils.setcbModifyYouTubeWidth(getApplicationContext(), cbModifyYouTubeWidth.isChecked());
			break;

		case R.id.cbExternalImage:
			PreferenceUtils.setcbExternalImage(getApplicationContext(), cbExternalImage.isChecked());
			break;

		case R.id.cbNightEyeProtect:
			PreferenceUtils.setcbNightEyeProtect(getApplicationContext(), cbNightEyeProtect.isChecked());

		case R.id.cbTextColor: {
			PreferenceUtils.setcbTextColor(getApplicationContext(), cbTextColor.isChecked());

			if (true == cbTextColor.isChecked()) {
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

		case R.id.cbFontSize: {

			PreferenceUtils.setcbFontSize(getApplicationContext(), cbFontSize.isChecked());
			PreferenceUtils.setfontSize(getApplicationContext(), inputFontSize);

			if (true == cbFontSize.isChecked()) {
				llFontSize.setVisibility(View.VISIBLE);
			} else {
				llFontSize.setVisibility(View.GONE);
			}
		}
			break;

		case R.id.cbShowExit:
			PreferenceUtils.setcbShowExit(getApplicationContext(), cbShowExit.isChecked());
			break;

		case R.id.cbShowPrev:
			PreferenceUtils.setcbShowPrev(getApplicationContext(), cbShowPrev.isChecked());
			break;

		case R.id.cbShowNext:
			PreferenceUtils.setcbShowNext(getApplicationContext(), cbShowNext.isChecked());
			break;

		case R.id.cbShowRandom:
			PreferenceUtils.setcbShowRandom(getApplicationContext(), cbShowRandom.isChecked());
			break;

		case R.id.cbShowReverseLink:
			PreferenceUtils.setcbShowReverseLink(getApplicationContext(), cbShowReverseLink.isChecked());
			break;

		case R.id.cbShowFootNote:
			PreferenceUtils.setcbShowFootNote(getApplicationContext(), cbShowFootNote.isChecked());
			break;

		case R.id.cbShowMenuLeft:
			if (true == isChecked) {
				PreferenceUtils.setcbShowMenuLeft(getApplicationContext(), true);
				cbShowMenuRight.setChecked(false);
			} else {
				PreferenceUtils.setcbShowMenuLeft(getApplicationContext(), false);
				cbShowMenuRight.setChecked(true);
			}

			break;
		case R.id.cbShowMenuRight:
			if (true == isChecked) {
				PreferenceUtils.setcbShowMenuLeft(getApplicationContext(), false);
				cbShowMenuLeft.setChecked(false);
			} else {
				PreferenceUtils.setcbShowMenuLeft(getApplicationContext(), true);
				cbShowMenuLeft.setChecked(true);
			}

			break;
		}

	}

	/*
	 * Delete the files older than numDays days from the application cache 0 means all files.
	 */
	public void clearCache(final Context context, final int numDays) {
		LogWrapper.i(TAG, String.format("Starting cache prune, deleting files older than %d days", numDays));
		int numDeletedFiles = Utils.clearCacheFolder(context.getCacheDir(), numDays);
		LogWrapper.i(TAG, String.format("Cache pruning completed, %d files deleted", numDeletedFiles));

	}
}
