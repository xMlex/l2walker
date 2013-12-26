package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestTargetCanceld extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _op;

	public RequestTargetCanceld(byte[] message, GameEngine clientThread) {
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_op = readH();
	}

	public RequestTargetCanceld(GameEngine clientThread) {
		_op = 0;
	}

	public  void runImpl()
	{
		System.out.print(getMessage());
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x37);
		writeH(_op);
	}
}
