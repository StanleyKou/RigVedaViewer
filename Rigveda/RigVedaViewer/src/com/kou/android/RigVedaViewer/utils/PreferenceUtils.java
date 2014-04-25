package com.kou.android.RigVedaViewer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * This class contains utility methods , variables and constants, that will be used by other activities.
 * 
 * */
public class PreferenceUtils {

	private static final String PREF_KEY = "pref";

	private static final String PREF_KEY_cbAlias = "cbAlias";
	private static final String PREF_KEY_cbModifyYouTubeWidth = "cbModifyYouTubeWidth";
	private static final String PREF_KEY_cbExternalImage = "cbExternalImage";
	private static final String PREF_KEY_cbNightEyeProtect = "cbNightEyeProtect";
	private static final String PREF_KEY_cbTextColor = "cbTextColor";

	private final static String PREF_KEY_textColor = "textColor";
	private final static String PREF_KEY_backgroundColor = "backgroundColor";
	private final static String PREF_KEY_linkColor = "linkColor";
	private final static String PREF_KEY_fontSize = "fontSize";

	private static final String PREF_KEY_cbFontSize = "cbFontSize";
	private static final String PREF_KEY_cbShowPrev = "cbShowPrev";
	private static final String PREF_KEY_cbShowNext = "cbShowNext";
	private static final String PREF_KEY_cbShowRandom = "cbShowRandom";
	private static final String PREF_KEY_cbShowReverseLink = "cbShowReverseLink";
	private static final String PREF_KEY_cbShowFootNote = "cbShowFootNote";
	private static final String PREF_KEY_cbShowMenuLeft = "cbShowMenuLeft";
	private static final String PREF_KEY_IsShortcutInstalled = "installShortCut";

	public final static int DEFAULT_FONT_SIZE_PERCENT = 100;

	public final static int TEXTCOLOR_TYPE1 = 0xFF000000;
	public final static int TEXTCOLOR_TYPE2 = 0xFF999999;
	public final static int TEXTCOLOR_TYPE3 = 0xFF121255;

	public final static int BACKGROUND_TYPE1 = 0xFFFFFFFF;
	public final static int BACKGROUND_TYPE2 = 0xFF000000;
	public final static int BACKGROUND_TYPE3 = 0xFFBBBBBB;

	public final static int LINKCOLOR_TYPE1 = 0xFF002EE2;
	public final static int LINKCOLOR_TYPE2 = 0xFFCC6600;
	public final static int LINKCOLOR_TYPE3 = 0xFF0072FF;

	private static void saveBooleanPreference(Context context, String key, boolean defValue) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, defValue);
		editor.commit();
	}

	private static boolean loadBooleanPreference(Context context, String key, boolean defValue) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		return pref.getBoolean(key, defValue);
	}

	private static void saveIntPreference(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private static int loadIntPreference(Context context, String key, int defValue) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		return pref.getInt(key, defValue);
	}

	private static void saveStringPreference(Context context, String key, String stringData) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, stringData);
		editor.commit();
	}

	private static String loadStringPreference(Context context, String key) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		return pref.getString(key, "");

	}

	public static boolean getcbAlias(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbAlias, true);
	}

	public static boolean getcbModifyYouTubeWidth(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbModifyYouTubeWidth, true);
	}

	public static boolean getcbExternalImage(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbExternalImage, true);
	}

	public static boolean getcbNightEyeProtect(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbNightEyeProtect, true);
	}

	public static boolean getcbTextColor(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbTextColor, false);
	}

	public static int gettextColor(Context applicationContext) {
		return loadIntPreference(applicationContext, PREF_KEY_textColor, TEXTCOLOR_TYPE1);
	}

	public static int getbackgroundColor(Context applicationContext) {
		return loadIntPreference(applicationContext, PREF_KEY_backgroundColor, BACKGROUND_TYPE1);
	}

	public static int getlinkColorPrefKey(Context applicationContext) {
		return loadIntPreference(applicationContext, PREF_KEY_linkColor, LINKCOLOR_TYPE1);
	}

	public static boolean getcbFontSize(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbFontSize, false);
	}

	public static int getfontSize(Context applicationContext) {
		return loadIntPreference(applicationContext, PREF_KEY_fontSize, DEFAULT_FONT_SIZE_PERCENT);
	}

	public static boolean getcbShowPrev(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowPrev, false);
	}

	public static boolean getcbShowNext(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowNext, false);
	}

	public static boolean getcbShowRandom(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowRandom, true);
	}

	public static boolean getcbShowReverseLink(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowReverseLink, false);
	}

	public static boolean getcbShowFootNote(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowFootNote, true);
	}

	public static boolean getcbShowMenuLeft(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_cbShowMenuLeft, true);
	}

	public static boolean getIsShortcutInstalled(Context applicationContext) {
		return loadBooleanPreference(applicationContext, PREF_KEY_IsShortcutInstalled, false);
	}

	// ///////////////////////// SET /////////////////////////

	public static void setcbAlias(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbAlias, value);
	}

	public static void setcbModifyYouTubeWidth(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbModifyYouTubeWidth, value);
	}

	public static void setcbExternalImage(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbExternalImage, value);
	}

	public static void setcbNightEyeProtect(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbNightEyeProtect, value);
	}

	public static void setcbTextColor(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbTextColor, value);
	}

	public static void settextColor(Context applicationContext, int value) {
		saveIntPreference(applicationContext, PREF_KEY_textColor, value);
	}

	public static void setbackgroundColor(Context applicationContext, int value) {
		saveIntPreference(applicationContext, PREF_KEY_backgroundColor, value);
	}

	public static void setlinkColorPrefKey(Context applicationContext, int value) {
		saveIntPreference(applicationContext, PREF_KEY_linkColor, value);
	}

	public static void setcbFontSize(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbFontSize, value);
	}

	public static void setfontSize(Context applicationContext, int value) {
		saveIntPreference(applicationContext, PREF_KEY_fontSize, value);
	}

	public static void setcbShowPrev(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowPrev, value);
	}

	public static void setcbShowNext(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowNext, value);
	}

	public static void setcbShowRandom(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowRandom, value);
	}

	public static void setcbShowReverseLink(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowReverseLink, value);
	}

	public static void setcbShowFootNote(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowFootNote, value);
	}

	public static void setcbShowMenuLeft(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_cbShowMenuLeft, value);
	}

	public static void setIsShortcutInstalled(Context applicationContext, boolean value) {
		saveBooleanPreference(applicationContext, PREF_KEY_IsShortcutInstalled, value);
	}

}
