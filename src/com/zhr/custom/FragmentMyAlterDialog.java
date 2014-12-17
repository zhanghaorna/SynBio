package com.zhr.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.app.DialogFragment;

public class FragmentMyAlterDialog extends DialogFragment{
	
	public static FragmentMyAlterDialog newInstance(String title,String message)
	{
		FragmentMyAlterDialog myAlterDialogFragment = new FragmentMyAlterDialog();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		myAlterDialogFragment.setArguments(bundle);
		return myAlterDialogFragment;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		String title = getArguments().getString("title","title");
		String message = getArguments().getString("message","message");
		return new AlertDialog.Builder(getActivity())
		            .setTitle(title)
		            .setMessage(message)
		            .setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							
						}
					}).create();
	
	}

}
