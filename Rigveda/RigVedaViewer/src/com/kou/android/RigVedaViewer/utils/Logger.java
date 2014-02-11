package com.kou.android.RigVedaViewer.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

/**
 * Logger
 * 
 * android.util.Log의 wrapper
 * 
 */
public class Logger {
	private Logger() {
	}

	/**
	 * 로그를 전혀 출력하지 않음
	 */
	public static final int NONE = 7;

	/**
	 * 모든 로그에 대해 출력 (기본값)
	 */
	public static final int ALL = 0;

	public static final String TIMETAG = "TIME";
	public static final String TAG = "RIGVEDAVIEWER";
	public static final String ARTICLEREADER_TAG = "READER";

	public static int logLevel = Logger.NONE;

	/**
	 * Logger 클래스를 이용하여 찍을 로그의 등급을 결정 ERROR(6) > WARN(5) > INFO(4) > DEBUG(3) > VERBOSE(2)
	 * 
	 * @param logLevel
	 *            새로운 로그 레벨, WARN(5)으로 설정하면 WARN이상의 로그만 출력
	 */
	public static void setLogLevel(int logLevel) {
		if (logLevel >= Logger.ALL && logLevel <= Logger.NONE) {
			Logger.logLevel = logLevel;
		}
	}

	public static void e(String tag, String message, Throwable tr) {
		if (Logger.logLevel <= Log.ERROR) {

			Log.e(tag, getMessageWithCaller(message), tr);
		}
	}

	public static void e(String tag, String message) {
		if (Logger.logLevel <= Log.ERROR) {

			Log.e(tag, getMessageWithCaller(message));
		}
	}

	public static void w(String tag, String message) {
		if (Logger.logLevel <= Log.WARN) {
			Log.w(tag, getMessageWithCaller(message));
		}
	}

	public static void i(String tag, String message, Throwable tr) {
		if (Logger.logLevel <= Log.INFO) {
			Log.i(tag, getMessageWithCaller(message), tr);
		}
	}

	public static void i(String tag, String message) {
		if (Logger.logLevel <= Log.INFO) {
			Log.i(tag, getMessageWithCaller(message));
		}
	}

	public static void d(String tag, String message) {
		if (Logger.logLevel <= Log.DEBUG) {
			Log.d(tag, getMessageWithCaller(message));
		}
	}

	public static void d(String tag, String message, Object... params) {
		if (Logger.logLevel <= Log.DEBUG) {
			if (params != null && params.length > 0) {
				message = String.format(message, params);
			}
			Log.d(tag, getMessageWithCaller(message));
		}
	}

	public static void v(String tag, String message) {
		if (Logger.logLevel <= Log.VERBOSE) {
			Log.v(tag, getMessageWithCaller(message));
		}
	}

	private static String getMessageWithCaller(String message) {

		Exception exception = new Exception();

		if (exception.getStackTrace() != null && exception.getStackTrace().length >= 2) {
			StackTraceElement callerElement = exception.getStackTrace()[2];
			return new StringBuilder("(").append(callerElement.getFileName()).append(" ").append(callerElement.getLineNumber()).append(") ").append(message).toString();
		} else {
			return message;
		}
	}

	public static String getStringFromThrowable(Throwable throwable) {
		if (throwable == null) {
			return "";
		}

		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));

		return sw.getBuffer().toString();
	}
}
