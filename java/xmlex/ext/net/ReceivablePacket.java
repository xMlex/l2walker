/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package xmlex.ext.net;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;


/**
 * @author KenM
 * @param <T> 
 */
public abstract class ReceivablePacket<T extends MMOClient> extends AbstractPacket<T> implements Runnable
{
	protected ByteBuffer _buf;
	
	public void setByteBuffer(ByteBuffer buf) {
		_buf = buf;
	}
	
	@Override
	protected ByteBuffer getByteBuffer() {
		return _buf;
	}
	
	protected int getAvaliableBytes() {
		return getByteBuffer().remaining();
	}
	/** Read packet in socket thread */
	protected abstract void read();	
	/** Execute packet in thread thread pool */
	protected abstract void excecute();

	public void run(){
		try{
			excecute();
		}catch(Exception e){
			_log.warning("Error excecute packet: "+this.getClass().getName()+" Error: "+e.getMessage());
			e.printStackTrace();
		}
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

	protected String readS(int Maxlen) {
		String ret = readS();
		return ret.length() > Maxlen ? ret.substring(0, Maxlen) : ret;
	}
}
