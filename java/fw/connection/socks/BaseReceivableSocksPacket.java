package fw.connection.socks;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

public abstract class BaseReceivableSocksPacket<T extends ISocksListener> {
	protected T _client;
	protected ByteBuffer _buf;

	public void setClient(T client) {
		_client = client;
	}

	public T getClient() {
		return _client;
	}

	public void setByteBuffer(ByteBuffer buf) {
		_buf = buf;
	}

	protected ByteBuffer getByteBuffer() {
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

	protected boolean readC(int c) {
		int val = getByteBuffer().get() & 0xFF;
		return (val == c ? true : false);
	}

	protected int readH() {
		return getByteBuffer().getShort() & 0xFFFF;
	}

	protected boolean readH(int c) {
		int val = getByteBuffer().getShort() & 0xFFFF;
		return (val == c ? true : false);
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

	public abstract boolean excecute();

	public boolean run() {

		try {
			return excecute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}