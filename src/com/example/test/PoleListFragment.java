package com.example.test;

import static com.example.test.MainActivity.KEY_CREATED_AT;
import static com.example.test.MainActivity.KEY_OPTION;
import static com.example.test.MainActivity.KEY_OPTION_ID;
import static com.example.test.MainActivity.KEY_OPTION_TEXT;
import static com.example.test.MainActivity.KEY_POLES;
import static com.example.test.MainActivity.KEY_QUESTION;
import static com.example.test.MainActivity.KEY_QUESTION_ID;
import static com.example.test.MainActivity.KEY_QUESTION_TEXT;
import static com.example.test.MainActivity.KEY_UID;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.test.Adapters.PoleAdapter;
import com.example.test.db.UserDetailPref;

public class PoleListFragment extends Fragment implements View.OnClickListener {

	Communicator comm;
	ListView lvPoles;
	ArrayList<Pole> poleArrayList = new ArrayList<Pole>();
	JSONObject poles;
	JSONObject poleQuestion, optionData;
	public List<NameValuePair> options, optionsSelectedList;
	String questionId, questionText, optionId, optionText, createdAt;
	JSONArray poleOption;
	NameValuePair optionSelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// String value = getArguments().getString("YourKey");
		// System.out.println(value);
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_pole_list, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
		poles = comm.getPoles();

		try {
			JSONArray poleData = poles.getJSONArray(KEY_POLES);

			for (int i = 0; i < poleData.length(); i++) {
				JSONObject pole = poleData.getJSONObject(i);
				poleQuestion = pole.getJSONObject(KEY_QUESTION);
				questionId = poleQuestion.getString(KEY_QUESTION_ID);
				questionText = poleQuestion.getString(KEY_QUESTION_TEXT);
				createdAt = poleQuestion.getString(KEY_CREATED_AT);
				Log.d("QUESTION_ID + QUESTION_TEXT ", questionId + " "
						+ questionText);
				poleOption = pole.getJSONArray(KEY_OPTION);
				options = new ArrayList<NameValuePair>();

				for (int j = 0; j < poleOption.length(); j++) {
					optionData = poleOption.getJSONObject(j);
					optionId = optionData.getString(KEY_OPTION_ID);
					optionText = optionData.getString(KEY_OPTION_TEXT);
					Log.e("OPTION_ID + OPTION_TEXT ", optionId + " "
							+ optionText);
					options.add(new BasicNameValuePair(optionId, optionText));
				}
				UserDetailPref userDetailPref = new UserDetailPref(
						getActivity());
				String uniqueId = userDetailPref.getSharedPrefByKey(KEY_UID);
				// DatabaseHandler db = new DatabaseHandler(getActivity());
				// HashMap<String, String> userData = db.getUserDetails();
				// String uniqueId = userData.get(KEY_UID);
				Pole poleObject = new Pole(questionId, questionText, uniqueId,
						options, createdAt);
				poleArrayList.add(poleObject);
				options = null;
			}

			lvPoles = (ListView) getActivity().findViewById(R.id.lvPoleList);
			lvPoles.setAdapter(new PoleAdapter(poleArrayList,
					R.layout.list_view_pole_list, getActivity()));
			lvPoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					optionsSelectedList = new ArrayList<NameValuePair>();
					Pole singlePole = poleArrayList.get(position);
					String questionText = singlePole.getQuestion();
					optionsSelectedList = singlePole.getOptions();
					String optionMessage = "";

					for (int i = 0; i < optionsSelectedList.size(); i++) {
						NameValuePair optionSelected = optionsSelectedList
								.get(i);
						optionMessage = optionMessage + Integer.valueOf(i + 1)
								+ " : " + optionSelected.getValue();
						if (i < (optionsSelectedList.size() - 1)) {
							optionMessage = optionMessage + "\n";
						}
						Log.e("OPTION_ID_AND_OPTION_TEXT",
								optionSelected.getName() + " "
										+ optionSelected.getValue());
					}

					LayoutInflater inflater = (LayoutInflater) getActivity()
							.getSystemService(
									getActivity().LAYOUT_INFLATER_SERVICE);
					View dialogLayout = inflater.inflate(R.layout.pop_up,
							parent, false);

					TextView tvPopUpQuestionText = (TextView) dialogLayout
							.findViewById(R.id.tvPopUpQuestion);
					TextView tvPopUpOptionText = (TextView) dialogLayout
							.findViewById(R.id.tvPopUpOption);
					Button ok = (Button) dialogLayout.findViewById(R.id.btnOk);

					tvPopUpQuestionText.setText(questionText);
					tvPopUpOptionText.setText(optionMessage);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setView(dialogLayout);
					final AlertDialog dialogBox = builder.show();
					ok.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dialogBox.dismiss();
						}
					});
				}
			});

			super.onActivityCreated(savedInstanceState);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {

	}
}
