package jp.shimi.saufu;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.net.ParseException;

public class DayData{
	public Calendar date;
	public List<ItemData> itemList;
	public int balance;
	private DateChanger dc;
	
	DayData(){
		this.date.setTime(dc.ChangeToDate("2000/01/01"));
		this.itemList = new ArrayList<ItemData>();
		this.balance = 0;
	}
	
	DayData(Calendar date, int balance){
		this.date = date;
		this.itemList = new ArrayList<ItemData>();
		this.balance = balance;
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