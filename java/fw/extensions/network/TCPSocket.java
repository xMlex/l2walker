package fw.extensions.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class TCPSocket implements ISocket
{
	private final Socket _socket;

	public TCPSocket(Socket socket)
	{
		_socket = socket;
	}

	public void close() throws IOException
	{
		_socket.close();
	}

	public ReadableByteChannel getReadableByteChannel()
	{
		return _socket.getChannel();
	}

	public WritableByteChannel getWritableByteChannel()
	{
		return _socket.getChannel();
	}

	public InetAddress getInetAddress()
	{
		return _socket.getInetAddress();
	}

	public InetAddress getLocalAddress()
	{
		return _socket.getLocalAddress();
	}

	public Socket getSocket()
	{
		return _socket;
	}
}