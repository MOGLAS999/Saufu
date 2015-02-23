package android.DB;

public class DBEntity{
	private int id;
	private String date; 
	private String item;
	private int price;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setItem(String item){
		this.item = item;
	}
	
	public String getItem(){
		return item;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public int getPrice(){
		return price;
	}
}