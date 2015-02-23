package jp.shimi.saufu;

public class ItemData{
	public String item;
	public int price;
	public String date;
	
	ItemData(){
		this.item = "";
		this.price = 0;
		this.date = "2000/01/01";
	}
	
	ItemData(String item, int price, String date){
		this.item = item;
		this.price = price;
		this.date = date;
	}
}