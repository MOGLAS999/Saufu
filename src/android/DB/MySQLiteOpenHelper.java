package android.DB;

import jp.shimi.saufu.DayData;
import jp.shimi.saufu.ItemData;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
	static final private String DB_NAME = "Saifu.db";
	static final private String DAY_TABLE_NAME = "day_table";
	static final private String ITEM_TABLE_NAME = "item_table";
	static final private int DB_VERSION = 2;
	

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
				"order integer"+
				");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < newVersion){
			 if(oldVersion == 1){
				 db.execSQL("alter table " + ITEM_TABLE_NAME +
						 " add order integer default 0"
						 );
			 }
		}
		
		//db.execSQL("drop table " + DAY_TABLE_NAME);
		//db.execSQL("drop table " + ITEM_TABLE_NAME);
        //onCreate(db);
	}
	
	/*public void insertDayData(DayData dayData){
		ContentValues values = new ContentValues();  
        values.put("date", dayData.GetStringDate());  
        values.put("balance", dayData.GetBalance());   
		
		insert(DAY_TABLE_NAME, null, values);
	}
	
	public void insertItemData(ItemData itemData, int order){
		
	}*/

}
