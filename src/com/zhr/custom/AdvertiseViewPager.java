package com.zhr.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AdvertiseViewPager extends ViewPager{
	public AdvertiseViewPager(Context context) {
		// TODO 自动生成的构造函数存根
		super(context);
	}
	
	public AdvertiseViewPager(Context context, AttributeSet attrs)
	{
		super(context,attrs);
	}
	
	public boolean dispatchTouchEvent(MotionEvent ev) {   
        getParent().requestDisallowInterceptTouchEvent(true);  
        return super.dispatchTouchEvent(ev);    
    }   

}
