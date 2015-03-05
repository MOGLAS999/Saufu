package jp.shimi.saufu;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DayAdapter extends ArrayAdapter<DayData> {		
	private LayoutInflater inflater;
	private Context context;
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
    		holder.textDate.setText(day.GetStringDate());
    		// 各日の日付部分がクリックされた時のイベント
    		holder.textDate.setOnClickListener(new View.OnClickListener() {
        		public void onClick(View view){            			
        			// 追加・削除ダイアログを生成
        			DayMenuDialog dialog = new DayMenuDialog(day, position);
        			dialog.CreateDialog();   
        		}
			});
    		holder.textBalance.setText("残金    " + day.GetStringBalance() + " 円");
    	
    		ItemAdapter adapter = new ItemAdapter(context, 0, day.GetItemList());
    		holder.listItem.setAdapter(adapter);
    		
    		SetListViewHeightBasedOnItem(holder.listItem);
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
			ItemMenuDialogFragment newFragment = 
					ItemMenuDialogFragment.newInstance(day.GetStringDate());
			newFragment.setDialogListener(DayMenuDialog.this);
			//newFragment.setCancelable(false);
			newFragment.show(((Activity)context).getFragmentManager(), "item_menu_dialog");
    	}
    	
    	@Override
    	public void doFitstClick() {
    		EditItemDialog(day.GetDate(), -1);
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
}