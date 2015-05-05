package jp.shimi.saufu;

import java.util.Calendar;
import java.util.List;

import jp.shimi.saifu.dialog.DialogListener;
import jp.shimi.saifu.dialog.EditItemDialog;
import jp.shimi.saifu.dialog.ItemMenuDialogFragment;
import jp.shimi.saifu.dialog.ItemRemoveListener;
import jp.shimi.saifu.dialog.MenuListener;
import jp.shimi.saifu.setting.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<ItemData>{
	private LayoutInflater inflater;
	private Context context;
	private ItemRemoveListener itemRemoveListener = null;
	private class ViewHolder{
		Button btnCategory;
		TextView textItem;
		TextView textPrice;
		
		ViewHolder(View view){
			this.btnCategory = (Button) view.findViewById(R.id.buttonCategory);
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
    		holder.textItem.setText(item.GetItem());  
    		
    		// 符号（プラスマイナス）を付ける
    		String sign = "";
    		int priceColor = context.getResources().getColor(R.color.minus);
    		if(item.GetPrice() > 0){
    			sign = "+";
    			priceColor = context.getResources().getColor(R.color.plus);
    		}
    		holder.textPrice.setText(sign + Integer.toString(item.GetPrice()) + "円");
    		holder.textPrice.setTextColor(priceColor);
    		
    		// 文字サイズの設定
    		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    	    String initFontSize = context.getResources().getString(R.string.initial_font_size);
    		
    		ViewGroup.LayoutParams params = holder.btnCategory.getLayoutParams();
    		params.width = (int) convertDpToPixel(Float.parseFloat(
    				pref.getString("char_size", initFontSize)), context);
    	    params.height = params.width;
    	    holder.btnCategory.setLayoutParams(params);
    		
    		holder.textItem.setTextSize(Integer.parseInt(pref.getString("char_size", initFontSize)));
    		holder.textPrice.setTextSize(Integer.parseInt(pref.getString("char_size", initFontSize)));
    		
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
	
	/**                                                                                                                
	 * This method convets dp unit to equivalent device specific value in pixels.
	 *
	 * @param dp A value in dp(Device independent pixels) unit which will be converted to pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent Pixels equivalent to dp according to device
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi /160f);
	    return px;
	}
}