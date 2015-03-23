package com.example.test;

import static com.example.test.MainActivity.*;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UserMenuFragment extends Fragment implements View.OnClickListener {

	Button btnUserSubscribe, btnMenuPoleList;
	Communicator comm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("FRAGMENT", "USERMENU_FRAGMENT");
		View rootView = inflater.inflate(R.layout.fragment_user_menu,
				container, false);

		return rootView;
	}

	private void initialiseElements() {
		comm = (Communicator) getActivity();
		btnUserSubscribe = (Button) getActivity().findViewById(
				R.id.btnMenuSubscribe);
		
		btnMenuPoleList = (Button) getActivity().findViewById(
				R.id.btnMenuPoleList);
		
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
		initialiseElements();
		
		btnUserSubscribe.setOnClickListener(this);
		btnMenuPoleList.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.d("BUTTON", "Button clicked");
		switch (v.getId()) {
		case R.id.btnMenuSubscribe:
			Log.d("APPROVE_CLICKED", "Approve menu clicked");
			comm.changeActivity(FRAGMENT_SUBSCRIBE);
			break;
			
		case R.id.btnMenuPoleList:
			Log.d("APPROVE_CLICKED", "Approve menu clicked");
			comm.changeActivity(FRAGMENT_POLE_LIST);
			break;
		}
	}
}
