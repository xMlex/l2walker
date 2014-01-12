package fw.connection.game.clientpackets;

public class RequestSkillCoolTime extends L2GameClientPacket {

	@Override
	public void excecute() {
		writeC(0x9D);
	}

}
