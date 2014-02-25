package com.kou.android.RigVedaViewer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;

public class Downloader {

	
	public static long downloadFile(Activity activity, String pageUrl, String tabbedUrl) {
		long totalBytes = 0;
		try {
			tabbedUrl = Utils.getDownloadableRigVedaURL(activity, pageUrl, tabbedUrl);
			
			String splitted[] = tabbedUrl.split("/");
			String lastString = splitted[splitted.length - 1];

			URL url = new URL(tabbedUrl);
			InputStream is = url.openStream();
			File dir = new File(Utils.getAppStorageFolder(activity));

			File file = new File(dir, lastString);
			FileOutputStream fos = new FileOutputStream(file);
			try {

				byte[] buffer = new byte[1024];

				int bytesRead = 0;
				while ((bytesRead = is.read(buffer, 0, buffer.length)) >= 0) {
					fos.write(buffer, 0, bytesRead);
					totalBytes += bytesRead;
				}
			} finally {
				is.close();
				fos.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return totalBytes;
	}

	

}