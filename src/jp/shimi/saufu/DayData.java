package jp.shimi.saufu;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayData{
	public Date date;
	public List<ItemData> dataList;
	public int balance;
	
	DayData(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		this.date = df.parse("2000/01/01", new ParsePosition(0));
		this.dataList = new ArrayList<ItemData>();
		this.balance = 0;
	}
	
	DayData(Date date, int balance){
		this.date = date;
		this.dataList = new ArrayList<ItemData>();
		this.balance = balance;
	}
}