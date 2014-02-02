package fw.connection.socks;

import java.nio.ByteBuffer;

public interface IPacketHandler<T extends ISocksListener> {	
	public boolean handlePacketClient(ByteBuffer buf, T client);
	public boolean handlePacketServer(ByteBuffer buf, T client);
}
