package com.zhr.synbio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;

import android.R.integer;
import android.accounts.NetworkErrorException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.Bitmap;


public class SearchResultActivity extends Activity implements OnSharedPreferenceChangeListener
{
	
	private WebView webView;
	private TextView networknull_textView;
	private WebSettings webSettings;
	private PopupWindow mPopupWindow;
	private PopupWindow imagePopupWindow;
	
	
	private View popupwindow;
	private SimpleAdapter Imageitems;
	private ArrayList<HashMap<String, Object>> lstImageItem;
	private SharedPreferences preferences;
	private GridView gridView;

	private LinearLayout.LayoutParams layoutParams;
	private ActionBar actionBar;
	private Toast toast;
	private int gridview_heigth = 140;
	private SearchResultActivity searchResultActivity;

	
	
	public void onCreate(Bundle savedInstanceState)  
    {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
		setContentView(R.layout.activity_search_result);
		
		searchResultActivity = this;
		
		
	
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //设置Toast 持续时间
        toast = null;
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		
		

	    networknull_textView = (TextView)findViewById(R.id.search_result_networknull_textview);
	    networknull_textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(!CommonUtil.isConnectionAvailable(searchResultActivity))
				{
					getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF); 
					Toast.makeText(searchResultActivity, "网络未连接", Toast.LENGTH_SHORT).show();

				}
				else
				{
					getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
					networknull_textView.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);
					loadpage();
				}
			}
		});

		

		webView = (WebView)findViewById(R.id.webview);
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		
		
		

		
		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView webview,String url)
			{
				webview.loadUrl(url);
				
				
				return true;
			}
			public void onPageStarted (WebView view, String url, Bitmap favicon)
			{
				
				if(!CommonUtil.isConnectionAvailable(searchResultActivity))
				{
					getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF); 
					Toast.makeText(searchResultActivity, "网络未连接", Toast.LENGTH_SHORT).show();
					networknull_textView.setVisibility(View.VISIBLE);
					webView.setVisibility(View.GONE);

				}
				else
				{
					getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
					super.onPageStarted(view, url, favicon);
				}
			}

			public void onReceivedError (WebView view, int errorCode, String description, String failingUrl)
			{
				 super.onReceivedError(view, errorCode, description, failingUrl);

				
			}
			
			
		});
		webView.setWebChromeClient(new WebChromeClient(){

			
			public void onProgressChanged(WebView view, int progress)     
	        {  
	         //Make the bar disappear after URL is loaded, and changes string to Loading...  
			   super.onProgressChanged(webView, progress);
			   setTitle(getResources().getString(R.string.fragment_searchresult_loading));  
			   setProgress(progress * 100); 
			   
 
			            if(progress == 100)  
			            	setTitle(R.string.app_name);  
         
	         }
			
		});
		//长按图片弹出一个popupwindow
//		webView.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO 自动生成的方法存根
//				HitTestResult hitTestResult = ((WebView)v).getHitTestResult();
//				if(hitTestResult.getType() == HitTestResult.IMAGE_TYPE || hitTestResult.getType() == HitTestResult.SRC_ANCHOR_TYPE
//						||hitTestResult.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)
//				{
//					
//				}
//				return false;
//			}
//		});
		
		
		popupwindow = inflater.inflate(R.layout.searchresult_web_popupwindow, null);
		mPopupWindow = new PopupWindow(popupwindow,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		//弹出式菜单显示内容
		gridView = (GridView)popupwindow.findViewById(R.id.gridview);

		

		
		lstImageItem = new ArrayList<HashMap<String, Object>>(); 
		//手动添加显示项
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference),0); //0也即使MODE_PRIVATE
		if(preferences.getBoolean(getResources().getString(R.string.websetting_picture), true))
		{
			map.put("ItemImage", R.drawable.no_picture);
			map.put("ItemText", getResources().getString(R.string.fragment_searchresult_nopicture));
			lstImageItem.add(map);
		}
		else
		{
			map.put("ItemImage", R.drawable.yes_picture);
			map.put("ItemText", getResources().getString(R.string.fragment_searchresult_havepicture));
			lstImageItem.add(map);
			webSettings.setBlockNetworkImage(true);
		}
		
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.refresh);
		map.put("ItemText", "刷新");
		lstImageItem.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.web_setting);
		map.put("ItemText", "设置");
		lstImageItem.add(map);
		
		
		
		Imageitems = new SimpleAdapter(getApplicationContext(), lstImageItem, R.layout.searchresult_web_popupwindow_button
		                                    , new String[] {"ItemImage","ItemText"}, new int[] {R.id.ItemImage,R.id.ItemText});
		


		gridView.setAdapter(Imageitems);
		

		//为弹出式菜单按钮添加响应项
		gridView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				switch (arg2) {
				case 0: 
					if(lstImageItem.get(arg2).get("ItemText").toString().equals(getResources().getString(R.string.fragment_searchresult_havepicture)))
					{
						
					//	noToHavePicture();
						
						
						SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference), 0);
						
						SharedPreferences.Editor  editor =	sharedPreferences.edit();
						
						editor = editor.putBoolean(getResources().getString(R.string.websetting_picture), true);
						
						editor.commit();
						
					}
					else
					{
					//	haveToNoPicture();
						
						
						SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference), 0);
						
						SharedPreferences.Editor  editor =	sharedPreferences.edit();
						
						editor = editor.putBoolean(getResources().getString(R.string.websetting_picture),false);
						
						editor.commit();

					}
					break;
				case 1:
					webView.reload();
					mPopupWindow.dismiss();
					break;
				case 2:
					mPopupWindow.dismiss();

					
					Intent intent = new Intent(getBaseContext(),WebSettingPreferenceActivity.class);
					startActivity(intent);
					
					break;

				default:
					break;
				}
			}
		});
		
		
		//注册首选项，以便监听
		Context context = getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.registerOnSharedPreferenceChangeListener(this);

			
				
				

				
				
		ImageButton gowardButton = (ImageButton)findViewById(R.id.goward);
		gowardButton.setOnClickListener(new OnClickListener() {
					
					@Override
		           public void onClick(View v) {
						// TODO 自动生成的方法存根
						if(mPopupWindow.isShowing())
							mPopupWindow.dismiss();
						
						if(webView.canGoForward())
							webView.goForward();
					}
				});
		ImageButton backwardButton = (ImageButton)findViewById(R.id.backward);
		backwardButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						if(mPopupWindow.isShowing())
							mPopupWindow.dismiss();

						if(webView.canGoBack())
							webView.goBack();
					}
				});
		ImageButton settingButton = (ImageButton)findViewById(R.id.setting);
		settingButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						
						
						if(mPopupWindow.isShowing())
							mPopupWindow.dismiss();

						RadioGroup radioGroup = (RadioGroup)findViewById(R.id.bottombutton);
						int height = radioGroup.getHeight();
						int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
				        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
				        gridView.measure(w, h);
				        height = -1 * (height + gridView.getMeasuredHeight()); 
						
						

 
     					mPopupWindow.showAsDropDown(v, 0, height);  //写死不好,要改值
						
						
					}
				});
				


		
		if(!CommonUtil.isConnectionAvailable(this))
		{
			getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF); 
			networknull_textView.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
		else
		{
			loadpage();
		}
		
		
		


		
		
				
       
    }
	
	//加载网页
	private void loadpage() 
	{
		if(!getIntent().getExtras().getString("keyword", "").equals(""))
		{
			String keyword = getIntent().getExtras().getString("keyword","molname");
			String value = getIntent().getExtras().getString("value","S-adenosyl-L-methionine");
			String postData = keyword + "=" + value;
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpResponse httpResponse = httpClient.execute(new HttpGet("http://www.rxnfinder.org/keywordsrxn/"));
//			httpClient.getCookieStore().getCookies();
			
            
			webView.postUrl("http://www.rxnfinder.org/keywordsrxn/", EncodingUtils.getBytes(postData, "base64"));
			actionBar.setTitle("Result");
		}
		else if(!getIntent().getExtras().getString("group","").equals(""))
		{
			String link = getIntent().getExtras().getString("group","");
			webView.loadUrl(link);
			actionBar.setTitle("Groups");
		}
		else
		{
			String link = getIntent().getExtras().getString("link");
			webView.loadUrl(link);
			actionBar.setTitle("News");
		}
	}
	
	public void noToHavePicture()
	{
		lstImageItem.remove(0);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.no_picture);
		map.put("ItemText",getResources().getString(R.string.fragment_searchresult_nopicture) );
		lstImageItem.add(0, map);
		webSettings.setBlockNetworkImage(false);
		Imageitems.notifyDataSetChanged();
		mPopupWindow.dismiss();
		
		if(toast == null)
		{
			toast = Toast.makeText(this, "当前切换到了 有图模式", Toast.LENGTH_SHORT);
		}
		else
		{
			toast.setText("当前切换到了 有图模式");
		}
		toast.show();
	}
	
	public void haveToNoPicture()
	{
		lstImageItem.remove(0);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.yes_picture);
		map.put("ItemText",getResources().getString(R.string.fragment_searchresult_havepicture) );
		lstImageItem.add(0, map);
		webSettings.setBlockNetworkImage(true);
		Imageitems.notifyDataSetChanged();
		mPopupWindow.dismiss();
		
		if(toast == null)
		{
			toast = Toast.makeText(this, "当前切换到了 无图模式", Toast.LENGTH_SHORT);
		}
		else
		{
			toast.setText("当前切换到了 无图模式");
		}
		toast.show();
	}
	
	public void onDestory()
	{
		super.onDestroy();
		preferences.unregisterOnSharedPreferenceChangeListener(this);
		Log.d("test", "onDestory");
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO 自动生成的方法存根
		if(key.equals(getResources().getString(R.string.websetting_picture)))
		{
			Log.d("fragment_search", "enter");
			if(sharedPreferences.getBoolean(getResources().getString(R.string.websetting_picture), true))
			{
				noToHavePicture();
			}
			else 
			{
				haveToNoPicture();
			}
		}
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
//			Intent intent = new Intent(this,RxnfinderActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
