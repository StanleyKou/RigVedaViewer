package com.kou.android.RigVedaViewer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.activity.OptionActivity;
import com.kou.android.RigVedaViewer.activity.WebViewFragmentHolderActivity;
import com.kou.android.RigVedaViewer.utils.LogWrapper;
import com.kou.android.RigVedaViewer.utils.PreferenceUtils;

/**
 * FavoriteMenuFragment
 * 
 * */
public class FootNoteMenuFragment extends Fragment implements OnClickListener {

	public final String TAG = FootNoteMenuFragment.class.getSimpleName();
	private final int REQUEST_OPTION = 1000;

	private RelativeLayout mainView;
	private TextView tvFootNote;

	private View ivFootNoteClose;
	private View ivFootNoteSettings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogWrapper.d(TAG, "onCreateView()");

		mainView = (RelativeLayout) inflater.inflate(R.layout.fragment_list, null);

		tvFootNote = (TextView) mainView.findViewById(R.id.tvFootNote);

		float footNoteTextSize = tvFootNote.getTextSize();

		ivFootNoteClose = mainView.findViewById(R.id.ivFootNoteClose);
		ivFootNoteClose.setContentDescription(getString(R.string.footnote_close));
		ivFootNoteClose.setOnClickListener(this);

		ivFootNoteSettings = mainView.findViewById(R.id.ivFootNoteSettings);
		ivFootNoteSettings.setOnClickListener(this);

		return mainView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		LogWrapper.d(TAG, "onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	public TextView getFootNote() {
		return tvFootNote;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivFootNoteClose:
			if (((WebViewFragmentHolderActivity) getActivity()).getSlidingMenu().isMenuShowing()) {
				((WebViewFragmentHolderActivity) getActivity()).toggle();
			}
			break;
		case R.id.ivFootNoteSettings:
			if (((WebViewFragmentHolderActivity) getActivity()).getSlidingMenu().isMenuShowing()) {
				((WebViewFragmentHolderActivity) getActivity()).toggle();
			}
			Intent intent = new Intent(getActivity(), OptionActivity.class);
			startActivityForResult(intent, REQUEST_OPTION);
		}

	}

	public void setFootNoteSSB(SpannableStringBuilder ssb) {
		if (tvFootNote != null) {
			setFootNoteTextSize();
			tvFootNote.setText("");
			tvFootNote.setMovementMethod(LinkMovementMethod.getInstance());
			tvFootNote.setText(ssb, BufferType.SPANNABLE);

		}
	}

	public void setFootNote(String footNote) {
		if (tvFootNote != null) {
			setFootNoteTextSize();
			tvFootNote.setText(footNote);
		}

	}

	private void setFootNoteTextSize() {
		if (getActivity() != null) {

			boolean valuecbFontSize = PreferenceUtils.getcbFontSize(getActivity());
			if (true == valuecbFontSize) {

				float fontSize = PreferenceUtils.getfontSize(getActivity()); // 0: Biggest, 1: Bigger, 2: Normal, 3: Smaller, 4:Smallest

				float footNoteTextSize = 22;

				if (fontSize == 0) {
					footNoteTextSize = 26;
				} else if (fontSize == 1) {
					footNoteTextSize = 22;
				} else if (fontSize == 2) {
					footNoteTextSize = 16;
				} else if (fontSize == 3) {
					footNoteTextSize = 12;
				} else if (fontSize == 4) {
					footNoteTextSize = 8;
				}

				tvFootNote.setTextSize(TypedValue.COMPLEX_UNIT_DIP, footNoteTextSize);

			}
		}
	}

}
