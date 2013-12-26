package fw.extensions.network;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface ISocket
{
	public void close() throws IOException;

	public WritableByteChannel getWritableByteChannel();

	public ReadableByteChannel getReadableByteChannel();

	public InetAddress getInetAddress();

	public InetAddress getLocalAddress();
}
