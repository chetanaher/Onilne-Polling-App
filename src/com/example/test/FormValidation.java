package com.example.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

/**
 * Provides common validation class
 * 
 * @author aher
 * 
 */
public class FormValidation {

	public static final int EMAIL = 1;
	public static final int NAME = 2;
	public static final int PASSWORD = 3;

	/**
	 * Validate edit text and set error message.
	 * 
	 * @param editText
	 * @param type
	 * @return
	 */
	public Boolean validateByName(EditText editText, int type) {
		String valueTovalidate = editText.getText().toString();
		Boolean isValid = true;
		switch (type) {
		case EMAIL:
			if (!isValidEmail(valueTovalidate)) {
				setRegBgError("Invalid Email", editText);
				isValid = false;
			}
			break;
		case NAME:
			if (!isValidName(valueTovalidate)) {
				setRegBgError("Invalid Name", editText);
				isValid = false;
			}
			break;
		case PASSWORD:
			if (!isValidPassword(valueTovalidate)) {
				setRegBgError("Invalid Password", editText);
				isValid = false;
			}
			break;
		}

		return isValid;
	}

	/**
	 * Set background color for error message
	 * 
	 * @param errorMessage
	 * @param errorEditText
	 */
	private void setRegBgError(String errorMessage, EditText errorEditText) {
		int ecolor = Color.RED; // whatever color you want
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
				errorMessage);
		ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
		errorEditText.setError(ssbuilder);
	}

	/**
	 * Validates email
	 * 
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Validates password
	 * 
	 * @param pass
	 * @return
	 */
	private boolean isValidPassword(String pass) {
		if (pass != null && pass.length() > 6) {
			return true;
		}
		return false;
	}

	/**
	 * Validate name or username
	 * 
	 * @param pass
	 * @return
	 */
	private boolean isValidName(String pass) {
		if (pass != null && pass.length() > 3) {
			return true;
		}
		return false;
	}
}
