package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class AccountKicked extends ServerBasePacket
{
	private int _reasonCode;

	public AccountKicked(byte data[])
	{
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		//		readH();			//read message lenght
		readC();					// (1) message type
		_reasonCode = readD();		// 
									//		REASON_DATA_STEALER			(0x01)
									//		REASON_GENERIC_VIOLATION	(0x08)
									//		REASON_7_DAYS_SUSPENDED		(0x10)
									//		REASON_PERMANENTLY_BANNED	(0x20)

	}

	public void runImpl() {

	}
	
	public void write()
	{
		writeC(0x02);
		writeD(_reasonCode);
	}

}
