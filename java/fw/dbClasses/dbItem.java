package fw.dbClasses;

public class dbItem {
	private String name="none",type="none";
	private int price = 0;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String toString(){
		return "Item "+ getName()+" price: "+getPrice()+" type: "+getType();		
	}
}
