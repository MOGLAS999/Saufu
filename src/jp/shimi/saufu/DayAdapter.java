package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DayAdapter extends ArrayAdapter<DayData> implements ItemRemoveListener{		
	private LayoutInflater inflater;
	private Context context;
	private DayItemDeletedListener DIDListener = null;
	
	private class ViewHolder{
		TextView textDate;
		//ListView listItem;
		LinearLayout listItem;
		TextView textBalance;
		
		ViewHolder(View view){
			this.textDate = (TextView) view.findViewById(R.id.txtDate);
    		//this.listItem = (ListView) view.findViewById(R.id.lstItem);
			this.listItem = (LinearLayout) view.findViewById(R.id.lstItem);
    		this.textBalance = (TextView) view.findViewById(R.id.txtBalance);
		}
	} 
	
    public DayAdapter(Context context, int textViewResourceId, List<DayData> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
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
    		holder.textDate.setText(day.GetStringDate() + GetWeekByDate(day.GetDate()));
    		// 各日の日付部分がクリックされた時のイベント
    		holder.textDate.setOnClickListener(new View.OnClickListener() {
        		public void onClick(View view){            			
        			// 追加・削除ダイアログを生成
        			DayMenuDialog dialog = new DayMenuDialog(day, position);
        			dialog.CreateDialog();
        		}
			});
    		holder.textBalance.setText("残金    " + day.GetStringBalance() + " 円");
    		
    		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    		holder.textDate.setTextSize(Integer.parseInt(pref.getString("char_size", "16")));
    		holder.textBalance.setTextSize(Integer.parseInt(pref.getString("char_size", "16")));
    		    	
    		ItemAdapter adapter = new ItemAdapter(context, 0, day.GetItemList());
    		adapter.setItemRemoveListener(DayAdapter.this);
    		
    		holder.listItem.removeAllViews();
    		for(int i = 0; i < adapter.getCount(); i++){		
    			holder.listItem.addView(adapter.getView(i, null, holder.listItem));
    		}
    	}
    	return convertView;
    }
    
    private class DayMenuDialog implements MenuListener{
    	DayData day;
    	int position;
    	
    	DayMenuDialog(DayData day, int position){
    		this.day = day;
    		this.position = position;
    	}
    	
    	public void CreateDialog(){
    		// 編集・削除ダイアログを生成        			       			
			DayMenuDialogFragment newFragment = 
					DayMenuDialogFragment.newInstance(day.GetStringDate());
			newFragment.setDialogListener(DayMenuDialog.this);
			//newFragment.setCancelable(false);
			newFragment.show(((Activity)context).getFragmentManager(), "day_menu_dialog");
    	}
    	
    	@Override
    	public void doFitstClick() {
    		EditItemDialog dialog = new EditItemDialog(context, day.GetDate());
    		dialog.CreateDialog();
    	}

    	@Override
    	public void doSecondClick() {
    		remove(day);     
    	}
    }
    
    /**
     * リストビューの高さを内容に応じて変更
     */
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
    
    public String GetWeekByDate(Calendar cal){
    	switch(cal.get(Calendar.DAY_OF_WEEK)){
    		case Calendar.SUNDAY:    return "(日)";
    		case Calendar.MONDAY:    return "(月)";
    		case Calendar.TUESDAY:   return "(火)";
    		case Calendar.WEDNESDAY: return "(水)";
    		case Calendar.THURSDAY:  return "(木)";
    		case Calendar.FRIDAY:    return "(金)";
    		case Calendar.SATURDAY:  return "(土)";
    	}
    	
    	return null;
    }

	@Override
	public void removeItem(Calendar deletedDate) {
		DIDListener.DayItemDeleted(deletedDate);
		Log.d("removeItem", "passed");
	}
	
	/**
	 * リスナーを追加
	 */
	public void setDayItemDeletedListener(DayItemDeletedListener listener){
	    this.DIDListener = listener;
	}
	    
	/**
	 * リスナー削除
	 */
	public void removeDayItemDeletedListener(){
	    this.DIDListener = null;
	}
}