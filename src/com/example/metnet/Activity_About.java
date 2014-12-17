package com.example.metnet;

import com.zhr.synbio.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Activity_About extends Activity {
	private TextView mTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);
		mTextView=(TextView)findViewById(R.id.layout_about_textview);
		mTextView.setText(Html.fromHtml("<p align=center><font size='5'>Ecoli System</font></p>"+
		"<p>About:</p>"+
		"<p>The ecoli system is a 3D model of Escherichia coli. It mainly shows the reaction pathways in the Escherichia coli and some related information.</p>"+
		"<p>Instructions: </p>"+
		"<p>Network by Pathway :Sphere in this activity represents a pathway, long touch on a sphere will show its name , and if you click one it will go inside it </p>"+
		"<p>Pathway:Sphere in this activity represents a compound, and edge represents reaction. If you click on the sphere, it will show its structure. And If you click on the edge, it will go to the website that shows the information about the  enzyme in the reaction</p>"+

		"<p align='center'>All copyright reserved</p>"));
	}
}
