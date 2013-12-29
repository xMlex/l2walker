package fw.connection.game.clientpackets;

public class AuthLogin extends L2GameClientPacket {

	@Override
	public void excecute() {
		writeC(0x08);
		writeS(getClient().getLogin());
		writeD(getClient().getLoginResult().playId2);
		writeD(getClient().getLoginResult().playId1);
		writeD(getClient().getLoginResult().loginId1);
		writeD(getClient().getLoginResult().loginId2);
		writeD(1);
	}

}
