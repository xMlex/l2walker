package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class PlayFail extends ServerBasePacket
{
	private int _reason;
//		REASON_SYSTEM_ERROR			(0x01),
//		REASON_USER_OR_PASS_WRONG	(0x02),
//		REASON3						(0x03),
//		REASON4						(0x04),
//		REASON_TOO_MANY_PLAYERS		(0x0f);

	public PlayFail(byte data[])
	{
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); //read message lenght
		readC(); // (1) message type
	}


	protected void write()
	{
		writeC(0x06);
		writeC(_reason);
	}
}
