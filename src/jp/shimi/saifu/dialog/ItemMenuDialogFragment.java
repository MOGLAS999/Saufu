package jp.shimi.saifu.dialog;

import java.util.EventListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ItemMenuDialogFragment extends DialogFragment{		
	private ClickedMenuListener listener = null;
	
	public interface ClickedMenuListener extends EventListener{
		
		public void doEditClick();
		public void doDeleteClick();
		public void doUpClick();
		public void doDownClick();
	}
	
	public static ItemMenuDialogFragment newInstance(String title, boolean up, boolean down) {
		ItemMenuDialogFragment fragment = new ItemMenuDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putBoolean("up_is_possible", up);
		args.putBoolean("down_is_possible", down);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstatnceState){
		CharSequence[] items;
		final CharSequence[] all      = {"編集", "削除", "上に移動", "下に移動"};
		final CharSequence[] upOnly   = {"編集", "削除", "上に移動"};
		final CharSequence[] downOnly = {"編集", "削除", "下に移動"};
		final CharSequence[] noMove   = {"編集", "削除"};
		final boolean upIsPossible = getArguments().getBoolean("up_is_possible");
		final boolean downIsPossible = getArguments().getBoolean("down_is_possible");
		
		// 項目を設定
		if(upIsPossible && downIsPossible){
			items = all;
		}else if(upIsPossible){
			items = upOnly;
		}else if(downIsPossible){
			items = downOnly;
		}else{
			items = noMove;
		}
		
		String title = getArguments().getString("title");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(title);
	    builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case 0:
						listener.doEditClick();
						break;
					case 1:
						listener.doDeleteClick();
						break;
					case 2:
						if(upIsPossible){
							listener.doUpClick();
						}else{
							listener.doDownClick();
						}
						break;
					case 3:
						listener.doDownClick();
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
	public void setClickedMenuListener(ClickedMenuListener listener){
	    this.listener = listener;
	}
	    
	/**
	 * リスナー削除
	 */
	public void removeClickedMenuListener(){
	    this.listener = null;
	}
}