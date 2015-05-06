package jp.shimi.saifu.dialog;

import java.util.EventListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class CheckDialogFragment extends DialogFragment{
	private ClickedPositiveButtonListener positiveListener = null;
	
	//　ポジティブボタンが押された際のリスナー
	public interface ClickedPositiveButtonListener extends EventListener{
		public void ClickedPositiveButton();
	}
	
	public static CheckDialogFragment newInstance(String title, String message){
		CheckDialogFragment fragment = new CheckDialogFragment();

		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("positive", "OK");
		args.putString("negative", "Cancel");
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	public static CheckDialogFragment newInstance(String title, String message,
			String positive, String negative){
		CheckDialogFragment fragment = new CheckDialogFragment();

		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("positive", positive);
		args.putString("negative", negative);
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle(getArguments().getString("title"));
        builder.setMessage(getArguments().getString("message"));
        builder.setPositiveButton(getArguments().getString("positive"),
        		new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				positiveListener.ClickedPositiveButton();
			}
        });
        builder.setNegativeButton(getArguments().getString("negative"), null);
        
        return builder.create();
	}

	public void setClickedPositiveButtonListener(ClickedPositiveButtonListener listener){
	    this.positiveListener = listener;
	}
	    
	public void removeClickedPositiveButtonListener(){
	    this.positiveListener = null;
	}
}
