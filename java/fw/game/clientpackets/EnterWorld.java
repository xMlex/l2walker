package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EnterWorld extends ClientBasePacket
{
	private GameEngine clientThread;
	private byte[] myArray = new byte[]{00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00};
	private byte[] _inbuf;
	
	public EnterWorld(byte[] message, GameEngine clientThread)
	{
		this.clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
	}

	public EnterWorld(GameEngine clientThread)
	{
		this.clientThread = clientThread;
		_inbuf = myArray;
	}

	public void runImpl()
	{
		clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x03);
		writeB(_inbuf);
	}
}
