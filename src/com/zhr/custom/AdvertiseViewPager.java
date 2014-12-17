package com.zhr.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AdvertiseViewPager extends ViewPager{
	public AdvertiseViewPager(Context context) {
		// TODO �Զ����ɵĹ��캯�����
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
