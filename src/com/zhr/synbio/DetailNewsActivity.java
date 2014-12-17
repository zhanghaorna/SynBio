package com.zhr.synbio;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhr.database.DBNewsHelper;
import com.zhr.sqlitedao.News;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailNewsActivity extends Activity{
	static String news_html = "http://www.rxnfinder.org/update/";
	private PullToRefreshListView pullToRefreshListView;
	private int category;
	private NewsListAdapter mAdapter;
	private DetailNewsActivity detailNewsActivity;
	private TextView loadMoreTextView;
    private String last_update_time;
    private HashMap<String, String> postdate;
    private ArrayList<News> news;
    private AsyncHttpClient client;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Toast toast = null;
    private SharedPreferences preferences;
    private String datestr;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_news);
        news = new ArrayList<News>();
		detailNewsActivity = this;
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pulltorefreshlistview);
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(detailNewsActivity));
		
		options = new DisplayImageOptions.Builder()
		  .showImageOnLoading(R.drawable.ic_stub)
		  .showImageForEmptyUri(R.drawable.ic_empty)
		  .showImageOnFail(R.drawable.ic_error)
		  .cacheInMemory(true)
		  .cacheOnDisc(true)
		  .considerExifParams(true)
		  .build();
		client = new AsyncHttpClient();
		postdate = new HashMap<String, String>();
		

		preferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference),0); //0也即使MODE_PRIVATE
		last_update_time = preferences.getString("news_time","null");
		if(!last_update_time.equals("null"))
		{
			postdate.put("date", last_update_time);
		}
		
		
		
		
		
		
		
		
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    Bundle bundle = getIntent().getExtras();
	    category  = bundle.getInt("category");
	    switch(category)
	    {
	    case 0:
	    	category = 0;
	    	getActionBar().setTitle("科研类新闻");
	    	postdate.put("type", "research_news");
	    	break;
	    case 1:
	    	category = 1;
	    	getActionBar().setTitle("会议类新闻");
	    	postdate.put("type", "meeting_news");
	    	break;
	    case 2:
	    	category = 2;
	    	getActionBar().setTitle("产品类新闻");
	    	postdate.put("type", "product_news");
	    	break;
	    }
	    

	    
        
        mAdapter = new NewsListAdapter();
        pullToRefreshListView.setAdapter(mAdapter);
//        pullToRefreshListView.setMode(Mode.BOTH);
        pullToRefreshListView.setMode(Mode.PULL_FROM_START);
        //定义下拉时显示的文字
		pullToRefreshListView.getLoadingLayoutProxy().setRefreshingLabel("加载中。。。");
		pullToRefreshListView.getLoadingLayoutProxy().setPullLabel("下拉可以刷新");
		pullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
        pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				postdate.put("date", last_update_time);
				UpdateNewsFromServer();
			}
		});
        pullToRefreshListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true,false));
        

		
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				//由于pullTorefreshlistView有一个headerview，所以第一项返回的arg2为1，不是0
				if(arg2 == news.size() + 1)
				{
					
					if(arg2 > 1)
					{
						
						new FinishRefresh().execute(news.get(arg2 - 2));
					}
					
				}
				else
				{
					String link = news.get(arg2 - 1).getLink();
					Intent intent = new Intent(detailNewsActivity,SearchResultActivity.class);
					Bundle bundle = new Bundle();
					
					bundle.putString("link", link);
					intent.putExtras(bundle);
					startActivity(intent);
				}

				
			}
		});
		
		
	    
	     List<News> beginNews = DBNewsHelper.getInstance(detailNewsActivity).queryNews("" + category);
	     for(News ns:beginNews)
	     {
	    	 news.add(ns);
	     }
	    	 
	     
//	     mAdapter.notifyDataSetChanged();
	     UpdateNewsFromServer();
	    
		
	}
	
	private void LoadMoreNews(News ns)
	{
		
		List<News> moreNews = DBNewsHelper.getInstance(getBaseContext()).getMoreNews(ns);
		if(moreNews.size() == 0)
			return;
        for(News temp:moreNews)
        {
        	news.add(temp);
        }
        
	    

	    
	}
	
	private void UpdateNewsFromServer()
	{
	    this.client.post(news_html, new RequestParams(this.postdate), new JsonHttpResponseHandler()
	    {
	      public void onFailure(Throwable paramThrowable, JSONObject paramJSONObject)
	      {
	        if (toast == null)
	        {
		         toast = Toast.makeText(DetailNewsActivity.this.detailNewsActivity, "服务器无法连接", Toast.LENGTH_SHORT);
		         toast.show();
		          
	        }
            else 
	        {
	        	toast.setText("服务器无法连接");
			} 
	        pullToRefreshListView.onRefreshComplete();
	        
	      }

	      public void onSuccess(JSONArray paramJSONArray)
	      {
	        datestr = DateUtils.formatDateTime(DetailNewsActivity.this.detailNewsActivity, System.currentTimeMillis(), 131217);
	        pullToRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + datestr);
	        preferences.edit().putString("news_time", CommonUtil.convertDate(datestr)).commit();
	        
	        UpdateNewNews(paramJSONArray);
	        mAdapter.notifyDataSetChanged();
	        pullToRefreshListView.onRefreshComplete();
	      }
	    });
	}
	
	private void UpdateNewNews(JSONArray NewsArray) 
	{
		
		try 
		{
			
			
			for(int i = 0;i < NewsArray.length();i++)
			{
				
				JSONObject object = NewsArray.getJSONObject(i);

				String title = object.getString("title");
				String content = object.getString("content");
				String icon = object.getString("icon");
				String link = object.getString("link");
				if(!DBNewsHelper.getInstance(detailNewsActivity).queryNewsFromTitle(title,category + ""))
				{
					News news = new News();
					news.setTitle(title);
					news.setContent(content);
					news.setIcon(icon);
					news.setLink(link);
					news.setCategory("" + category);
					DBNewsHelper.getInstance(detailNewsActivity).insertNews(news);
	                this.news.add(0,news);
				}


			}
			
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

			
	}

	private class NewsListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return news.size() + 1; 
		}

		@Override
		public Object getItem(int arg0) {
			// TODO 自动生成的方法存根
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
//			Log.d("view", ""+position+" size:"+titles.size());
			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(position == news.size())
			{
				View view = layoutInflater.inflate(R.layout.activity_loading_more, null);
				loadMoreTextView = (TextView)view.findViewById(R.id.news_loading_more);
				
				return view;
			}
			else {
				
				

				View view = layoutInflater.inflate(R.layout.activity_custom_news_detail,null);

				TextView titleView = (TextView)view.findViewById(R.id.news_listview_title);

				titleView.setText(news.get(position).getTitle());

			
				
				TextView contentView = (TextView)view.findViewById(R.id.news_listview_summary);

				contentView.setText(news.get(position).getContent());
				
				ImageView iconView = (ImageView)view.findViewById(R.id.news_listview_icon);
				final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.news_listview_progress);

                imageLoader.displayImage(news.get(position).getIcon(), iconView,options,new SimpleImageLoadingListener(){
                	@Override
					 public void onLoadingStarted(String imageUri, View view) {
						 progressBar.setProgress(0);
						 progressBar.setVisibility(View.VISIBLE);
					 }

					 @Override
					 public void onLoadingFailed(String imageUri, View view,
							 FailReason failReason) {
						 progressBar.setVisibility(View.GONE);
					 }

					 @Override
					 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						 progressBar.setVisibility(View.GONE);
					 }
				 }, new ImageLoadingProgressListener() {
					 @Override
					 public void onProgressUpdate(String imageUri, View view, int current,
							 int total) {
						 progressBar.setProgress(Math.round(100.0f * current / total));
					 }
				 }
                	
                
                );
	            
	            
		
				
				
				return view;
			}
			
			
			
			
		}
		
	}
	
	private class FinishRefresh extends AsyncTask<News, Void, Void>{  
        @Override  
        protected Void doInBackground(News... params) {
          
          LoadMoreNews(params[0]);
          publishProgress();
          return null;
           
        }
        protected void onProgressUpdate(Void... progress) 
        {
        	
        	loadMoreTextView.setText("加载中...");
//        	loadMoreTextView.setClickable(false);
        }
   
        @Override  
        protected void onPostExecute(Void result){  
        	
        	
//        	loadMoreTextView.setClickable(true);
        	loadMoreTextView.setText("点击加载更多");
        	mAdapter.notifyDataSetChanged();
        }  
    }  
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    getMenuInflater().inflate(R.menu.news_menu, menu);
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
		case R.id.news_clear_cache:
			imageLoader.clearDiscCache();
			imageLoader.clearMemoryCache();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
            DBNewsHelper.getInstance(detailNewsActivity).deleteNews("" + category);
			imageLoader.stop();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
