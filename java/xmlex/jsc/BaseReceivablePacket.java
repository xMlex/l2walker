package xmlex.jsc;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

public abstract class BaseReceivablePacket <T extends ISocketClientListener> implements Runnable {

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
	
	public void setByteBuffer(ByteBuffer buf){
		_buf = buf;
	}
	
	protected ByteBuffer getByteBuffer(){
		return _buf;
	}
	
	protected void readB(byte[] dst) {
		getByteBuffer().get(dst);
	}

	protected void readB(byte[] dst, int offset, int len) {
		getByteBuffer().get(dst, offset, len);
	}

	protected int readC() {
		return getByteBuffer().get() & 0xFF;
	}

	protected int readH() {
		return getByteBuffer().getShort() & 0xFFFF;
	}

	protected int readD() {
		return getByteBuffer().getInt();
	}

	protected long readQ() {
		return getByteBuffer().getLong();
	}

	protected double readF() {
		return getByteBuffer().getDouble();
	}
	
	protected String readS() {
		TextBuilder tb = TextBuilder.newInstance();
		char ch;
		ByteBuffer buf = getByteBuffer();

		while ((ch = buf.getChar()) != 0)
			tb.append(ch);
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}
	public abstract void read();
	public abstract void excecute();
	
	public void run() {
		
		try{
			excecute();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
