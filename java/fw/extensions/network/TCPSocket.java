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

	@Override
	public void close() throws IOException
	{
		_socket.close();
	}

	@Override
	public ReadableByteChannel getReadableByteChannel()
	{
		return _socket.getChannel();
	}

	@Override
	public WritableByteChannel getWritableByteChannel()
	{
		return _socket.getChannel();
	}

	@Override
	public InetAddress getInetAddress()
	{
		return _socket.getInetAddress();
	}

	@Override
	public InetAddress getLocalAddress()
	{
		return _socket.getLocalAddress();
	}

	public Socket getSocket()
	{
		return _socket;
	}
}