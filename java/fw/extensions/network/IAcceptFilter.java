package fw.extensions.network;

import java.nio.channels.SocketChannel;

public interface IAcceptFilter
{
	public boolean accept(SocketChannel sc);
}
