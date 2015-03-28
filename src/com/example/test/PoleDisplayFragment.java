package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.test.MainActivity.*;
import static com.example.test.PoleDb.KEY_ID;


public class PoleDisplayFragment extends Fragment implements
		View.OnClickListener {

	Communicator comm;
	LinearLayout linearLayout;
	Activity currentActivity;
	String poleId = null;
	RadioGroup radioGroup;
	PoleDb poleDb;
	RadioButton radioButton;
	TextView tvQuestion;
	String questionId = null;
	String questionText = null;
	JSONArray poleOption;
	JSONObject poleQuestion = null;
	ArrayList<NameValuePair> options;
	JSONObject optionData;
	String optionId;
	String optionText;
	JSONArray poleData = null;
	Button btnPostPoleResult;
	JSONObject poleResponse;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_display_pole, container,
				false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initialiseElements();
		linearLayout.removeAllViews();
		HashMap<String, String> poleNameId = poleDb.getPoleLastInsertedPole();
		poleId = poleNameId.get(KEY_ID);
		
		JSONObject poleResponse = comm.getPoleByPoleId(poleId);
		extractPoleRespones(poleResponse);

		linearLayout.addView(radioGroup);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPostResult:

			int selectedOptionInt = radioGroup.getCheckedRadioButtonId();
			String selectedOptionString = String.valueOf(selectedOptionInt);
			poleResponse = comm
					.postPoleResult(questionId, selectedOptionString);
			poleDb.removeAllPole();
			break;
		}
	}

	/**
	 * Extract pole response.
	 * 
	 * @param poleResponse
	 */
	private void extractPoleRespones(JSONObject poleResponse) {
		try {
			poleData = poleResponse.getJSONArray(KEY_POLES);

			for (int i = 0; i < poleData.length(); i++) {
				JSONObject pole = null;
				pole = poleData.getJSONObject(i);
				poleQuestion = pole.getJSONObject(KEY_QUESTION);
				questionId = poleQuestion.getString(KEY_QUESTION_ID);
				questionText = poleQuestion.getString(KEY_QUESTION_TEXT);
				tvQuestion.setText(questionText);
				poleOption = pole.getJSONArray(KEY_OPTION);
				options = new ArrayList<NameValuePair>();
				Log.d("QUESTION_ID + QUESTION_TEXT ", questionId + " "
						+ questionText);
				for (int j = 0; j < poleOption.length(); j++) {
					optionData = poleOption.getJSONObject(j);
					optionId = optionData.getString(KEY_OPTION_ID);
					optionText = optionData.getString(KEY_OPTION_TEXT);
					Log.d("OPTION_ID + OPTION_TEXT ", optionId + " "
							+ optionText);
					addOptions(optionId, optionText);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initialize all objects
	 */
	private void initialiseElements() {
		currentActivity = getActivity();
		tvQuestion = (TextView) currentActivity
				.findViewById(R.id.tvPoleQuestion);
		linearLayout = (LinearLayout) currentActivity
				.findViewById(R.id.layoutOptions);
		radioGroup = new RadioGroup(getActivity());
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		comm = (Communicator) currentActivity;
		poleDb = new PoleDb(currentActivity.getApplicationContext());
		btnPostPoleResult = (Button) currentActivity
				.findViewById(R.id.btnPostResult);
		btnPostPoleResult.setOnClickListener(this);
	}

	/**
	 * Add options dynamically.
	 * 
	 * @param optionId
	 * @param optionText
	 */
	private void addOptions(String optionId, String optionText) {
		radioButton = new RadioButton(getActivity());
		radioButton.setText(optionText);
		radioButton.setId(Integer.valueOf(optionId));
		radioGroup.addView(radioButton);
	}
}
