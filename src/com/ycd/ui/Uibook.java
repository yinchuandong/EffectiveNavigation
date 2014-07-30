package com.ycd.ui;

import com.xjl.effectivenavigation.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Uibook extends Fragment{
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_layout, container,
				false);
		Button openBtn = (Button) rootView.findViewById(R.id.open);
		openBtn.setText("uifriend");
		return rootView;
	}

}
