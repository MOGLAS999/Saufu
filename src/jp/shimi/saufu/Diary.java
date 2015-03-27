package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import jp.shimi.saifu.setting.Preferences;

import android.os.Bundle;
import android.DB.MySQLiteOpenHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class Diary extends FragmentActivity implements OnClickListener, DayDeletedListener, DayItemDeletedListener{
	private Button button1;
	private ListView listView;
	private DayList lDay = new DayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		
		LoadDayDataFromDB();
		LoadItemDataFromDB();
		
		button1 = (Button)findViewById(R.id.addButton1);
        button1.setOnClickListener(this);
        
        listView = (ListView)findViewById(R.id.diaryListView);
        
        // 初期残高設定ダイアログ
        if(lDay.GetListSize() == 0){
        	EditItemDialog dialog = new EditItemDialog(this, Calendar.getInstance(), 0, "初期残高", 1);
    		dialog.CreateDialog();
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_settings:
				Intent intent = new Intent(this, Preferences.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
	
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		adapter.setDayDeletedListener(this);
		adapter.setDayItemDeletedListener(this);
		listView.setAdapter(adapter);
		
		listView.setSelection(lDay.GetListSize());
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
		adapter.setDayDeletedListener(this);
		adapter.setDayItemDeletedListener(this);
		listView.setAdapter(adapter); 
		
		SaveDayDataToDB();
		SaveItemDataToDB();
	}
	
	@Override
	public void DayDeleted(Calendar deletedDate) {
		lDay.UpdateBalance(lDay.GetNextDate(deletedDate));
		
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		adapter.setDayDeletedListener(this);
		adapter.setDayItemDeletedListener(this);
		listView.setAdapter(adapter);
		
		SaveDayDataToDB();
		SaveItemDataToDB();
	}	
	
	@Override
	public void DayItemDeleted(Calendar deletedDate) {
		lDay.CheckItemListSize();
		lDay.UpdateBalance(deletedDate);
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		adapter.setDayItemDeletedListener(this);
		listView.setAdapter(adapter);
		
		SaveDayDataToDB();
		SaveItemDataToDB();
	}
	
	public void LoadDayDataFromDB(){
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		DateChanger dc = new DateChanger();
		
		lDay = new DayList();
		Cursor c = db.query("day_table", new String[] {"date", "balance"},
				null, null, null, null, null);
		
		boolean isEOF = c.moveToFirst();
		while (isEOF) {
			lDay.AddData(new DayData(dc.ChangeToCalendar(c.getString(0)), c.getInt(1)));
		    isEOF = c.moveToNext();
		}
		c.close();
			 
		db.close();
	}
	
	public void LoadItemDataFromDB(){
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor c = db.query("item_table", new String[] {"date", "name", "price"},
				null, null, null, null, null);
		
		boolean isEOF = c.moveToFirst();
		while (isEOF) {
			lDay.AddItemData(new ItemData(c.getString(1), c.getInt(2), c.getString(0)));
		    isEOF = c.moveToNext();
		}
		c.close();
			 
		db.close();
	}
	
	public void SaveDayDataToDB(){
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		
		db.delete("day_table", null, null);
		
		for(int i = 0; i < lDay.GetListSize(); i++){
			DayData day = lDay.GetData(i);
			ContentValues values = new ContentValues();
			values.put("date", day.GetStringDate());
			values.put("balance", day.GetBalance());
			db.insert("day_table", null, values);
		}
			 
		db.close();
	}
	
	public void SaveItemDataToDB(){
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		
		db.delete("item_table", null, null);
		
		for(int i = 0; i < lDay.GetListSize(); i++){
			DayData day = lDay.GetData(i);
			for(int j = 0; j < day.GetItemSize(); j++){
				ItemData item = day.GetItemList().get(j);
				ContentValues values = new ContentValues();
				values.put("date", item.GetStringDate());
				values.put("name", item.GetItem());
				values.put("price", item.GetPrice());
				db.insert("item_table", null, values);
			}
		}
			 
		db.close();
	}
	
	public void InsertDayDataToDB(DayData dayData){
	}
	
	public void InsertItemDataToDB(ItemData itemData, int positonInList){
	}
	
	public void DeleteDayDataFromDB(DayData dayData){
	}
	
	public void DeleteItemDataFromDB(ItemData itemData, int positonInList){
	}
}
