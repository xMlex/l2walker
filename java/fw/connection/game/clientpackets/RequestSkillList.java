package fw.connection.game.clientpackets;

public class RequestSkillList extends L2GameClientPacket {

	@Override
	public void excecute() {
		writeC(0x3F);
	}

}
