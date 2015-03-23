package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


import static com.example.test.MainActivity.*;

@SuppressWarnings("ALL")
public class AddPoleFragment extends Fragment implements View.OnClickListener {

	LinearLayout linearLayout;
	EditText etPoleQuestion;
	Button btnAddOption, btnRemoveOption, btnAddPole;
	int optionCount = 0;
	public static int optionMinLimit = 2;
	int optionId = 1000;
	int optionBase = 1000;
	Activity currentActivity;
	List<NameValuePair> AddPoleParams = new ArrayList<NameValuePair>();
	Communicator comm;
	JSONObject poleDataJson;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_pole, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialiseElements();
		comm = (Communicator) getActivity();
		btnAddOption.setOnClickListener(this);
		btnRemoveOption.setOnClickListener(this);
		btnAddPole.setOnClickListener(this);
		addEditText();
		addEditText();
	}

	/**
	 * Initialize all elements.
	 */
	private void initialiseElements() {
		currentActivity = getActivity();
		linearLayout = (LinearLayout) currentActivity
				.findViewById(R.id.layoutEt);
		btnAddOption = (Button) currentActivity.findViewById(R.id.btnAddOption);
		btnRemoveOption = (Button) currentActivity
				.findViewById(R.id.btnRemoveOption);
		btnAddPole = (Button) currentActivity.findViewById(R.id.btnAddPole);
		etPoleQuestion = (EditText) currentActivity
				.findViewById(R.id.etPoleQuestion);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddOption:
			addEditText();
			break;
		case R.id.btnRemoveOption:
			removeEditText(optionId);
			break;
		case R.id.btnAddPole:
			addPole();
			break;
		}
	}

	/**
	 * Add pole from admin user
	 */
	private void addPole() {
		String poleQuestion, optionText;
		poleQuestion = etPoleQuestion.getText().toString();

		AddPoleParams.add((new BasicNameValuePair("optionCount", Integer
				.toString(optionCount))));
		AddPoleParams
				.add((new BasicNameValuePair("poleQuestion", poleQuestion)));
		for (int i = (optionBase + 1), j = 1; i <= (optionBase + optionCount); i++, j++) {

			EditText etOption = (EditText) currentActivity.findViewById(i);
			optionText = etOption.getText().toString();
			Log.d("OPTION_TEXT", optionText);
			Log.d("OTION_ID", Integer.toString(i));
			AddPoleParams.add((new BasicNameValuePair("option_"
					+ Integer.toString(j), optionText)));
		}
		poleDataJson = comm.addPole(AddPoleParams);
		String message = "";
		try {
			if (poleDataJson.getString(KEY_SUCCESS) != null) {
				String res = poleDataJson.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1 || Integer.parseInt(res) == 3) {
					message = "Pole Added Successfully";
					//comm.changeActivity(FRAGMENT_POLE_LIST);
				} else {
					message = poleDataJson.getString(KEY_ERROR_MSG);
				}
			}
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add edit text dynamically.
	 */
	private void addEditText() {
		optionCount++;
		optionId++;
		EditText etOption = new EditText(getActivity());
		etOption.setId(optionId);
		etOption.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int marginLeftTopRight = (int) getResources().getDimensionPixelSize(
				R.dimen.input_left_right_margin);
		llp.setMargins(marginLeftTopRight, marginLeftTopRight,
				marginLeftTopRight, 0);
		etOption.setLayoutParams(llp);
		etOption.setHint("Option " + Integer.toString(optionCount));

		linearLayout.addView(etOption);
	}

	/**
	 * Remove last added option
	 * 
	 * @param idToRemove
	 */
	private void removeEditText(int idToRemove) {
		if (optionCount > optionMinLimit) {
			Log.d("ID_TO_REMOVE", Integer.toString(idToRemove));
			EditText etOptionToRemove = (EditText) getActivity().findViewById(
					idToRemove);
			linearLayout.removeView(etOptionToRemove);
			optionId--;
			optionCount--;
		} else {
			Toast.makeText(getActivity(), "Atleast two options required",
					Toast.LENGTH_LONG).show();
		}
	}
}
