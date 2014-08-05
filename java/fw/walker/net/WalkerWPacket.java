package fw.walker.net;

import fw.extensions.util.Location;
import xmlex.ext.crypt.NewCrypt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Maxim on 05.08.14.
 */
public abstract class WalkerWPacket {
    //protected T _client;
    private ByteBuffer _buf;
    protected ByteBuffer getByteBuffer(){
        return _buf;
    }

    public abstract void excecute();

    public void run() {
        _buf = ByteBuffer.wrap(new byte[1*1024]).order(ByteOrder.LITTLE_ENDIAN);
        try{
            excecute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ByteBuffer getData()
    {
        byte[] result;
        _buf.flip();
        int size = _buf.limit() + 4;
        size += 8 - size % 8;

        result = new byte[size];
        System.arraycopy(_buf.array(), 0, result, 0, result.length);
        //NewCrypt.appendChecksum(result);
        return ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);
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
}
