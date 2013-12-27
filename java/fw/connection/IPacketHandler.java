package fw.connection;

import java.nio.ByteBuffer;

import xmlex.jsc.BaseReceivablePacket;
import xmlex.jsc.ISocketClientListener;

public interface IPacketHandler<T extends ISocketClientListener> {
	
	public BaseReceivablePacket<T> handlePacket(ByteBuffer buf, T client);
}
