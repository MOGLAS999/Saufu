package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import jp.shimi.saifu.setting.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<ItemData>{
	private LayoutInflater inflater;
	private Context context;
	private ItemRemoveListener itemRemoveListener = null;
	private class ViewHolder{
		//TextView textDate;
		TextView textItem;
		TextView textPrice;
		
		ViewHolder(View view){
			//this.textDate = (TextView) view.findViewById(R.id.textView1);
    		//this.textItem = (TextView) view.findViewById(R.id.textView2);
    		//this.textPrice = (TextView) view.findViewById(R.id.textView3);
    		this.textItem = (TextView) view.findViewById(R.id.txtItemName);
    		this.textPrice = (TextView) view.findViewById(R.id.textItemPrice);
		}
	}
	
	public ItemAdapter(Context context, int textViewResourceId, List<ItemData> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	if(convertView == null){
    		convertView = inflater.inflate(R.layout.item_row, null);
    		holder = new ViewHolder(convertView);
    		convertView.setTag(holder);
    	}else{
    		holder = (ViewHolder)convertView.getTag();
    	}
    	
    	final ItemData item = (ItemData)getItem(position);
    	if(item != null){	    		
    		//holder.textDate.setText(item.GetStringDate());
    		holder.textItem.setText(item.GetItem());  
    		
    		String sign = "";
    		if(item.GetPrice() > 0) sign = "+";
    		holder.textPrice.setText(sign + Integer.toString(item.GetPrice()) + "円");
    		
    		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    		//holder.textDate.setTextSize(Integer.parseInt(pref.getString("char_size", "16")));
    		holder.textItem.setTextSize(Integer.parseInt(pref.getString("char_size", "16")));
    		holder.textPrice.setTextSize(Integer.parseInt(pref.getString("char_size", "16")));
    		
    		// 背景色を交互に変更
			if(position % 2 == 0){
    			convertView.setBackgroundResource(R.drawable.list_item_color1);
    		}else{
    			convertView.setBackgroundResource(R.drawable.list_item_color2);
    		}
    	}
    	
    	convertView.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view){            			
    			ItemMenuDialog dialog = new ItemMenuDialog(item, position);
    			dialog.CreateDialog();
    		}
		});
    	
    	return convertView;
    }

    private class ItemMenuDialog implements MenuListener{
    	ItemData item;
    	int position;
    	
    	
    	ItemMenuDialog(ItemData item, int position){
    		this.item = item;
    		this.position = position;
    	}
    	
    	public void CreateDialog(){
    		// 編集・削除ダイアログを生成        			       			
			ItemMenuDialogFragment newFragment = 
					ItemMenuDialogFragment.newInstance(item.GetItem());
			newFragment.setDialogListener(ItemMenuDialog.this);
			//newFragment.setCancelable(false);
			newFragment.show(((Activity)context).getFragmentManager(), "item_menu_dialog");
    	}
    	
    	/**
    	 * 編集
    	 */
    	@Override
    	public void doFitstClick() {
    		EditItemDialog dialog = new EditItemDialog(context ,item, position);
    		EditItemDialogListener listener = new EditItemDialogListener();
    		dialog.CreateDialog(listener);
    		//remove(item);
    	}

    	/**
    	 * 削除
    	 */
    	@Override
    	public void doSecondClick() {
    		Calendar c = item.GetDate();
    		remove(item);
    		itemRemoveListener.removeItem(c);
    	}
    	
    	private class EditItemDialogListener implements DialogListener{

    		@Override
    		public void doPositiveClick() {
    			Calendar c = item.GetDate();
        		remove(item);
        		itemRemoveListener.removeItem(c);
    		}

			@Override
			public void doNegativeClick() {
				// TODO 自動生成されたメソッド・スタブ
			}
    	}

    }

    /**
	 * リスナーを追加
	 */
	public void setItemRemoveListener(ItemRemoveListener listener){
	    this.itemRemoveListener = listener;
	}
	    
	/**
	 * リスナー削除
	 */
	public void removeItemRemoveListener(){
	    this.itemRemoveListener = null;
	}
}