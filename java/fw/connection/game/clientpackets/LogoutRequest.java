package fw.connection.game.clientpackets;

public class LogoutRequest extends L2GameClientPacket {

	private boolean _now = false;
	public LogoutRequest(boolean now){
		_now = now;
	}
	public LogoutRequest(){}
	
	@Override
	public void excecute() {		
		writeC(0x09);
		if(_now)
			getClient().getSocket().disconnect();
	}

}
