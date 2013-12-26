package fw.connection.game;

public class GameAuthLogin extends GameSendablePacket {

	@Override
	public void write() {
		writeC(0x2B);	
		writeS(getClient().loginResult.login);
		writeD(getClient().loginResult.playId2);
		writeD(getClient().loginResult.playId1);
		writeD(getClient().loginResult.loginId1);
		writeD(getClient().loginResult.loginId2);
		writeD(1);
		writeD(0);
		writeD(0);
		writeC(0x01);
		writeH(0);
		writeC(0);
	}

}
