package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.test.Adapters.ApproveAdapter;
import com.example.test.library.DatabaseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.test.MainActivity.*;

/**
 * create an instance of this fragment.
 */
public class ApproveFragment extends Fragment {

	Communicator comm;
	ListView lvUsersToApprove;

	ArrayList<User> userArrayList = new ArrayList<User>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_approve, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		comm = (Communicator) getActivity();
		try {
			DatabaseHandler db = new DatabaseHandler(getActivity());
			HashMap<String, String> userData = db.getUserDetails();
			String uniqueId = userData.get(KEY_UID);
			JSONObject json = comm.getUsersToApprove(uniqueId);
			JSONArray adminUser = json.getJSONArray(KEY_APPROVE_USERS);

			for (int i = 0; i < adminUser.length(); i++) {
				JSONObject admin = adminUser.getJSONObject(i);
				User user = new User(R.id.btnApprove,
						admin.getString(KEY_NAME), admin.getString(KEY_EMAIL),
						admin.getString(KEY_UID));
				userArrayList.add(user);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		lvUsersToApprove = (ListView) getActivity().findViewById(
				R.id.lvUsersToApprove);
		lvUsersToApprove.setAdapter(new ApproveAdapter(userArrayList,
				R.layout.list_view_approve, getActivity()));
		super.onActivityCreated(savedInstanceState);
	}
}
