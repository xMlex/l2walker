package fw.extensions.network;

import java.nio.ByteBuffer;

@SuppressWarnings({ "rawtypes" })
public abstract class SendablePacket<T extends MMOClient> extends AbstractPacket<T>
{
	@Override
	protected ByteBuffer getByteBuffer()
	{
		return null;//getCurrentSelectorThread().getWriteBuffer();
	}

	@Override
	public T getClient()
	{
		//SelectorThread<T> selector = getCurrentSelectorThread();
		return null;//selector == null ? null : selector.getWriteClient();
	}

	protected void writeC(int data)
	{
		getByteBuffer().put((byte) data);
	}

	protected void writeF(double value)
	{
		getByteBuffer().putDouble(value);
	}

	protected void writeH(int value)
	{
		getByteBuffer().putShort((short) value);
	}

	protected void writeD(int value)
	{
		getByteBuffer().putInt(value);
	}

	/**
	 * Отсылает число позиций + массив
	 */
	protected void writeDD(int[] values, boolean sendCount)
	{
		ByteBuffer buf = getByteBuffer();
		if(sendCount)
			buf.putInt(values.length);
		for(int value : values)
			buf.putInt(value);
	}

	protected void writeDD(int[] values)
	{
		writeDD(values, false);
	}

	protected void writeQ(long value)
	{
		getByteBuffer().putLong(value);
	}

	protected void writeB(byte[] data)
	{
		getByteBuffer().put(data);
	}

	protected void writeS(CharSequence charSequence)
	{
		ByteBuffer buf = getByteBuffer();
		if(charSequence == null)
			charSequence = "";

		int length = charSequence.length();
		for(int i = 0; i < length; i++)
			buf.putChar(charSequence.charAt(i));
		buf.putChar('\000');
	}

	protected abstract void write();

	protected abstract int getHeaderSize();

	protected abstract void writeHeader(int dataSize);
}