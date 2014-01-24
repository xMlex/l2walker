package fw.dbClasses;

public class dbItem {
	private String name="none",type="none",icon="none",bodypart="none";
	private int price = 0;
	private boolean destroyable=false,sellable=false,dropable=false,tradeable=false;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBodypart() {
		return bodypart;
	}
	public void setBodypart(String bodypart) {
		this.bodypart = bodypart;
	}
	public boolean isDestroyable() {
		return destroyable;
	}
	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}
	public boolean isSellable() {
		return sellable;
	}
	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}
	public boolean isDropable() {
		return dropable;
	}
	public void setDropable(boolean dropable) {
		this.dropable = dropable;
	}
	public boolean isTradeable() {
		return tradeable;
	}
	public void setTradeable(boolean tradeable) {
		this.tradeable = tradeable;
	}
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
	
	public boolean isWeapon(){
		return bodypart.equals("weapon");
	}
	
	public boolean isArmor(){
		return bodypart.equals("armor");
	}
	
	@Override
	public String toString(){
		return "Item "+ getName()+" price: "+getPrice()+" type: "+getType()+" BodyPart: "+getBodypart();		
	}
}
