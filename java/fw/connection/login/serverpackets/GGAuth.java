package fw.connection.login.serverpackets;

import fw.connection.login.clientpackets.RequestAuthLogin;

public class GGAuth extends LoginServerPacket{

	private int _response;
	
	@Override
	public void read() {
		_response = readD(); 		
	}

	@Override
	public void excecute() {		
		getClient().sendPacket(new RequestAuthLogin(_response));
	}

}
