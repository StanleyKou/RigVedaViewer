package com.kou.android.RigVedaViewer.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
				Logger.d(TAG, "isInstalled : true");
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
			Logger.e(TAG, e.toString());
		}
		return versionName;
	}

	public static void installShortcut(Context context) {
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN, null);
		shortcutIntent.setClass(context, SplashActivity.class);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon_launcher_rigveda));
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra("duplicate", false);
		context.sendBroadcast(intent);
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
}
