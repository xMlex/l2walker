package fw.extensions.network;

import javolution.text.TextBuilder;

import java.nio.ByteBuffer;

@SuppressWarnings({"rawtypes" })
public abstract class ReceivablePacket<T extends MMOClient> extends AbstractPacket<T> implements Runnable
{
	protected ByteBuffer _buf;

	protected ReceivablePacket()
	{}

	protected void setByteBuffer(ByteBuffer buf)
	{
		_buf = buf;
	}

	@Override
	protected ByteBuffer getByteBuffer()
	{
		return _buf;
	}

	protected int getAvaliableBytes()
	{
		return getByteBuffer().remaining();
	}

	protected abstract boolean read();

	@Override
	public abstract void run();

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