package android.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDao{
	private static final String TABLE_NAME = "dbItems";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_DATE = "date";
	private static final String COLUMN_ITEM = "item";
	private static final String COLUMN_PRICE = "price";
	private static final String[] COLUMNS = {
		COLUMN_ID, COLUMN_DATE, COLUMN_ITEM, COLUMN_PRICE};
	
	private SQLiteDatabase db;
	
	public DBDao(SQLiteDatabase db){
		this.db = db;
	}
	
	/** 全データの取得 */
	public List<DBEntity> findAll(){
		List<DBEntity> entityList = new ArrayList<DBEntity>();
		Cursor cursor = db.query(
				TABLE_NAME, 
				COLUMNS,
				null,
				null,
				null,
				null,
				COLUMN_ID);
		
		while(cursor.moveToNext()){
			DBEntity entity = new DBEntity();
			entity.setId(cursor.getInt(0));
			entity.setDate(cursor.getString(1));
			entity.setItem(cursor.getString(2));
			entity.setPrice(cursor.getInt(3));
			entityList.add(entity);
		}
		
		return entityList;
	}
	
	/** 特定IDのデータを取得 */
	public DBEntity findById(int id){
		String selection = COLUMN_ID + "=" + id;
		Cursor cursor = db.query(
				TABLE_NAME, 
				COLUMNS,
				selection,
				null,
				null,
				null,
				null);
		
		cursor.moveToNext();
		DBEntity entity = new DBEntity();
		entity.setId(cursor.getInt(0));
		entity.setDate(cursor.getString(1));
		entity.setItem(cursor.getString(2));
		entity.setPrice(cursor.getInt(3));
		
		return entity;
	}
	
	/**
     * データの登録
     * @param data
     * @return
     */
    public long insert(String date, String item, int price) {
    	ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_ITEM, item);
        values.put(COLUMN_PRICE, price);
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * データの更新 
     * @param rowid
     * @param date
     * @return
     */
    public int update(DBEntity entity) {
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_DATE, entity.getDate());
        values.put(COLUMN_ITEM, entity.getItem());
        values.put(COLUMN_PRICE, entity.getPrice());
        String whereClause = COLUMN_ID + "=" + entity.getId();
        return db.update(TABLE_NAME, values, whereClause, null);
    }

    /**
     * データの削除
     * @param rowId
     * @return
     */
    public int delete(int rowId) {
        String whereClause = COLUMN_ID + "=" + rowId;
        return db.delete(TABLE_NAME, whereClause, null);
    }
}