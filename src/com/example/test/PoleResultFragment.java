package com.example.test;

//static import
import static com.example.test.MainActivity.KEY_ERROR_MSG;
import static com.example.test.MainActivity.KEY_OPTION_ID;
import static com.example.test.MainActivity.KEY_OPTION_TEXT;
import static com.example.test.MainActivity.KEY_POLE_OPTION_COUNT;
import static com.example.test.MainActivity.KEY_POLE_OPTION_RESULT;
import static com.example.test.MainActivity.KEY_POLE_RESULT;
import static com.example.test.MainActivity.KEY_POLE_RESULT_PERCENTAGE;
import static com.example.test.MainActivity.KEY_POLE_TOTAL_OPTION_COUNT;
import static com.example.test.MainActivity.KEY_QUESTION_ID;
import static com.example.test.MainActivity.KEY_SUCCESS;
import static com.example.test.PoleDb.KEY_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PoleResultFragment extends Fragment {
	Random r = new Random();
	double[] pieChartValues = { 10, 15, 15, 40, 20 };
	ArrayList<PoleResult> poleResultList;
	public static final String TYPE = "type";
	private CategorySeries mSeries = new CategorySeries("");
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView;
	Communicator comm;
	PoleDb poleDb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		poleDb = new PoleDb(getActivity());
		HashMap<String, String> poleNameId = poleDb.getPoleLastInsertedPole();
		String poleId = poleNameId.get(KEY_ID);
		Log.d("POLE_RESULT_POLE_ID", poleId);
		comm = (Communicator) getActivity();
		poleResultList = new ArrayList<PoleResult>();
		JSONObject poleResult = comm.getPoleResult(poleId);
		extractPoleResult(poleResult);

		String message = null;
		try {
			if (poleResult.getString(KEY_SUCCESS) != null) {
				String res = poleResult.getString(KEY_SUCCESS);

				if (Integer.parseInt(res) == 1) {
					message = "Pole result displayed successfully";
				} else {
					message = poleResult.getString(KEY_ERROR_MSG);
				}
			}
		} catch (NumberFormatException | JSONException e) {
			e.printStackTrace();
		}

		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

		// Inflate the layout for this fragment
		return inflater
				.inflate(R.layout.fragment_pole_result, container, false);
	}

	private void extractPoleResult(JSONObject poleResultData) {

		String poleTotalOptionsCount = "0";
		String poleId = null;
		try {
			JSONObject poleResultDetails = poleResultData
					.getJSONObject(KEY_POLE_RESULT);

			poleTotalOptionsCount = poleResultDetails
					.getString(KEY_POLE_TOTAL_OPTION_COUNT);
			poleId = poleResultDetails.getString(KEY_QUESTION_ID);
			JSONArray poleOptionResult = poleResultDetails
					.getJSONArray(KEY_POLE_OPTION_RESULT);
			for (int i = 0; i < poleOptionResult.length(); i++) {
				JSONObject optionResultObject = poleOptionResult
						.getJSONObject(i);

				PoleResult poleResultObject = new PoleResult(
						optionResultObject.getString(KEY_OPTION_ID),
						optionResultObject.getString(KEY_POLE_OPTION_COUNT),
						optionResultObject
								.getString(KEY_POLE_RESULT_PERCENTAGE),
						optionResultObject.getString(KEY_OPTION_TEXT));

				poleResultList.add(poleResultObject);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setChartTitleTextSize(15);
		mRenderer.setLabelsTextSize(12);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLegendTextSize(10);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(90);

		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) getActivity().findViewById(
					R.id.chart);
			mChartView = ChartFactory.getPieChartView(getActivity(), mSeries,
					mRenderer);
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
		fillPieChart();
	}

	/**
	 * Fill pie chart for pie chart values
	 */
	public void fillPieChart() {

		for (PoleResult poleResult : poleResultList) {
			String optionResultCount = poleResult.getOptionCount();
			mSeries.add(
					poleResult.getOptionText() + "("
							+ poleResult.getOptionResultPercentage() + "%)",
					Double.valueOf(optionResultCount));

			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(getRandomColors());
			mRenderer.addSeriesRenderer(renderer);
			if (mChartView != null)
				mChartView.repaint();
		}
	}

	/**
	 * Get random colors for graph
	 * 
	 * @return
	 */
	public int getRandomColors() {

		int alpha = r.nextInt(130);
		int red = r.nextInt(100);
		int green = r.nextInt(150);
		int blue = r.nextInt(120);

		return Color.argb(alpha, red, green, blue);
	}
	
	
}
