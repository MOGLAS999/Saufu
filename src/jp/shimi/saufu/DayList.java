package jp.shimi.saufu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DayList {
	public List<DayData> dataList;
	
	public DayList(){
		this.dataList = new ArrayList<DayData>();
	}
	
	public DayList(List<DayData> dataList){
		this.dataList = dataList;
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
	
	// 指定した日付のデータを返す
	public DayData GetDayData(Calendar date){
		for(int i = 0; i < this.dataList.size(); i++){
			if(this.dataList.get(i).date == date){
				return this.dataList.get(i);
			}
		}
		return null;
	}
	
	// 指定した日データの日付のデータを上書き
	public void UpdateData(DayData newData){
		for(int i = 0; i < this.dataList.size(); i++){
			if(this.dataList.get(i).date == newData.date){
				this.dataList.set(i, newData);
			}
		}
	}
	
	// 指定した日にアイテムデータを追加する
	public void AddItemData(Calendar date, ItemData newItem){
		this.GetDayData(date).AddItem(newItem);
	}
}
