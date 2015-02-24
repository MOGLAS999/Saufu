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
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	
	DayData(){
		try{
			this.date.setTime(df.parse("2000/01/01"));
		} catch (java.text.ParseException e) {
			this.date = null;
		}
		this.itemList = new ArrayList<ItemData>();
		this.balance = 0;
	}
	
	DayData(Calendar date, int balance){
		this.date = date;
		this.itemList = new ArrayList<ItemData>();
		this.balance = balance;
	}
	
	public String GetStringDate(){
		return df.format(this.date);
	}
	
	public String GetStringBalance(){
		return Integer.toString(this.balance);
	}
}