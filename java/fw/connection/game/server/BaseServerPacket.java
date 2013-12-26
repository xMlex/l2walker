package fw.connection.game.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javolution.text.TextBuilder;

public abstract class BaseServerPacket<T> implements Runnable {
	protected T _client;
	protected ByteBuffer _buf;

	public void setClient(T client)
	{
		_client = client;
	}

	public T getClient()
	{
		return _client;
	}
	
	public ByteBuffer getByteBuffer(){
		return _buf;
	}	
	
	public void setByteBuffer(ByteBuffer buf)
	{
		_buf = buf;	
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		readC();
	}
	
	protected int getAvaliableBytes()
	{
		return getByteBuffer().remaining();
	}

	public abstract void run();
	public abstract void read();

	protected void readB(byte[] dst)
	{
		getByteBuffer().get(dst);
	}

	protected void readB(byte[] dst, int offset, int len)
	{
		getByteBuffer().get(dst, offset, len);
	}

	protected int readC()
	{
		return getByteBuffer().get() & 0xFF;
	}

	protected int readH()
	{
		return getByteBuffer().getShort() & 0xFFFF;
	}

	protected int readD()
	{
		return getByteBuffer().getInt();
	}

	protected long readQ()
	{
		return getByteBuffer().getLong();
	}

	protected double readF()
	{
		return getByteBuffer().getDouble();
	}

	protected String readS()
	{
		TextBuilder tb = TextBuilder.newInstance();
		char ch;
		ByteBuffer buf = getByteBuffer();

		while((ch = buf.getChar()) != 0)
			tb.append(ch);
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}

	protected String readS(int Maxlen)
	{
		String ret = readS();
		return ret.length() > Maxlen ? ret.substring(0, Maxlen) : ret;
	}
}
