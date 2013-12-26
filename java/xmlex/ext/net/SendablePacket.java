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

/**
 * @author KenM
 * @param <T> 
 */
public abstract class SendablePacket<T extends MMOClient> extends AbstractPacket<T> implements Runnable
{
	protected final void putInt(final int value)
	{
		_buf.putInt(value);
	}
	
	protected final void putDouble(final double value)
	{
		_buf.putDouble(value);
	}
	
	protected final void putFloat(final float value)
	{
		_buf.putFloat(value);
	}
	
	protected final void writeC(final int data)
	{
		_buf.put((byte) data);
	}
	
	protected final void writeF(final double value)
	{
		_buf.putDouble(value);
	}
	
	protected final void writeH(final int value)
	{
		_buf.putShort((short) value);
	}
	
	protected final void writeD(final int value)
	{
		_buf.putInt(value);
	}
	
	protected final void writeQ(final long value)
	{
		_buf.putLong(value);
	}
	
	protected final void writeB(final byte[] data)
	{
		_buf.put(data);
	}
	
	protected final void writeS(final String text)
	{
		if (text != null)
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
			{
				_buf.putChar(text.charAt(i));
			}
		}
		
		_buf.putChar('\000');
	}
	/** Read packet in socket thread */
	protected abstract void write();
	/** Execute packet in socket thread */
	protected abstract boolean excecute();

	public void run(){
		try{
			write();
			excecute();
		}catch(Exception e){
			_log.warning("Error excecute packet: "+this.getClass().getName()+" Error: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
