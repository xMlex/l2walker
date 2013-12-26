package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Format: d d: the failure reason
 */
public final class LoginFail extends ServerBasePacket {
	private int _reason;

	// REASON_SYSTEM_ERROR (0x01),
	// REASON_PASS_WRONG (0x02),
	// REASON_USER_OR_PASS_WRONG (0x03),
	// REASON_ACCESS_FAILED (0x04),
	// REASON_ACCOUNT_IN_USE (0x07),
	// REASON_SERVER_OVERLOADED (0x0f),
	// REASON_SERVER_MAINTENANCE (0x10),
	// REASON_TEMP_PASS_EXPIRED (0x11),
	// REASON_DUAL_BOX (0x23);

	public LoginFail(byte data[]) {
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); //read message lenght
		readC(); // (1) message type
		_reason = readD();
	}

	public void write() {
		writeC(0x01);
		writeD(_reason);
	}
}
