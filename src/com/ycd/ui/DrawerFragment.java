/**
 * 
 */
package com.ycd.ui;

import com.xjl.effectivenavigation.R;
import com.ycd.service.ApkService;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author BillKalin
 * 
 */
public class DrawerFragment extends Fragment implements OnClickListener {

	public DrawerLayout layout;
	public View view;
	private Button goBtn;

	public DrawerFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setDrawerLayout(DrawerLayout layout, View view) {
		this.layout = layout;
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
	 * , android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_layout, container,
				false);
		Button openBtn = (Button) rootView.findViewById(R.id.open);
		goBtn = (Button)rootView.findViewById(R.id.go);
		openBtn.setOnClickListener(this);
		goBtn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = getFragmentManager();
		
	}
	
	FragmentManager manager;
	FragmentTransaction transaction;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.open) {
			//´ò¿ª²à±ßÀ¸
			if (!layout.isDrawerOpen(view)) {
				layout.openDrawer(view);
			}
		}else if (v.getId() == R.id.go) {
//			transaction = manager.beginTransaction();
//			transaction.addToBackStack("uifragment");
//			transaction.replace(R.id.main_content, new Uibook());
//			transaction.commit();
			
			Intent intent = new Intent(getActivity(), ApkService.class);
			getActivity().startService(intent);
			
		}
	}
}
