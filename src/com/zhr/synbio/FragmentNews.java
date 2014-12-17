package com.zhr.synbio;

import com.zhr.custom.CustomListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentNews extends Fragment
{
	private View contextView;
	private String[] titles = {" 科研"," 会议"," 产品"};
	private int[] icons = {R.drawable.scientific_research,R.drawable.meeting,R.drawable.product};
	private String[] summarys = {};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
       contextView = inflater.inflate(R.layout.fragment_news, container,false);
       
       ListView listView = (ListView)contextView.findViewById(R.id.fragment_news_listview);
       CustomListAdapter customListAdapter = new CustomListAdapter(titles, icons, summarys, getActivity());
       
       listView.setAdapter(customListAdapter);
       listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			Intent intent = new Intent(getActivity(),DetailNewsActivity.class);
			Bundle bundle = new Bundle();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			bundle.putInt("category", arg2);
			intent.putExtras(bundle);
			startActivity(intent);
			
			
		}
	   });
       
       
       return contextView;
    }

}
