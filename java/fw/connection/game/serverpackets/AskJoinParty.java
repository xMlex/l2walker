package fw.connection.game.serverpackets;

import fw.connection.game.clientpackets.RequestAnswerJoinParty;

public class AskJoinParty extends L2GameServerPacket {

	@Override
	public void read() {
		//TODO AskJoinParty
	}

	@Override
	public void excecute() {
		getClient().getGameEngine().getGameConnection().sendPacket(new RequestAnswerJoinParty());
	}

}
