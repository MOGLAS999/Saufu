package android.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
	static final private String DB_NAME = "Saifu.db";
	static final private int DB_VERSION = 1;

	public MySQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table day_table(" +
				"date text not null," +
				"balance integer" +
				//"difference integer not null" +
				");"
		);
		
		db.execSQL("create table item_table(" +
				"date text not null,"+
				"name text,"+
				"price integer"+
				");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table day_table");
		db.execSQL("drop table item_table");
        onCreate(db);
	}

}
