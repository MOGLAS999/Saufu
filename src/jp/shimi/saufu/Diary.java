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
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
        
        // 初期残高設定ダイアログ
        if(lDay.GetListSize() == 0){
        	LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        	final View layout = inflater.inflate(R.layout.init_balance_dialog,
        			(ViewGroup)findViewById(R.id.init_balance_dialog));
        
        	AlertDialog.Builder adb = new AlertDialog.Builder(this);
        	adb.setTitle("初期残高の設定");
        	adb.setView(layout);
        
        	final DateChanger dc = new DateChanger();
        	final Calendar dCalendar = Calendar.getInstance();
        	final EditText dateEdit = (EditText)layout.findViewById(R.id.editInitBalanceDate);
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
        	adb.setCancelable(false);
        	adb.setPositiveButton("設定", new DialogInterface.OnClickListener() {
        		@Override
        		public void onClick(DialogInterface dialog, int which) {
        			EditText etxtBalance  = (EditText)layout.findViewById(R.id.editInitBalance);
        			if(!etxtBalance.getText().toString().equals("")){
        				// 入力内容を取得
        				String strBalance = etxtBalance.getText().toString();
		    	
        				// 数値に変換
        				int initBalance = Integer.parseInt(strBalance);
        				
        				DayData dayData = new DayData(dCalendar, initBalance);
                		lDay.AddData(dayData);
    		    	
                		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
                		listView.setAdapter(adapter);
        			}
        		}
        	});
        	adb.show();
        }
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
	
		DayAdapter adapter = new DayAdapter(Diary.this, 0, lDay.GetList());
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v){
		EditItemDialog(Calendar.getInstance(), -1);
	}
	
	public class DayAdapter extends ArrayAdapter<DayData> {		
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
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder;
	    	
	    	if(convertView == null){
	    		convertView = inflater.inflate(R.layout.day, null);
	    		holder = new ViewHolder(convertView);
	    		convertView.setTag(holder);
	    	}else{
	    		holder = (ViewHolder)convertView.getTag();
	    	}
	    	
	    	final DayData day = (DayData)getItem(position);
	    	if(day != null){	        
	    		holder.textDate.setText(day.GetStringDate());
	    		// 各日の日付部分がクリックされた時のイベント
	    		holder.textDate.setOnClickListener(new View.OnClickListener() {
            		public void onClick(View view){            			
            			// 追加・削除ダイアログを生成
            			LayoutInflater inflater = (LayoutInflater)Diary.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            		    final View layout = inflater.inflate(R.layout.edit_delete_dialog,(ViewGroup)findViewById(R.id.edit_delete_layout));
            		        
            		    AlertDialog dialog = null;
            		    
            		    String[] items = {"項目を追加", "削除"};
            		    ListView listED = (ListView)layout.findViewById(R.id.listEditDelete);
            		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Diary.this,
            		            android.R.layout.simple_expandable_list_item_1, items);
            		    listED.setAdapter(adapter);
            		    listED.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    		public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id){  
                    			if(pos == 0){ // 項目を追加
                    				EditItemDialog(day.GetDate(), -1);
                    			}else if(pos == 1){ // 削除
                    				remove(day);
                    				//lDay.RemoveData(position);               
                    			}
                    			//dialog.dismiss();
                    		}
            		    });         
            		        
            		    final AlertDialog.Builder adb = new AlertDialog.Builder(Diary.this);
            		    adb.setTitle(day.GetStringDate());
            		    adb.setView(layout);

            		    adb.show();       
            		}
				});
	    		holder.textBalance.setText("残金    " + day.GetStringBalance() + " 円");
	    	
	    		ItemAdapter adapter = new ItemAdapter(Diary.this, 0, day.GetItemList());
	    		holder.listItem.setAdapter(adapter);
	    		// 各アイテムがクリックされた時のイベント
	    		holder.listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            		public void onItemClick(AdapterView<?> parent,
                            View view, final int pos, long id){  
            			// 選択アイテムを取得
                        ListView listView = (ListView)parent;
                        final ItemData item = (ItemData)listView.getItemAtPosition(pos);
            			
            			// 編集・削除ダイアログを生成
            			LayoutInflater inflater = (LayoutInflater)Diary.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            		    final View layout = inflater.inflate(R.layout.edit_delete_dialog,(ViewGroup)findViewById(R.id.edit_delete_layout));
            		        
            		    String[] items = {"編集", "削除"};
            		    ListView listED = (ListView)layout.findViewById(R.id.listEditDelete);
            		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Diary.this,
            		            android.R.layout.simple_expandable_list_item_1, items);
            		    listED.setAdapter(adapter);
            		    listED.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    		public void onItemClick(AdapterView<?> parent,
                                    View view, int strPos, long id){  
                    			if(strPos == 0){ // 編集
                    				EditItemDialog(day.GetDate(), pos);
                    			}else if(strPos == 1){ // 削除
                    				lDay.RemoveItemData(day.GetDate(), pos);
                    			}
                    		}
            		    });     
            		        
            		    AlertDialog.Builder adb = new AlertDialog.Builder(Diary.this);
            		    adb.setTitle(item.GetItem());
            		    adb.setView(layout);

            		    adb.show();       
            		}
				});
	    		SetListViewHeightBasedOnItem(holder.listItem);
	    	}
	    	return convertView;
	    }
	}	
	
	public class ItemAdapter extends ArrayAdapter<ItemData> {
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

	public void SetListViewHeightBasedOnItem(ListView listView){
		ItemAdapter listAdapter = (ItemAdapter) listView.getAdapter();
		
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		
		for(int i = 0; i < listAdapter.getCount(); i++){
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	
	/* アイテム追加・編集ダイアログ
	 * initDate ：最初に設定しておく日付。編集モードの場合は編集するアイテムの日付。
	 * editPosition　：編集するアイテムの位置。編集モードでなければ-1が渡される。
	*/ 
	public void EditItemDialog(final Calendar initDate, final int editPosition){
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.diary_dialog,(ViewGroup)findViewById(R.id.diarydialog_layout));
        
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        String title = "項目の追加";
        if(editPosition >= 0) title = "項目の編集";
        adb.setTitle(title);
        adb.setView(layout);
        
        // 日付選択テキストボックス＆ダイアログ
        final DateChanger dc = new DateChanger();
        final Calendar dCalendar = initDate;
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
            		
            		// 項目を追加
            		ItemData itemData = new ItemData(strItem, price, dateEdit.getText().toString());
            		int d = lDay.GetDayData(itemData.GetDate());
            		if(d < 0){ 
            			lDay.AddDataByDate(new DayData(itemData.GetDate(), 0));
            		}
            		if(editPosition >= 0){
            			// 編集モード
            			if(itemData.GetDate().equals(initDate)){
            				lDay.SetItemData(initDate, itemData, editPosition);
            			}else{
            				lDay.RemoveItemData(initDate, editPosition);
            				lDay.AddItemData(itemData.GetDate(), itemData);
            			}
            		}else{
            			lDay.AddItemData(itemData.GetDate(), itemData);
            		}
		    	
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
        adb.setCancelable(true);
        adb.show();
	}
}
