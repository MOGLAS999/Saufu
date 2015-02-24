package jp.shimi.saufu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Diary extends Activity implements OnClickListener{

	private Button button1;
	private ListView listView;
	static List<ItemData> lItem = new ArrayList<ItemData>();
	static List<DayData> lDay = new ArrayList<DayData>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		
		button1 = (Button)findViewById(R.id.addButton1);
        button1.setOnClickListener(this);
        
        listView = (ListView)findViewById(R.id.diaryListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
		
		ItemAdapter adapter = new ItemAdapter();
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v){
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.diary_dialog,(ViewGroup)findViewById(R.id.diarydialog_layout));
        
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Title Test");
        adb.setView(layout);
        //adb.setMessage("ボタンが押されました。");
        
        // 日付選択テキストボックス＆ダイアログ
        final DateChanger dc = new DateChanger();
        final Calendar dCalendar = Calendar.getInstance();
    	final EditText dateEdit = (EditText)layout.findViewById(R.id.editDialogDate);
    	dateEdit.setText(dc.ChangeToString(dCalendar.getTime()));
    	dateEdit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						dCalendar.set(Calendar.YEAR, year);
						dCalendar.set(Calendar.MONTH, monthOfYear);
						dCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						dateEdit.setText(dc.ChangeToString(dCalendar.getTime()));
					}
				};
				
				final DatePickerDialog datePickerDialog = new DatePickerDialog(
						Diary.this, dateSetListener, 
						dCalendar.get(Calendar.YEAR), 
						dCalendar.get(Calendar.MONTH),
						dCalendar.get(Calendar.DAY_OF_MONTH));
				
				datePickerDialog.show();
			}
    	});
        // プラマイ切り替えボタン
        final Button plusMinusButton = (Button)layout.findViewById(R.id.plusMinusButton);
        plusMinusButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
        			plusMinusButton.setText(getResources().getString(R.string.plus));
        		}
        		else if((String)plusMinusButton.getText() == getResources().getString(R.string.plus)){
        			plusMinusButton.setText(getResources().getString(R.string.minus));
        		}
        		Log.d("PMButtonLog", (String) plusMinusButton.getText());
        	}
        });
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
				// 押された時の処理
            	EditText etxtItem   = (EditText)layout.findViewById(R.id.editDialogItem);
            	EditText etxtPrice  = (EditText)layout.findViewById(R.id.editDialogPrice);
            	
            	if(!etxtItem.getText().toString().equals("") &&
            			!etxtPrice.getText().toString().equals("")){
            		// 入力内容を取得
            		String strItem  = etxtItem.getText().toString();
            		String strPrice = etxtPrice.getText().toString();
            		//Log.d("Log", strItem + "/" + strPrice);
		    	
            		// 数値に変換
            		int price = Integer.parseInt(strPrice);
            		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
            			price *= (-1);
            		}
            		
            		ItemData itemData = new ItemData(strItem, price, dateEdit.getText().toString());
            		
            		
            		//lItem.add(new ItemData(strItem, price, dateEdit.getText().toString()));
		    	
            		ItemAdapter adapter = new ItemAdapter();
            		listView.setAdapter(adapter);
            	}
			}
		});
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
				// 押された時の処理
			}
		});
        adb.setCancelable(false);
        adb.show();
	}
	
	private class DayAdapter extends BaseAdapter {
	    @Override
	    public int getCount() {
	    	return lDay.size();
	    }

	    @Override
	    public Object getItem(int position) {
	    	return lDay.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	    	return position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	TextView textDate;
	    	ListView listItem;
	    	TextView textBalance;
	    	View v = convertView;

	    	if(v == null){
	    		LayoutInflater inflater = (LayoutInflater)
	            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		v = inflater.inflate(R.layout.day, null);
	    	}
	    	DayData day = (DayData)getItem(position);
	    	if(day != null){
	    		textDate = (TextView) v.findViewById(R.id.txtDate);
	    		listItem = (ListView) v.findViewById(R.id.lstItem);
	    		textBalance = (TextView) v.findViewById(R.id.txtBalance);
	        
	    		textDate.setText(day.GetStringDate());
	    		textBalance.setText(day.GetStringBalance());
	    		
	    		//listItem
	    	}
	    	return v;
	    }
	    
	}
	
	private class ItemAdapter extends BaseAdapter {
	    @Override
	    public int getCount() {
	    	return lItem.size();
	    }

	    @Override
	    public Object getItem(int position) {
	    	return lItem.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	    	return position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	TextView textView1;
	    	TextView textView2;
	    	TextView textView3;
	    	View v = convertView;

	    	if(v == null){
	    		LayoutInflater inflater = (LayoutInflater)
	            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		v = inflater.inflate(R.layout.row, null);
	    	}
	    	ItemData item = (ItemData)getItem(position);
	    	if(item != null){
	    		textView1 = (TextView) v.findViewById(R.id.textView1);
	    		textView2 = (TextView) v.findViewById(R.id.textView2);
	    		textView3 = (TextView) v.findViewById(R.id.textView3);
	        
	    		textView1.setText(item.GetStringDate());
	    		textView2.setText(item.item);
	    		String sign = "";
	    		if(item.price > 0) sign = "+";
	    		textView3.setText(sign + Integer.toString(item.price) + "円");
	    	}
	    	return v;
	    }
	    
	}

}
