package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.test.library.AlertDialogManager;
import com.example.test.library.ConnectionDetector;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	// give your server registration url here
	static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php";
	static final String SEND_MESSAGE_URL = "http://10.0.2.2/gcm_server_php/send_message_to_all.php";
	// Google project id
	public static final String SENDER_ID = "342787762982";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "AndroidHive GCM";

	static final String DISPLAY_MESSAGE_ACTION = "com.example.test.DISPLAY_MESSAGE";

	static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param data
	 *            message to be displayed.
	 */
	public static void displayMessage(Context context, String msg) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, msg);
		context.sendBroadcast(intent);
	}
	
	/**
	 * Check Internet connection available or not.
	 * 
	 * @return
	 */
	public static boolean checkInternetConnection(Context context, Activity activity) {
		
		ConnectionDetector cd = new ConnectionDetector(context);
		AlertDialogManager alert = new AlertDialogManager();
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(activity,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return false;
		} else {
			return true;
		}
	}
}
