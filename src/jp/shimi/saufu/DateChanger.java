package jp.shimi.saufu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class DateChanger {
	private SimpleDateFormat df;
	
	DateChanger(){
		this.df = new SimpleDateFormat("yyyy/MM/dd");
	}
	
	public String ChangeToString(Date cDate){
		return this.df.format(cDate);
	}
	
	public String ChangeToString(Calendar cDate){
		return this.df.format(cDate.getTime());
	}
	
	public Date ChangeToDate(String sDate){
		try{
			return this.df.parse(sDate);
		} catch (java.text.ParseException e) {
			Log.d("PerseError", sDate);
			return null;
		}
	}
}
