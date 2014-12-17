package com.example.metnet;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.SimpleVector;

import database.DBHelper;
import database.DBManager;

import android.R.layout;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Activity_Search extends ListActivity {
	
	private static List<String> mList=new ArrayList<String>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		DBManager db=new DBManager(this);
		mList=db.QueryForPathwayName();
		db.closeDB();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,mList));
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		database.Pathway mPathway=null;
		DBManager db=new DBManager(this);
		mPathway=db.QueryForPathwayByIndex(position+1);
		db.closeDB();
		int pid=mPathway.pid;
		SimpleVector pos=new SimpleVector(mPathway.x,mPathway.y,mPathway.z);
		Intent mIntent=new Intent(this,Activity_ReactionLayer.class); 
		Bundle b=new Bundle();
		b.putInt("pid",pid);
		b.putSerializable("pos",pos);
		b.putString("name",mPathway.name );
		mIntent.putExtras(b);
		startActivity(mIntent);
	}
	
}
