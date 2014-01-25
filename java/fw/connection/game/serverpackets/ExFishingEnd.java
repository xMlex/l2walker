package fw.connection.game.serverpackets;

public class ExFishingEnd extends L2GameServerPacket {

	@Override
	public void read() {
		int  charObjID = readD();
		if(charObjID != getPlayer().getObjectId()) return;	
		getPlayer().getFishingInfo().setStarted(false);
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub

	}

}
