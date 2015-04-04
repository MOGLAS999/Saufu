package jp.shimi.saihu.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ItemMenuDialogFragment extends DialogFragment{		
	private MenuListener listener = null;
	
	public static ItemMenuDialogFragment newInstance(String title) {
		ItemMenuDialogFragment fragment = new ItemMenuDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putString("title", title);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstatnceState){
		CharSequence[] items = {"編集", "削除"};	
		
		String title = getArguments().getString("title"); 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(title);
	    builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case 0:
						listener.doFitstClick();
						break;
					case 1:
						listener.doSecondClick();
						break;
					default:
	        			break;
				}
				dismiss();
			}
		});
	    
	    return builder.create();
	}
	
	/**
	 * リスナーを追加
	 */
	public void setDialogListener(MenuListener listener){
	    this.listener = listener;
	}
	    
	/**
	 * リスナー削除
	 */
	public void removeDialogListener(){
	    this.listener = null;
	}
}