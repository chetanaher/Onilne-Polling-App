package com.example.test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.test.Communicator;
import com.example.test.Pole;
import com.example.test.R;
import java.util.ArrayList;

/**
 * Created by Aher on 9/11/2014.
 */
public class PoleAdapter extends BaseAdapter {
	private ArrayList<Pole> _data;
	Context _c;
	int _layoutResourceId;
	Communicator comm;

	public PoleAdapter(ArrayList<Pole> data, int layoutResourceId, Context c) {
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
		PoleHolder holder = null;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) _c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_view_pole_list, null);
			holder = new PoleHolder();
			holder.tvPoleQuestion = (TextView) v
					.findViewById(R.id.tvPoleQuestion);
			holder.tvCreatedAt = (TextView) v.findViewById(R.id.tvCreatedAt);
			v.setTag(holder);
		} else {
			holder = (PoleHolder) v.getTag();
		}

		final Pole pole = _data.get(position);
		holder.tvPoleQuestion.setText(pole.getQuestion());
		holder.tvCreatedAt.setText(pole.getCreatedAt());
		holder.questionId = pole.getQuestionId();
		return v;
	}

	static class PoleHolder {
		TextView tvPoleQuestion;
		TextView tvCreatedAt;
		String questionId;
	}
}