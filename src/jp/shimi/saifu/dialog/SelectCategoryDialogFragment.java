package jp.shimi.saifu.dialog;

import jp.shimi.saufu.DayAdapter;
import jp.shimi.saufu.Diary;
import jp.shimi.saufu.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectCategoryDialogFragment extends DialogFragment{
	
	public static SelectCategoryDialogFragment newInstance(int categoryNum){
		SelectCategoryDialogFragment fragment = new SelectCategoryDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		args.putInt("before_category_num", categoryNum);
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*final View layout = inflater.inflate(R.layout.category_dialog,
        		(ViewGroup)getActivity().findViewById(R.id.listCategory));*/
        ListView listCategory = (ListView)inflater.inflate(R.layout.category_dialog, null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("カテゴリーの設定");
        
        CategoryAdapter adapter = new CategoryAdapter(getActivity());
		listCategory.setAdapter(adapter); 
        
        builder.setView(listCategory);
        
        return builder.create();
	}
	
	private class CategoryAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private Context context;
		
		private class ViewHolder{
			Button btnCategory;
			TextView textCategory;
			
			ViewHolder(View view){
				this.btnCategory = (Button) view.findViewById(R.id.categoryColor);;
				this.textCategory = (TextView) view.findViewById(R.id.textCategory);
			}
		} 
		
		public CategoryAdapter(Context context) {
			super();
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.category_row, null);
	    		holder = new ViewHolder(convertView);
	    		convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			int color = getResources().getIdentifier("category_" + position, "color",
					context.getPackageName());
			holder.btnCategory.setBackgroundColor(getResources().getColor(color));
			holder.textCategory.setText("カテゴリー" + position);
			
			return convertView;
		}
		
	}

}
