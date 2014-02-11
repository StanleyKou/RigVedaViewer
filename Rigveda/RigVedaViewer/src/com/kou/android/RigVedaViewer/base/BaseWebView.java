package com.kou.android.RigVedaViewer.base;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.kou.android.RigVedaViewer.utils.GlobalVariables;
import com.kou.android.RigVedaViewer.utils.Logger;

/**
 * BaseWebView
 * 
 * */
public class BaseWebView extends WebView {
	private final String TAG = BaseWebView.class.getSimpleName();
	
	public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BaseWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseWebView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		Logger.d(TAG, "BaseWebView onScrollChanged :" + l);
		GlobalVariables.setWebviewScrollX(l);
	}
}
