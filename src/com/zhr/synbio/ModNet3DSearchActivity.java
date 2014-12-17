package com.zhr.synbio;

import java.util.ArrayList;
import java.util.List;

import com.example.metnet.Activity_ReactionLayer;
import com.threed.jpct.SimpleVector;

import database.DBManager;
import database.Pathway;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ModNet3DSearchActivity extends Activity{
	private Spinner categorySpinner;
	private Spinner resultSpinner;
	private String[] categoryStrings;
	private ArrayAdapter<String> dBadAdapter;
	private ModNet3DSearchActivity modNet3DSearchActivity;
	private Button searchButton;
	private ListView pathwayListView;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modnet3d_search);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		modNet3DSearchActivity = this;

		categorySpinner = (Spinner)findViewById(R.id.modnet3d_search_category_spinner);
		resultSpinner = (Spinner)findViewById(R.id.modnet3d_search_result_spinner);
		searchButton = (Button)findViewById(R.id.modnet3d_search_button);
		pathwayListView = (ListView)findViewById(R.id.modnet3d_pathwaty_listview);
		categoryStrings = getResources().getStringArray(R.array.ecoli3d_category);
		
		ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categoryStrings);
		category_adapter.setDropDownViewResource(R.xml.spinner_property);
	    categorySpinner.setAdapter(category_adapter);
	    categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO 自动生成的方法存根
				DBManager dbManager = new DBManager(modNet3DSearchActivity);
				int position = categorySpinner.getSelectedItemPosition();
				switch (position) {
				case 0:
					List<String> pathwayID = dbManager.queryforpathwayID();
					
					dBadAdapter = new ArrayAdapter<String>(modNet3DSearchActivity, android.R.layout.simple_spinner_item,pathwayID);
					dBadAdapter.setDropDownViewResource(R.xml.spinner_property);
					resultSpinner.setAdapter(dBadAdapter);
					break;
				case 1:
					List<String> pathwayName = dbManager.QueryForPathwayName();
					dBadAdapter = new ArrayAdapter<String>(modNet3DSearchActivity, android.R.layout.simple_spinner_item,pathwayName);
					resultSpinner.setAdapter(dBadAdapter);
					break;
				case 2:
					List<String> CompoundID = dbManager.QueryForCompoundId();
					dBadAdapter = new ArrayAdapter<String>(modNet3DSearchActivity, android.R.layout.simple_spinner_item,CompoundID);
					resultSpinner.setAdapter(dBadAdapter);
					break;
				case 3:
					List<String> CompoundName = dbManager.QueryForCompoundName();
					dBadAdapter = new ArrayAdapter<String>(modNet3DSearchActivity, android.R.layout.simple_spinner_item,CompoundName);
					resultSpinner.setAdapter(dBadAdapter);
					break;

				default:
					break;
				}
				dbManager.closeDB();
				
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO 自动生成的方法存根
				
			}
		});
	    
	    
	    searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO 自动生成的方法存根
				Pathway mPathway = new Pathway();
				List<String> listStrings = new ArrayList<String>();
				DBManager db = new DBManager(modNet3DSearchActivity);
				
				switch (categorySpinner.getSelectedItemPosition()) 
				{
				case 0:
					
					mPathway = db.QueryforPathwayByUid(resultSpinner.getSelectedItem().toString().trim());
					break;
				case 1:
					mPathway = db.QueryforPathwayByName(resultSpinner.getSelectedItem().toString().trim());
					break;
				case 2:
					
					listStrings = db.QueryForPathwayNameByCompoundID(resultSpinner.getSelectedItem().toString().trim());
					
					pathwayListView.setAdapter(new ArrayAdapter<String>(modNet3DSearchActivity,android.R.layout.simple_list_item_1,listStrings));
					
				
					break;
				case 3:
					listStrings = db.QueryForPathwayNameByCompoundName(resultSpinner.getSelectedItem().toString().trim());
					
					pathwayListView.setAdapter(new ArrayAdapter<String>(modNet3DSearchActivity,android.R.layout.simple_list_item_1,listStrings));
				
					break;

				default:
					break;
				}
				db.closeDB();
				if(categorySpinner.getSelectedItemPosition() < 2)
				{
					Intent intent = new Intent(modNet3DSearchActivity,Activity_ReactionLayer.class);
			    	SimpleVector pos=new SimpleVector(mPathway.x,mPathway.y,mPathway.z);
					Bundle b=new Bundle();
					b.putInt("pid",mPathway.pid);
					b.putSerializable("pos",pos);
					b.putString("name",mPathway.name);
					intent.putExtras(b);
					startActivity(intent);
				}
				
				
			}
		});
	    
	    pathwayListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				
				DBManager db = new DBManager(modNet3DSearchActivity);
				
				String pathwayname = arg0.getItemAtPosition(arg2).toString();
				
				Pathway mPathway = new Pathway();
				mPathway = db.QueryforPathwayByName(pathwayname);
				
				SimpleVector pos=new SimpleVector(mPathway.x,mPathway.y,mPathway.z);
				Bundle b=new Bundle();
				b.putInt("pid",mPathway.pid);
				b.putSerializable("pos",pos);
				b.putString("name",mPathway.name);
				Intent intent = new Intent(modNet3DSearchActivity,Activity_ReactionLayer.class);
				intent.putExtras(b);
				startActivity(intent);
				
			}
		});
	    
		
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
            finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
