package com.kou.android.RigVedaViewer.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kou.android.RigVedaViewer.R;

public class DownloadFilesTask extends AsyncTask<String, Void, Long> {

	private Activity activity;
	private String pageUrl;

	public DownloadFilesTask(Activity activity, String pageUrl) {
		this.activity = activity;
		this.pageUrl = pageUrl;
	}

	protected Long doInBackground(String... urls) {
		int count = urls.length;
		long totalSize = 0;
		for (int i = 0; i < count; i++) {
			totalSize += Downloader.downloadFile(activity, pageUrl, urls[i]);
		}
		return totalSize;
	}

	protected void onProgressUpdate() {
	}

	protected void onPostExecute(Long result) {
		Toast.makeText(activity, activity.getString(R.string.dialog_image_save_complete), Toast.LENGTH_SHORT).show();
	}
}