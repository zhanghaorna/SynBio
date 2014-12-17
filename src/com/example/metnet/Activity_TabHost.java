package com.example.metnet;

import com.zhr.synbio.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;

public class Activity_TabHost extends TabActivity {
	private TabHost mTabHost = null;
	private TabWidget mTabWidget = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tabhost);
		mTabHost = this.getTabHost();
		mTabWidget = mTabHost.getTabWidget();
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Search")
				.setContent(new Intent(this, Activity_Search.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Search Path")
				.setContent(new Intent(this, Activity_SearchPath.class)));

	}

}
