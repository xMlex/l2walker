package fw.connection.game.serverpackets;

public class DeleteObject extends L2GameServerPacket {
	
	private int objectId;
	
	@Override
	public void read() {		
		objectId = readD();
	}

	@Override
	public void excecute() {		
		getClient().getGameEngine().getWorld().remove(objectId);
	}

}
