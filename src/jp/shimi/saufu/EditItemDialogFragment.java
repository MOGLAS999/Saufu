package jp.shimi.saufu;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditItemDialogFragment extends DialogFragment{
	private DialogListener listener = null;
	
	public static EditItemDialogFragment newInstance(String initDate) {
		EditItemDialogFragment fragment = new EditItemDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putString("init_date", initDate);
		args.putInt("edit_position", -1);
		args.putString("item_name", "");
		args.putInt("item_price", 0);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	public static EditItemDialogFragment newInstance(String initDate, int editPosition,
			String itemName, int itemPrice) {
		EditItemDialogFragment fragment = new EditItemDialogFragment();
		  
		Bundle args = new Bundle();
		args.putString("init_date", initDate);
		args.putInt("edit_position", editPosition);
		args.putString("item_name", itemName);
		args.putInt("item_price", itemPrice);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstatnceState){	
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.diary_dialog,
        		(ViewGroup)getActivity().findViewById(R.id.diarydialog_layout));
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int editPosition = getArguments().getInt("edit_position");
        String title = "項目の追加";
        if(editPosition >= 0) title = "項目の編集";
        builder.setTitle(title);
        builder.setView(layout);
	    
        final DateChanger dc = new DateChanger();
		Calendar initDate = Calendar.getInstance();
		initDate.setTime(dc.ChangeToDate(getArguments().getString("init_date"))); 
        final Calendar dCalendar = initDate;
        
        if(editPosition >= 0){
        	EditText itemEdit = (EditText)layout.findViewById(R.id.editDialogItem);
        	EditText priceEdit = (EditText)layout.findViewById(R.id.editDialogPrice);

        	itemEdit.setText(getArguments().getString("item_name"));    	
        	priceEdit.setText(Integer.toString(Math.abs(getArguments().getInt("item_price"))));
        }
        
    	final EditText dateEdit = (EditText)layout.findViewById(R.id.editDialogDate);
       	dateEdit.setText(dc.ChangeToString(dCalendar.getTime()));
    	dateEdit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						dCalendar.set(Calendar.YEAR, year);
						dCalendar.set(Calendar.MONTH, monthOfYear);
						dCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						dateEdit.setText(dc.ChangeToString(dCalendar.getTime()));
					}
				};
				
				final DatePickerDialog datePickerDialog = new DatePickerDialog(
						getActivity(), dateSetListener, 
						dCalendar.get(Calendar.YEAR), 
						dCalendar.get(Calendar.MONTH),
						dCalendar.get(Calendar.DAY_OF_MONTH));
				
				datePickerDialog.show();
			}
    	});
        // プラマイ切り替えボタン
        final Button plusMinusButton = (Button)layout.findViewById(R.id.plusMinusButton);
        if(editPosition >= 0 && getArguments().getInt("item_price") > 0){
        	plusMinusButton.setText(getResources().getString(R.string.plus));
        }
        plusMinusButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
        			plusMinusButton.setText(getResources().getString(R.string.plus));
        		}
        		else if((String)plusMinusButton.getText() == getResources().getString(R.string.plus)){
        			plusMinusButton.setText(getResources().getString(R.string.minus));
        		}
        	}
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
				// 押された時の処理
            	EditText etxtItem   = (EditText)layout.findViewById(R.id.editDialogItem);
            	EditText etxtPrice  = (EditText)layout.findViewById(R.id.editDialogPrice);
            	
            	if(!etxtItem.getText().toString().equals("") &&
            			!etxtPrice.getText().toString().equals("")){
            		// 入力内容を取得
            		String strItem  = etxtItem.getText().toString();
            		String strPrice = etxtPrice.getText().toString();
		    	
            		// 数値に変換
            		int price = Integer.parseInt(strPrice);
            		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
            			price *= (-1);
            		}
            		
            		// 項目を元のアクティビティに返す
            		ItemData itemData = new ItemData(strItem, price, dateEdit.getText().toString());
            		Diary callingActivity = (Diary)getActivity();
            		// 何故かinitDateがdateEdit.getText().toString()に置き換わっているので新しく取り直す
            		Calendar initDate = Calendar.getInstance();
            		initDate.setTime(dc.ChangeToDate(getArguments().getString("init_date"))); 
            		callingActivity.onReturnValue(itemData, initDate, editPosition);  
            	}
            	            	           	
            	listener.doPositiveClick();
            	dismiss();
			}
		});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.doNegativeClick();
				dismiss();
			}
		});
        
	    return builder.create();
	}
	
	/**
	 * リスナーを追加
	 */
	public void setDialogListener(DialogListener listener){
	    this.listener = listener;
	}
	    
	/**
	 * リスナー削除
	 */
	public void removeDialogListener(){
	    this.listener = null;
	}
}	
