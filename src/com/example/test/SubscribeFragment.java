package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.test.Adapters.SubscribeAdapter;
import com.example.test.library.DatabaseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.test.MainActivity.*;

/**
 * Activities that contain this fragment must implement the create an instance
 * of this fragment.
 * 
 */
public class SubscribeFragment extends Fragment {

	Communicator comm;
	ListView lvUsersToSubscribe;
	ArrayList<User> userArrayList = new ArrayList<User>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_subscribe, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
		try {
			DatabaseHandler db = new DatabaseHandler(getActivity());
			HashMap<String, String> userData = db.getUserDetails();
			String uniqueId = userData.get(KEY_UID);
			JSONObject json = comm.getAllAdminUsers(uniqueId);
			JSONArray adminUser = json.getJSONArray(KEY_ADMINS);

			for (int i = 0; i < adminUser.length(); i++) {
				JSONObject admin = adminUser.getJSONObject(i);
				User user = new User(R.id.btnSubscribe,
						admin.getString(KEY_NAME), admin.getString(KEY_EMAIL),
						admin.getString(KEY_UID));
				userArrayList.add(user);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		lvUsersToSubscribe = (ListView) getActivity().findViewById(
				R.id.lvUsersToSubscribe);
		lvUsersToSubscribe.setAdapter(new SubscribeAdapter(userArrayList,
				R.layout.list_view_item_user, getActivity()));
		lvUsersToSubscribe
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
	}
}
