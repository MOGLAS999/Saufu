package jp.shimi.saufu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
	 final static private int DB_VERSION = 1;
	 
	 public DatabaseOpenHelper(Context context){
		 super(context, null, null, DB_VERSION);
	 }
	 
	 @Override
	 public void onCreate(SQLiteDatabase db){
		 db.execSQL(
			 "create table item_table("+
		     " item text not null,"+
		     " price integer"+
		     " date text"+
		     ");"
		 );
		 
		 db.execSQL("insert into item_table(item,price,date) values ('漫画', 500, '2014/12/20');");
	 }
	 
	 @Override
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	     // データベースの変更が生じた場合は、ここに処理を記述する。
	 }
}