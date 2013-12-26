package fw.game.clientpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import fw.game.GameEngine;

public class TradeDone extends ClientBasePacket
{
	private GameEngine _clientThread;
	private final int _response;

	public TradeDone(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_response = readD();
	}

	public TradeDone(GameEngine clientThread, int response)
	{
		_clientThread = clientThread;
		_response = response;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x17);
		writeD(_response);
	}
}
