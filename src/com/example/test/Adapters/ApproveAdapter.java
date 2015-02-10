package com.example.test.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.Communicator;
import com.example.test.R;
import com.example.test.User;

import java.util.ArrayList;

/**
 * Created by aher on 9/11/2014.
 */
public class ApproveAdapter extends BaseAdapter {
	private ArrayList<User> _data;
	Context _c;
	int _layoutResourceId;
	Communicator comm;

	public ApproveAdapter(ArrayList<User> data, int layoutResourceId, Context c) {
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
		UserHolderApprove holder = null;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) _c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_view_approve, null);
			holder = new UserHolderApprove();
			holder.tvUsernameApprove = (TextView) v
					.findViewById(R.id.tvUsernameApprove);
			holder.tvEmailApprove = (TextView) v
					.findViewById(R.id.tvUserEmailApprove);
			holder.btnApprove = (Button) v.findViewById(R.id.btnApprove);
			v.setTag(holder);
		} else {
			holder = (UserHolderApprove) v.getTag();
		}

		final User user = _data.get(position);
		holder.tvUsernameApprove.setText(user.getUserName());
		holder.tvEmailApprove.setText(user.getUserEmail());
		holder.userToApprove = user.getuId();
		final String subscriberId = user.getuId();
		holder.btnApprove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("SUBSCRIBER_ID", subscriberId);
				comm = (Communicator) _c;
				comm.approveUsers(subscriberId);
			}
		});

		return v;
	}

	static class UserHolderApprove {
		TextView tvUsernameApprove;
		TextView tvEmailApprove;
		Button btnApprove;
		String userToApprove;
	}
}
