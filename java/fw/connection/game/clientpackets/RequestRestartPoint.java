package fw.connection.game.clientpackets;

public class RequestRestartPoint extends L2GameClientPacket {

	private int PointType = 0;
	
	@Override
	public void excecute() {
		writeC(0x6D);
		writeD(PointType);
	}

}
