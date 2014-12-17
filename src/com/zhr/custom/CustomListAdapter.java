package com.zhr.custom;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhr.synbio.R;

public class CustomListAdapter extends BaseAdapter
{
	
	private String[] titles;
	private int[] icons;
	private String[] summarys;
	private Context context;
	
	
	public CustomListAdapter(String[] titles,int[] icons,String[] summarys,Context context)
	{
		// TODO 自动生成的构造函数存根
		this.titles = titles;
		this.icons = icons;
		this.summarys = summarys;
		this.context = context;
	
	}


	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View list = layoutInflater.inflate(R.layout.fragment_more_information_custom_listview, null);
		ImageView imageView = (ImageView)list.findViewById(R.id.listview_icon);
		if(icons.length <= position)
		{
			imageView.setVisibility(View.INVISIBLE);
			
		}
	    else {
	    	
			imageView.setImageResource(icons[position]);
		}
		TextView title = (TextView)list.findViewById(R.id.listview_title);
		title.setText(titles[position]);
		TextView summary = (TextView)list.findViewById(R.id.listview_summary);
		if(summarys.length <= position)
		{
			summary.setVisibility(View.INVISIBLE);
			
			
		}
		else
		{
			
			summary.setText(summarys[position]);
		}
	
		
		return list;
	}
	
}
