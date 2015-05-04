package jp.shimi.saifu.dialog;

import jp.shimi.saufu.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

public class CalendarDialogFragment extends DialogFragment {
	LayoutInflater inflater;

	public static CalendarDialogFragment newInstance() {
		CalendarDialogFragment fragment = new CalendarDialogFragment();
		  
		// 引数を設定
		Bundle args = new Bundle();
		//args.putString("init_date", initDate);
		//args.putInt("item_category", 0);
		fragment.setArguments(args);
		 
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstatnceState){	
		inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.calendar,
        		(ViewGroup)getActivity().findViewById(R.id.calendar_dialog));
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("日付の選択");
        builder.setView(layout);
        
        final DatePicker calendar = (DatePicker)layout.findViewById(R.id.calendar_dialog);
        
        return builder.create();
	}
}
