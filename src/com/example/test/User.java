package com.example.test;

/**
 * Created by aher on 8/29/2014.
 */
public class User {
	public int subscribeIcon;
	public String userName;
	public String userEmail;
	public String uId;

	public int getSubscribeIcon() {
		return subscribeIcon;
	}

	public void setSubscribeIcon(int subscribeIcon) {
		this.subscribeIcon = subscribeIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	// Constructor.
	public User(int subscribeIcon, String userName, String userEmail, String uId) {

		this.subscribeIcon = subscribeIcon;
		this.userName = userName;
		this.userEmail = userEmail;
		this.uId = uId;
	}
}
