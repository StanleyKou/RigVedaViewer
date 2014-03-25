package com.kou.android.RigVedaViewer.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.kou.android.RigVedaViewer.utils.GlobalVariables;
import com.kou.android.RigVedaViewer.utils.Logger;

/**
 * FavoriteMenuFragment
 * 
 * */
public class FootNoteMenuFragment extends Fragment implements OnClickListener {

	public final String TAG = FootNoteMenuFragment.class.getSimpleName();

	private RelativeLayout mainView;
	private TextView tvFootNote;

	private View ivFootNoteClose;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.d(TAG, "onCreateView()");

		mainView = (RelativeLayout) inflater.inflate(R.layout.fragment_list, null);

		tvFootNote = (TextView) mainView.findViewById(R.id.tvFootNote);

		GlobalVariables.footNoteTextSize = tvFootNote.getTextSize();

		ivFootNoteClose = mainView.findViewById(R.id.ivFootNoteClose);
		ivFootNoteClose.setContentDescription(getString(R.string.footnote_close));
		ivFootNoteClose.setOnClickListener(this);

		return mainView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Logger.d(TAG, "onSaveInstanceState()");
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
			SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);

			boolean valuecbFontSize = pref.getBoolean("cbFontSize", false);
			if (true == valuecbFontSize) {

				float fontSize = pref.getInt(OptionActivity.fontSizefKey, OptionActivity.DEFAULT_FONT_SIZE_PERCENT);
				float footNoteTextSize = GlobalVariables.footNoteTextSize;

				if (footNoteTextSize == 0) {
					footNoteTextSize = tvFootNote.getTextSize();
				}

				float modifiedFontsize = footNoteTextSize * (fontSize / 100f);
				tvFootNote.setTextSize(TypedValue.COMPLEX_UNIT_PX, modifiedFontsize);
			}
		}
	}

}
