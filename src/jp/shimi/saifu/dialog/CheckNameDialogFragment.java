package jp.shimi.saifu.dialog;

import java.util.EventListener;

import jp.shimi.saifu.dialog.CheckDialogFragment.ClickedNegativeButtonListener;
import jp.shimi.saifu.dialog.CheckDialogFragment.ClickedPositiveButtonListener;
import jp.shimi.saifu.parcelable.ItemDataParcelable;
import jp.shimi.saufu.ItemData;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class CheckNameDialogFragment extends DialogFragment{
	static ItemDataParcelable itemDataParcelable;
	private ClickedNamePositiveButtonListener positiveListener = null;
	
	public interface ClickedNamePositiveButtonListener extends EventListener{
		public void ClickedNamePositiveButton(ItemData itemData, int editPosition);
	}
	
	public static CheckNameDialogFragment newInstance(String title, String message,
			String positive, String negative, ItemData itemData, int editPosition){
		CheckNameDialogFragment fragment = new CheckNameDialogFragment();

		itemDataParcelable = new ItemDataParcelable(itemData);
		
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("positive", positive);
		args.putString("negative", negative);
		args.putParcelable("item_data", itemDataParcelable);
		args.putInt("edit_position", editPosition);
		
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	public static CheckNameDialogFragment newInstance(String title, String message,
			ItemData itemData, int editPosition){
		CheckNameDialogFragment fragment = new CheckNameDialogFragment();

		itemDataParcelable = new ItemDataParcelable(itemData);
		
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("positive", "OK");
		args.putString("negative", "Cancel");
		args.putParcelable("item_data", itemDataParcelable);
		args.putInt("edit_position", editPosition);
		
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
				itemDataParcelable = getArguments().getParcelable("itemData");
				positiveListener.ClickedNamePositiveButton(itemDataParcelable.GetItemData(), 
						getArguments().getInt("edit_position"));
			}
        });
        builder.setNegativeButton(getArguments().getString("negative"), null);
        
        return builder.create();
	}
	
	public void setClickedNamePositiveButtonListener(ClickedNamePositiveButtonListener listener){
	    this.positiveListener = listener;
	}
	    
	public void removeClickedNamePositiveButtonListener(){
	    this.positiveListener = null;
	}
}
