package fw.connection.login.serverpackets;

public class LoginFail extends LoginServerPacket {

	int code;
	@Override
	public void read() {
		code=readD();		
	}

	@Override
	public void excecute() {		
		getClient().onErrorConnect(getNameByCode(code));
		_log.warning(getNameByCode(code));
		getClient().getSocket().disconnect();
	}

	private String getNameByCode(int code) {
		String name;
		switch (code) {
		case 0x09:
			name = "LOGIN FAIL (ACCOUNT BANNED)";
			break;
		case 0x07:
			name = "LOGIN FAIL (ACCOUNT IN USE)";
			break;
		case 0x04:
			name = "LOGIN FAIL (ACCESS FAILED)";
			break;
		case 0x03:
			name = "LOGIN FAIL (USER OR PASSWORD IS WRONG)";
			break;
		case 0x02:
			name = "LOGIN FAIL (PASSWORD WRONG)";
			break;
		case 0x01:
			name = "LOGIN FAIL (SYSTEM ERROR)";
			break;
		default:
			name = "LOGIN FAIL (UNKNOW)";
			break;
		}
		return name;
	}
}
