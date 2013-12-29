package fw.connection.game.clientpackets;

public class CharacterSelected extends L2GameClientPacket{

	private int _charSlot = 0;
	public CharacterSelected(int charSlot) {
		super();
		_charSlot = charSlot;
		_log.info("Selected char slot: "+_charSlot);
	}

	@Override
	public void excecute() {
		writeC(0x0d);
		writeD(_charSlot);
		writeH(0);
		writeD(0);
		writeD(0);
		writeD(0);
	}

}
