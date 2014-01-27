package fw.connection.game.serverpackets;

import fw.game.model.ai.L2FishingInfo;

public class ExFishingStart extends L2GameServerPacket {

	private L2FishingInfo _info = null;
	@Override
	public void read() {
		int charObjID = readD();
		if(charObjID != getPlayer().getObjectId()) return;	
		_info = getPlayer().getFishingInfo();	
	}

	@Override
	public void excecute() {
		if(_info == null) return;
		_info.setStarted(true);
	}

}
