package jp.shimi.saifu.setting;

import jp.shimi.saufu.Diary;
import jp.shimi.saufu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, 
				new PreferencesFragment()).commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public static class PreferencesFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				onDestory();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void onDestory(){
		super.onDestroy();
		Intent intent = new Intent(this, Diary.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
