package fw.connection.login.serverpackets;

import fw.connection.login.clientpackets.RequestServerList;

public class LoginOk extends LoginServerPacket {
	
	private int _loginOk1, _loginOk2;
	
	@Override
	public void read() {
		_loginOk1 = readD();
		_loginOk2 = readD();
	}

	@Override
	public void excecute() {
		getClient().setLoginOk12(_loginOk1,_loginOk2);
		getClient().sendPacket(new RequestServerList());
	}

}
