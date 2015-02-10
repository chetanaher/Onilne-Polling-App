package com.example.test;

import static com.example.test.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.test.CommonUtilities.EXTRA_MESSAGE;
import static com.example.test.CommonUtilities.SENDER_ID;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.example.test.library.AlertDialogManager;
import com.example.test.library.ConnectionDetector;
import com.example.test.library.DatabaseHandler;
import com.google.android.gcm.GCMRegistrar;

//Static imports

public class MainActivity extends FragmentActivity implements Communicator {

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	static final int FRAGMENT_REGISTER = 0;
	static final int FRAGMENT_LOGIN = 1;
	static final int FRAGMENT_SUBSCRIBE = 2;
	static final int FRAGMENT_MENU_ADMIN = 3;
	static final int FRAGMENT_ADD_POLE = 4;
	static final int FRAGMENT_APPROVE = 5;
	static final int FRAGMENT_POLE_LIST = 6;
	static final int FRAGMENT_MENU_USER = 7;

	static final int USER_TYPE_ADMIN = 1;
	static final int USER_TYPE_VOTER = 0;

	UserFunctions userFunction;
	DatabaseHandler db;

	// JSON Response node names
	public static String KEY_ADMINS = "admins";
	public static String KEY_SUCCESS = "success";
	public static String KEY_ERROR = "error";
	public static String KEY_ERROR_MSG = "error_msg";
	public static String KEY_UID = "uid";
	public static String KEY_NAME = "name";
	public static String KEY_EMAIL = "email";
	public static String KEY_CREATED_AT = "created_at";
	public static String KEY_USER_TYPE = "user_type";
	public static String KEY_USER = "user";
	public static String KEY_APPROVE_USERS = "to_approve_users";
	public static String KEY_POLES = "poles";
	public static String KEY_QUESTION_ID = "question_id";
	public static String KEY_QUESTION = "question";
	public static String KEY_OPTION_ID = "option_id";
	public static String KEY_OPTION = "option";
	public static String KEY_QUESTION_TEXT = "question_text";
	public static String KEY_OPTION_TEXT = "option_text";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		userFunction = new UserFunctions();
		db = new DatabaseHandler(getApplicationContext());
		if (savedInstanceState == null) {
			if (checkInternetConnection()) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.add(R.id.fragment_container,
						new LoginFragment(), "LOGIN");
				fragmentTransaction.commit();
			}
		} else {
			Bundle extras = getIntent().getExtras();
			Log.e("THIS_IS_RECEIVED_MAIN_ACTIVITY",
					extras.getString("testmessagee"));
		}
	}

	@Override
	public void changeActivity(int fragmentType) {

		if (checkInternetConnection()) {
			Log.d("CHANGE_ACTIVITY", "Change activity");
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			switch (fragmentType) {
			case FRAGMENT_LOGIN:
				Log.e("DEPLOY_MESSAGE", "WE ARE IN TEST MESSAGE");
				break;
			case FRAGMENT_REGISTER:
				RegisterFragment registerFragment = new RegisterFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						registerFragment, "REGISTER");
				break;
			case FRAGMENT_SUBSCRIBE:
				SubscribeFragment subscribeFragment = new SubscribeFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						subscribeFragment, "SUBSCRIBE");
				break;
			case FRAGMENT_APPROVE:
				ApproveFragment approveFragment = new ApproveFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						approveFragment, "APPROVE");
				break;
			case FRAGMENT_MENU_ADMIN:
				AdminMenuFragment adminMenuFragment = new AdminMenuFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						adminMenuFragment, "ADMIN_MENU");
				break;
			case FRAGMENT_ADD_POLE:
				AddPoleFragment addPoleFragment = new AddPoleFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						addPoleFragment, "ADD_POLE");
				break;
			case FRAGMENT_POLE_LIST:
				PoleListFragment poleListFragment = new PoleListFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						poleListFragment, "POLE_LIST_FRAGMENT");
				break;
			case FRAGMENT_MENU_USER:
				UserMenuFragment userMenuFragment = new UserMenuFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						userMenuFragment, "USER_MENU");
				break;
			}

			fragmentTransaction.commit();
		}
	}

	@Override
	public JSONObject login(String email, String password) {
		JSONObject json = userFunction.loginUser(email, password);

		try {
			if (json.getString(KEY_SUCCESS) != null) {

				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					// user successfully logged in
					// Store user details in SQLite Database
					JSONObject json_user = json.getJSONObject("user");

					// Clear all previous data in database
					userFunction.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_NAME),
							json_user.getString(KEY_EMAIL),
							json.getString(KEY_UID),
							json_user.getString(KEY_CREATED_AT),
							json_user.getString(KEY_USER_TYPE));
					Log.d("USER_LOGEDIN_SUCCEESSFULY",
							"User loged in successfuly");
				} else {
					// Error in login
					Log.d("INVALID_USER", "Invalid User Id/Password");
					Toast.makeText(getApplicationContext(),
							"Invalid User Id/Password", Toast.LENGTH_LONG)
							.show();
					return json;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public JSONObject register(final String name, final String email,
			final String password, final String userType) {
		Log.d("REGISTER", "REGISTER");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		GCMRegistrar.checkManifest(this);
		Log.e("REGISTER1", "REGISTER1");

		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("REG_ID", regId);

		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Newly registerd with GCM", Toast.LENGTH_LONG).show();
			}

			final Context context = this;

			Log.e("MESSAGES", "name:::::" + name + "email:::::" + email
					+ "pass::::" + password + "usertype::::" + userType
					+ "regId::" + regId);
			JSONObject json = userFunction.registerUser(context, name, email,
					password, userType, regId);
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully registred
						// Store user details in SQLite Database
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT),
								json_user.getString(KEY_USER_TYPE));
						// Launch Dashboard Screen
					} else {
						// Error in registration
						Toast.makeText(getApplicationContext(),
								"Unable To Register", Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;

		}
		return null;
	}

	@Override
	public JSONObject getAllAdminUsers(String uId) throws JSONException {
		JSONObject json = userFunction.getAllAdminUser(uId);
		return json;
	}

	@Override
	public JSONObject subscribe(String toSubscriberUserId) {
		HashMap<String, String> userData = db.getUserDetails();
		String userId = userData.get(KEY_UID);
		Log.d("USER_ID", userId);
		Log.d("TO_SUBSCRIBE_USER_ID", toSubscriberUserId);
		JSONObject json = userFunction
				.subscribeUser(userId, toSubscriberUserId);
		String message = "";

		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					message = "Subscribed Successfully";
				} else {
					message = json.getString(KEY_ERROR_MSG);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();

		return json;
	}

	@Override
	public JSONObject getUsersToApprove(String uId) {
		JSONObject json = userFunction.getUsersToApprove(uId);
		return json;
	}

	@Override
	public JSONObject approveUsers(String subscriberId) {
		HashMap<String, String> userData = db.getUserDetails();
		String userId = userData.get(KEY_UID);
		JSONObject json = userFunction.approveUsers(userId, subscriberId);
		String message = "";
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					message = "Approved Successfully";
				} else {
					message = json.getString(KEY_ERROR_MSG);
				}
			}
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
					.show();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public JSONObject addPole(List<NameValuePair> AddPoleParams) {
		HashMap<String, String> userData = db.getUserDetails();
		String userId = userData.get(KEY_UID);
		JSONObject json = userFunction.addPole(AddPoleParams, userId);

		return json;
	}

	@Override
	public JSONObject getPoles() {
		HashMap<String, String> userData = db.getUserDetails();
		String userId = userData.get(KEY_UID);
		JSONObject json = userFunction.getPoleByUserId(userId);
		return json;
	}

	/**
	 * Check Internet connection available or not.
	 * 
	 * @return
	 */
	protected boolean checkInternetConnection() {
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Receiving push messages
	 * 
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("MESAGE_RECIEVED", "MESSAGE+RECIEVED");
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Log.e("MY_MESASGE",
			// intent.getExtras().getString("testmessagee"));
			Toast.makeText(context, newMessage, Toast.LENGTH_LONG).show();
		}
	};

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(getApplicationContext());
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
}
