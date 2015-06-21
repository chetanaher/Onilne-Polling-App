package com.example.test;

import static com.example.test.MainActivity.KEY_ADMINS;
import static com.example.test.MainActivity.KEY_EMAIL;
import static com.example.test.MainActivity.KEY_NAME;
import static com.example.test.MainActivity.KEY_UID;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.Adapters.SubscribeAdapter;
import com.example.test.db.UserDetailPref;

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
			UserDetailPref userDetailPref = new UserDetailPref(getActivity());
			String uniqueId = userDetailPref.getSharedPrefByKey(KEY_UID);
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
			Toast.makeText(getActivity(),
					"User not exists to exists to subscribe",
					Toast.LENGTH_SHORT).show();
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
