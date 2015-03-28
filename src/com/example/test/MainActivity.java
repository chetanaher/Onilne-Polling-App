package com.example.test;

//Static imports
import static com.example.test.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.test.CommonUtilities.EXTRA_MESSAGE;
import static com.example.test.CommonUtilities.SENDER_ID;
import static com.example.test.CommonUtilities.checkInternetConnection;
import static com.example.test.db.UserDetailPref.KEY_PASSWORD;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.test.db.UserDetailPref;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends FragmentActivity implements Communicator {

	static final int FRAGMENT_REGISTER = 0;
	static final int FRAGMENT_LOGIN = 1;
	static final int FRAGMENT_SUBSCRIBE = 2;
	static final int FRAGMENT_MENU_ADMIN = 3;
	static final int FRAGMENT_ADD_POLE = 4;
	static final int FRAGMENT_APPROVE = 5;
	static final int FRAGMENT_POLE_LIST = 6;
	static final int FRAGMENT_MENU_USER = 7;
	static final int FRAGMENT_POLE_DISPLAY = 8;
	static final int FRAGMENT_POLE_RESULT = 9;
	// user types
	static final int USER_TYPE_ADMIN = 1;
	static final int USER_TYPE_VOTER = 0;
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
	public static String KEY_POLE_RESULT = "pole_result";
	public static String KEY_POLE_TOTAL_OPTION_COUNT = "total_option_count";
	public static String KEY_POLE_OPTION_RESULT = "option_result";
	public static String KEY_POLE_OPTION_COUNT = "option_count";
	public static String KEY_POLE_RESULT_PERCENTAGE = "option_result_percentage";

	public int dbPoleId;
	public String poleIdResult;
	PoleDb poleDb;
	UserFunctions userFunction;
	UserDetailPref userDetailPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userFunction = new UserFunctions();
		userDetailPref = new UserDetailPref(getApplicationContext());

		if (checkInternetConnection(getApplicationContext(), MainActivity.this)) {
			if (!checkLoginByShraredPrefs()) {
				changeActivity(FRAGMENT_LOGIN);
			}
		}

	}

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

	@Override
	protected void onResume() {
		super.onResume();
		poleDb = new PoleDb(getApplicationContext());
		if (poleDb.checkPoleExists()) {
			changeActivity(FRAGMENT_POLE_DISPLAY);
		}
	}

	@Override
	public void changeActivity(int fragmentType) {

		if (checkInternetConnection(getApplicationContext(), MainActivity.this)) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			switch (fragmentType) {
			case FRAGMENT_LOGIN:
				Log.d("FRAGMENT_LOGIN", "FRAGMENT_LOGIN");
				LoginFragment loginFragment = new LoginFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						loginFragment, "LOGIN");
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
			case FRAGMENT_POLE_DISPLAY:
				PoleDisplayFragment poleDisplayFragment = new PoleDisplayFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						poleDisplayFragment, "POLE_DISPLAY");
				break;
			case FRAGMENT_POLE_RESULT:
				PoleResultFragment poleResultFragment = new PoleResultFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						poleResultFragment, "POLE_RESULT");
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
					String jsonUid = json.getString(KEY_UID);
					JSONObject json_user = json.getJSONObject("user");
					String jsonName = json_user.getString(KEY_NAME);
					String jsonUserType = json_user.getString(KEY_USER_TYPE);
					String jsonCreatedAt = json_user.getString(KEY_CREATED_AT);

					Log.d("LOGIN_STORE_SHARED_PREF", " " + jsonName + " "
							+ email + " " + password + " " + jsonUserType + " "
							+ jsonUid);

					userDetailPref.addUserToSharedPref(jsonName, email,
							password, jsonUserType, jsonUid, jsonCreatedAt);

					Log.d("USER_LOGEDIN_SUCCEESSFULY",
							"User loged in successfully");
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

			Log.d("MESSAGES", "name:::::" + name + "email:::::" + email
					+ "pass::::" + password + "usertype::::" + userType
					+ "regId::" + regId);
			JSONObject json = userFunction.registerUser(context, name, email,
					password, userType, regId);
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully registred
						JSONObject json_user = json.getJSONObject("user");

						String jsonName = json_user.getString(KEY_NAME);
						String jsonEmail = json_user.getString(KEY_EMAIL);
						String jsonUid = json.getString(KEY_UID);
						String jsonUserType = json_user
								.getString(KEY_USER_TYPE);
						String jsonCreateAt = json_user
								.getString(KEY_CREATED_AT);

						userDetailPref.addUserToSharedPref(jsonName, jsonEmail,
								password, jsonUserType, jsonUid, jsonCreateAt);
						changeActivity(FRAGMENT_LOGIN);
						// Clear all previous data in database
						userFunction.logoutUser(getApplicationContext());
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
		String userId = userDetailPref.getSharedPrefByKey(KEY_UID);
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
		String userId = userDetailPref.getSharedPrefByKey(KEY_UID);
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
		// userDb.open();
		// HashMap<String, String> userData = userDb.getUserDetails();
		// userDb.close();
		String userId = userDetailPref.getSharedPrefByKey(KEY_UID);
		// String userId = userData.get(KEY_UID);
		JSONObject json = userFunction.addPole(AddPoleParams, userId);

		return json;
	}

	@Override
	public JSONObject getPoles() {
		String userId = userDetailPref.getSharedPrefByKey(KEY_UID);
		String userType = userDetailPref.getSharedPrefByKey(KEY_USER_TYPE);
		JSONObject json = null;
		Log.d("GET_POLE_BY_USER_ID", userId);
		if (Integer.parseInt(userType) == USER_TYPE_ADMIN) {
			json = userFunction.getPoleByUserId(userId);
		} else {
			json = userFunction.getPoleListBySubscribedUserId(userId);
		}

		return json;
	}

	@Override
	public JSONObject getPoleByPoleId(String poleId) {
		return userFunction.getPoleByPoleId(poleId);
	}

	@Override
	public JSONObject postPoleResult(String poleId, String optionId) {
		String userId = userDetailPref.getSharedPrefByKey(KEY_UID);
		poleIdResult = poleId;
		Log.d("POLE_RESULT_USER_ID", userId);
		JSONObject jsonResult = userFunction.postPoleResult(poleId, optionId,
				userId);
		String message = null;
		try {
			if (jsonResult.getString(KEY_SUCCESS) != null) {
				String res = jsonResult.getString(KEY_SUCCESS);

				if (Integer.parseInt(res) == 1) {
					message = "Pole result posted successfully";
					changeActivity(FRAGMENT_POLE_RESULT);
				} else {
					message = jsonResult.getString(KEY_ERROR_MSG);
				}
			}
		} catch (NumberFormatException | JSONException e) {
			e.printStackTrace();
		}

		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
		return jsonResult;
	}

	/**
	 * Check login by shared preferences.
	 * 
	 * @return
	 */
	private boolean checkLoginByShraredPrefs() {
		Log.d("IN_CHECK_SHARED_PREF", "IN_CHECK_SHARED_PREF");
		String email = userDetailPref.getSharedPrefByKey(KEY_EMAIL);
		String password = userDetailPref.getSharedPrefByKey(KEY_PASSWORD);

		if (email == null || password == null) {
			return false;
		} else {
			JSONObject json = login(email, password);
			String res = null;
			try {
				res = json.getString(KEY_SUCCESS);

				if (Integer.parseInt(res) == 1) {
					String user = json.getString(KEY_USER);
					JSONObject userDetail = new JSONObject(user);
					String userType = userDetail.getString(KEY_USER_TYPE);

					if (Integer.parseInt(userType) == USER_TYPE_ADMIN) {
						changeActivity(FRAGMENT_MENU_ADMIN);
					} else {
						changeActivity(FRAGMENT_MENU_USER);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (Integer.parseInt(res) == 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public JSONObject getPoleResult(String poleId) {
		return userFunction.getPoleResultByPoleId(poleId);
	}

	@Override
	public JSONObject getPoleListBySubscribedUserId(String subscribedUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Receiving push messages
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("MESAGE_RECIEVED", "MESSAGE+RECIEVED");
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			Toast.makeText(context, newMessage, Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_logout:
			userDetailPref.removeUserFromSharedPref();
			changeActivity(FRAGMENT_LOGIN);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Log.d("BACK_PRESSED", "Back button pressed");
		String userType = userDetailPref.getSharedPrefByKey(KEY_USER_TYPE);
		if (userType != null) {
			if (Integer.parseInt(userType) == USER_TYPE_ADMIN) {
				changeActivity(FRAGMENT_MENU_ADMIN);
			} else {
				changeActivity(FRAGMENT_MENU_USER);
			}
		}	
	}
}
