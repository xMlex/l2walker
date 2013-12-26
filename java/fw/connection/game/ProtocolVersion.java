package fw.connection.game;

public class ProtocolVersion extends GameSendablePacket{

	@Override
	public void write() {		
		writeC(0x0E);
		writeD(getClient().clientProtocolVersion);
		writeB(new byte[260]);
	}


}
