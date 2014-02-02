package fw.connection.socks;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javolution.text.TextBuilder;
import fw.extensions.util.Location;

public abstract class BaseSendableSocketPacket<T extends ISocksListener> implements Runnable {
	protected T _client;
	private ByteBuffer _buf,_buf_read;
	
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
	
	protected void writeLoc(Location loc){
		writeD(loc.x);
		writeD(loc.y);
		writeD(loc.z);
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
	
	//READ
	protected void readB(byte[] dst) {
		getBufRead().get(dst);
	}

	protected void readB(byte[] dst, int offset, int len) {
		getBufRead().get(dst, offset, len);
	}

	protected int readC() {
		return getBufRead().get() & 0xFF;
	}

	protected boolean readC(int c) {
		int val = getBufRead().get() & 0xFF;
		return (val == c ? true : false);
	}

	protected int readH() {
		return getBufRead().getShort() & 0xFFFF;
	}

	protected boolean readH(int c) {
		int val = getBufRead().getShort() & 0xFFFF;
		return (val == c ? true : false);
	}

	protected int readD() {
		return getBufRead().getInt();
	}
	protected Location readLoc() {
		return new Location(readD(), readD(), readD());
	}

	protected long readQ() {
		return getBufRead().getLong();
	}

	protected double readF() {
		return getBufRead().getDouble();
	}

	protected String readS() {
		TextBuilder tb = TextBuilder.newInstance();
		char ch;
		ByteBuffer buf = getBufRead();

		while ((ch = buf.getChar()) != 0)
			tb.append(ch);
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}
	//END
	
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
		_buf = ByteBuffer.wrap(new byte[1*1024]).order(ByteOrder.LITTLE_ENDIAN);		
		try{
			excecute();
			//getClient().addToServer(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ByteBuffer getBufRead() {
		return _buf_read;
	}

	public void setBufRead(ByteBuffer _buf_read) {
		this._buf_read = _buf_read;
	}

}
