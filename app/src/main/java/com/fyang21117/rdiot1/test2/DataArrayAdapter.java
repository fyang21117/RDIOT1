package com.fyang21117.rdiot1.test2;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DataArrayAdapter extends ArrayAdapter<List<String>> {

	public DataArrayAdapter(Context context, int textViewResourceId, List<List<String>> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		List<String> dataList = getItem(position);
		LinearLayout layout;
		if (convertView == null) {
			layout = new LinearLayout(getContext());
		} else {
			layout = (LinearLayout) convertView;
		}
		layout.removeAllViews();
		int width = Utility.dp2px(getContext(), 100);
		int height = Utility.dp2px(getContext(), 30);
		for (String data : dataList) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
			TextView textView = new TextView(getContext());
			textView.setText(data);
			textView.setSingleLine(true);
			textView.setEllipsize(TruncateAt.END);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			layout.addView(textView, params);
		}
		return layout;
	}

}