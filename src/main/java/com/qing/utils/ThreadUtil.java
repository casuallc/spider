package com.qing.utils;

public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
