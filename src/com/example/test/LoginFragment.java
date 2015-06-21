package com.example.test;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

// static import
import static com.example.test.MainActivity.FRAGMENT_MENU_ADMIN;
import static com.example.test.MainActivity.FRAGMENT_MENU_USER;
import static com.example.test.MainActivity.FRAGMENT_REGISTER;
import static com.example.test.MainActivity.KEY_SUCCESS;
import static com.example.test.MainActivity.KEY_USER;
import static com.example.test.MainActivity.KEY_USER_TYPE;
import static com.example.test.MainActivity.USER_TYPE_ADMIN;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

	Button btnLinkToRegisterScreen, btnLogin;
	EditText etLoginEmail, etLoginPassword;
	Communicator comm;
	FormValidation formValidation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		if (savedInstanceState != null) {
			String mTagObjectId = this.getArguments().getString("edttext");
			Log.d("mTagObjectId", mTagObjectId);
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialiseElements();
		btnLinkToRegisterScreen.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			Log.d("BUTTON_LOGIN_CLICKED", "BUTTON_LOGIN_CLICKED");
			String email = etLoginEmail.getText().toString();
			String password = etLoginPassword.getText().toString();
			if (formValidation.validateByName(etLoginEmail, FormValidation.EMAIL) == false) {
				break;
			}
			
			Log.d("LOGIN_CALLED", "Login function called");
			JSONObject json = comm.login(email, password);

			String res = null;
			String user = null;
			JSONObject userDetail = null;
			String userType = null;
			try {
				res = json.getString(KEY_SUCCESS);
				user = json.getString(KEY_USER);
				userDetail = new JSONObject(user);
				userType = userDetail.getString(KEY_USER_TYPE);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (Integer.parseInt(res) == 1) {
				if (Integer.parseInt(userType) == USER_TYPE_ADMIN) {
					comm.changeActivity(FRAGMENT_MENU_ADMIN);
				} else {
					comm.changeActivity(FRAGMENT_MENU_USER);
				}
			}
			break;
		case R.id.btnLinkToRegisterScreen:
			Log.d("OPEN_REGISTER_SCREEN", "OPEN REGESTRATION SCREEN");
			comm.changeActivity(FRAGMENT_REGISTER);
			break;
		}
	}

	/**
	 * Initializes all UI elements
	 */
	private void initialiseElements() {
		comm = (Communicator) getActivity();
		formValidation = new FormValidation();
		btnLinkToRegisterScreen = (Button) getActivity().findViewById(
				R.id.btnLinkToRegisterScreen);
		btnLogin = (Button) getActivity().findViewById(R.id.btnLogin);
		etLoginEmail = (EditText) getActivity().findViewById(R.id.etEmail);
		etLoginPassword = (EditText) getActivity()
				.findViewById(R.id.etPassword);
	}
}
