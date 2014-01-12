package fw.game.model;

import fw.dbClasses.*;

public class L2Item extends L2Object {

	private int _id,_count=0;
	private dbItem _dbitem;
	
	private int itemType1,itemType2,BodyPart,EnchantLevel,CustType1,CustType2,AugId,ShadowTime;
	private boolean isEquipped = false;
	
	public L2Item(int objectId) {
		super(objectId);	
	}
	
	public void setId(int id){
		_id = id;
		_dbitem = dbManager.getInstance().getItem(id);
	}
	
	public String getName() {
		return _dbitem.getName();
	}
	
	public String getType() {
		return _dbitem.getType();
	}
	public int getPrice() {
		return _dbitem.getPrice();
	}

	public int getId() {
		return _id;
	}

	public int getCount() {
		return _count;
	}	
	public void setCount(int count) {
		_count = count;
	}

	public int getItemType1() {
		return itemType1;
	}

	public void setItemType1(int itemType1) {
		this.itemType1 = itemType1;
	}

	public int getItemType2() {
		return itemType2;
	}

	public void setItemType2(int itemType2) {
		this.itemType2 = itemType2;
	}

	public int getBodyPart() {
		return BodyPart;
	}

	public void setBodyPart(int bodyPart) {
		BodyPart = bodyPart;
	}

	public int getEnchantLevel() {
		return EnchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		EnchantLevel = enchantLevel;
	}

	public int getCustType1() {
		return CustType1;
	}

	public void setCustType1(int custType1) {
		CustType1 = custType1;
	}

	public int getCustType2() {
		return CustType2;
	}

	public void setCustType2(int custType2) {
		CustType2 = custType2;
	}

	public int getAugId() {
		return AugId;
	}

	public void setAugId(int augId) {
		AugId = augId;
	}

	public int getShadowTime() {
		return ShadowTime;
	}

	public void setShadowTime(int shadowTime) {
		ShadowTime = shadowTime;
	}

	public boolean isEquipped() {
		return isEquipped;
	}

	public void setEquipped(boolean isEquipped) {
		this.isEquipped = isEquipped;
	}	

}
