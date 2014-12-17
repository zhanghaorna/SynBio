package com.zhr.custom;

import com.zhr.synbio.R;

import android.R.integer;
import android.content.Context;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class ToggleListener implements OnCheckedChangeListener{
	
	private ImageView switchButton;
	private ToggleButton toggleButton;
	private Context context;
	private int id;
	
	public ToggleListener(Context context,ImageView switchButton,ToggleButton toggleButton,int id)
	{
		this.switchButton = switchButton;
		this.context = context;
		this.toggleButton = toggleButton;
		this.id = id;
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)switchButton.getLayoutParams();
		if(toggleButton.isChecked())
		{
			params.addRule(RelativeLayout.ALIGN_RIGHT, -1);
			params.addRule(RelativeLayout.ALIGN_LEFT,id);
			switchButton.setLayoutParams(params);
			
//			int width = toggleButton.getWidth();
//			
//			TranslateAnimation translateAnimation = new TranslateAnimation(0,0,width,0);
//			switchButton.setAnimation(translateAnimation);
		}
		else 
		{
			params.addRule(RelativeLayout.ALIGN_RIGHT, id);
			params.addRule(RelativeLayout.ALIGN_LEFT,-1);
			switchButton.setLayoutParams(params);
			
//			int width = toggleButton.getWidth();
//			TranslateAnimation translateAnimation = new TranslateAnimation(0,0,-width,0);
//			switchButton.setAnimation(translateAnimation);
		}
		
	}

}
