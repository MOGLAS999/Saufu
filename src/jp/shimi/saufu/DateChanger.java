package jp.shimi.saufu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateChanger {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	
	public String ChangeToString(Date cDate){
		return df.format(cDate);
	}
	
	public String ChangeToString(Calendar cDate){
		return df.format(cDate);
	}
	
	public Date ChangeToDate(String sDate){
		try{
			return df.parse(sDate);
		} catch (java.text.ParseException e) {
			return null;
		}
	}
}
