package com.zhr.synbio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhr.custom.CustomListAdapter;

public class FragmentResources extends Fragment
{
	private View contextView;
	private String[] titles = {"Groups"};
	private int[] icons = {R.drawable.group};
	private String[] summarys = {};
	

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
       contextView = inflater.inflate(R.layout.fragment_resources, container,false);
       
       ListView listView = (ListView)contextView.findViewById(R.id.fragment_resources_listview);
       CustomListAdapter customListAdapter = new CustomListAdapter(titles, icons, summarys, getActivity());
       
       listView.setAdapter(customListAdapter);
       listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
		    switch (position) {
			case 0:
				Intent intent = new Intent(getActivity(),ResourceOrGroupActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	   });
       
       
       return contextView;
    }

}
