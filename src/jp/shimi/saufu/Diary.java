package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import jp.shimi.saifu.dialog.CalendarDialogFragment;
import jp.shimi.saifu.dialog.CheckDialogFragment;
import jp.shimi.saifu.dialog.CheckNameDialogFragment;
import jp.shimi.saifu.dialog.DayDeletedListener;
import jp.shimi.saifu.dialog.DayItemDeletedListener;
import jp.shimi.saifu.dialog.EditItemDialog;
import jp.shimi.saifu.dialog.EditItemDialogFragment;
import jp.shimi.saifu.setting.Preferences;

import android.os.Bundle;
import android.DB.MySQLiteAdapter;
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

public class Diary extends FragmentActivity 
implements OnClickListener, DayDeletedListener, DayItemDeletedListener,
ItemAdapter.MoveItemListener, CheckNameDialogFragment.ClickedNamePositiveButtonListener{
	private Button button1;
	private ListView listView;
	private DayList lDay = new DayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		
		LoadDayDataFromDB();
		LoadItemDataFromDB();
		
		lDay.UpdateBalance(0);
		
		button1 = (Button)findViewById(R.id.addButton1);
        button1.setOnClickListener(this);
        
        listView = (ListView)findViewById(R.id.diaryListView);
        
        // 初期残高設定ダイアログ
        if(lDay.GetListSize() == 0){
        	EditItemDialog dialog = new EditItemDialog(this, Calendar.getInstance(), 0, "初期残高", 1, 1, 0);
    		dialog.CreateDialog();
        }
        
        // リストビューの表示
        UpdateListViewAndScroll(lDay.GetListSize() - 1);
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
			case R.id.dayjump_calendar:
				CalendarDialogFragment newFragment;
				newFragment = CalendarDialogFragment.newInstance();
				newFragment.show(getFragmentManager(), "calendar_dialog");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onClick(View v){
		EditItemDialog dialog = new EditItemDialog(this, Calendar.getInstance());
		dialog.CreateDialog();
	}	
	
	private void UpdateListView(){
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		adapter.setDayDeletedListener(this);
		adapter.setDayItemDeletedListener(this);
		adapter.setMoveItemListener(this);
		listView.setAdapter(adapter);
	}
	
	//　listViewの表示をlDayの状態と同期し、positionの項目までスクロールする
	private void UpdateListViewAndScroll(int position){
		UpdateListView();
			
		if(position > listView.getCount() - 1){
			listView.setSelection(listView.getCount() - 1);
		}
		else if(position < 0){
			listView.setSelection(0);
		}
		else{
			listView.setSelection(position);
		}
	}
	
	//　listViewの表示をlDayの状態と同期し、保持していた元の位置までスクロールする
	private void UpdateListViewWithNoScroll(){
		if(listView.getChildCount() > 0){
			int position = listView.getFirstVisiblePosition();
			int yOffset = listView.getChildAt(0).getTop();
		
			UpdateListView();
		
			listView.setSelectionFromTop(position, yOffset);
		}
	}
	
	/**
	 * EditItemDialogで返される値を受け取る
	 * @param itemData 変更後のデータ
	 * @param initDate 変更前の日付
	 * @param editPosition 
	 */
	public void onReturnValue(ItemData itemData, Calendar initDate, int editPosition){
		int d = lDay.GetDataPositionByDate(itemData.GetDate());
		if(d < 0){ 
			DayData newDay = new DayData(itemData.GetDate(), 0);
			lDay.AddDataByDate(newDay);
			InsertDayDataToDB(newDay);
			Log.d("InsertDayData", newDay.GetStringDate()+" Passed");
		}
		
		/*if(lDay.ItemIsExist(itemData.GetDate(), itemData.GetItem())){
			String title = "警告";
			String text = itemData.GetStringDate() + "には既に" + itemData.GetItem() + "が存在します。\n"
				+ "項目名を再編集してください。";
			
			CheckNameDialogFragment newFragment;
			newFragment = CheckNameDialogFragment.newInstance(title, text, itemData, editPosition);
			newFragment.setClickedNamePositiveButtonListener(this);
			newFragment.show(getFragmentManager(), "name_check_dialog");
		}*/
		//else{
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
	
			// 項目が編集された日にスクロールする
			UpdateListViewAndScroll(lDay.GetDataPositionByDate(itemData.GetDate()));
		
			if(!initDate.equals(itemData.GetDate())){
				UpdateDayDataToDB(lDay.GetData(initDate));
			}
			UpdateDayDataToDB(lDay.GetData(itemData.GetDate()));
		//}
	}
	
	@Override
	public void ClickedNamePositiveButton(ItemData itemData, int editPosition) {
		EditItemDialog dialog = new EditItemDialog(this, itemData, editPosition);
		dialog.CreateDialog();
	}
	
	@Override
	public void DayDeleted(Calendar deletedDate) {
		lDay.UpdateBalance(lDay.GetNextDate(deletedDate));
		
		//　削除された日の前日にスクロールする
		UpdateListViewAndScroll(lDay.GetDataPositionByDate(lDay.GetBeforeDate(deletedDate)));
		
		DeleteDayDataFromDB(deletedDate);
	}	
	
	@Override
	public void DayItemDeleted(Calendar deletedDate) {
		lDay.CheckItemListSize();
		lDay.UpdateBalance(deletedDate);

		UpdateListViewWithNoScroll();
		
		int position = lDay.GetDataPositionByDate(deletedDate);
		if(position == -1){
			DeleteDayDataFromDB(deletedDate);
		}
		else{
			UpdateDayDataToDB(lDay.GetData(deletedDate));
		}
	}
	
	@Override
	public void upItem(ItemData item, int itemPosition) {
		lDay.GetData(item.GetDate()).UpItemPosition(itemPosition);
		UpdateListViewWithNoScroll();
		UpdateDayDataToDB(lDay.GetData(item.GetDate()));
	}

	@Override
	public void downItem(ItemData item, int itemPosition) {
		lDay.GetData(item.GetDate()).DownItemPosition(itemPosition);
		UpdateListViewWithNoScroll();
		UpdateDayDataToDB(lDay.GetData(item.GetDate()));
	}
	
	/*public boolean ItemIsExistInDay(Calendar date, String name){
		return lDay.ItemIsExist(date, name);
	}*/
	
	public void LoadDayDataFromDB(){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		lDay = dbAdapter.loadDayData();
	}
	
	public void LoadItemDataFromDB(){		
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		for(int i = 0; i < lDay.GetListSize(); i++){
			Calendar date = lDay.GetData(i).GetDate();
			lDay.SetItemList(date, dbAdapter.loadItemData(date));
		}
	}
	
	public void InsertDayDataToDB(DayData dayData){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		dbAdapter.insertDayData(dayData);
	}
	
	public void InsertItemDataToDB(ItemData itemData, int sequence){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		dbAdapter.insertItemData(itemData, sequence);
	}
	
	// 渡した日データのアイテムリストでアイテムテーブル内の指定日のデータを再初期化する。
	public void UpdateDayDataToDB(DayData dayData){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		DeleteDayDataFromDB(dayData.GetDate());
		InsertDayDataToDB(dayData);
		dbAdapter.insertItemList(dayData);
	}
	
	public void DeleteDayDataFromDB(Calendar date){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		DateChanger dc = new DateChanger();
		
		dbAdapter.deleteDayData(dc.ChangeToString(date));
	}
	
	public void DeleteItemDataFromDB(ItemData itemData, int sequence){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		dbAdapter.deleteItemData(itemData.GetStringDate(), sequence);
	}
	
	public void ReinitializationToDB(){
		MySQLiteAdapter dbAdapter = new MySQLiteAdapter(this);
		
		dbAdapter.saveDayList(lDay);
	}
}
