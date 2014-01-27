package fw.connection.game.serverpackets;

public class ExFishingEnd extends L2GameServerPacket {

	private int  charObjID = -1;
	@Override
	public void read() {
		charObjID = readD();		
	}

	@Override
	public void excecute() {
		if(charObjID != getPlayer().getObjectId()) return;
		getPlayer().getFishingInfo().setStarted(false);
	}

}
