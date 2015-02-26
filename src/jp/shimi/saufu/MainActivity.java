package jp.shimi.saufu;

import java.util.ArrayList;
import java.util.List;

import jp.shimi.saufu.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;

public class MainActivity extends Activity{

	static List<ItemData> lItem = new ArrayList<ItemData>();
	ListView listView;
	static ItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button)findViewById(R.id.btnSave);
		btn.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	// オブジェクトを取得
		    	DatePicker dateDate  = (DatePicker)findViewById(R.id.cDate);
		    	EditText etxtItem    = (EditText)findViewById(R.id.eItem);
		    	EditText etxtPrice   = (EditText)findViewById(R.id.ePrice);
		    	TextView txtDate     = (TextView)findViewById(R.id.txtDate);
		    	TextView txtItem     = (TextView)findViewById(R.id.txtItem);
		    	TextView txtPrice    = (TextView)findViewById(R.id.txtPrice);

		    	// 入力内容を取得
		    	String strItem  = etxtItem.getText().toString();
		    	String strPrint = etxtPrice.getText().toString();
		    	
		    	// 数値に変換
		    	int price = Integer.parseInt(strPrint);
		    	StringBuilder builder = new StringBuilder();
		    	builder.append(dateDate.getYear() + "-" )
		    			.append((dateDate.getMonth() + 1) + "-")
		    		.append(dateDate.getDayOfMonth());
		    	lItem.add(new ItemData(strItem, price, builder.toString()));
		    	
		    	// 結果表示用テキストに設定
		    	txtDate.setText(builder.toString());
		    	txtItem.setText(strItem);
		    	txtPrice.setText(Integer.toString(price));

		    	// 結果表示用テキストを表示
		    	txtDate.setVisibility(View.VISIBLE);
		    	txtItem.setVisibility(View.VISIBLE);
		    	txtPrice.setVisibility(View.VISIBLE);
		    }
		    
		});
		
		// ListViewオブジェクトの生成
	    listView = (ListView) findViewById(R.id.listView);

	    // ArrayAdapterオブジェクトの生成 （1）
	    adapter = new ItemAdapter();
	    /*ArrayAdapter<ItemData> adapter = new ArrayAdapter<ItemData>(this,
	    		android.R.layout.simple_list_item_1, lItem);*/

	    // Adapterの指定
	    listView.setAdapter(adapter);
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
	        
	        //textView1.setText(item.date);
	        //textView2.setText(item.item);
	        //textView3.setText(Integer.toString(item.price) + "円");
	      }
	      return v;
	    }
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
