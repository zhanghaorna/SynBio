package com.zhr.synbio;



import java.util.Timer;
import java.util.TimerTask;















import com.zhr.custom.ToggleListener;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity{
    

	private Fragment[] home_fragments;
    private Toast toast;
	private ViewGroup.LayoutParams layoutParams;

	private ImageButton rxnfinder_imageButton;
	private ImageButton news_imageButton;
	private ImageButton resource_imageButton;
	private ToggleButton rxnfinder_toggleButton;
	private ToggleButton news_toggleButton;
	private ToggleButton resource_toggleButton;
	private ImageView rxnfinder_switch;
	private ImageView news_switch;
	private ImageView resource_switch;
	
	private ViewPager pager;
	



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
//		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearlayout_button_host);
//		layoutParams = linearLayout.getLayoutParams();
//		layoutParams.height = CommonUtil.getScreenHeight(this) / 6;
//		linearLayout.setLayoutParams(layoutParams);
		
		
 		
		PreferenceManager.setDefaultValues(this, R.xml.websetting_preferences, false);
//		SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.fragment_searchresult_preference),0); //0也即使MODE_PRIVATE
//		PreferenceManager.setDefaultValues(this, "news_time", MODE_PRIVATE, 0, false);
//		SharedPreferences appPreferences = getSharedPreferences("news_time", MODE_PRIVATE);
		
		
		home_fragments = new Fragment[3];
		
//		getActionBar().hide();
		pager = (ViewPager)findViewById(R.id.pager);
		
		
		setListenerAndLayout();
		
	
		pager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
	
        //设置页面切换监听
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根
				switch(arg0)
				{
				case 0:
					rxnfinder_imageButton.performClick();
					break;
				case 1:
					news_imageButton.performClick();
					break;
				case 2:
					resource_imageButton.performClick();
					break;
				default:
					
				
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO 自动生成的方法存根
				
			}
		});
		
		
		


	    
		
		
	}
	
	//设置布局与togglebutton的监听
	private void setListenerAndLayout()
	{
		
				rxnfinder_imageButton = (ImageButton)findViewById(R.id.mainactivity_rxnfinder);
				news_imageButton = (ImageButton)findViewById(R.id.mainactivity_news);
				resource_imageButton = (ImageButton)findViewById(R.id.mainactivity_resource);
				
				rxnfinder_switch = (ImageView)findViewById(R.id.rxnfinder_switch);
				news_switch = (ImageView)findViewById(R.id.news_switch);
				resource_switch = (ImageView)findViewById(R.id.resource_switch);
				
				

				
				rxnfinder_toggleButton = (ToggleButton)findViewById(R.id.rxnfinder_toggle);
				news_toggleButton = (ToggleButton)findViewById(R.id.news_toggle);
				resource_toggleButton = (ToggleButton)findViewById(R.id.resource_toggle);
				
				OnClickListener onClickListener_rxnfinder = new OnClickListener() {
					
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						
						if(!rxnfinder_toggleButton.isChecked())
						{

							rxnfinder_toggleButton.toggle();
							if(news_toggleButton.isChecked())
								news_toggleButton.toggle();
							if(resource_toggleButton.isChecked())
								resource_toggleButton.toggle();
							pager.setCurrentItem(0);

						}
						
						
					}
				};
				rxnfinder_switch.setOnClickListener(onClickListener_rxnfinder);
				rxnfinder_imageButton.setOnClickListener(onClickListener_rxnfinder);
				rxnfinder_toggleButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						if(!news_toggleButton.isChecked()&&!rxnfinder_toggleButton.isChecked()&&!resource_toggleButton.isChecked())
						{
							rxnfinder_toggleButton.toggle();
						}
						else if(rxnfinder_toggleButton.isChecked())
						{
							
							if(news_toggleButton.isChecked())
								news_toggleButton.toggle();
							if(resource_toggleButton.isChecked())
								resource_toggleButton.toggle();
							pager.setCurrentItem(0);
						}
					}
				});
				
				OnClickListener onClickListener_news = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						
						if(!news_toggleButton.isChecked())
						{
							news_toggleButton.toggle();

							if(rxnfinder_toggleButton.isChecked())
								rxnfinder_toggleButton.toggle();
							if(resource_toggleButton.isChecked())
								resource_toggleButton.toggle();
							pager.setCurrentItem(1);
						}
					}
				};
				news_switch.setOnClickListener(onClickListener_news);
				news_imageButton.setOnClickListener(onClickListener_news);

				news_toggleButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v)
					{
						// TODO 自动生成的方法存根
						if(!news_toggleButton.isChecked()&&!rxnfinder_toggleButton.isChecked()&&!resource_toggleButton.isChecked())
						{
							news_toggleButton.toggle();
						}
						else if(news_toggleButton.isChecked())
						{
							if(rxnfinder_toggleButton.isChecked())
								rxnfinder_toggleButton.toggle();
							if(resource_toggleButton.isChecked())
								resource_toggleButton.toggle();
							pager.setCurrentItem(1);
						}
					}
				});
				
				OnClickListener onClickListener_resource = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						
						if(!resource_toggleButton.isChecked())
						{

							resource_toggleButton.toggle();
							if(rxnfinder_toggleButton.isChecked())
								rxnfinder_toggleButton.toggle();
							if(news_toggleButton.isChecked())
								news_toggleButton.toggle();
							pager.setCurrentItem(2);
						}
					}
				};
				resource_switch.setOnClickListener(onClickListener_resource);
				resource_imageButton.setOnClickListener(onClickListener_resource);

				resource_toggleButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						if(!news_toggleButton.isChecked()&&!rxnfinder_toggleButton.isChecked()&&!resource_toggleButton.isChecked())
						{
							resource_toggleButton.toggle();
						}
						else if(resource_toggleButton.isChecked())
						{
							if(rxnfinder_toggleButton.isChecked())
								rxnfinder_toggleButton.toggle();
							if(news_toggleButton.isChecked())
								news_toggleButton.toggle();	
							pager.setCurrentItem(2);
						}
					}
				});
				
				//每个togglebutton设置oncheckedchange监听
				rxnfinder_toggleButton.setOnCheckedChangeListener(new ToggleListener(this, rxnfinder_switch, rxnfinder_toggleButton,R.id.rxnfinder_toggle));
				news_toggleButton.setOnCheckedChangeListener(new ToggleListener(this, news_switch, news_toggleButton,R.id.news_toggle));
				resource_toggleButton.setOnCheckedChangeListener(new ToggleListener(this, resource_switch, resource_toggleButton,R.id.resource_toggle));

	}
	
	
	
	private class HomePagerAdapter extends FragmentPagerAdapter
	{

		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public Fragment getItem(int position) {
			// TODO 自动生成的方法存根
			switch(position)
			{
			case 0:
				if(home_fragments[position] == null)
				{
					home_fragments[position] = new FragmentRxnFinder();
					
				}
				break;
			case 1:
				if(home_fragments[position] == null)
				{
					home_fragments[position] = new FragmentNews();
					
				}
				break;
			case 2:
				if(home_fragments[position] == null)
				{
					home_fragments[position] = new FragmentResources();
					
				}
				break;
			default:
				
			}
			
			return home_fragments[position];
		}

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return home_fragments.length;
		}


		
	}
	

	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onBackPressed ()
	{
		
		
		if(toast == null)
		{
			toast = Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT);
			toast.show();
			Log.d("main activity",""+ toast.getDuration());
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					toast = null;
					
				}
			}, 2000);
		}
		else {
			super.onBackPressed();
		}
		
	}
	
	public boolean onKeyDown (int keyCode, KeyEvent event)
	{
		
		
		
		return super.onKeyDown(keyCode, event);
		
	}

}
