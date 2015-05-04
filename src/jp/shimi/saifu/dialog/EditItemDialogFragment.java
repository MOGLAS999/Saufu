package jp.shimi.saifu.dialog;

import java.util.Calendar;

import jp.shimi.saufu.DateChanger;
import jp.shimi.saufu.Diary;
import jp.shimi.saufu.ItemData;
import jp.shimi.saufu.R;
import jp.shimi.saufu.R.id;
import jp.shimi.saufu.R.layout;
import jp.shimi.saufu.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemDialogFragment extends DialogFragment{
	LayoutInflater inflater;
	private DialogListener listener = null;
	
	public static EditItemDialogFragment newInstance(String initDate) {
		EditItemDialogFragment fragment = new EditItemDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putString("init_date", initDate);
		args.putInt("edit_position", -1);
		args.putString("item_name", "");
		args.putInt("item_price", 0);
		args.putInt("item_number", 1);
		args.putInt("item_category", 0);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	public static EditItemDialogFragment newInstance(String initDate, int editPosition,
			String itemName, int itemPrice, int itemNumber, int itemCategory) {
		EditItemDialogFragment fragment = new EditItemDialogFragment();
		  
		Bundle args = new Bundle();
		args.putString("init_date", initDate);
		args.putInt("edit_position", editPosition);
		args.putString("item_name", itemName);
		args.putInt("item_price", itemPrice);
		args.putInt("item_number", itemNumber);
		args.putInt("item_category", itemCategory);
		fragment.setArguments(args);
		 
		return fragment;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstatnceState){	
		inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        
        final EditText editItem = (EditText)layout.findViewById(R.id.editDialogItem);
        final EditText editPrice = (EditText)layout.findViewById(R.id.editDialogPrice);
        final EditText editDate = (EditText)layout.findViewById(R.id.editDialogDate);
        final EditText editNumber = (EditText)layout.findViewById(R.id.editDialogNumber);
        final TextView textTotalPrice = (TextView)layout.findViewById(R.id.textTotalPrice);
        final Button btnCategory = (Button)layout.findViewById(R.id.categoryButton);
        
        textTotalPrice.setWidth(textTotalPrice.getWidth());
        
        // テキスト入力を監視
        editPrice.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s){	
				setTotalPrice(layout);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
        });
        editNumber.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s){	
				setTotalPrice(layout);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
        });
        
        // 編集の場合EditTextを元データで初期化する
        if(editPosition >= 0){
        	int itemPrice = getArguments().getInt("item_price");
        	int itemNumber = getArguments().getInt("item_number");
        	
        	editItem.setText(getArguments().getString("item_name"));    	
        	editPrice.setText(Integer.toString(Math.abs(itemPrice)));
        	editNumber.setText(Integer.toString(itemNumber));
        	
        	if(itemNumber > 1){        		
        		long totalPrice = itemPrice * itemNumber;		
        		textTotalPrice.setText(Long.toString(totalPrice));
        	}
        }
        
        // カテゴリー(色)選択ダイアログ
        btnCategory.setOnClickListener(new OnClickListener(){
        	@Override
			public void onClick(View v) {
        		SelectCategoryDialogFragment newFragment;
        		newFragment = SelectCategoryDialogFragment.newInstance(0);
        		newFragment.show(getActivity().getFragmentManager(), "select_category_dialog");
        	}
        });
        
        editDate.setText(dc.ChangeToString(dCalendar.getTime()));
        editDate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						dCalendar.set(Calendar.YEAR, year);
						dCalendar.set(Calendar.MONTH, monthOfYear);
						dCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						editDate.setText(dc.ChangeToString(dCalendar.getTime()));
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
        final int plusColor = getActivity().getResources().getColor(R.color.plus);
        final int minusColor = getActivity().getResources().getColor(R.color.minus);
        plusMinusButton.setTextColor(minusColor);
        if(editPosition >= 0 && getArguments().getInt("item_price") > 0){
        	plusMinusButton.setText(getResources().getString(R.string.plus));
        	plusMinusButton.setTextColor(plusColor);
        }
        plusMinusButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
        			plusMinusButton.setText(getResources().getString(R.string.plus));
        			plusMinusButton.setTextColor(plusColor);
        		}
        		else if((String)plusMinusButton.getText() == getResources().getString(R.string.plus)){
        			plusMinusButton.setText(getResources().getString(R.string.minus));
        			plusMinusButton.setTextColor(minusColor);
        		}
        	}
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
				// 押された時の処理
            	EditText etxtItem   = (EditText)layout.findViewById(R.id.editDialogItem);
            	EditText etxtPrice  = (EditText)layout.findViewById(R.id.editDialogPrice);
            	EditText etxtNumber = (EditText)layout.findViewById(R.id.editDialogNumber);
            	
            	if(!etxtItem.getText().toString().equals("") &&
            			!etxtPrice.getText().toString().equals("")){
            		// 入力内容を取得
            		String strItem   = etxtItem.getText().toString();
            		String strPrice  = etxtPrice.getText().toString();
            		String strNumber = etxtNumber.getText().toString();
		    	
            		// 数値に変換
            		int price = Integer.parseInt(strPrice);
            		int number = Integer.parseInt(strNumber);
            		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
            			price *= (-1);
            		}
            		
            		// 項目を元のアクティビティに返す
            		ItemData itemData = new ItemData(strItem, price, editDate.getText().toString(),
            				number, 0);
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

	private void setTotalPrice(View v){
		TextView textTotalPrice = (TextView)v.findViewById(R.id.textTotalPrice);
		EditText editPrice = (EditText)v.findViewById(R.id.editDialogPrice);
		EditText editNumber = (EditText)v.findViewById(R.id.editDialogNumber);
		
		if(editPrice.getText().toString().isEmpty() ||
				editNumber.getText().toString().isEmpty()){
			textTotalPrice.setText(R.string.total_price);
		}
		else{
			long itemPrice = Long.parseLong(editPrice.getText().toString());
    		long itemNumber = Long.parseLong(editNumber.getText().toString());
		
			long totalPrice = itemPrice * itemNumber;		
			textTotalPrice.setText(Long.toString(totalPrice));
		}
	}
}	
