package com.example.test;

/**
 * Created by aher on 7/30/2014.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.test.library.DatabaseHandler;
import com.example.test.library.JSONParser;
import com.google.android.gcm.GCMRegistrar;

// static import
import static com.example.test.CommonUtilities.displayMessage;

public class UserFunctions {

	private JSONParser jsonParser;

	/* web URL */
	private static String loginURL = "http://localhost/Online_Polling_App/"; 
	/* local host URL 
	private static String loginURL = "http://10.0.2.2/Online_Polling_App/";*/
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String get_all_admin_tag = "getAllAdmin";
	private static String subscribe_user_tag = "subscribeUser";
	private static String get_user_to_approve_tag = "getUserToApprove";
	private static String approve_user_tag = "approveUser";
	private static String add_pole_tag = "addPole";
	private static String poles_by_user_tag = "getAllPoleByUser";
	private static String poles_by_pole_id_tag = "getPoleByPoleId";
	private static String post_pole_result_tag = "postPoleResult";
	private static String get_pole_result_tag = "getPoleResult";
	private static String get_pole_list_by_subscribed_user_id_tag = "getPolesBySubscribedUserId";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * function make Login Request
	 * 
	 * @param context
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(Context context, String name, String email,
			String password, String userType, String regId) {
		Log.d("USER_TYPE", userType);
		// Building Parameters

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("userType", userType));
		params.add(new BasicNameValuePair("regId", regId));

		displayMessage(context, context.getString(R.string.server_registering));
		GCMRegistrar.setRegisteredOnServer(context, true);
		String message = context.getString(R.string.server_registered);
		displayMessage(context, message);
		
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

	/**
	 * Get all admin user
	 * 
	 * @return jsonObject
	 * */
	public JSONObject getAllAdminUser(String uId) {
		Log.d("GET_ALL_ADMIN", "Get all admin called");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_all_admin_tag));
		params.add(new BasicNameValuePair("uId", uId));
		
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Subscribe user by subscriber
	 * 
	 * @param userId
	 * @param toSubscribeUserId
	 * @return
	 */
	public JSONObject subscribeUser(String userId, String toSubscribeUserId) {
		Log.d("GET_ALL_ADMIN", "Get all admin called");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", subscribe_user_tag));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("toSubscribeUserId",
				toSubscribeUserId));
		// getting JSON Object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Get all user s who need to approve or disapprove
	 * 
	 * @param uId
	 * @return JSOnObject json
	 */
	public JSONObject getUsersToApprove(String uId) {
		Log.d("GET_USER_TO_APPROVE", "Get all user to approve");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_user_to_approve_tag));
		params.add(new BasicNameValuePair("uId", uId));
		// getting JSON Object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Approve users
	 * 
	 * @param userId
	 * @param subscriberId
	 * @return JSOnObject json
	 */
	public JSONObject approveUsers(String userId, String subscriberId) {
		Log.d("APPROVE_USERS", "Approve users called");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", approve_user_tag));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("subscriberId", subscriberId));
		// getting JSON Object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Add pole by user id
	 * 
	 * @param params
	 * @param userId
	 * @return
	 */
	public JSONObject addPole(List<NameValuePair> params, String userId) {
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("tag", add_pole_tag));

		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * Get poles of user
	 * 
	 * @param userId
	 * @return
	 */
	public JSONObject getPoleByUserId(String userId) {
		Log.d("GET_POLE_BY_USER_ID", "Get pole by user id called");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", poles_by_user_tag));
		params.add(new BasicNameValuePair("userId", userId));
		// getting JSON object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	public JSONObject getPoleByPoleId(String poleId, String userId) {
		Log.d("GET_POLE_BY_POLE_ID", "Get pole by pole id called" + poleId);
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", poles_by_pole_id_tag));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("poleId", poleId));
		// getting JSON object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	/**
	 * post pole result.
	 * 
	 * @param poleId
	 * @param selectedOptionId
	 * @param userId
	 * @return
	 */
	public JSONObject postPoleResult(String poleId, String selectedOptionId,
			String userId) {
		Log.d("POST_POLE_RESULT", "Post pole result");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("tag", post_pole_result_tag));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("selectedOptionId", selectedOptionId));
		params.add(new BasicNameValuePair("poleId", poleId));
		// getting JSON object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	public JSONObject getPoleResultByPoleId(String poleId) {
		Log.d("GET_POLE_RESULT", "Get pole result");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_pole_result_tag));
		params.add(new BasicNameValuePair("poleId", poleId));
		// getting JSON object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}

	public JSONObject getPoleListBySubscribedUserId(String userId) {
		Log.d("GET_POLE_LIST_BY_SUBSCRIBED_ID",
				"Get pole list by subscribed user id");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",
				get_pole_list_by_subscribed_user_id_tag));
		params.add(new BasicNameValuePair("subscribedUserId", userId));
		// getting JSON object
		PostDataTask task = new PostDataTask();
		task.setParams(params);
		task.setUrl(loginURL);
		
		JSONObject resultJson = null;
		try {
			resultJson =  task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return json
		return resultJson ;
	}
	
	private class PostDataTask extends AsyncTask<Void,Void, JSONObject> {

		private String url = null;
		private List<NameValuePair> params = null;
		
		public void setUrl(String url) {
			this.url = url;
		}

		public void setParams(List<NameValuePair> params) {
			this.params = params;
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			return jsonParser.getJSONFromUrl(this.url, this.params);
		}
		
	}
}
