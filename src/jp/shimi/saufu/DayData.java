package jp.shimi.saufu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayData{
	private Calendar date;
	private List<ItemData> itemList;
	private int balance;
	private DateChanger dc;
	
	DayData(){
		this.date = Calendar.getInstance();
		this.date.setTime(dc.ChangeToDate("2000/01/01"));
		this.itemList = new ArrayList<ItemData>();
		this.balance = 0;
		this.dc = new DateChanger();
	}
	
	DayData(Calendar date, int balance){
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
	
	public String GetStringDate(){
		return dc.ChangeToString(this.date);
	}
	
	public String GetStringBalance(){
		return Integer.toString(this.balance);
	}
	
	public void AddItem(ItemData newItem){
		this.itemList.add(newItem);
	}
}