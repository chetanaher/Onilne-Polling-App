package com.example.test.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Communicator;
import com.example.test.R;
import com.example.test.User;

import java.util.ArrayList;

/**
 * Created by aher on 8/29/2014.
 */
public class SubscribeAdapter extends BaseAdapter {
	private ArrayList<User> _data;
	Context _c;
	int _layoutResourceId;
	Communicator comm;

	public SubscribeAdapter(ArrayList<User> data, int layoutResourceId,
			Context c) {
		_data = data;
		_c = c;
		_layoutResourceId = layoutResourceId;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		UserHolder holder = null;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) _c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_view_item_user, null);
			holder = new UserHolder();
			holder.tvUsername = (TextView) v.findViewById(R.id.tvUsername);
			holder.tvEmail = (TextView) v.findViewById(R.id.tvUserEmail);
			holder.btnSubscribe = (Button) v.findViewById(R.id.btnSubscribe);
			v.setTag(holder);
		} else {
			holder = (UserHolder) v.getTag();
		}

		final User user = _data.get(position);
		holder.tvUsername.setText(user.getUserName());
		holder.tvEmail.setText(user.getUserEmail());
		holder.subscriberUId = user.getuId();
		final String toSubscribeUserId = user.getuId();
		holder.btnSubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("SUBSCRIBER_ID", toSubscribeUserId);
				comm = (Communicator) _c;
				comm.subscribe(toSubscribeUserId);
			}
		});

		return v;
	}

	static class UserHolder {
		TextView tvUsername;
		TextView tvEmail;
		Button btnSubscribe;
		String subscriberUId;
	}
}
