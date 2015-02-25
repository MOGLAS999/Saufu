package jp.shimi.saufu;

import java.sql.Date;
import java.util.Calendar;

import android.util.Log;

public class ItemData{
	public String item;
	public int price;
	public Calendar date;
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
	
	public String GetStringDate(){
		return dc.ChangeToString(this.date);
	}
}