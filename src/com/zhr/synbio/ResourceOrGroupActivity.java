package com.zhr.synbio;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;






import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResourceOrGroupActivity extends Activity{
	private ViewPager viewPager;
	private ProgressBar progressBar;
	private TextView textView;
	
	private LinearLayout navigatonLayout;
	
	private AsyncHttpClient client;
	private String group_html = "http://www.rxnfinder.org/update/?type=group";
	private ResourceOrGroupActivity resourceOrGroupActivity;
	private ArrayList<String> picturePath = new ArrayList<String>();
	private ArrayList<String> pictureLink = new ArrayList<String>();
	private ArrayList<View> pictureViews = new ArrayList<View>();
	private ArrayList<TextView> navigationView = new ArrayList<TextView>();
	private GroupHandler groupHandler = new GroupHandler();
	private GroupPagerAdapter groupAdapter = new GroupPagerAdapter();
	
	private Set<String> picturenameSet = new HashSet<String>();
	
	private SharedPreferences preferences;
	
	private int pagerCurItem;
	private int prepagerItem;
	private boolean transform = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource_or_group);
		resourceOrGroupActivity = this;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		client = new AsyncHttpClient();
		
		preferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference),0);
		
		viewPager = (ViewPager)findViewById(R.id.resource_group_viewpager);
		progressBar = (ProgressBar)findViewById(R.id.resource_group_progressbar);
		textView = (TextView)findViewById(R.id.resource_group_networknull_textview);
		navigatonLayout = (LinearLayout)findViewById(R.id.resource_group_navigation);
		viewPager.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		textView.setVisibility(View.GONE);
		navigatonLayout.setVisibility(View.GONE);
		
		viewPager.setAdapter(groupAdapter);

		
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CommonUtil.isConnectionAvailable(resourceOrGroupActivity))
				{
					queryPictureFormServer();
				}
				else
				{
					Toast.makeText(resourceOrGroupActivity, "网络不给力", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(transform)
				{
//					Log.d("change1", ""+arg0);
					transform = false;
					return;
					
				}
				pagerCurItem = arg0;
//				Log.d("change", ""+arg0);

				TextView textView = navigationView.get((pagerCurItem - 1 + navigationView.size()) % navigationView.size());
				textView.setTextColor(getResources().getColor(R.color.blue));
				
				textView = navigationView.get((prepagerItem - 1 + navigationView.size()) % navigationView.size());
				textView.setTextColor(getResources().getColor(R.color.white));
				prepagerItem = arg0;
				

			
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == 0)
				{
					if(pagerCurItem == 0)
					{
						transform = true;
						viewPager.setCurrentItem(pictureViews.size() - 2,false);
						
					}
					else if(pagerCurItem == pictureViews.size() - 1)
					{
						transform = true;
						viewPager.setCurrentItem(1,false);
						
					}

				}
			}
		});
		
		
//		preferences.edit().putStringSet("group_picture", null).commit();
		
		picturenameSet = preferences.getStringSet("group_picture", null);
		
		if(picturenameSet == null&&!CommonUtil.isConnectionAvailable(resourceOrGroupActivity))
		{
//			Toast.makeText(resourceOrGroupActivity, "网络不给力", Toast.LENGTH_LONG).show();
			viewPager.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);

			Log.d("group", "1");
			
		}
		else if(!CommonUtil.isConnectionAvailable(resourceOrGroupActivity))
		{
//			Toast.makeText(resourceOrGroupActivity, "2 size:" + picturenameSet.size(), Toast.LENGTH_LONG).show();
			Log.d("group", "2");
			
			if(LoadFromLocal())
			   showViewpagerDirectly();
			else
			{
				Toast.makeText(resourceOrGroupActivity, "文件已被删除，请重新从网上获取", Toast.LENGTH_LONG).show();
				viewPager.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				textView.setVisibility(View.VISIBLE);
			}
		}
		else 
		{
//			Toast.makeText(resourceOrGroupActivity, "3", Toast.LENGTH_LONG).show();
			picturenameSet = new HashSet<String>();
			Log.d("group", "3");
			queryPictureFormServer();
		}
		

		
		
	}
	
	private boolean LoadFromLocal() {
		// TODO Auto-generated method stub
		for(int i = 0;i < picturenameSet.size();i++)
		{
			pictureLink.add("");
			picturePath.add("");
//			Log.d("pager", "" + picturenameSet.size());
		}
		
		Iterator<String> iterator = picturenameSet.iterator();
		while(iterator.hasNext())
		{
			String picture = iterator.next();
			String[] params = picture.split("@@");
//			Log.d("pager", params[0]);
//			Log.d("pager", params[1]);
//			Log.d("pager", params[2]);
			pictureLink.set(Integer.parseInt(params[2]),params[1]);
			picturePath.set(Integer.parseInt(params[2]),params[0]);

		}
		//判断本地文件是否存在，不存在则重新重网上获取，目前是只要有一个不存在就全部从网上获取
		for(int i = 0;i < picturePath.size();i++)
		{
			File file = new File(picturePath.get(i));
			if(!file.exists())
			{
				preferences.edit().putStringSet("group_picture", null).commit();
				return false;
			}
				
		}
		setPictureAndNavigation();
		return true;
	}
	
	private void setPictureAndNavigation()
	{
		for(int i = 0;i < picturePath.size();i++)
		{
			ImageView imageView = new ImageView(resourceOrGroupActivity);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
			
			pictureViews.add(imageView);
			TextView textView = new TextView(resourceOrGroupActivity);
			textView.setLayoutParams(new ViewGroup.LayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
			textView.setText(".");
			textView.setTextSize(20);
			if(i == 0)
				textView.setTextColor(getResources().getColor(R.color.blue));
			else
		     	textView.setTextColor(getResources().getColor(R.color.white));
			navigationView.add(textView);
			navigatonLayout.addView(textView);
		}
		for(int i = 0;i < 2;i++)
		{
			ImageView imageView = new ImageView(resourceOrGroupActivity);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
			pictureViews.add(imageView);
		}
		for(int i = 1;i < pictureViews.size() - 1;i++)
		{
			ImageView imageView = (ImageView) pictureViews.get(i);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if((pictureLink.size() > pagerCurItem - 1)&&!(pictureLink.get(pagerCurItem - 1).equals("")))
					{
						Intent intent = new Intent(resourceOrGroupActivity,SearchResultActivity.class);
						intent.putExtra("group", pictureLink.get(pagerCurItem - 1));
						startActivity(intent);
					}
				}
			});
		}
		
	}

	private void queryPictureFormServer()
	{
		client.get(group_html, new JsonHttpResponseHandler(){
			public void onFailure(Throwable Throwable, JSONObject JSONObject)
			{
				Toast.makeText(resourceOrGroupActivity, "请求服务器失败", Toast.LENGTH_SHORT).show();
			}
			
			public void onSuccess(JSONArray JSONArray)
			{
				//如果服务器每一张图片本地都有，则不用更新
				try 
				{
				   for(int i = 0;i < JSONArray.length();i++)
				   {
					   JSONObject object = JSONArray.getJSONObject(i);
					   pictureLink.add(object.getString("link"));
					   picturePath.add(object.getString("icon"));
					
				   }
				   setPictureAndNavigation();

				   if(IsPictureExist())
				   {
					   Log.d("group", "query from local");
					   showViewpagerDirectly();
				   }
				   else
				   {
					   Log.d("group", "query from network");
					   processGroupJson(JSONArray);
				   }
				   
				   
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				   e.printStackTrace();
			    }
				
				
			}
		});
	}
	
	private void showViewpagerDirectly() 
	{
		// TODO Auto-generated method stub
		for(int i = 0;i < picturePath.size();i++)
		{
		
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath.get(i));
			((ImageView)pictureViews.get(i + 1)).setImageBitmap(bitmap);
			if(i == 0)
			{
				((ImageView)pictureViews.get(pictureViews.size() - 1)).setImageBitmap(bitmap);
			}
			else if(i == picturePath.size() - 1)
			{
				((ImageView)pictureViews.get(0)).setImageBitmap(bitmap);
			}

		}
		
		groupAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(1,false);
		
		
		Message message = new Message();
		Bundle data = new Bundle();
		data.putString("info", "showUI");
		message.setData(data);
		groupHandler.sendMessage(message);
		
	}

	private void processGroupJson(JSONArray array)
	{

		
		try 
		{
			for(int i = 0;i < array.length();i++)
			{
				JSONObject object = array.getJSONObject(i);
				    
				
				String icon_path = object.getString("icon");
				picturePath.set(i,icon_path);
				Log.d("picture_", icon_path);
				AsyncHttpClient pictureClient = new AsyncHttpClient();
			    pictureClient.get(icon_path, new PictureDownloadHandler(icon_path,i));
//				picturePath.add(object.getString("icon"));
			}
		} catch (JSONException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	private boolean IsPictureExist()
	{
		Set<String> pictureExistInPhone = new HashSet<String>();
		Set<String> pictureExistInSDcard = new HashSet<String>();
		
		File directoryInSDcard = new File(Environment.getExternalStorageDirectory(), "/Synbio");
		if(directoryInSDcard.exists())
		{
			String[] files = directoryInSDcard.list();
			for(int i = 0;i < files.length;i++)
			{
				pictureExistInSDcard.add(files[i]);
			}
		}
		File directoryInPhone  = new File(getFilesDir() + "/Synbio");
		if(directoryInPhone.exists())
		{
			String[] files = directoryInPhone.list();
			for(int i = 0;i < files.length;i++)
			{
				pictureExistInPhone.add(files[i]);
			}
		}
		
		for(int i = 0;i < picturePath.size();i++)
		{
			int length = picturePath.get(i).split("/").length;
			String name = picturePath.get(i).split("/")[length - 1];
			if(pictureExistInSDcard.contains(name))
			{
				picturePath.set(i, directoryInSDcard.getAbsolutePath() + "/" + name);
			}
			else if(pictureExistInPhone.contains(name))
			{
				picturePath.set(i, directoryInPhone.getAbsolutePath() + "/" + name);
			}
			else {
				return false;
			}
		}
		
		
		return true;
		

	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this,MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	private class PictureDownloadHandler extends BinaryHttpResponseHandler
	{
		private String picture;
		private int location;
		public PictureDownloadHandler(String picture,int location)
		{
			this.picture = picture;
			this.location = location;
		}
		
		public void onSuccess(byte[] fileData) {
            // Do something with the file
			
			
    		int length = picture.split("/").length;
    		String name = picture.split("/")[length - 1];
    		
    		try
    		{
    			File directory;
    			
    			if(CommonUtil.isSDcardAvailable(resourceOrGroupActivity))
    			{
           			directory = new File(Environment.getExternalStorageDirectory(), "/Synbio");
    			}
    			else
    			{
    				directory = new File(getFilesDir() + "/Synbio");
    			}
                directory.mkdir();
               
        	    File file = new File(directory,name);
        			
        		FileOutputStream fileOutputStream = new FileOutputStream(file);

        			
        		DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        		dataOutputStream.write(fileData);
        		dataOutputStream.flush();
        		dataOutputStream.close();
        		
        		
        		
        		
        		picturePath.set(location,file.getAbsolutePath());
        		picturenameSet.add(picturePath.get(location) + "@@" + pictureLink.get(location) + "@@" + location);
        		if(picturenameSet.size() == pictureLink.size())
        		   preferences.edit().putStringSet("group_picture", picturenameSet).commit();
        			//建议在pageadapter里面动态加载，这里会占用内存,少量图片可以，多了就容易引发问题
        		Bitmap bitmap = BitmapFactory.decodeFile(picturePath.get(location));
        		((ImageView)pictureViews.get(location + 1)).setImageBitmap(bitmap);
    			if(location == 0)
    				((ImageView)pictureViews.get(pictureViews.size() - 1)).setImageBitmap(bitmap);
    			else if(location == picturePath.size() - 1)
    				((ImageView)pictureViews.get(0)).setImageBitmap(bitmap);
        		//全部下载完后显示，要改
//    			Toast.makeText(resourceOrGroupActivity, "downloaded " + picturenameSet.size() + " " + pictureLink.size(), Toast.LENGTH_LONG).show();
        		if(location == 0)
        		{
        			
        			groupAdapter.notifyDataSetChanged();	
        			viewPager.setCurrentItem(1);
        			Message message = new Message();
        			Bundle data = new Bundle();
        			data.putString("info", "showUI");
        			message.setData(data);
        			groupHandler.sendMessage(message);
        		}
        			
    			
        			
        		
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		
    		
        }
	}
	
	//class内部的handler不含有外部类的隐式引用,weakprefernece
	private class GroupHandler extends Handler
	{
		public void handleMessage(Message msg) {
			String info = msg.getData().getString("info");
		    if(info.equals("showUI"))
		    {
				viewPager.setVisibility(View.VISIBLE);
				navigatonLayout.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
		    }

		
	
		
		}
	}
	
	private class GroupPagerAdapter extends PagerAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pictureViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		public Object instantiateItem(ViewGroup arg0, int arg1) {  
            // TODO Auto-generated method stub  
//			Log.d("instan", ""+arg1);

			if(pictureViews.size() <= arg1)
				return null;
			

//			if(picturePath.get(arg1).contains("/"))
//			{
//				((ViewPager) arg0).setVisibility(View.GONE);
//				progressBar.setVisibility(View.VISIBLE);
//			}

			
			 ((ViewPager) arg0).addView(pictureViews.get(arg1));  
            return pictureViews.get(arg1);  
        }  
		
		 public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {  
	            // TODO Auto-generated method stub  
//			 Log.d("des", ""+arg1);
			 ((ViewPager) arg0).removeView(pictureViews.get(arg1));  
	     }  
	  
		
	}

}
