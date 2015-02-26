package jp.shimi.saufu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;


public class DayList {
	private  List<DayData> dataList;
	
	public DayList(){
		this.dataList = new ArrayList<DayData>();
	}
	
	public DayList(List<DayData> dataList){
		this.dataList = dataList;
	}
	
	public List<DayData> GetList(){
		return this.dataList;
	}
	
	// 末尾にデータを追加
	public void AddData(DayData fd){
		this.dataList.add(fd);
	}
	
	// 指定した位置にデータを追加
	public void AddData(int index, DayData fd){
		this.dataList.add(index, fd);
	}
	
	// 末尾にリストをデータとして追加
	public void AddList(DayList addList){
		this.dataList.addAll(addList.dataList);
	}
		
	// 指定した位置にリストをデータとして追加
	public void AddList(int index, DayList addList){
		this.dataList.addAll(index, addList.dataList);
	}
		
	// 指定した位置にデータを上書き
	public void SetData(int index, DayData newData){
		this.dataList.set(index, newData);
	}
	
	// 指定した位置のデータを削除
	public void RemoveData(int index){
		this.dataList.remove(index);
	}
	
	// データを全削除
	public void ClearList(){
		this.dataList.clear();
	}
		
	// 指定した位置のデータを返す
	public DayData GetData(int index){
		return this.dataList.get(index);
	}
	
	// データの個数を返す
	public int GetListSize(){
		return this.dataList.size();
	}
	
	// 指定した日付のデータの位置を返す
	public int GetDayData(Calendar date){
		DateChanger dc = new DateChanger();
		for(int i = 0; i < this.dataList.size(); i++){
			Log.d("GetDayData", dc.ChangeToString(this.dataList.get(i).GetDate()) +"=?"+ dc.ChangeToString(date));
			if(dc.ChangeToString(this.dataList.get(i).GetDate()).equals(dc.ChangeToString(date))){
				Log.d("Check", "if pass");
				return i;
			}
		}
		return -1;
	}
	
	// 指定した日データの日付のデータを上書き
	public void UpdateData(DayData newData){
		for(int i = 0; i < this.dataList.size(); i++){
			if(this.dataList.get(i).GetDate() == newData.GetDate()){
				this.dataList.set(i, newData);
			}
		}
	}
	
	// 指定した日にアイテムデータを追加する
	public void AddItemData(Calendar date, ItemData newItem){
		int pos = GetDayData(date);
		if(pos < 0){
			Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).AddItem(newItem);
		}
	}
}
