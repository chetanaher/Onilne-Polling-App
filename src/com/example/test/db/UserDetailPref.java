package com.example.test.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserDetailPref {
	private static String SHARED_PREF_NAME = "loginPrefs";

	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_UID = "uid";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_USER_TYPE = "user_type";

	private static int PRIVATE_MODE = 0;
	private SharedPreferences pref;

	public UserDetailPref(Context context) {
		pref = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
	}

	/**
	 * Store user details to shared preference.
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param userType
	 * @param uId
	 * @param createdAt
	 */
	public void addUserToSharedPref(String name, String email,
			String password, String userType, String uId, String createdAt) {
		Editor editor = pref.edit();

		editor.putString(KEY_NAME, name);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_PASSWORD, password);
		editor.putString(KEY_USER_TYPE, userType);
		editor.putString(KEY_UID, uId);
		editor.putString(KEY_CREATED_AT, createdAt);

		editor.commit();
	}

	/**
	 * Remove user details from shared preference.
	 */
	public void removeUserFromSharedPref() {
		Editor editor = pref.edit();

		editor.remove(KEY_NAME);
		editor.remove(KEY_EMAIL);
		editor.remove(KEY_PASSWORD);
		editor.remove(KEY_USER_TYPE);
		editor.remove(KEY_UID);
		editor.remove(KEY_CREATED_AT);

		editor.commit();
	}

	/**
	 * Get value or null if shared preference exists.
	 * 
	 * @param key
	 * @return
	 */
	public String getSharedPrefByKey(String key) {
		String value = pref.getString(key, null);

		return value;
	}
}
