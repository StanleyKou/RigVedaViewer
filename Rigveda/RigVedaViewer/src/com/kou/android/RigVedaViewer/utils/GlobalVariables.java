package com.kou.android.RigVedaViewer.utils;

/**
 * GlobalVariables
 * 
 * */
public class GlobalVariables {

	private static final String TAG = GlobalVariables.class.getSimpleName();

	public static int webviewScrollX = 0;

	public static boolean cbAlias = true;
	public static boolean cbShowPrev = false;
	public static boolean cbShowNext = false;;
	public static boolean cbShowRandom = true;
	public static boolean cbShowFootNote = true;
	public static boolean cbShowMenuLeft = true;
	public static boolean cbShowMenuRight = false;

	public static int getWebviewScrollX() {
		Logger.d(TAG, "wx X:" + webviewScrollX);
		return webviewScrollX;
	}

	public static void setWebviewScrollX(int webviewScrollX) {
		GlobalVariables.webviewScrollX = webviewScrollX;
	}
}
