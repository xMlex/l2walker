package fw.connection.game.clientpackets;

public class RequestManorList extends L2GameClientPacket {

	@Override
	public void excecute() {		
		writeC(0xD0);
		writeH(8);
	}

}
