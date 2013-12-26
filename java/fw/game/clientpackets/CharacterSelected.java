package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class CharacterSelected extends ClientBasePacket
{
	private GameEngine _clientThread;
	private byte[] _myArray = new byte[]{00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00};
	private byte[] _inbuf;

	public CharacterSelected(byte[] data, GameEngine clientThread)
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_inbuf = readB(18);
	}

	public CharacterSelected(GameEngine clientThread)
	{
		_clientThread = clientThread;
		_inbuf = _myArray;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x0d);
		writeB(_inbuf);
	}

}
