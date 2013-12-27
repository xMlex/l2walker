package fw.connection.game.clientpackets;

public class ProtocolVersion extends L2GameClientPacket {

	@Override
	public void excecute() {
		writeC(0x00);
		writeH(getClient().getProtocolVersion());
		writeB(new byte[260]);
	}

}
