package fw.connection.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class BaseSendablePacket<T> {
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
	
	public abstract void write();
	
	protected ByteBuffer getByteBuffer(){
		return _buf;
	}
	
	public final byte[] getBytes() {
		byte[] result;
		_buf = ByteBuffer.allocate(16 * 1024);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		try {
			// _buf.putShort((short)0);
			try {
				write();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			_buf.flip();
			//_buf.putShort((short)_buf.limit());
			result = new byte[(short) _buf.limit()];
			System.arraycopy(_buf.array(), 0, result, 0, result.length);
		} finally {
			_buf = null;
		}
		return result;
	}
	
}
