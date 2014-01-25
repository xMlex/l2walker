package fw.connection.game.serverpackets;

import fw.connection.game.clientpackets.EnterWorld;
import fw.connection.game.clientpackets.RequestSkillCoolTime;
import fw.connection.game.clientpackets.RequestSkillList;

public class ExSendManorList extends L2GameServerPacket {

	@Override
	public void read() {}

	@Override
	public void excecute() {
		//getClient().sendPacket(new EnterWorld());		
	}

}
