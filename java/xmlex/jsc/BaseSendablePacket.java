package xmlex.jsc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import xmlex.jsc.ISendablePacket;

public abstract class BaseSendablePacket<T extends ISocketClientListener> implements ISendablePacket,Runnable {

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
	
	protected ByteBuffer getByteBuffer(){
		return _buf;
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
	
	public abstract void excecute();
	
	public ByteBuffer getData()
	{		
		byte[] result;
		_buf.flip();		
		result = new byte[_buf.limit()];
		System.arraycopy(_buf.array(), 0, result, 0, result.length);
		return ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);		
	}

	public void run() {				
		_buf = ByteBuffer.wrap(new byte[2*1024]).order(ByteOrder.LITTLE_ENDIAN);		
		try{
			excecute();
			getClient().getSocket().addPacket(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
