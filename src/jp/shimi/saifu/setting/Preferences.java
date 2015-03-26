package jp.shimi.saifu.setting;

import jp.shimi.saufu.Diary;
import jp.shimi.saufu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Preferences extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, 
				new PreferencesFragment()).commit();
	}
	
	public void onDestory(){
		super.onDestroy();
		Intent intent = new Intent(this, Diary.class);
		startActivity(intent);
	}
	
	public static class PreferencesFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}
