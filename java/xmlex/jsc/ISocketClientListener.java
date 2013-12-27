package xmlex.jsc;

import java.nio.ByteBuffer;

public interface ISocketClientListener {
	
	public abstract void onConnected();
	public abstract void onDisconnected();
	public abstract void onDataRead(ByteBuffer buf);
	public abstract void onDataWrite(ByteBuffer buf);
	public abstract SocketClient getSocket();
}
