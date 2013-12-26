package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class PlayOk extends ServerBasePacket
{
	private int _playOk1, _playOk2;

	public PlayOk(byte data[])
	{
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); //read message lenght
		readC(); // (1) message type
	}

	public void write()
	{
		writeC(0x07);
		writeD(_playOk1);
		writeD(_playOk2);
	}
}
