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
	public void AddData(DayData dd){
		this.dataList.add(dd);
		UpdateBalance(GetListSize()-1);
	}
	
	// 指定した位置にデータを追加
	public void AddData(int index, DayData dd){
		this.dataList.add(index, dd);
		UpdateBalance(index);
	}
	
	// 日付に基づいた位置にデータを追加
	public void AddDataByDate(DayData dd){
		for(int i = 0; i < this.dataList.size(); i++){
			if(dd.GetDate().compareTo(this.GetData(i).GetDate()) < 0){
				this.dataList.add(i, dd);
				UpdateBalance(i);
				return;
			}
		}
		this.AddData(dd);
		UpdateBalance(GetListSize()-1);
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
		UpdateBalance(index);
	}
	
	// データを全削除
	public void ClearList(){
		this.dataList.clear();
	}
		
	// 指定した位置のデータを返す
	public DayData GetData(int index){
		if(index < 0 || index >= this.GetListSize())
			return null;
		return this.dataList.get(index);
		
	}
	
	public DayData GetData(Calendar date){
		int pos = GetDataPositionByDate(date);
		if(pos == -1){
			return null;
		}else{
			return this.dataList.get(pos);
		}
	}
	
	// データの個数を返す
	public int GetListSize(){
		return this.dataList.size();
	}
	
	// 指定した位置のデータの持つアイテムの数を返す
	public int GetItemListSize(int index){
		return this.GetData(index).GetItemList().size();
	}
	
	// 指定した日付のデータの位置を返す
	public int GetDataPositionByDate(Calendar date){
		DateChanger dc = new DateChanger();
		for(int i = 0; i < this.dataList.size(); i++){
			//Log.d("GetDayData", dc.ChangeToString(this.dataList.get(i).GetDate()) +"=?"+ dc.ChangeToString(date));
			if(dc.ChangeToString(this.dataList.get(i).GetDate()).equals(dc.ChangeToString(date))){
				//Log.d("Check", "if pass");
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
				UpdateBalance(i);
			}
		}
	}
	
	// 指定した日にアイテムデータを追加する
	public void AddItemData(Calendar date, ItemData newItem){
		int pos = GetDataPositionByDate(date);
		if(pos < 0){
			//Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).AddItem(newItem);
			UpdateBalance(pos);
		}
	}
	
	public void AddItemData(ItemData newItem){
		int pos = GetDataPositionByDate(newItem.GetDate());
		if(pos < 0){
			//Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).AddItem(newItem);
			UpdateBalance(pos);
		}
	}
	
	// 指定した日の指定した位置にアイテムデータを追加する
	public void AddItemData(Calendar date, ItemData newItem, int position){
		int pos = GetDataPositionByDate(date);
		if(pos < 0){
			//Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).AddItem(newItem, position);
			UpdateBalance(pos);
		}
	}
	
	// 指定した日にアイテムデータを上書きする
	public void SetItemData(Calendar date, ItemData newItem, int itemPos){
		int pos = GetDataPositionByDate(date);
		if(pos < 0){
			//Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).SetItem(itemPos, newItem);
			UpdateBalance(pos);
		}
	}
	
	// 指定した日にアイテムリストを上書きする
	public void SetItemList(Calendar date, List<ItemData> itemList){
		this.GetData(date).SetItemList(itemList);
	}
	
	// 指定した日の指定した位置のアイテムデータを削除する
	public void RemoveItemData(Calendar date, int itemPos){
		int pos = GetDataPositionByDate(date);
		if(pos < 0){
			//Log.d("AddItemData", "Error pos == "+ pos);
		}
		else{
			this.dataList.get(pos).RemoveItem(itemPos);
			UpdateBalance(pos);
		}
	}
	
	// 指定した日付(位置)以降の残金を計算する
	public void UpdateBalance(int index){
		if(index < 0){
			//Log.d("UpdateBalance", "error");
		}else{
			for(int i = index; i < GetListSize(); i++){
				Log.d("UpdateBalance", ""+GetData(i).GetItemSize());
				int balance;
				if(i == 0) balance = 0;
				else balance = GetData(i-1).GetBalance(); // 指定した日付以前の日の残金
				for(int j = 0; j < GetData(i).GetItemList().size(); j++){
					balance += GetData(i).GetItemList().get(j).GetTotalPrice();
				}
				GetData(i).SetBalance(balance);
			}
		}
	}
	
	public void UpdateBalance(Calendar changedDate){
		UpdateBalance(GetDataPositionByDate(changedDate));
	}
	
	// データのアイテムリストのサイズを確認し、サイズが0なら削除してそれ以降を再計算する
	public void CheckItemListSize(){
		int zeroPos = -1;
		for(int i = 0; i < GetListSize(); i++){
			if(GetItemListSize(i) <= 0){
				RemoveData(i);
				if(zeroPos == -1){
					zeroPos = i;
				}
			}
		}
		UpdateBalance(zeroPos);
	}
	
	// 指定した日付の次の日付を返す
	public Calendar GetNextDate(Calendar date){
		if(GetListSize() == 0) return null;
		if(GetListSize() == 1) return GetData(0).GetDate();
		for(int i = 1; i < GetListSize(); i++){
			DayData d1 = GetData(i-1);
			DayData d2 = GetData(i);
			
			if(d1.GetDate().equals(date)){
				return d2.GetDate();
			}
			else if(d1.GetDate().before(date) && d2.GetDate().after(date)){
				return d2.GetDate();
			}
		}
		return GetData(GetListSize()-1).GetDate();
	}
	
	// 指定した日付の前の日付を返す
	public Calendar GetBeforeDate(Calendar date){
		if(GetListSize() == 0) return null;
		if(GetListSize() == 1) return GetData(0).GetDate();
		for(int i = 1; i < GetListSize(); i++){
			DayData d1 = GetData(i-1);
			DayData d2 = GetData(i);
				
			if(d2.GetDate().equals(date)){
				return d1.GetDate();
			}
			else if(d1.GetDate().before(date) && d2.GetDate().after(date)){
				return d1.GetDate();
			}
		}
		return GetData(GetListSize() - 1).GetDate();
	}
	
	// 指定した日に指定した名前のアイテムがあるかどうかを返す
	public boolean ItemIsExist(Calendar date, String itemName){
		DayData dd = GetData(date);
		if(dd == null){
			return false;
		}
		else{
			if(dd.ItemIsExist(itemName)){
				return true;
			}
			else return false;
		}
	}
}
