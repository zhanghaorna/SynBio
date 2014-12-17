package com.zhr.synbio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ModNet3DActivity extends Activity
{
	private ModNet3DActivity modNet3DActivity;
	private GridView gridView;
	private LinearLayout progressLayout;
	Integer[] imageIDs = {R.drawable.ecoli};
	private Handler mHandler = new Handler();
	private Runnable mFinishload = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			progressLayout.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
		}
	};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modnet3d);
		
		modNet3DActivity = this;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		progressLayout = (LinearLayout)findViewById(R.id.modnet3d_progressLayout);
		gridView = (GridView)findViewById(R.id.modnet3d_gridview);
		
		gridView.setAdapter(new ImageAdapter(this));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				Intent intent = null;
				switch (arg2) {
				case 0:
					intent = new Intent(modNet3DActivity,ModNet3DSearchActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
				
			}
		});
		
		mHandler.postDelayed(mFinishload, 2000);
		
	}
	
	public class ImageAdapter extends BaseAdapter
	{
		private Context context;
		public ImageAdapter(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return imageIDs.length;
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
			ImageView imageView;
			if(convertView == null)
			{
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			}
			else {
				imageView = (ImageView)convertView;
			}
			imageView.setImageResource(imageIDs[position]);
			
			
			return imageView;
		}
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
