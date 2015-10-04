package android.DB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.shimi.saufu.DateChanger;
import jp.shimi.saufu.DayData;
import jp.shimi.saufu.DayList;
import jp.shimi.saufu.ItemData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteAdapter {
	static final private String DB_NAME = "Saifu.db";
	static final private String DAY_TABLE_NAME = "day_table";
	static final private String ITEM_TABLE_NAME = "item_table";
	static final private int DB_VERSION = 3;
	
	Context context;
	MySQLiteOpenHelper DBHelper;
	SQLiteDatabase db;
	
	public MySQLiteAdapter(Context context){
		this.context = context;
		this.DBHelper = new MySQLiteOpenHelper(context);
		this.db = DBHelper.getWritableDatabase();
	}
	
	// デストラクタ処理(SQLiteDatabaseの解放)
	@Override
	protected void finalize() throws Throwable{
		try {
			super.finalize();
		}finally{
			destruction();
		}
	}
	
	private void destruction(){
		this.db.close();
	}
	
	private static class MySQLiteOpenHelper extends SQLiteOpenHelper{
		public MySQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + DAY_TABLE_NAME +"(" +
					"date text not null," +
					"balance integer" +
					//"difference integer not null" +
					");"
			);
			
			db.execSQL("create table " + ITEM_TABLE_NAME +"(" +
					"date text not null,"+
					"name text,"+
					"price integer,"+
					"number integer,"+
					"category integer"+
					"sequence integer"+
					");"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion < newVersion){
				 if(oldVersion <= 2){
					 db.execSQL("alter table " + ITEM_TABLE_NAME +
							 " add sequence integer default 0;"
							 );
				 }
				 
				 /*if(oldVersion == 2){
					 db.execSQL("alter table " + ITEM_TABLE_NAME +
							 " drop column order;"
							 );
				 }*/
			}
			
			//db.execSQL("drop table " + DAY_TABLE_NAME);
			//db.execSQL("drop table " + ITEM_TABLE_NAME);
	        //onCreate(db);
		}
	}
	
	public ContentValues getDayDataContentValues(DayData dayData){
		ContentValues values = new ContentValues();  
        values.put("date", dayData.GetStringDate());  
        values.put("balance", dayData.GetBalance()); 
        
        return values;
	}
	
	public ContentValues getItemDataContentValues(ItemData itemData, int sequence){
		ContentValues values = new ContentValues();  
        values.put("date", itemData.GetStringDate());  
        values.put("name", itemData.GetItem());
        values.put("price", itemData.GetPrice());
        values.put("number", itemData.GetNumber());
        values.put("category", itemData.GetCategory());
        values.put("sequence", sequence);
        
        return values;
	}
	
	public void insertDayData(DayData dayData){
		long recodeCount = DatabaseUtils.queryNumEntries(db, DAY_TABLE_NAME, 
				"date = '" + dayData.GetStringDate() +"'");
		
		if(recodeCount == 0){
			db.insert(DAY_TABLE_NAME, null, getDayDataContentValues(dayData));
		}
	}
	
	public void updateDayData(DayData dayData){
		db.update(DAY_TABLE_NAME, getDayDataContentValues(dayData), 
				"date = '" + dayData.GetStringDate() + "'", null);
	}
	
	public void deleteDayData(String deletedDate){
		db.delete(DAY_TABLE_NAME, "date == '" + deletedDate + "'", null);
		
		deleteItemDataByDate(deletedDate);
	}
	
	public void insertItemData(ItemData itemData, int order){		
		db.insert(ITEM_TABLE_NAME, null, getItemDataContentValues(itemData, order));
	}
	
	public void updateItemData(ItemData itemData, int sequence){
		db.update(ITEM_TABLE_NAME, getItemDataContentValues(itemData, sequence), 
				"date = '" + itemData.GetStringDate() + "'" +
						" AND sequence = " + Integer.toString(sequence), null);
	}
	
	public void deleteItemData(String deletedDate, int deleteSequence){
		db.delete(ITEM_TABLE_NAME, "date = '" + deletedDate + "'" 
				+ " AND sequence = " + Integer.toString(deleteSequence), null);
		
		updateItemDataOrder(deletedDate);
	}
	
	public void deleteItemDataByDate(String deletedDate){
		db.delete(ITEM_TABLE_NAME, "date = '" + deletedDate + "'", null);
	}
	
	public void updateItemDataOrder(String date){
		Cursor c = db.query(ITEM_TABLE_NAME, 
				new String[] {"date", "sequence"},
				"date = '" + date + "'", null, null, null, "sequence ASC");
		
		boolean isEOF = c.moveToFirst();
		int sequenceCounter = 0;
		String where;
		while (isEOF) {
			ContentValues values = new ContentValues();
			values.put("sequence", sequenceCounter);   
		
	        where = "date = " + c.getString(0) + "' AND sequence = " + c.getInt(1) + "'";
			db.update(ITEM_TABLE_NAME, values, where, null);
			
			sequenceCounter++;
		    isEOF = c.moveToNext();
		}
		c.close();
			 
		db.close();
		
	}
	
	public DayList loadDayData(){
		DayList dayList = new DayList();
		DateChanger dc = new DateChanger();
		
		dayList = new DayList();
		Cursor c = db.query(DAY_TABLE_NAME, new String[] {"date", "balance"},
				null, null, null, null, "date ASC");
		
		boolean isEOF = c.moveToFirst();
		while (isEOF) {
			dayList.AddData(new DayData(dc.ChangeToCalendar(c.getString(0)), c.getInt(1)));
		    isEOF = c.moveToNext();
		}		
		c.close();
		
		return dayList;
	}
	
	public List<ItemData> loadItemData(Calendar date){
		List<ItemData> itemList = new ArrayList<ItemData>();
		DateChanger dc = new DateChanger();
		
		Cursor c = db.query(ITEM_TABLE_NAME, 
				new String[] {"date", "name", "price", "number", "category", "sequence"},
				"date == '" + dc.ChangeToString(date) + "'", null, null, null, "sequence ASC");
		
		boolean isEOF = c.moveToFirst();
		while (isEOF) {
			itemList.add(new ItemData(c.getString(1), c.getInt(2), c.getString(0), 
					c.getInt(3), c.getInt(4)));
		    isEOF = c.moveToNext();
		}
		c.close();
		
		return itemList;
	}
	
	public void insertDayList(DayList dayList){
		db.delete("day_table", null, null);
		
		for(int i = 0; i < dayList.GetListSize(); i++){
			DayData day = dayList.GetData(i);
			ContentValues values = new ContentValues();
			values.put("date", day.GetStringDate());
			values.put("balance", day.GetBalance());
			db.insert(DAY_TABLE_NAME, null, values);
		}
	}
	
	public void insertItemList(DayData dayData){
		for(int i = 0; i < dayData.GetItemSize(); i++){
			ItemData item = dayData.GetItemList().get(i);
			ContentValues values = new ContentValues();
			values.put("date", item.GetStringDate());
			values.put("name", item.GetItem());
			values.put("price", item.GetPrice());
			values.put("number", item.GetNumber());
			values.put("category", item.GetCategory());
			values.put("sequence", i);
			db.insert(ITEM_TABLE_NAME, null, values);
		}
	}
	
	public void saveDayList(DayList dayList){
		db.execSQL("drop table " + DAY_TABLE_NAME);
		db.execSQL("drop table " + ITEM_TABLE_NAME);
		this.DBHelper.onCreate(db);
		
		insertDayList(dayList);
		
		for(int i = 0; i < dayList.GetListSize(); i++){
			insertItemList(dayList.GetData(i));
		}
	}
}
