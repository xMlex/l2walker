package fw.game.model;

public class L2Drop extends L2Object {

	private int PlayerID = 0,ItemID=0,count=0;
	private boolean Stackable = false;	
	
	public L2Drop(Integer objectId) {
		super(objectId);
	}
	
	public int getPlayerID() {
		return PlayerID;
	}


	public void setPlayerID(int playerID) {
		PlayerID = playerID;
	}


	public int getItemID() {
		return ItemID;
	}


	public void setItemID(int itemID) {
		ItemID = itemID;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public boolean isStackable() {
		return Stackable;
	}


	public void setStackable(boolean stackable) {
		Stackable = stackable;
	}
}
