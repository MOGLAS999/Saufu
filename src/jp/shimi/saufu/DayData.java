package jp.shimi.saufu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.util.Log;

/**
 * 日ごとのデータ
 * @param date 日付
 * @param　itemList 項目のリスト
 * @param balance 残金
 */
public class DayData{
	private Calendar date;
	private List<ItemData> itemList;
	private int balance;
	private DateChanger dc;
	
	public DayData(){
		this.date = Calendar.getInstance();
		this.date.setTime(dc.ChangeToDate("2000/01/01"));
		this.itemList = new ArrayList<ItemData>();
		this.balance = 0;
		this.dc = new DateChanger();
	}
	
	public DayData(Calendar date, int balance){
		this.date = date;
		this.itemList = new ArrayList<ItemData>();
		this.balance = balance;
		this.dc = new DateChanger();
	}

	public Calendar GetDate(){
		return this.date;
	}
	
	public List<ItemData> GetItemList(){
		return this.itemList;
	}
	
	public int GetBalance(){
		return this.balance;
	}
	
	public int SetBalance(int balance){
		return this.balance = balance;
	}
	
	public void AddBalance(int price){
		this.balance += price;
	}
	
	public String GetStringDate(){
		return dc.ChangeToString(this.date);
	}
	
	public String GetStringBalance(){
		return Integer.toString(this.balance);
	}
	
	public void AddItem(ItemData newItem){
		this.itemList.add(newItem);
	}
	
	public void AddItem(ItemData newItem, int position){
		this.itemList.add(position, newItem);
	}
	
	public void SetItem(int index, ItemData newItem){
		this.itemList.set(index, newItem);
	}
	
	public void SetItemList(List<ItemData> itemList){
		this.itemList = itemList;
	}
	
	public void RemoveItem(int index){
		this.itemList.remove(index);
	}
	
	public int GetItemSize(){
		return this.itemList.size();
	}
	
	public int GetDifference(){
		int dif = 0;
		for(int i = 0; i < this.itemList.size(); i++){
			dif += this.itemList.get(i).GetPrice();
		}
		return dif;
	}
	
	public void ExchangeItemPosition(int pos1, int pos2){
		ItemData tmp = this.itemList.get(pos1);
		this.itemList.set(pos1, this.itemList.get(pos2));
		this.itemList.set(pos2, tmp);
	}
	
	public void UpItemPosition(int position){
		if(this.itemList.size() > 1 && position > 0){
			ExchangeItemPosition(position, position - 1);
		}
	}
	
	public void DownItemPosition(int position){
		if(this.itemList.size() > 1 && position < this.itemList.size() - 1){
			ExchangeItemPosition(position, position + 1);
		}
	}
	
	public boolean ItemIsExist(String name){
		for(int i = 0; i < this.itemList.size(); i++){
			if(this.itemList.get(i).GetItem().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public void ShowListLog(){
		for(int i = 0; i < this.itemList.size(); i++){
			Log.d("ShowListLog", i+":"+this.itemList.get(i).GetItem()+":"+this.itemList.get(i).GetPrice());
		}
	}

}