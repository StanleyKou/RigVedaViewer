package com.kou.android.RigVedaViewer.utils;

import java.io.File;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.Display;

import com.kou.android.RigVedaViewer.R;
import com.kou.android.RigVedaViewer.activity.SplashActivity;

/**
 * Utils
 * 
 * This class contains utility methods , variables and constants, that will be used by other activities.
 * 
 * */
public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	private final static String DOWNLOAD_PREFIX = "?action=download&value=";

	public static int getScreenWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenWidthPercent(Activity activity, int percent) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return (int) (width * percent * 0.01);
	}

	public static boolean isInstalled(Context context, String schemUrl) {
		boolean isInstalled = false;
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo packageInfo : packages) {
			if (schemUrl.equalsIgnoreCase(packageInfo.packageName)) {
				LogWrapper.d(TAG, "isInstalled : true");
				isInstalled = true;
				break;
			}
		}
		return isInstalled;
	}

	public static String getApplicationVersionInfo(Context context) {
		PackageInfo pInfo;
		String versionName = "";
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			LogWrapper.e(TAG, e.toString());
		}
		return versionName;
	}

	public static void installShortcut(Context context) {

		boolean isShortcutInstalled = PreferenceUtils.getIsShortcutInstalled(context);

		if (false == isShortcutInstalled) {

			Intent shortcutIntent = new Intent(Intent.ACTION_MAIN, null);
			shortcutIntent.setClass(context, SplashActivity.class);
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			Intent intent = new Intent();
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon_launcher_rigveda));
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			intent.putExtra("duplicate", false); // go launcher에서 인식불가. 원래는 preference 이용하지 않고 이 값만 이용해도 중복되지 않음.
			context.sendBroadcast(intent);

			PreferenceUtils.setIsShortcutInstalled(context, true);
		}
	}

	public static String getRomanNumber(int number) {
		StringBuilder sb = new StringBuilder();

		// 3 : iii -> reverse -> iii
		// 4 : vi -> reverse -> iv
		// 6 : iv -> reverse -> vi
		// 7 : iiv -> reverse -> vii
		// 9 : xi -> reverse -> ix

		if (number > 9000 || number < 0) {
			return "";
		}
		final String digits = "ivxlcdmf"; // 1, 5, 10, 50, 100, 500, 1000, 5000

		for (int k = 0; k < digits.length() && number > 0; k += 2) {
			int mod = number % 10;

			number = (number - mod) / 10;

			// 4 or 9 : add 5 or 10
			if (mod % 5 == 4) {
				sb.append(digits.charAt(k + (mod + 1) / 5));
				mod = 1;
			}

			// 1~4, 6~9 : add 1s.
			// 4, 9 : add only one 1, by above procedure.
			// 6 : add only one 1, by calculation.
			for (int j = 0; j < mod % 5; j++) {
				sb.append(digits.charAt(k));
			}

			// 5 ~ 9 : add 5
			sb.append(mod >= 5 ? digits.charAt(k + 1) : "");
		}

		return sb.reverse().toString();
	}

	public static boolean createDirIfNotExists(Activity activity) {
		boolean ret = true;

		File file = new File(Environment.getExternalStorageDirectory(), activity.getString(R.string.app_name_eng));
		if (!file.exists()) {
			if (!file.mkdirs()) {
				LogWrapper.e(TAG, "Create app folder failed.");
				ret = false;
			}
		}
		return ret;
	}

	public static String getAppStorageFolder(Activity activity) {
		return Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.app_name_eng);
	}

	public static String getDownloadableRigVedaURL(Activity activity, String pageUrl, String tabbedUrl) {
		// 아래 형태는 다운로드 가능
		// http://rigvedawiki.net/r1/wiki.php/%EB%9E%8D%ED%8B%B0%EB%AF%B8%EC%8A%A4%ED%8A%B8?action=download&value=1297609867_loptimist_.jpg

		// 아래 형태는 다운로드 불가능. 다운로드 가능한 형태로 만들어주어야 함.
		// http://rigvedawiki.net/r1/pds/1297609867_loptimist_.jpg

		String splittedForCheck[] = tabbedUrl.split("/");
		String lastStringForCheck = splittedForCheck[splittedForCheck.length - 1];

		if (tabbedUrl.startsWith(activity.getString(R.string.url_home_page_pds_with_slash))) {
			if (false == lastStringForCheck.contains(DOWNLOAD_PREFIX)) {
				tabbedUrl = pageUrl + DOWNLOAD_PREFIX + lastStringForCheck;
			}
		}
		return tabbedUrl;
	}

	public static String getSixDigitHexString(int textColor) {
		StringBuilder sb = new StringBuilder();

		String hexString = String.format("%x", textColor);

		int i = 0;
		int size = hexString.length();

		for (i = 0; i < 6 - size; i++) {
			sb.append("0");
		}
		sb.append(hexString);

		return sb.toString();
	}

	// http://stackoverflow.com/questions/2465432/android-webview-completely-clear-the-cache
	// helper method for clearCache() , recursive
	// returns number of deleted files
	public static int clearCacheFolder(final File dir, final int numDays) {

		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {

					// first delete subdirectories recursively
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}

					// then delete the files and subdirectories in this dir
					// only empty directories can be deleted, so subdirs have been done first
					if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				LogWrapper.e(TAG, String.format("Failed to clean the cache, error %s", e.getMessage()));
			}
		}
		return deletedFiles;
	}

	public static String getReverseLink(Context context, String currentURL) {
		String itemName = getItemName(currentURL);
		String reverseURL = context.getString(R.string.url_home_page_with_slash) + itemName + context.getString(R.string.url_backlink_keyword) + itemName;
		return reverseURL;
	}

	private static String getItemName(String currentURL) {
		String[] array = currentURL.split("/");

		return array[array.length - 1];
	}

}