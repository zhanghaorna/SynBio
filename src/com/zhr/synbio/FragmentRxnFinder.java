package com.zhr.synbio;

import java.util.ArrayList;
import java.util.List;

import com.zhr.custom.AdvertiseViewPager;
import com.zhr.custom.CustomListAdapter;
















import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class FragmentRxnFinder extends Fragment
{
	private View contextView;
	private String[] titles = {"   RxnFinder","   ModNet3D"};
	private int[] icons = {R.drawable.rxnfinder_listview_rxnfinder,R.drawable.rxnfinder_listview_modnet3d};
	private String[] summarys = {};

	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
       contextView = inflater.inflate(R.layout.fragment_rxnfinder, container,false);
       
       ListView listView = (ListView)contextView.findViewById(R.id.fragment_rxnfiner_listview);
       CustomListAdapter customListAdapter = new CustomListAdapter(titles, icons, summarys, getActivity());
       listView.setAdapter(customListAdapter);
       listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			Intent intent = null;
			switch (arg2) {
			case 0:
				intent = new Intent(getActivity(),RxnfinderActivity.class);

				break;
			case 1:
				intent = new Intent(getActivity(), ModNet3DActivity.class);

			default:
				break;
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
	    });
       

        
        
      
       
        return contextView;
    }
	

	

}
