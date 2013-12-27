package fw.connection.login.clientpackets;

public class RequestServerList extends LoginClientPacket {

	@Override
	public void excecute() {
		writeC(0x05);
		writeD(getClient().geLoginOk()[0]);
		writeD(getClient().geLoginOk()[1]);
		writeC(4);
		writeB(new byte[14]);
	}

}
