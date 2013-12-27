package fw.connection.login.serverpackets;

public class PlayFail extends LoginServerPacket{
	private int code;
	@Override
	public void read() {
		code = readC();
	}

	@Override
	public void excecute() {
		getClient().onErrorConnect(getNameByCode(code));
		_log.warning(getNameByCode(code));
		getClient().getSocket().disconnect();
	}
	
	private String getNameByCode(int id) {
		String name;
		switch (id) {
		case 0x0f:
			name = "PLAY FAIL (TOO MANY PLAYERS IN SERVER)";
			break;
		case 0x01:
			name = "PLAY FAIL (SYSTEM ERROR)";
			break;
		case 0x02:
			name = "PLAY FAIL (USER OR PASSWORD WRONG)";
			break;
		default:
			name = "PLAY FAIL (UNKNOW 0x"+Integer.toHexString(id)+")";
			break;
		}
		return name;
	}

}
