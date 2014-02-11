package com.kou.android.RigVedaViewer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.activity.WebViewFragmentHolderActivity;
import com.kou.android.RigVedaViewer.base.BaseWebView;
import com.kou.android.RigVedaViewer.utils.Logger;

/**
 * CustomWebViewFragment.
 * 
 * Core part of this app. Holding WebView,
 * 
 * */
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

		CookieManager.getInstance().setAcceptCookie(true);
		CookieSyncManager.createInstance(getActivity());
		CookieSyncManager.getInstance().startSync();
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
		CookieSyncManager.getInstance().stopSync();
		super.onPause();
	}

	@Override
	public void onStop() {
		mWebview.stopLoading();
		super.onStop();
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
		// mWebview.getSettings().setUserAgentString(getString(R.string.user_agent_pc));

		// Fullpage�� ��� �ε� �Ϸ� �� ȭ�� ��ü�� �� ���� �����ֵ��� ������ ����.
		mWebview.setInitialScale(1);
		mWebview.getSettings().setJavaScriptEnabled(true); // for redirect

		mWebview.addJavascriptInterface(new CustomJavaScriptInterface(), "HTMLOUT");

		// String footNote = sb.toString();
		//
		// if (footNote.equalsIgnoreCase("") == true) {
		// footNote = getString(R.string.no_footnote);
		// ((WebViewFragmentHolderActivity) getActivity()).setFootNote(footNote);
		// } else {
		//
		// // [wiki:"����" ��] [wiki:"����" �Ϸ���]
		// // [[2000���]]
		// // http://fifthsun5.egloos.com/3031076
		//
		// StringBuilder sbOut = new StringBuilder();
		// StringBuilder sbLink = new StringBuilder();
		//
		// Field field;
		// boolean flag1 = false;
		// boolean flag2 = false;
		// try {
		// field = String.class.getDeclaredField("value");
		// field.setAccessible(true);
		//
		// try {
		// final char[] chars = (char[]) field.get(footNote);
		// final int len = chars.length;
		// for (int i = 0; i < len; i++) {
		//
		// if (chars[i] == '[') {
		// if (i + 1 < len && chars[i + 1] == '[') {
		// flag1 = true;
		// }
		// }
		//
		// if (chars[i] == ']') {
		// if (i + 1 < len && chars[i + 1] == ']') {
		// flag2 = true;
		// }
		// }
		//
		// if (flag2 == true) {
		// sbOut.append(sbLink);
		// sbLink.setLength(0);
		// flag1 = flag2 = false;
		// }
		//
		// if (flag1 == true) {
		// sbLink.append(chars[i]);
		// } else {
		// sbOut.append(chars[i]);
		// }
		//
		// }
		//
		// ((WebViewFragmentHolderActivity) getActivity()).setFootNote(sbOut.toString());
		//
		// } catch (Exception ex) {
		// throw new RuntimeException(ex);
		// }
		// } catch (NoSuchFieldException e) {
		// e.printStackTrace();
		// }
		// }

		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(0);
				super.onPageStarted(view, url, favicon);

				handler.removeCallbacks(modifyWebPageRunnable);

				ssb.clear();

				// getResource�� �о�� �� ���� �����̸� ����. isAdded�� ���� �ʴ� ������, Activity�� ������ �Ǳ� ����.
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

				if (url.contains(CustomWebViewFragment.this.getString(R.string.url_home_page)) == false) {
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

				// getResource�� �о�� �� ���� �����̸� ����. isAdded�� ���� �ʴ� ������, Activity�� ������ �Ǳ� ����.
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

	private void modifyWebPageAfterFinished(final String url) {
		// ���� ȭ���� ���� �������� �ƴ� ��쿡�� ����
		if (false == url.equalsIgnoreCase(getString(R.string.url_random_page))) {
			handler.postDelayed(modifyWebPageRunnable, 100);
			// Ÿ�̹� �̽������� ������. ���۾ֵ尡 �������� �ð�, �̹����� �Ϸ�Ǵ� �ð��� �ʿ���. �׷��� ��Ȯ�� �Ϸ�Ǵ� ������ �˾Ƴ��� ����� Ȯ�� ���� �ϴ� �����̸� ������ ��.
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
		runFootNoteJS();
	}

	private void showLinkDrip() {
		Logger.d(TAG, "showLinkDrip()");
		// ��ũ��� ǥ�� ���� �ٸ� ���, ǥ�� �� ��ũ ���� �߰��� ǥ��
		SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
		boolean value = pref.getBoolean("cbAlias", true);
		if (true == value) {

			//
			// ������ ���ָ� ǥ������ ����
			mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id == '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			// ������ ���ָ� ��ٷ� ǥ��
			// mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id != '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			mWebview.loadUrl("javascript:if (typeof alreadyAttachToA === 'undefined'){attachToA();}");
			mWebview.loadUrl("javascript:var alreadyAttachToA = true;");
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
			// random���� ������ ���, ���� ������ ������ ȭ������ ����.
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

		// ���� ������
		// ������
		// http://stackoverflow.com/questions/9584136/how-to-click-or-tap-on-a-textview-text-on-different-words

		// TODO : ���� ���� �ø���
		// <a id="rfn13" href="#fn13" title="[[http://nocher.egloos.com/27255 ����]]">[13]</a>

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

		// ����������, ��ũ�� ���ֹ�ȣ�� �����ϱ� ���� ����
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

			if (linkWikiTag.startsWith("[http://")) {
				// case 1 : [http
				int linkidx1 = linkWikiTag.indexOf("h");
				int linkidx2 = linkWikiTag.indexOf(" ", linkidx1 + 1);
				linkitem = linkWikiTag.substring(linkidx1, linkidx2);
			} else if (linkWikiTag.startsWith("[w")) {
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

}
