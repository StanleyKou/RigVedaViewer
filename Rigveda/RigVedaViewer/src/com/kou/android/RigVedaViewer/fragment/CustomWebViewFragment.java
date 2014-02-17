package com.kou.android.RigVedaViewer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.activity.WebViewFragmentHolderActivity;
import com.kou.android.RigVedaViewer.base.BaseWebView;
import com.kou.android.RigVedaViewer.utils.DownloadFilesTask;
import com.kou.android.RigVedaViewer.utils.Logger;
import com.kou.android.RigVedaViewer.utils.Utils;

;

/**
 * CustomWebViewFragment.
 * 
 * Core part of this app. Holding WebView,
 * 
 * */
@SuppressWarnings("deprecation")
public class CustomWebViewFragment extends Fragment implements OnClickListener, OnTouchListener {
	private final String TAG = CustomWebViewFragment.class.getSimpleName();

	private View mMainView;
	private BaseWebView mWebview;

	private Handler handler = new Handler(Looper.getMainLooper());

	private ProgressBar mProgressBar;
	private AlertDialog mNetworkErrorDialog;

	private LinearLayout llMenu;
	private boolean isMenuLeft = true;

	private Button btnRandom;
	private Button btnFootNote;

	private ImageView ivToRightNotSelected;
	private ImageView ivToRightSelected;

	private ImageView ivToLeftNotSelected;
	private ImageView ivToLeftSelected;

	private ImageView ivNavPrev;
	private ImageView ivNavNext;

	private String mCurrentUrl = "";

	public String getmCurrentUrl() {
		return mCurrentUrl;
	}

	private SpannableStringBuilder ssb = new SpannableStringBuilder();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = (View) inflater.inflate(R.layout.fragment_webview, container, false);
		initWebView();

		btnRandom = (Button) mMainView.findViewById(R.id.btnRandom);
		btnFootNote = (Button) mMainView.findViewById(R.id.btnFootNote);

		btnRandom.setOnClickListener(this);
		btnRandom.setOnTouchListener(this);
		btnFootNote.setOnClickListener(this);
		btnFootNote.setOnTouchListener(this);

		mProgressBar = (ProgressBar) mMainView.findViewById(R.id.loadingBar);
		mProgressBar.setMax(100);

		llMenu = (LinearLayout) mMainView.findViewById(R.id.llMenu);
		llMenu.setOnTouchListener(this);
		llMenu.setOnClickListener(this);

		ivToRightNotSelected = (ImageView) mMainView.findViewById(R.id.ivToRightNotSelected);
		ivToRightNotSelected.setVisibility(View.GONE);

		ivToRightSelected = (ImageView) mMainView.findViewById(R.id.ivToRightSelected);
		ivToRightSelected.setVisibility(View.GONE);

		ivToLeftNotSelected = (ImageView) mMainView.findViewById(R.id.ivToLeftNotSelected);
		ivToLeftNotSelected.setVisibility(View.GONE);

		ivToLeftSelected = (ImageView) mMainView.findViewById(R.id.ivToLeftSelected);
		ivToLeftSelected.setVisibility(View.GONE);

		ivNavPrev = (ImageView) mMainView.findViewById(R.id.ivNavPrev);
		ivNavPrev.setOnClickListener(this);

		ivNavNext = (ImageView) mMainView.findViewById(R.id.ivNavNext);
		ivNavNext.setOnClickListener(this);

		return mMainView;
	}

	@Override
	public void onResume() {
		CookieSyncManager.getInstance().sync();

		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		boolean valuecbShowPrev = pref.getBoolean("cbShowPrev", false);
		boolean valuecbShowNext = pref.getBoolean("cbShowNext", false);
		boolean valuecbShowRandom = pref.getBoolean("cbShowRandom", true);
		boolean valuecbShowFootNote = pref.getBoolean("cbShowFootNote", true);
		boolean valuecbShowMenuLeft = pref.getBoolean("cbShowMenuLeft", true);

		ivNavPrev.setVisibility(valuecbShowPrev == true ? View.VISIBLE : View.GONE);
		ivNavNext.setVisibility(valuecbShowNext == true ? View.VISIBLE : View.GONE);
		btnRandom.setVisibility(valuecbShowRandom == true ? View.VISIBLE : View.GONE);
		btnFootNote.setVisibility(valuecbShowFootNote == true ? View.VISIBLE : View.GONE);

		if (valuecbShowMenuLeft == true) {
			showMenuLeft();
		} else {
			showMenuRight();
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		mWebview.stopLoading();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// mWebview.destroy();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRandom:
			loadUrl(R.string.url_random_page);
			break;
		case R.id.btnFootNote:
			openMenu();
			break;
		case R.id.ivNavPrev:
			processNaviPrev();
			break;
		case R.id.ivNavNext:
			processNaviNext();
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (isMenuLeft == true) {
			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				ivToRightNotSelected.setVisibility(View.VISIBLE);
				break;
			case MotionEvent.ACTION_UP:
				ivToRightNotSelected.setVisibility(View.GONE);
				ivToRightSelected.setVisibility(View.GONE);

				ivToLeftNotSelected.setVisibility(View.GONE);
				ivToLeftSelected.setVisibility(View.GONE);

				if (event.getX() > 100) {
					showMenuRight();
				}
				break;
			case MotionEvent.ACTION_MOVE:

				if (event.getX() > 100) {
					ivToRightNotSelected.setVisibility(View.INVISIBLE);
					ivToRightSelected.setVisibility(View.VISIBLE);
				} else {
					ivToRightNotSelected.setVisibility(View.VISIBLE);
					ivToRightSelected.setVisibility(View.GONE);
				}

				break;
			}
		} else {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				ivToLeftNotSelected.setVisibility(View.VISIBLE);
				((WebViewFragmentHolderActivity) getActivity()).getSlidingMenu().setSlidingEnabled(false);

				break;
			case MotionEvent.ACTION_UP:
				ivToRightNotSelected.setVisibility(View.GONE);
				ivToRightSelected.setVisibility(View.GONE);

				ivToLeftNotSelected.setVisibility(View.GONE);
				ivToLeftSelected.setVisibility(View.GONE);

				((WebViewFragmentHolderActivity) getActivity()).getSlidingMenu().setSlidingEnabled(true);

				if (event.getX() < -50) {
					showMenuLeft();
				}

				break;
			case MotionEvent.ACTION_MOVE:

				if (event.getX() < -50) {
					ivToLeftNotSelected.setVisibility(View.INVISIBLE);
					ivToLeftSelected.setVisibility(View.VISIBLE);
				} else {
					ivToLeftNotSelected.setVisibility(View.VISIBLE);
					ivToLeftSelected.setVisibility(View.GONE);
				}

				break;
			}

		}

		return false;
	}

	private void showMenuLeft() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llMenu.setLayoutParams(lp);
		isMenuLeft = true;

		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("cbShowMenuLeft", true);
		editor.putBoolean("cbShowMenuRight", false);
		editor.commit();
	}

	private void showMenuRight() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llMenu.setLayoutParams(lp);
		isMenuLeft = false;

		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("cbShowMenuLeft", false);
		editor.putBoolean("cbShowMenuRight", true);
		editor.commit();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		mWebview = (BaseWebView) mMainView.findViewById(R.id.webviewMain);
		// mWebview.getSettings().setBuiltInZoomControls(true);
		mWebview.getSettings().setSupportZoom(true);
		mWebview.getSettings().setUseWideViewPort(true);
		mWebview.getSettings().setAllowFileAccess(true);

		mWebview.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				HitTestResult result = mWebview.getHitTestResult();
				Logger.d(TAG, "LongClick: " + result.getExtra());

				if (result.getType() == HitTestResult.IMAGE_TYPE || result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
					AlertDialog dialog = getImageProcessDialog(result.getExtra());
					dialog.show();
				} else if (result.getType() == HitTestResult.ANCHOR_TYPE || result.getType() == HitTestResult.SRC_ANCHOR_TYPE) {
					AlertDialog dialog = getLinkProcessDialog(result.getExtra());
					dialog.show();
				}

				return false;
			}

		});

		mWebview.getSettings().setJavaScriptEnabled(true); // for redirect
		mWebview.addJavascriptInterface(new CustomJavaScriptInterface(), "HTMLOUT");

		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(0);
				super.onPageStarted(view, url, favicon);

				handler.removeCallbacks(modifyWebPageRunnable);

				ssb.clear();

				// getResource를 읽어올 수 없는 상태이면 종료. isAdded를 쓰지 않는 이유는, Activity만 있으면 되기 때문.
				if (getActivity() == null) {
					Logger.d(TAG, "getActivity is null");
					return;
				} else {
					Logger.d(TAG, "getActivity is good");
				}

				String footNote = getString(R.string.loading_footnote);
				((WebViewFragmentHolderActivity) getActivity()).setFootNote(footNote);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (mCurrentUrl != null && url != null && url.equals(mCurrentUrl)) {
					mWebview.goBack();
					return true;
				}

				view.stopLoading();

				if (url.contains("file:///android_asset/webkit/")) {
					// do nothing. security origin error.
				} else if (url.contains(CustomWebViewFragment.this.getString(R.string.url_home_page)) == false) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(i);
				} else {
					view.loadUrl(url);
				}

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Logger.d(TAG, "onPageFinished");
				mCurrentUrl = url;

				mProgressBar.setVisibility(View.GONE);

				// getResource를 읽어올 수 없는 상태이면 종료. isAdded를 쓰지 않는 이유는, Activity만 있으면 되기 때문.
				if (getActivity() == null) {
					Logger.d(TAG, "getActivity is null");
					return;
				} else {
					Logger.d(TAG, "getActivity is good");
				}

				String homeURL = getHomeURL();
				if (url.equalsIgnoreCase(homeURL) == true && mWebview.canGoForward() == false) {
					clearHistory();
				}

				modifyWebPageAfterFinished(url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Logger.d(TAG, "error code:" + errorCode);

				mNetworkErrorDialog = getNetworkErrorDialog();
				if (mNetworkErrorDialog != null && !mNetworkErrorDialog.isShowing()) {
					mNetworkErrorDialog.show();
				}
			}
		});

		mWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

				if (newProgress > 70) {
					mProgressBar.setVisibility(View.GONE);
				} else {
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setProgress(newProgress);
				}
			}
		});

		loadUrlHomePage();
	}

	private AlertDialog getImageProcessDialog(final String tabbedUrl) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle(getString(R.string.dialog_image_title));
		String[] itemList = { getString(R.string.dialog_image_save), getString(R.string.dialog_image_url_copy) };

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, itemList);
		dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					downloadImageFromUrl(mCurrentUrl, tabbedUrl);

					break;
				case 1:
					ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
					String downloadablelUrl = Utils.getDownloadableRigVedaURL(getActivity(), mCurrentUrl, tabbedUrl);
					clipboard.setText(downloadablelUrl);
					Toast.makeText(getActivity(), getString(R.string.dialog_image_url_copy_colon) + downloadablelUrl, Toast.LENGTH_SHORT).show();
					break;

				}
			}

		});

		return dialogBuilder.create();
	}

	private AlertDialog getLinkProcessDialog(final String tabbedUrl) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle(getString(R.string.dialog_link_title));
		String[] itemList = { getString(R.string.dialog_link_new_page), getString(R.string.dialog_link_copy) };

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, itemList);
		dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(tabbedUrl));
					startActivity(i);
					break;
				case 1:
					ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
					String downloadablelUrl = Utils.getDownloadableRigVedaURL(getActivity(), mCurrentUrl, tabbedUrl);
					clipboard.setText(downloadablelUrl);
					Toast.makeText(getActivity(), getString(R.string.dialog_image_url_copy_colon) + downloadablelUrl, Toast.LENGTH_SHORT).show();
					break;

				}
			}

		});

		return dialogBuilder.create();
	}

	private void modifyWebPageAfterFinished(final String url) {
		// 현재 화면이 랜덤 페이지가 아닌 경우에만 동작
		if (false == url.equalsIgnoreCase(getString(R.string.url_random_page))) {
			handler.postDelayed(modifyWebPageRunnable, 100);
			// 타이밍 이슈때문에 딜레이. 리그베다 원래 페이지의 구글애드가 읽혀오는 시간, 이미지 로딩이 완료되는 시간이 필요함. 그런데 정확히 완료되는 시점을 알아내는 방법을 확인 못해 일단 딜레이를 강제로 줌.
			// handler.postDelayed(modifyWebPageRunnable, 2000);
		}
	}

	private Runnable modifyWebPageRunnable = new Runnable() {

		@Override
		public void run() {
			modifyWebPage();
		}
	};

	private void modifyWebPage() {
		showLinkDrip();
		modifyYouTubeIframeWidth();
		runFootNoteJS();
	}

	private void showLinkDrip() {
		Logger.d(TAG, "showLinkDrip()");
		// 링크명과 표시 명이 다른 경우, 표시 명에 링크 명을 추가로 표시
		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		boolean value = pref.getBoolean("cbAlias", true);
		if (true == value) {

			// 본문에 각주를 표시하지 않음
			mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id == '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			// 본문에 각주를 곧바로 표시
			// mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id != '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			mWebview.loadUrl("javascript:if (typeof alreadyAttachToA === 'undefined'){attachToA();}");
			mWebview.loadUrl("javascript:var alreadyAttachToA = true;");
		}
	}

	private void modifyYouTubeIframeWidth() {

		// <iframe width="560" height="315" src="http://www.youtube.com/embed/0xeO1qWH1JA" frameborder="0" allowfullscreen></iframe>
		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		boolean value = pref.getBoolean("cbModifyYouTubeWidth", true);
		if (true == value) {
			// iframe 태그의 가로세로를 모두 auto로 설정
			mWebview.loadUrl("javascript:function modifyYoutubeWidth(){$('iframe').each(function(i, obj) {obj.width='auto';obj.height='auto';});}modifyYoutubeWidth();");
		}
	}

	private void runFootNoteJS() {
		Logger.d(TAG, "runFootNoteJS()");
		mWebview.loadUrl("javascript:$('.foot').children().each(function(i, obj) {if (obj.tagName.toLowerCase() == 'a' && obj.title != '') {window.HTMLOUT.processFootNote(obj.innerHTML, $(obj).attr('id'), obj.title);}});");
		mWebview.loadUrl("javascript:window.HTMLOUT.setFootNote();");
	}

	public void loadUrlHomePage() {
		String homeURL = getHomeURL();
		mWebview.stopLoading();
		mWebview.loadUrl(homeURL);
	}

	public void loadUrl(int urlStringId) {
		String url = getString(urlStringId);
		mWebview.stopLoading();
		mWebview.loadUrl(url);
	}

	public void loadUrl(String url) {
		mWebview.loadUrl(url);
	}

	public void clearHistory() {
		Logger.d(TAG, "clearHistory()");
		mWebview.clearHistory();
	}

	public void openMenu() {
		WebViewFragmentHolderActivity webViewFragmentActivity = (WebViewFragmentHolderActivity) getActivity();
		webViewFragmentActivity.showMenu();
	}

	public void processNaviPrev() {
		if (true == mWebview.canGoBack()) {
			goBack();
		}
	}

	public void processNaviNext() {
		if (true == mWebview.canGoForward()) {
			mWebview.goForward();
		}
	}

	public void processRandomPage() {
		mWebview.stopLoading();
		mWebview.loadUrl(getString(R.string.url_random_page));
	}

	public void processNaviHome() {
		loadUrlHomePage();
	}

	public boolean goBack() {
		if (mWebview.canGoBack()) {
			// random으로 들어왔을 경우, 랜덤 누르기 이전의 화면으로 보냄.
			WebBackForwardList mWebBackForwardList = mWebview.copyBackForwardList();
			int historySize = mWebBackForwardList.getSize();

			String historyUrl = "";
			int historyBack = 1;

			for (; historyBack < historySize; ++historyBack) {
				historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - historyBack).getUrl();
				if (false == historyUrl.equalsIgnoreCase(getString(R.string.url_random_page))) {
					break;
				}
			}
			mWebview.goBackOrForward(-historyBack);
			return true;
		}

		return false;
	}

	public boolean canGoBack() {

		return mWebview.canGoBack();
	}

	public AlertDialog getNetworkErrorDialog() {
		Builder alertBuilder = new AlertDialog.Builder(getActivity());
		alertBuilder.setMessage(getActivity().getString(R.string.error_network_not_available));
		alertBuilder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		return alertBuilder.create();
	}

	public int getWebViewScrollX() {
		return mWebview.getScrollX();
	}

	private String getHomeURL() {
		return getString(R.string.url_home_page);
	}

	private class CustomJavaScriptInterface {
		@SuppressWarnings("unused")
		public void processFootNote(String fnInnerHTML, String href, String rfnTitle) {
			makeFootNoteList(fnInnerHTML, href, rfnTitle);
		}

		@SuppressWarnings("unused")
		public void setFootNote() {
			if (ssb.length() == 0) {
				ssb.append(getString(R.string.no_footnote));
			}
			((WebViewFragmentHolderActivity) getActivity()).setFootNoteSSB(ssb);
			ssb = new SpannableStringBuilder();
		}
	}

	private void makeFootNoteList(String fnInnerHTML, String href, String rfnTitle) {
		Logger.d(TAG, "makeFootNoteList" + fnInnerHTML);

		// 한줌 물망초
		// 공생전
		// http://stackoverflow.com/questions/9584136/how-to-click-or-tap-on-a-textview-text-on-different-words

		// TODO : 인텔 아톰 시리즈
		// <a id="rfn13" href="#fn13" title="[[http://nocher.egloos.com/27255 참조]]">[13]</a>

		int initialLength = ssb.length();
		ssb.append(fnInnerHTML);

		int idx1 = fnInnerHTML.indexOf("[");
		int idx2 = 0;
		while (idx1 != -1) {
			idx2 = fnInnerHTML.indexOf("]", idx1) + 1;
			String currentUrl = ((WebViewFragmentHolderActivity) getActivity()).getmCurrentUrl();
			if (currentUrl.contains("#")) {
				currentUrl = currentUrl.split("#")[0];
			}

			final String clickString = currentUrl + "#" + href;
			ssb.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					((WebViewFragmentHolderActivity) getActivity()).loadUrl(clickString);
					((WebViewFragmentHolderActivity) getActivity()).toggle();
				}
			}, idx1 + initialLength, idx2 + initialLength, 0);
			idx1 = fnInnerHTML.indexOf("[", idx2);
		}

		ssb.append(" ");

		// 조악하지만, 링크와 각주번호를 구분하기 위해 변경
		if (rfnTitle.contains("[[[")) {
			rfnTitle = rfnTitle.replace("[[[", "[");
			rfnTitle = rfnTitle.replace("]]]", "]");
		}

		if (rfnTitle.contains("[[")) {
			rfnTitle = rfnTitle.replace("[[", "[");
			rfnTitle = rfnTitle.replace("]]", "]");
		}

		int initialLengthForLink = ssb.length();
		ssb.append(rfnTitle);

		idx1 = rfnTitle.indexOf("[");
		idx2 = 0;
		while (idx1 != -1) {
			idx2 = rfnTitle.indexOf("]", idx1) + 1;
			String linkWikiTag = rfnTitle.substring(idx1, idx2);

			String linkitem = "";

			if (linkWikiTag.contains("|")) {
				// case 1 : [[안나(겨울왕국)|안나]]
				String linkitems[] = linkWikiTag.split("[|]");
				linkitem = linkitems[0];
				linkitem = linkitem.replace("[", "");
				linkitem = getString(R.string.url_home_page_with_slash) + linkitem;
			} else if (linkWikiTag.startsWith("[http://")) {
				// case 1 : [http
				int linkidx1 = linkWikiTag.indexOf("h");
				int linkidx2 = linkWikiTag.indexOf(" ", linkidx1 + 1);
				linkitem = linkWikiTag.substring(linkidx1, linkidx2);
			} else if (linkWikiTag.startsWith("[wiki")) {
				// case 2: [w
				int linkidx1 = linkWikiTag.indexOf("\"");
				int linkidx2 = linkWikiTag.indexOf("\"", linkidx1 + 1);
				linkitem = getString(R.string.url_home_page_with_slash) + linkWikiTag.substring(linkidx1 + 1, linkidx2);
			} else {
				// case 3: [
				linkWikiTag = linkWikiTag.replace("[", "");
				linkWikiTag = linkWikiTag.replace("]", "");
				linkitem = getString(R.string.url_home_page_with_slash) + linkWikiTag;
			}

			final String clickStringWiki = linkitem;
			ssb.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					((WebViewFragmentHolderActivity) getActivity()).loadUrl(clickStringWiki);
					((WebViewFragmentHolderActivity) getActivity()).toggle();
				}
			}, idx1 + initialLengthForLink, idx2 + initialLengthForLink, 0);

			idx1 = rfnTitle.indexOf("[", idx2);
		}

		ssb.append("\n\n");
	}

	private void downloadImageFromUrl(String mCurrentUrl, String tabbedUrl) {

		DownloadFilesTask downloadFilesTask = new DownloadFilesTask(getActivity(), mCurrentUrl);
		downloadFilesTask.execute(tabbedUrl);
	}

}
