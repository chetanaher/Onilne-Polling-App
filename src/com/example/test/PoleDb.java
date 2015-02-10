package com.example.test;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PoleDb extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// pole table name
	private static final String TABLE_POLE = "pole";

	private static final String KEY_POLE_QUESTION = "pole_question";
	private static final String KEY_ID = "pole_id";

	/**
	 * Constructor for pole db
	 * 
	 * @param context
	 */
	public PoleDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_POLE + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_POLE_QUESTION + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLE);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Add Pole to data base.
	 * 
	 * @param poleId
	 * @param poleText
	 */
	public void addPoleToDb(String poleId, String poleText) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, Integer.valueOf(poleId)); // pole id
		values.put(KEY_POLE_QUESTION, poleText); // pole question
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+values.toString());
		// Inserting Row
		db.insert(TABLE_POLE, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Get pole.
	 * 
	 * @param poleId
	 * @return
	 */
	public HashMap<String, String> getPoleByPoleId(String poleId) {
		HashMap<String, String> pole = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_POLE + " WHERE " + KEY_ID + " = ?";
		SQLiteDatabase db = this.getReadableDatabase();
		String args[] = {poleId};
		
 		Cursor cursor = db.rawQuery(selectQuery, args);
		// Move to first row
		cursor.moveToFirst();
		System.out.println(cursor.getCount()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		if (cursor.getCount() > 0) {
			pole.put(KEY_POLE_QUESTION, cursor.getString(1));
		}

		cursor.close();
		db.close();
		// return user
		return pole;
	}
}
