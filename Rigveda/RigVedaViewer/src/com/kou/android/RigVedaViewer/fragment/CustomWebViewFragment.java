package com.kou.android.RigVedaViewer.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
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
import com.kou.android.RigVedaViewer.utils.GlobalVariables;
import com.kou.android.RigVedaViewer.utils.LogWrapper;
import com.kou.android.RigVedaViewer.utils.PreferenceUtils;
import com.kou.android.RigVedaViewer.utils.Utils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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
	private View mWebviewNightEyeProtecter;
	private ShimmerTextView tvShimmmer;
	private Shimmer shimmer;

	private Handler handler = new Handler(Looper.getMainLooper());

	private ProgressBar mProgressBar;
	private AlertDialog mNetworkErrorDialog;

	private LinearLayout llMenu;
	private boolean isMenuLeft = true;

	// Left bottom menu
	private Button btnRandom;
	private Button btnReverseLink;
	private Button btnFootNote;
	private Button btnExit;

	private ImageView ivToRightNotSelected;
	private ImageView ivToRightSelected;

	private ImageView ivToLeftNotSelected;
	private ImageView ivToLeftSelected;

	private ImageView ivNavPrev;
	private ImageView ivNavNext;

	private boolean isModifyFinished = false;

	public String getCurrentURL() {
		return GlobalVariables.currentURL;
	}

	public void setCurrentURL(String currentURL) {
		GlobalVariables.currentURL = currentURL;
	}

	private SpannableStringBuilder ssb = new SpannableStringBuilder();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = (View) inflater.inflate(R.layout.fragment_webview, container, false);
		mMainView.setOnTouchListener(this);

		initWebView(savedInstanceState);

		btnRandom = (Button) mMainView.findViewById(R.id.btnRandom);
		btnReverseLink = (Button) mMainView.findViewById(R.id.btnReverseLink);
		btnFootNote = (Button) mMainView.findViewById(R.id.btnFootNote);
		btnExit = (Button) mMainView.findViewById(R.id.btnExit);

		btnRandom.setOnClickListener(this);
		btnRandom.setOnTouchListener(this);
		btnReverseLink.setOnClickListener(this);
		btnReverseLink.setOnClickListener(this);
		btnFootNote.setOnClickListener(this);
		btnFootNote.setOnTouchListener(this);
		btnExit.setOnClickListener(this);
		btnExit.setOnTouchListener(this);

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
		CookieSyncManager.getInstance().sync(); // ������ �ʿ� ����. ���߿� ��Ű�� ���ԵǸ� ������.
		boolean valuecbShowExit = PreferenceUtils.getcbShowExit(getActivity());
		boolean valuecbShowPrev = PreferenceUtils.getcbShowPrev(getActivity());
		boolean valuecbShowNext = PreferenceUtils.getcbShowNext(getActivity());
		boolean valuecbShowRandom = PreferenceUtils.getcbShowRandom(getActivity());
		boolean valuecbShowReverseLink = PreferenceUtils.getcbShowReverseLink(getActivity());
		boolean valuecbShowFootNote = PreferenceUtils.getcbShowFootNote(getActivity());
		boolean valuecbShowMenuLeft = PreferenceUtils.getcbShowMenuLeft(getActivity());

		ivNavPrev.setVisibility(true == valuecbShowPrev ? View.VISIBLE : View.GONE);
		ivNavNext.setVisibility(true == valuecbShowNext ? View.VISIBLE : View.GONE);
		btnRandom.setVisibility(true == valuecbShowRandom ? View.VISIBLE : View.GONE);
		btnReverseLink.setVisibility(true == valuecbShowReverseLink ? View.VISIBLE : View.GONE);
		btnFootNote.setVisibility(true == valuecbShowFootNote ? View.VISIBLE : View.GONE);
		btnExit.setVisibility(true == valuecbShowExit ? View.VISIBLE : View.GONE);

		if (true == valuecbShowMenuLeft) {
			showMenuLeft();
		} else {
			showMenuRight();
		}

		super.onResume();
	}

	@Override
	public void onDestroyView() {
		saveScrollPosition();
		saveLastPageURL();
		mWebview.destroy();
		super.onDestroyView();
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
		handler.post(menuShowRunnable);

		switch (v.getId()) {

		case R.id.btnRandom:
			loadUrl(R.string.url_random_page);
			break;
		case R.id.btnReverseLink:
			processReverseLink();
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
		case R.id.btnExit:
			getActivity().finish();
			break;

		// case R.id.ivUpperMenuPrev:
		// processNaviPrev();
		// break;
		// case R.id.ivUpperMenuNext:
		// processNaviNext();
		// break;
		// case R.id.btnUpperMenuSearch:
		// processSearch();
		// break;
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		mWebview.saveState(outState);
		super.onSaveInstanceState(outState);
	}

	private void captureScreen() {

		String mPath = Utils.getAppStorageFolder(getActivity()) + File.separator + "test.png";

		// create bitmap screen capture
		Bitmap bitmap;
		View v1 = mMainView.getRootView();
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);

		OutputStream fout = null;
		File imageFile = new File(mPath);

		try {
			fout = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.llMenu || v.getId() == R.id.btnRandom || v.getId() == R.id.btnReverseLink || v.getId() == R.id.btnFootNote) {
			if (true == isMenuLeft) {
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
		}

		return false;
	}

	private void showMenuLeft() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llMenu.setLayoutParams(lp);
		isMenuLeft = true;

		PreferenceUtils.setcbShowMenuLeft(getActivity(), true);
	}

	private void showMenuRight() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llMenu.setLayoutParams(lp);
		isMenuLeft = false;

		PreferenceUtils.setcbShowMenuLeft(getActivity(), false);
	}

	private void initWebView(Bundle savedInstanceState) {

		mWebview = (BaseWebView) mMainView.findViewById(R.id.webviewMain);
		mWebview.restoreState(savedInstanceState);

		mWebview.getSettings().setUseWideViewPort(false); // prevent double-tap zoom
		mWebview.getSettings().setBuiltInZoomControls(false);
		mWebview.getSettings().setSupportZoom(false);
		mWebview.getSettings().setDefaultZoom(ZoomDensity.FAR);

		mWebview.getSettings().setAllowFileAccess(true);
		mWebview.getSettings().setDomStorageEnabled(true);

		// mWebview.setInitialScale(1);

		mWebview.getSettings().setJavaScriptEnabled(true); // for redirect
		mWebview.addJavascriptInterface(new CustomJavaScriptInterface(), "HTMLOUT");

		mWebview.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				HitTestResult result = mWebview.getHitTestResult();
				LogWrapper.d(TAG, "LongClick: " + result.getExtra());

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

		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, final String url, Bitmap favicon) {

				modifyTextSize();

				boolean isNightEyeProtecter = PreferenceUtils.getcbNightEyeProtect(getActivity());
				if (true == isNightEyeProtecter) {
					mWebviewNightEyeProtecter.setVisibility(View.VISIBLE);
					shimmer.start(tvShimmmer);
				}

				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(0);

				isModifyFinished = false;

				super.onPageStarted(view, url, favicon);

				handler.removeCallbacks(modifyWebPageRunnable);

				ssb.clear();

				// getResource�� �о�� �� ���� �����̸� ����. isAdded�� ���� �ʴ� ������, Activity�� ������ �Ǳ� ����.
				if (getActivity() == null) {
					LogWrapper.d(TAG, "getActivity is null");
					return;
				} else {
					LogWrapper.d(TAG, "getActivity is good");
				}

				String footNote = getString(R.string.loading_footnote);
				((WebViewFragmentHolderActivity) getActivity()).setFootNote(footNote);

				postModifyWebPage(url);
			}

			// http://stackoverflow.com/questions/23298290/webview-shouldoverrideurlloading-not-called-for-invalid-links
			// Kitkat webview will not call shouldOverrideUrlLoading
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (getCurrentURL() != null && url != null && url.equals(getCurrentURL())) {
					mWebview.goBack();
					return true;
				}

				view.stopLoading();

				if (url.contains("file:///android_asset/webkit/")) {
					// do nothing. security origin error.
				}
				// else if (false == url.contains(CustomWebViewFragment.this.getString(R.string.url_home_page))) {
				// Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				// startActivity(i);
				// }
				else {
					view.loadUrl(url);
				}

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				LogWrapper.d(TAG, "onPageFinished");
				setCurrentURL(url);

				saveLastPageURL();

				mProgressBar.setVisibility(View.GONE);
				// shimmer.cancel();

				// getResource�� �о�� �� ���� �����̸� ����. isAdded�� ���� �ʴ� ������, Activity�� ������ �Ǳ� ����.
				if (getActivity() == null) {
					LogWrapper.d(TAG, "getActivity is null");
					return;
				} else {
					LogWrapper.d(TAG, "getActivity is good");
				}

				String homeURL = getHomeURL();
				if (true == url.equalsIgnoreCase(homeURL) && false == mWebview.canGoForward()) {
					clearHistory();
				}

				// postModifyWebPage(url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				LogWrapper.d(TAG, "error code:" + errorCode);

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

		mWebview.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// search â�� ��Ŀ���� �޾� Ű���尡 �ö���� ���� �����ϱ� ����
				// InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(etUpperMenuSearch.getWindowToken(), 0);

				// switch (event.getAction() & MotionEvent.ACTION_MASK) {
				// case MotionEvent.ACTION_DOWN:
				// LogWrapper.d(TAG, "ACTION_DOWN. Y:" + event.getY());
				// prevDistanceY = event.getY();
				// break;
				//
				// case MotionEvent.ACTION_UP:
				// LogWrapper.d(TAG, "ACTION_UP");
				//
				// if (prevDistanceY - event.getY() > 0) {
				// scrollerLinearLayout.pullUp();
				// } else if (prevDistanceY - event.getY() < 0) {
				// scrollerLinearLayout.pullDown();
				// }
				// break;
				// case MotionEvent.ACTION_MOVE:
				// LogWrapper.d(TAG, "ACTION_MOVE");
				//
				// scrollerLinearLayout.pullMove(0, (int) event.getY());
				//
				// break;
				//
				// }

				handler.removeCallbacks(menuHideRunnable);
				handler.post(menuShowRunnable);
				handler.postDelayed(menuHideRunnable, 2000);
				return false;
			}
		});

		mWebviewNightEyeProtecter = mMainView.findViewById(R.id.mWebviewNightEyeProtecter);
		tvShimmmer = (ShimmerTextView) mMainView.findViewById(R.id.tvShimmmer);
		shimmer = new Shimmer();

		// getLastPageURL
		if (getCurrentURL() != null && getCurrentURL().length() > 0) {
			loadUrl(getCurrentURL());
		} else if (PreferenceUtils.getLastPageURL(getActivity()) != null && PreferenceUtils.getLastPageURL(getActivity()).length() > 0) {
			loadUrl(PreferenceUtils.getLastPageURL(getActivity()));
		} else {
			loadUrlHomePage();
		}

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
					downloadImageFromUrl(getCurrentURL(), tabbedUrl);

					break;
				case 1:
					ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
					String downloadablelUrl = Utils.getDownloadableRigVedaURL(getActivity(), getCurrentURL(), tabbedUrl);
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
					String downloadablelUrl = Utils.getDownloadableRigVedaURL(getActivity(), getCurrentURL(), tabbedUrl);
					clipboard.setText(downloadablelUrl);
					Toast.makeText(getActivity(), getString(R.string.dialog_image_url_copy_colon) + downloadablelUrl, Toast.LENGTH_SHORT).show();
					break;

				}
			}

		});

		return dialogBuilder.create();
	}

	private void postModifyWebPage(final String url) {
		// ���� ȭ���� ���� �������� �ƴ� ��쿡�� ����
		if (false == url.equalsIgnoreCase(getString(R.string.url_random_page))) {
			// Ÿ�̹� �̽������� ������. ���׺��� ���� �������� ���۾ֵ尡 �������� �ð�, �̹��� �ε��� �Ϸ�Ǵ� �ð��� �ʿ���. �׷��� ��Ȯ�� �Ϸ�Ǵ� ������ �˾Ƴ��� ����� Ȯ�� ���� �ϴ� �����̸� ������ ��.
			// bool ������ ������ ������ �Ϸ�Ǿ����� üũ�� �ϱ� ������ �� �� ���������� ����.
			handler.postDelayed(modifyWebPageRunnable, 3000);
			handler.postDelayed(modifyWebPageRunnable, 10000);
		}
	}

	private Runnable modifyWebPageRunnable = new Runnable() {

		@Override
		public void run() {
			if (isModifyFinished == false) {
				modifyWebPage();
			}
		}
	};

	private void modifyWebPage() {
		if (getActivity() != null) { // on orientation changed, there is no activity for a while.
			modifyRemoveNavbar();
			modifyShowLinkDrip();
			modifyYouTubeIframeWidth();
			// modifyTextBackgroundColor();
			// modifyTextSize(); // Move to page start
			modifyOrientationCSS();
			modifyExternalImageShow();
		}
		modifyMakeFootNote();

		handler.postDelayed(menuHideRunnable, 2000);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mWebviewNightEyeProtecter.setVisibility(View.GONE);
				shimmer.cancel();

			}
		}, 500);

		isModifyFinished = true;
	}

	private void modifyRemoveNavbar() {
		LogWrapper.d(TAG, "showLinkDrip()");
		mWebview.loadUrl("javascript:$('.navbar-nav').hide();");
		mWebview.loadUrl("javascript:$('.header').hide();");
		mWebview.loadUrl("javascript:$('div').each(function() {if($(this).height()==42){$(this).hide();}});");
	}

	private void modifyShowLinkDrip() {
		LogWrapper.d(TAG, "showLinkDrip()");
		// ��ũ��� ǥ�� ���� �ٸ� ���, ǥ�� �� ��ũ ���� �߰��� ǥ��
		boolean value = PreferenceUtils.getcbAlias(getActivity());

		if (true == value) {

			// ������ ���ָ� ǥ������ ����
			mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id == '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			// ������ ���ָ� ��ٷ� ǥ��
			// mWebview.loadUrl("javascript:function attachToA(){$('a').each(function(i, obj){if (obj.id != '' && obj.title != '' && obj.title != obj.innerHTML) { obj.innerHTML += '<span style=background-color:#8AC007;>(' + obj.title + ')</span>';}});}");

			mWebview.loadUrl("javascript:if (typeof alreadyAttachToA === 'undefined'){attachToA();}");
			mWebview.loadUrl("javascript:var alreadyAttachToA = true;");
		}
	}

	private void modifyYouTubeIframeWidth() {
		LogWrapper.d(TAG, "modifyYouTubeIframeWidth()");
		boolean value = PreferenceUtils.getcbModifyYouTubeWidth(getActivity());

		if (true == value) {
			// iframe �±��� ���θ� 100%, ���θ� auto�� ����
			mWebview.loadUrl("javascript:function modifyYoutubeWidth(){$('iframe').each(function(i, obj) {obj.width='100%';obj.height='auto';});}modifyYoutubeWidth();");
		}
	}

	// private void modifyTextBackgroundColor() {
	// boolean value = PreferenceUtils.getcbTextColor(getActivity());
	// if (true == value) {
	// LogWrapper.d(TAG, "modifyTextBackgroundColor()");
	//
	// int textColor = PreferenceUtils.gettextColor(getActivity());
	// int backgroundColor = PreferenceUtils.getbackgroundColor(getActivity());
	// int linkTextColor = PreferenceUtils.getlinkColorPrefKey(getActivity());
	//
	// textColor = 0x00FFFFFF & textColor;
	// backgroundColor = 0x00FFFFFF & backgroundColor;
	// linkTextColor = 0x00FFFFFF & linkTextColor;
	//
	// String textColorString = Utils.getSixDigitHexString(textColor);
	// String backgroundColorString = Utils.getSixDigitHexString(backgroundColor);
	// String linkTextColorString = Utils.getSixDigitHexString(linkTextColor);
	//
	// String jQueryString = String
	// .format("javascript:$('#mainBody').css('background-color','#%s');$('#wikiContent').css('color','#%s');$('a').each(function(i, obj){$(this).css('color','#%s');$(this).css('background-color','#%s');});",
	// backgroundColorString, textColorString, linkTextColorString, backgroundColorString);
	// mWebview.loadUrl(jQueryString);
	// }
	// }

	private void modifyTextSize() {
		boolean value = PreferenceUtils.getcbFontSize(getActivity());
		if (true == value) {
			LogWrapper.d(TAG, "modifyTextSize()");
			int fontSize = PreferenceUtils.getfontSize(getActivity());
			if (fontSize > 4) { // for Backward Compatibility.
				fontSize = 2;
			}

			WebSettings.TextSize textSize = WebSettings.TextSize.NORMAL;
			switch (fontSize) {
			case 0:
				textSize = WebSettings.TextSize.LARGEST;
				break;
			case 1:
				textSize = WebSettings.TextSize.LARGER;
				break;
			case 2:
				textSize = WebSettings.TextSize.NORMAL;
				break;
			case 3:
				textSize = WebSettings.TextSize.SMALLER;
				break;
			case 4:
				textSize = WebSettings.TextSize.SMALLEST;
				break;
			}

			mWebview.getSettings().setTextSize(textSize);

			// String loadFontSize = String.format("javascript:$('div').each(function(i, obj){$(this).css('font-size','%d%%');})", fontSize);
			// mWebview.loadUrl(loadFontSize);

		}
	}

	private void modifyMakeFootNote() {
		LogWrapper.d(TAG, "runFootNoteJS()");
		mWebview.loadUrl("javascript:$('.foot').children().each(function(i, obj) {if (obj.tagName.toLowerCase() == 'a' && obj.title != '') {window.HTMLOUT.processFootNote(obj.innerHTML, $(obj).attr('id'), obj.title);}});");
		mWebview.loadUrl("javascript:window.HTMLOUT.setFootNote();");
	}

	private void modifyOrientationCSS() {
		if (GlobalVariables.currentURLScrollPercent > 0) {
			float webviewsize = mWebview.getContentHeight() - mWebview.getTop();
			float positionInWV = webviewsize * GlobalVariables.currentURLScrollPercent;
			int positionY = Math.round(mWebview.getTop() + positionInWV);
			mWebview.scrollTo(0, positionY);
			GlobalVariables.currentURLScrollPercent = 0;
		}

		// landscape : rightbox float none, leftbox width 100%
		// $('#rightBox').css('float', 'none');
		// $('#leftBox').css('width', '100%');

		int orientation = getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			String leftBoxRightBoxCSS = "javascript:$('#rightBox').css('float', 'none');$('#leftBox').css('width', '100%');";
			mWebview.loadUrl(leftBoxRightBoxCSS);
		} else {
			// portrait : rightbox float true, leftbox clear
			// $( "#rightBox" ).css( "float", "right" );
		}

	}

	private void modifyExternalImageShow() {
		boolean value = PreferenceUtils.getcbExternalImage(getActivity());
		if (true == value) {
			LogWrapper.d(TAG, "modifyExternalImageShow()");
			String loadExternalImage = "javascript:$('.external').css('display', 'block').css('width', '100%');";
			mWebview.loadUrl(loadExternalImage);
		}
	}

	private Runnable menuHideRunnable = new Runnable() {
		@Override
		public void run() {
			AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.3F);
			alpha.setDuration(200);
			alpha.setFillAfter(true);
			llMenu.startAnimation(alpha);
		}
	};

	private Runnable menuShowRunnable = new Runnable() {
		@Override
		public void run() {
			AlphaAnimation alpha = new AlphaAnimation(0.3F, 1.0F);
			alpha.setDuration(200);
			alpha.setFillAfter(true);
			llMenu.startAnimation(alpha);
		}
	};

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
		LogWrapper.d(TAG, "clearHistory()");
		mWebview.clearHistory();
	}

	public void processReverseLink() {
		String currentURL = getCurrentURL();
		if (currentURL.equals(getString(R.string.url_home_page))) {
			return;
		}

		String reverseURL = Utils.getReverseLink(getActivity().getApplicationContext(), currentURL);
		loadUrl(reverseURL);
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

	public void processSearch() {
		// http://rigvedawiki.net/r1/wiki.php/data
		// String searchKeyword = etUpperMenuSearch.getText().toString();
		//
		// if (searchKeyword.length() > 0) {
		// String searchString = getString(R.string.url_home_page_with_slash) + searchKeyword;
		// mWebview.loadUrl(searchString);
		// }
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

	public void reload() {
		mWebview.reload();
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
		public void processFootNote(String fnInnerHTML, String href, String rfnTitle) {
			makeFootNoteList(fnInnerHTML, href, rfnTitle);
		}

		public void setFootNote() {
			if (ssb.length() == 0) {
				ssb.append(getString(R.string.no_footnote));
			}
			((WebViewFragmentHolderActivity) getActivity()).setFootNoteSSB(ssb);
			ssb = new SpannableStringBuilder();
		}
	}

	private void makeFootNoteList(String fnInnerHTML, String href, String rfnTitle) {
		LogWrapper.d(TAG, "makeFootNoteList" + fnInnerHTML);

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

			if (linkWikiTag.contains("|")) {
				// case 1 : [[�ȳ�(�ܿ�ձ�)|�ȳ�]]
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

	public void saveScrollPosition() {
		float percentWebview = (mWebview.getScrollY() - mWebview.getTop()) / (float) mWebview.getContentHeight();
		GlobalVariables.currentURLScrollPercent = percentWebview;
	}

	public void saveLastPageURL() {
		String lastPageURL = mWebview.getUrl();
		PreferenceUtils.setLastPageURL(getActivity(), lastPageURL);
	}

	// private float prevDistanceY = 0;
	private SimpleOnGestureListener mListener = new SimpleOnGestureListener() {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// FIXING
			LogWrapper.d(TAG, "mListener distanceY:" + distanceY);
			// if (distanceY > 0) {
			// scrollerLinearLayout.pullUp();
			// } else if (distanceY < 0) {
			// scrollerLinearLayout.pullDown();
			// }

			// scrollerLinearLayout.scrollMenuBy(0, (int) distanceY);
			// prevDistanceY = distanceY;
			return true;
		}
	};

}
