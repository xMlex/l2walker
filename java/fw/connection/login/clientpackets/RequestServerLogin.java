package fw.connection.login.clientpackets;

public class RequestServerLogin extends LoginClientPacket {

	@Override
	public void excecute() {
		writeC(0x02);
		writeD(getClient().geLoginOk()[0]);
		writeD(getClient().geLoginOk()[1]);
		writeC(getClient().getServerId());
		writeB(new byte[14]);
	}

}
