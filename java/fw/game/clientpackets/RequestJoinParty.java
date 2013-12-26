package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestJoinParty extends ClientBasePacket
{
	private GameEngine _clientThread;
	private String _charName;
	private int _itemDistribution;

	public RequestJoinParty(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_charName = readS();
		_itemDistribution = readD();
	}

	public RequestJoinParty(GameEngine clientThread, String charName, int itemDistribution)
	{
		_clientThread = clientThread;
		_charName = charName;
		_itemDistribution = itemDistribution;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x29);
		writeS(_charName);
		writeD(_itemDistribution);
	}
}
