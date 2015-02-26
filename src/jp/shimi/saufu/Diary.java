package jp.shimi.saufu;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Diary extends Activity implements OnClickListener{
	private Button button1;
	private ListView listView;
	private DayList lDay = new DayList();;
	
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
	
		//DayAdapter adapter = new DayAdapter();
		//listView.setAdapter(adapter);
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
		    	
            		// 数値に変換
            		int price = Integer.parseInt(strPrice);
            		if((String)plusMinusButton.getText() == getResources().getString(R.string.minus)){
            			price *= (-1);
            		}
            		
            		ItemData itemData = new ItemData(strItem, price, dateEdit.getText().toString());
            		int d = lDay.GetDayData(itemData.GetDate());
            		if(d < 0){ 
            			lDay.AddData(new DayData(itemData.GetDate(), 0));
            		}
            		lDay.AddItemData(itemData.GetDate(), itemData);
		    	
            		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
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
	
	private class DayAdapter extends ArrayAdapter<DayData> {		
		private LayoutInflater inflater;
		private class ViewHolder{
			TextView textDate;
    		ListView listItem;
    		TextView textBalance;
    		
    		ViewHolder(View view){
    			this.textDate = (TextView) view.findViewById(R.id.txtDate);
	    		this.listItem = (ListView) view.findViewById(R.id.lstItem);
	    		this.textBalance = (TextView) view.findViewById(R.id.txtBalance);
    		}
		} 
		
	    public DayAdapter(Context context, int textViewResourceId, List<DayData> objects) {
			super(context, textViewResourceId, objects);
			this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder;
	    	
	    	if(convertView == null){
	    		convertView = inflater.inflate(R.layout.day, null);
	    		holder = new ViewHolder(convertView);
	    		convertView.setTag(holder);
	    	}else{
	    		holder = (ViewHolder)convertView.getTag();
	    	}
	    	
	    	DayData day = (DayData)getItem(position);
	    	if(day != null){	        
	    		holder.textDate.setText(day.GetStringDate());
	    		holder.textBalance.setText(day.GetStringBalance());
	    	
	    		Log.d("ItemAdapter", "size="+day.GetItemList().size());
	    		ItemAdapter adapter = new ItemAdapter(Diary.this, 0, day.GetItemList());
	    		holder.listItem.setAdapter(adapter);
	    	}
	    	return convertView;
	    }
	}	
	
	private class ItemAdapter extends ArrayAdapter<ItemData> {
		private LayoutInflater inflater;
		private class ViewHolder{
    		TextView textDate;
    		TextView textItem;
    		TextView textPrice;
    		
    		ViewHolder(View view){
    			this.textDate = (TextView) view.findViewById(R.id.textView1);
	    		this.textItem = (TextView) view.findViewById(R.id.textView2);
	    		this.textPrice = (TextView) view.findViewById(R.id.textView3);
    		}
    	}
		
    	public ItemAdapter(Context context, int textViewResourceId, List<ItemData> objects) {
			super(context, textViewResourceId, objects);
			this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
    	
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder;
	    	if(convertView == null){
	    		convertView = inflater.inflate(R.layout.row, null);
	    		holder = new ViewHolder(convertView);
	    		convertView.setTag(holder);
	    	}else{
	    		holder = (ViewHolder)convertView.getTag();
	    	}
	    	
	    	Log.d("getView()", "position="+position);
	    	ItemData item = (ItemData)getItem(position);
	    	if(item != null){	
	    		holder.textDate.setText(item.GetStringDate());
	    		holder.textItem.setText(item.GetItem());  
	    		
	    		String sign = "";
	    		if(item.GetPrice() > 0) sign = "+";
	    		holder.textPrice.setText(sign + Integer.toString(item.GetPrice()) + "円");
	    	}
	    	return convertView;
	    }
	}

}
