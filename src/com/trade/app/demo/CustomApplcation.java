package com.trade.app.demo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CustomApplcation extends Application{
	public static CustomApplcation mInstance;
	
	public static CustomApplcation getInstance() {
		return mInstance;
	}
	private static final String PREF_NICK = "nick";
	private String nick = null;

	private static final String PREF_SEX = "sex";
	private boolean sex;
	
	/**
	 * 获取性别
	 * 
	 * @return
	 */
//	public String getGender() {
//		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		sex = preferences.getBoolean(PREF_SEX, true);
//		return sex;
//	}

	/**
	 * 
	 * @param pwd
	 */
//	public void setGender(String gender) {
//		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		SharedPreferences.Editor editor = preferences.edit();
//		if (editor.putString(PREF_SEX, true).commit()) {
//			sex = gender;
//		}
//	}
}
