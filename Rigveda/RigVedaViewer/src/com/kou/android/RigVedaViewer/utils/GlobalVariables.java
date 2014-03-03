package com.kou.android.RigVedaViewer.utils;

/**
 * GlobalVariables
 * 
 * */
public class GlobalVariables {

	private static final String TAG = GlobalVariables.class.getSimpleName();

	public static int webviewScrollX = 0;
	
	public static String mCurrentURL = "";
	public static float mCurrentURLScrollPercent = 0;

	public static int getWebviewScrollX() {
		Logger.d(TAG, "wx X:" + webviewScrollX);
		return webviewScrollX;
	}

	public static void setWebviewScrollX(int webviewScrollX) {
		GlobalVariables.webviewScrollX = webviewScrollX;
	}
}
