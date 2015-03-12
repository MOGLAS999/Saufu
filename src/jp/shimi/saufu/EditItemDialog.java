package jp.shimi.saufu;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class EditItemDialog implements DialogListener{
	Calendar initDate;
	int editPosition;
	String itemName;
	int itemPrice;
	Context context;
	
	EditItemDialog(Context context, Calendar initDate){
		this.context = context;
		this.initDate = initDate;
		this.editPosition = -1;
		this.itemName = "";
		this.itemPrice = 0;
	}
	
	EditItemDialog(Context context, Calendar initDate, int editPosition, 
			String itemName, int itemPrice){
		this.context = context;
		this.initDate = initDate;
		this.editPosition = editPosition;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
	}
	
	EditItemDialog(Context context, ItemData editItem, int editPosition){
		this.context = context;
		this.initDate = editItem.GetDate();
		this.editPosition = editPosition;
		this.itemName = editItem.GetItem();
		this.itemPrice = editItem.GetPrice();
	}
	
	public void CreateDialog(){
		SubCreateDialog(null);  
	}
	
	public void CreateDialog(DialogListener listener){
		SubCreateDialog(listener);
	}
	
	private void SubCreateDialog(DialogListener listener){
		// アイテム編集ダイアログを生成     
		if(((Activity)context).getFragmentManager().findFragmentByTag("edit_item_dialog") == null){
			EditItemDialogFragment newFragment;
			DateChanger dc = new DateChanger();
			if(this.editPosition == -1){
				newFragment = EditItemDialogFragment.newInstance(dc.ChangeToString(this.initDate));
			}else{
				newFragment = EditItemDialogFragment.newInstance(dc.ChangeToString(this.initDate),
						this.editPosition, this.itemName, this.itemPrice);
			}
			if(listener == null){
				newFragment.setDialogListener(EditItemDialog.this);
			}else{
				newFragment.setDialogListener(listener);
			}
			//newFragment.setCancelable(false);
			newFragment.show(((Activity)context).getFragmentManager(), "edit_item_dialog");
		}
	}

	@Override
	public void doPositiveClick() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void doNegativeClick() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
    	
}