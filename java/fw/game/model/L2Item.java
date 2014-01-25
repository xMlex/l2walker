package fw.game.model;

import fw.dbClasses.*;

public class L2Item extends L2Object {

	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	public static final int TYPE1_SHIELD_ARMOR = 1;
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;

	public static final int TYPE2_WEAPON = 0;
	public static final int TYPE2_SHIELD_ARMOR = 1;
	public static final int TYPE2_ACCESSORY = 2;
	public static final int TYPE2_QUEST = 3;
	public static final int TYPE2_MONEY = 4;
	public static final int TYPE2_OTHER = 5;
	public static final int TYPE2_PET_WOLF = 6;
	public static final int TYPE2_PET_HATCHLING = 7;
	public static final int TYPE2_PET_STRIDER = 8;
	public static final int TYPE2_PET_BABY = 9;
	
	public static final int SLOT_NONE = 0x0000;
	public static final int SLOT_UNDERWEAR = 0x0001;
	public static final int SLOT_R_EAR = 0x0002;
	public static final int SLOT_L_EAR = 0x0004;
	public static final int SLOT_NECK = 0x0008;
	public static final int SLOT_R_FINGER = 0x0010;
	public static final int SLOT_L_FINGER = 0x0020;
	public static final int SLOT_HEAD = 0x0040;
	public static final int SLOT_R_HAND = 0x0080;
	public static final int SLOT_L_HAND = 0x0100;
	public static final int SLOT_GLOVES = 0x0200;
	public static final int SLOT_CHEST = 0x0400;
	public static final int SLOT_LEGS = 0x0800;
	public static final int SLOT_FEET = 0x1000;
	public static final int SLOT_BACK = 0x2000;
	public static final int SLOT_LR_HAND = 0x4000;
	public static final int SLOT_FULL_ARMOR = 0x8000;
	public static final int SLOT_HAIR = 0x010000;
	public static final int SLOT_WOLF = 0x020000;
	public static final int SLOT_HATCHLING = 0x100000;
	public static final int SLOT_STRIDER = 0x200000;
	public static final int SLOT_BABYPET = 0x400000;
	public static final int SLOT_FACE = 0x040000;
	public static final int SLOT_DHAIR = 0x080000;
	
	
	private int item_id,_count=0;
	private dbItem _dbitem;
	
	private int itemType1,itemType2,BodyPart,EnchantLevel,CustType1,CustType2,AugId,ShadowTime,Slot;
	private boolean isEquipped = false;
	
	public L2Item(int objectId) {
		super(objectId);	
	}
	
	public void setId(int id){
		item_id = id;
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
		return item_id;
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
	
	public boolean isWeapon(){
		return _dbitem.isWeapon();
	}
	
	public boolean isArmor(){
		return _dbitem.isArmor();
	}
	
	public int getSlot() {
		return Slot;
	}

	public void setSlot(int slot) {
		Slot = slot;
	}

	@Override
	public String toString() {
		return "L2Item id: "+item_id+" Count: "+_count+" Name: "+getName()+" "+super.toString();
	}

	public String getIcon() {
		return _dbitem.getIcon();
	}
}
