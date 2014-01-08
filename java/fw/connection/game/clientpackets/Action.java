package fw.connection.game.clientpackets;

public class Action extends L2GameClientPacket {

	private int _objectId;
	private boolean _shift;
	
	public Action(int objectId,boolean shift){
		_objectId =objectId;
		_shift = shift;
	}
	
	public  Action(int objectId) {
		this(objectId,false);
	}
	
	@Override
	public void excecute() {
		writeC(0x04);
		writeD(_objectId);
		writeLoc(getClient().getGameEngine().getSelfChar().getLoc());
		if(_shift)
			writeC(0x01);
		else
			writeC(0x00);
	}

}
