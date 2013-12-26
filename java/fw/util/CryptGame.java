/*
 * This program is free software; you can redistribute it and/or modify
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
package fw.util;

import java.nio.ByteBuffer;


public class CryptGame {
	private final byte[] _key = new byte[8];
	private boolean enabled;

	public void setKey(byte[] key)
	{
		System.arraycopy(key,0, _key, 0, 8);
		enabled = true;
	}

	public /*synchronized?*/ void decrypt(ByteBuffer buf)
	{
		if (!enabled)
			return;

		final int sz = buf.remaining();
		int temp = 0;
		for (int i=0; i < sz; i++)
		{
			int temp2 = buf.get(i);
			buf.put(i, (byte)(temp2 ^ _key[i&7] ^ temp));
			temp = temp2;
		}

		int old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;

		old += sz;

		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
	}

	public /*synchronized?*/ void encrypt(ByteBuffer buf)
	{
		if (!enabled)
			return;

		int temp = 0;
		final int sz = buf.remaining();
		for (int i=0; i < sz; i++)
		{
			int temp2 = buf.get(i);
			buf.put(i, (byte)(temp=(temp2 ^ _key[i&7] ^ temp)));
		}

		int old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;

		old += sz;

		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
	}

	public void decrypt(byte[] raw)
	{
		if (!enabled)
			return;

		int temp = 0;
		for (int i=0; i<raw.length; i++)
		{
			int temp2 = raw[i] &0xff;
			raw[i] = (byte)(temp2 ^ (_key[i&7] &0xff) ^ temp);
			temp = temp2;
		}

		int old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;

		old += raw.length;

		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
	}

	public void encrypt(byte[] raw)
	{
		if (!enabled)
			return;

		int temp = 0;
		for (int i=0; i<raw.length; i++)
		{
			int temp2 = raw[i] &0xff;
			raw[i] = (byte)(temp2 ^ (_key[i&7] &0xff) ^ temp);
			temp = raw[i];
		}

		int old = _key[0] &0xff;
		old |= _key[1] << 8 &0xff00;
		old |= _key[2] << 0x10 &0xff0000;
		old |= _key[3] << 0x18 &0xff000000;

		old += raw.length;

		_key[0] = (byte)(old &0xff);
		_key[1] = (byte)(old >> 0x08 &0xff);
		_key[2] = (byte)(old >> 0x10 &0xff);
		_key[3] = (byte)(old >> 0x18 &0xff);
	}
}
