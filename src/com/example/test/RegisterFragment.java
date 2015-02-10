package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterFragment extends Fragment implements View.OnClickListener {
	EditText etRegisterName, etRegisterEmail, etRegisterPassword;
	Button btnRegister;
	CheckBox chkbxIsAdmin;
	Communicator comm;
	String name, email, password, userType;

	static final int REGISTER_BUTTON = R.id.btnRegister;
	static final String ADMIN = "1";
	static final String POLLER = "0";

	public RegisterFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_register, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
		initialiseElements();
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.e("REGISTRATION_CLICKED", "REGISTRATION_CLICKED");
		switch (v.getId()) {
		case R.id.btnRegister:
			userType = POLLER;
			name = etRegisterName.getText().toString();
			email = etRegisterEmail.getText().toString();
			if (chkbxIsAdmin.isChecked()) {
				userType = ADMIN;
			}
			password = etRegisterPassword.getText().toString();
			comm.register(name, email, password, userType);
			break;
		}
	}

	/**
	 * Initializes all UI elements
	 */
	private void initialiseElements() {
		etRegisterEmail = (EditText) getActivity().findViewById(
				R.id.etRegisterEmail);
		etRegisterName = (EditText) getActivity().findViewById(R.id.eTName);
		etRegisterPassword = (EditText) getActivity().findViewById(
				R.id.etRegisterPassword);
		btnRegister = (Button) getActivity().findViewById(R.id.btnRegister);
		chkbxIsAdmin = (CheckBox) getActivity().findViewById(R.id.chkbxIsAdmin);
	}

	public void addMessage(String message) {
		etRegisterEmail.setText(message);
	}
}
