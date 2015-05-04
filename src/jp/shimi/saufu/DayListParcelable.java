package jp.shimi.saufu;

import android.os.Parcel;
import android.os.Parcelable;

public class DayListParcelable implements Parcelable{
	private DayList daylist;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
	}
}
