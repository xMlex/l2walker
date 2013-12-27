package fw.connection.login.serverpackets;

public class PlayOk extends LoginServerPacket {
	
	private int _playOk1, _playOk2;
	
	@Override
	public void read() {
		_playOk1=readD();
		_playOk2=readD();
	}

	@Override
	public void excecute() {
		getClient().onConnect2gameServer(_playOk1, _playOk2);
	}

}
