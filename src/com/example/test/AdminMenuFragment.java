package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.test.MainActivity.*;

public class AdminMenuFragment extends Fragment implements View.OnClickListener {
	Button btnApproveUserMenu, btnAddPoleMenu;
	Communicator comm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_admin_menu, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
		initialiseElements();
		btnAddPoleMenu.setOnClickListener(this);
		btnApproveUserMenu.setOnClickListener(this);
	}

	/**
	 * Initialize UI elements
	 */
	private void initialiseElements() {
		btnApproveUserMenu = (Button) getActivity().findViewById(
				R.id.btnMenuApprove);
		btnAddPoleMenu = (Button) getActivity().findViewById(
				R.id.btnMenuAddPole);
	}

	@Override
	public void onClick(View v) {
		Log.d("BUTTON", "Button clicked");
		switch (v.getId()) {
		case R.id.btnMenuApprove:
			Log.d("APPROVE_CLICKED", "Approve menu clicked");
			comm.changeActivity(FRAGMENT_APPROVE);
			break;
		case R.id.btnMenuAddPole:
			Log.d("APPROVE_CLICKED", "Approve menu clicked");
			comm.changeActivity(FRAGMENT_ADD_POLE);
			break;
		}
	}
}
