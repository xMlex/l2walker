package fw.extensions.network;

import fw.extensions.util.GArray;

import java.nio.ByteBuffer;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MMOClient<T extends MMOConnection>
{
	private T _connection;

	public boolean can_runImpl = true;
	public final GArray<Object> client_packets = new GArray<Object>();

	public MMOClient(T con)
	{
		setConnection(con);
		con.setClient(this);
	}

	public void setConnection(T con)
	{
		_connection = con;
	}

	public T getConnection()
	{
		return _connection;
	}

	public void closeNow(boolean error)
	{
		if(_connection != null)
			_connection.closeNow(error);
	}

	public void closeLater()
	{
		if(_connection != null)
			_connection.closeLater();
	}

	public boolean isConnected()
	{
		return _connection == null ? false : !_connection.isClosed();
	}

	public abstract boolean decrypt(ByteBuffer buf, int size);

	public abstract boolean encrypt(ByteBuffer buf, int size);

	protected void onDisconnection()
	{}

	protected void onForcedDisconnection()
	{}
}