package com.zhr.synbio;




import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhr.custom.FragmentMyAlterDialog;






import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class RxnfinderActivity extends Activity{
	EditText keyWordText;
    String[] keywords_array;
    String[] keywords_example;
    String[] keywords_id;
	ActionBar actionBar;
	Spinner keywordSpinner;
	Spinner keyword_detailinformationSpinner;
	private Button audio_input_Button;
    private SpeechRecognizer speechRecognizer;
	private RecognizerDialog recognizerDialog;
	private Toast mToast;



	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO 自动生成的方法存根
		keyWordText.clearFocus();

		return super.onTouchEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_rxnfinder);
		

		

		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
		
		actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		keywords_array = getResources().getStringArray(R.array.keyword_array);
		keywords_example = getResources().getStringArray(R.array.keyword_example);
		keywords_id = getResources().getStringArray(R.array.keyword_id);
		
		audio_input_Button = (Button)findViewById(R.id.audio_recognize);
		audio_input_Button.setEnabled(false);
		audio_input_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recognizerDialog.setListener(recognizerListener);
				recognizerDialog.show();
			}
		});
		
	
		keywordSpinner = (Spinner)findViewById(R.id.keywordspinner);
		keywordSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO 自动生成的方法存根
				keyWordText.setText("");
				

				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO 自动生成的方法存根
				
			}
		});
		
		
		
		
		
		
		
		keyWordText = (EditText)findViewById(R.id.keywordName);
		//setonfocuschangelistener 是当焦点改变后调用的方法，本身并不会引起焦点的改变
		keyWordText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO 自动生成的方法存根
				
				if(!hasFocus)
				{
					InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(keyWordText.getWindowToken(), 0);
				}
				
			}
		});
		
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,keywords_array);
		keywordSpinner.setAdapter(adapter);
		
		
		
		//语音识别初始化
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=537c91e4");
		
		speechRecognizer = SpeechRecognizer.createRecognizer(this, initListener);
		
		recognizerDialog = new RecognizerDialog(this,initListener);
		
		speechRecognizer.setParameter(SpeechConstant.DOMAIN,"iat");
		speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
		
		
		
		
	}
	
	private RecognizerDialogListener recognizerListener = new RecognizerDialogListener() {
		
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			// TODO Auto-generated method stub
			String text = CommonUtil.parseIatResult(results.getResultString());
			if(text.equals("."))
			    return;
			keyWordText.append(text);
			keyWordText.setSelection(text.length());
		}
		
		@Override
		public void onError(SpeechError error) {
			// TODO Auto-generated method stub
			showTip(error.getPlainDescription(true));
		}
		

	};
	
	@Override
	protected void onResume() {
//		SpeechUtility.getUtility().checkServiceInstalled();
		super.onResume();
	}

	

	
	private InitListener initListener = new InitListener() {
		
		@Override
		public void onInit(int code) {
			// TODO Auto-generated method stub
			if(code == ErrorCode.SUCCESS)
				audio_input_Button.setEnabled(true);
		}
	};
	
	public void onbtnExample(View view)
	{
		
		keyWordText.setText(keywords_example[keywordSpinner.getSelectedItemPosition()]);
	}
	
	
	
	public void onbtnSearch(View view)
	{ 

		if(keyWordText.getText().toString().trim().equals(""))
		{
			FragmentMyAlterDialog newFragment = FragmentMyAlterDialog.newInstance("Tip", "Please input something");
			newFragment.show(getFragmentManager(), "Dialog");
			return;
		}
		
		
		
		Intent intent;
		intent = new Intent(this, SearchResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		String keyword = keywords_id[keywordSpinner.getSelectedItemPosition()];
		intent.putExtra("keyword", keyword);
		intent.putExtra("value", keyWordText.getText().toString());
		startActivity(intent);
		
		

	}
	
	private void showTip(final String str)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
//			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
