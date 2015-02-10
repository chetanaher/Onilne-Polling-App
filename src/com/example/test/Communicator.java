package com.example.test;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public interface Communicator {
	/**
	 * Change activity by button click
	 * 
	 * @param btnLinkToLoginScreen
	 */
	public void changeActivity(int fragmentType);

	/**
	 * Check the login of User.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public JSONObject login(String email, String password);

	/**
	 * Register User by its name, email, password.
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @return JSONObject
	 */
	public JSONObject register(String name, String email, String password,
			String userType);

	/**
	 * Get all user from database.
	 * 
	 * @param uId
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject getAllAdminUsers(String uId) throws JSONException;

	/**
	 * Subscribe User to specific admin
	 * 
	 * @param toSubscribeUserId
	 * @return
	 */
	public JSONObject subscribe(String toSubscribeUserId);

	/**
	 * Get list of user remain for approval
	 * 
	 * @param uId
	 * @return
	 */
	public JSONObject getUsersToApprove(String uId);

	/**
	 * Approve users
	 * 
	 * @param subscriberId
	 * @return JSONObject json
	 */
	public JSONObject approveUsers(String subscriberId);

	/**
	 * Add pole by admin user.
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject addPole(List<NameValuePair> params);

	/**
	 * Get poles by user id
	 * 
	 * @return
	 */
	public JSONObject getPoles();
}
