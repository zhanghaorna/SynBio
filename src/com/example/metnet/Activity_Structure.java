package com.example.metnet;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhr.synbio.R;




public class Activity_Structure extends Activity{
	private String name;
	private String mtext;
	private TextView mTextView=null;
	private ImageView mImageView=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_structure);
		Intent mIntent=this.getIntent();
		
		Bundle b=mIntent.getExtras();

		
		name=b.getString("uid");

		mtext=b.getString("name");
		
		mImageView=(ImageView)findViewById(R.id.layout_structure_imageview);
		mTextView=(TextView)findViewById(R.id.layout_structure_textview);
		
		;
		mTextView.setText(mtext);
		
		Bitmap bmp=getImageFromAssetsFile("structure/"+name+".png");
		
		mImageView.setImageBitmap(bmp);
		
	}
	
	private Bitmap getImageFromAssetsFile(String fileName)  
    {  
        Bitmap image = null;  
        AssetManager am = getResources().getAssets();  
        try  
        {  
            InputStream is = am.open(fileName);  
            image = BitmapFactory.decodeStream(is);  
            is.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return image;    
    }
}
