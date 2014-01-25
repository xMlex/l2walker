package fw.connection.game.serverpackets;

import fw.connection.game.clientpackets.EnterWorld;
import fw.connection.game.clientpackets.RequestManorList;
import fw.game.model.L2Player;

public class CharSelected extends L2GameServerPacket{

	private String name;
	private int objectId;
	private L2Player _char = null;
	@Override
	public void read() {
		name=readS();
		objectId=readD();
		_char = getClient().getGameEngine().getWorld().getOrCreatePlayer(objectId);		
		_char.setName(name);
		readS(); // title
		readD();//session id		
		_char.setClanId(readD());
		
		readD();readD();readD();readD();readD();		
		_char.setXYZ(readD(), readD(), readD());
		_char.setCurrentHp(readF());
		_char.setCurrentMp(readF());
		_char.setSp(readD());
		_char.setExp(readQ());
		
		_char.setLevel(readD());
		_char.setKarma(readD());		
	}

	@Override
	public void excecute() {
		getClient().sendPacket(new EnterWorld());
		getClient().sendPacket(new RequestManorList());		
		//getClient().getGameEngine().setSelfChar(_char);			
	}

}
