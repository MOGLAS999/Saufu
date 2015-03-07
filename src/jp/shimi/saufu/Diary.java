package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class Diary extends FragmentActivity implements OnClickListener{
	private Button button1;
	private ListView listView;
	private DayList lDay = new DayList();;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		
		button1 = (Button)findViewById(R.id.addButton1);
        button1.setOnClickListener(this);
        
        listView = (ListView)findViewById(R.id.diaryListView);
        
        // 初期残高設定ダイアログ
        if(lDay.GetListSize() == 0){
        	LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        	final View layout = inflater.inflate(R.layout.init_balance_dialog,
        			(ViewGroup)findViewById(R.id.init_balance_dialog));
        
        	AlertDialog.Builder adb = new AlertDialog.Builder(this);
        	adb.setTitle("初期残高の設定");
        	adb.setView(layout);
        
        	final DateChanger dc = new DateChanger();
        	final Calendar dCalendar = Calendar.getInstance();
        	final EditText dateEdit = (EditText)layout.findViewById(R.id.editInitBalanceDate);
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
        					Diary.this, dateSetListener, 
        					dCalendar.get(Calendar.YEAR), 
        					dCalendar.get(Calendar.MONTH),
        					dCalendar.get(Calendar.DAY_OF_MONTH));
				
        			datePickerDialog.show();
        		}
        	});
        	adb.setCancelable(false);
        	adb.setPositiveButton("設定", new DialogInterface.OnClickListener() {
        		@Override
        		public void onClick(DialogInterface dialog, int which) {
        			EditText etxtBalance  = (EditText)layout.findViewById(R.id.editInitBalance);
        			if(!etxtBalance.getText().toString().equals("")){
        				// 入力内容を取得
        				String strBalance = etxtBalance.getText().toString();
		    	
        				// 数値に変換
        				int initBalance = Integer.parseInt(strBalance);
        				
        				DayData dayData = new DayData(dCalendar, initBalance);
                		lDay.AddData(dayData);
    		    	
                		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
                		listView.setAdapter(adapter);
        			}
        		}
        	});
        	adb.show();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
	
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v){
		//EditItemDialog(Calendar.getInstance(), -1);
		EditItemDialog dialog = new EditItemDialog(this, Calendar.getInstance());
		dialog.CreateDialog();
	}	
	
	/**
	 * EditItemDialogで返される値を受け取る
	 * @param itemData
	 * @param initDate
	 * @param editPosition
	 */
	public void onReturnValue(ItemData itemData, Calendar initDate, int editPosition){
		int d = lDay.GetDayData(itemData.GetDate());
		if(d < 0){ 
			lDay.AddDataByDate(new DayData(itemData.GetDate(), 0));
		}
		if(editPosition >= 0){
			// 編集モード
			if(itemData.GetDate().equals(initDate)){
				//lDay.SetItemData(initDate, itemData, editPosition);
				// 削除処理は各ArrayAdapterで行うので、追加のみ行う
				DateChanger dc = new DateChanger();
				Log.d("onReturnValue", dc.ChangeToString(initDate) + "=="+ itemData.GetStringDate());
				if(editPosition + 1 == lDay.GetListSize()){
					lDay.AddItemData(itemData.GetDate(), itemData);
				}else{
					lDay.AddItemData(itemData.GetDate(), itemData, editPosition+1);
				}
			}else{
				//lDay.RemoveItemData(initDate, editPosition);
				lDay.AddItemData(itemData.GetDate(), itemData);
			}
		}else{
			lDay.AddItemData(itemData.GetDate(), itemData);
		}
	
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		listView.setAdapter(adapter); 
	}
}
