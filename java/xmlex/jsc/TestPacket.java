package xmlex.jsc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestPacket implements ISendablePacket{

	private ByteBuffer buf;
	public TestPacket() {
		buf = ByteBuffer.wrap(new byte[4]).order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer getData() {
		return buf;
	}

}
