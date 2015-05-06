package jp.shimi.saifu.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class SimpleMessageDialogFragment extends DialogFragment {
	public static SimpleMessageDialogFragment newInstance(String title, String message){
		SimpleMessageDialogFragment fragment = new SimpleMessageDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle(getArguments().getString("title"));
        builder.setMessage(getArguments().getString("message"));
        builder.setPositiveButton("OK", null);
        
        return builder.create();
	}
}
