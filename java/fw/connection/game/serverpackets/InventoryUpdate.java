package fw.connection.game.serverpackets;

public class InventoryUpdate extends L2GameServerPacket {

	@Override
	public void read() {
		int count = readH();
		for (int i = 0; i < count; i++) {
			
		}
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub

	}

}
