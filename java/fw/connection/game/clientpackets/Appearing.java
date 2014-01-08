package fw.connection.game.clientpackets;

public class Appearing extends L2GameClientPacket {

	@Override
	public void excecute() {		
		writeC(0x30);
	}

}
