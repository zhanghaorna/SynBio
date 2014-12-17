package com.zhr.synbio;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class WebSettingPreferenceActivity extends PreferenceActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
		addPreferencesFromResource(R.xml.websetting_preferences);
//		Log.d("Networkong", "oncreateafter");
		
	}
	
	public void onResume()
	{
		super.onResume();

	}
	

}
