package fw.connection.login.clientpackets;

public class AuthGameGuard extends LoginClientPacket {

	private int _sessionId;
	private int _data1 = 0;
	private int _data2 = 0;
	private int _data3 = 0;
	private int _data4 = 0;

	public AuthGameGuard(int sessionId, int data1, int data2, int data3,
			int data4) {
		_sessionId = sessionId;
		_data1 = data1;
		_data2 = data2;
		_data3 = data3;
		_data4 = data4;
	}

	@Override
	public void excecute() {
		writeC(0x07);
		writeD(_sessionId);
		writeD(_data1);
		writeD(_data2);
		writeD(_data3);
		writeD(_data4);
	}

}
