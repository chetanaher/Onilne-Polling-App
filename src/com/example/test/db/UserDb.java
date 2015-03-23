package com.example.test.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.example.test.db.UserDbHelper.*;

public class UserDb {
	// Database fields
	private SQLiteDatabase database;
	private UserDbHelper userDbHelper;

	public UserDb(Context context) {
		userDbHelper = new UserDbHelper(context);
	}

	public void open() throws SQLException {
		database = userDbHelper.getWritableDatabase();
	}

	public void close() {
		userDbHelper.close();
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid,
			String created_at, String user_type) {
		Log.e("USER_ID>>>>>>>>>>", uid);

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At
		values.put(KEY_USER_TYPE, Integer.valueOf(user_type));
		// Inserting Row
		database.insert(TABLE_LOGIN, null, values);

	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		Cursor cursor = database.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
			user.put("user_type", cursor.getString(5));
		}

		cursor.close();

		// return user
		return user;
	}
	
	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;

		Cursor cursor = database.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();

		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		// Delete All Rows
		database.delete(TABLE_LOGIN, null, null);
	}
}
