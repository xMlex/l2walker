package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestWithDrawalParty extends ClientBasePacket
{
	private GameEngine _clientThread;

	public RequestWithDrawalParty(byte[] message, GameEngine clientThread) {
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
	}

	public RequestWithDrawalParty(GameEngine clientThread) {
		_clientThread = clientThread;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x2b);
	}
}
