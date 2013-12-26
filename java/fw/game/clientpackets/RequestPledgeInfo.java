package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestPledgeInfo extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _clanId;

	public RequestPledgeInfo(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_clanId = readD();
	}

	public RequestPledgeInfo(GameEngine clientThread,int clanId)
	{
		_clientThread = clientThread;
		_clanId = clanId;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x66);
		writeD(_clanId);
	}
}
