package jp.shimi.saufu;

import java.util.Calendar;

import android.util.Log;

public class ItemData{
	private String item;
	private int price;
	private Calendar date;
	private DateChanger dc;
	
	ItemData(){
		this.item = "";
		this.price = 0;
		this.date = Calendar.getInstance();
		this.date.setTime(dc.ChangeToDate("2000/01/01"));
		this.dc = new DateChanger();
	}
	
	ItemData(String item, int price, String date){
		this.item = item;
		this.price = price;
		this.dc = new DateChanger();
		this.date = Calendar.getInstance();
		if(dc.ChangeToDate(date) == null){
			this.date.setTime(dc.ChangeToDate("2000/01/01"));
		}
		else{
			this.date.setTime(dc.ChangeToDate(date));
		}
	}
	
	public String GetItem(){
		return this.item;
	}
	
	public int GetPrice(){
		return this.price;
	}
	
	public Calendar GetDate(){
		return this.date;
	}
	
	public String GetStringDate(){
		return dc.ChangeToString(this.date);
	}
}