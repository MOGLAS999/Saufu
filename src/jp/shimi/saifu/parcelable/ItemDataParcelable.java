package jp.shimi.saifu.parcelable;

import java.util.Calendar;

import jp.shimi.saufu.DateChanger;
import jp.shimi.saufu.ItemData;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ItemDataParcelable implements Parcelable{
	private String item;
	private int price;
	private Calendar date;
	private int number;
	private int category;
	private DateChanger dc;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(item);
		out.writeInt(price);
		out.writeString(dc.ChangeToString(date));
		out.writeInt(number);
		out.writeInt(category);	
	}
	
	public static final Parcelable.Creator<ItemDataParcelable> CREATOR
			= new Parcelable.Creator<ItemDataParcelable>(){
		public ItemDataParcelable createFromParcel(Parcel in){
			return new ItemDataParcelable(in);
		}
		
		public ItemDataParcelable[] newArray(int size){
			return new ItemDataParcelable[size];
		}
	};
	
	private ItemDataParcelable(Parcel in){
		item = in.readString();
		price = in.readInt();
		date = dc.ChangeToCalendar(in.readString());
		number = in.readInt();
		category = in.readInt();
	}
	
	public ItemDataParcelable(String item, int price, Calendar date, int number, int category){
		this.item = item;
		this.price = price;
		this.date = date;
		this.number = number;
		this.category = category;
	}
	
	public ItemDataParcelable(ItemData itemData){
		this.item = itemData.GetItem();
		this.price = itemData.GetPrice();
		this.date = itemData.GetDate();
		this.number = itemData.GetNumber();
		this.category = itemData.GetCategory();
	}
	
	public ItemData GetItemData(){
		Log.d("ItemDataParclable", this.item +" "+ this.price +" "+ this.date.toString() +" "+ this.number +" "+ this.category);
		return new ItemData(this.item, this.price, this.date, this.number, this.category);
	}
}
