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
package xmlex.ext.crypt;

import java.io.IOException;

import jext.util.Rnd;


/**
 * @author KenM
 */
public class LoginCrypt
{
	public static final byte[] STATIC_BLOWFISH_KEY =
	{
			(byte) 0x6b,
			(byte) 0x60,
			(byte) 0xcb,
			(byte) 0x5b,
			(byte) 0x82,
			(byte) 0xce,
			(byte) 0x90,
			(byte) 0xb1,
			(byte) 0xcc,
			(byte) 0x2b,
			(byte) 0x6c,
			(byte) 0x55,
			(byte) 0x6c,
			(byte) 0x6c,
			(byte) 0x6c,
			(byte) 0x6c
	};

	private NewCrypt _staticCrypt = new NewCrypt(true);
	private NewCrypt _crypt;
	private boolean _static = false,_first = true;

	public void setKey(byte[] key)
	{
		_crypt = new NewCrypt(key);
	}

	public boolean decrypt(byte[] raw, final int offset, final int size) throws IOException
	{
		
		if(_first){
			_staticCrypt.decrypt(raw, offset, size);			
			NewCrypt.decXORPass(raw, offset, size);
			_first =false;
			return true;
		}else
			_crypt.decrypt(raw, offset, size);
		
		return NewCrypt.verifyChecksum(raw, offset, size);
	}

	public int encrypt(byte[] raw, final int offset, int size) throws IOException
	{
		// reserve checksum
		size += 4;

		if(_static)
		{
			// reserve for XOR "key"
			size += 4;

			// padding
			size += 8 - size % 8;
			NewCrypt.encXORPass(raw, offset, size, Rnd.nextInt());
			_staticCrypt.crypt(raw, offset, size);

			_static = false;
		}
		else
		{
			// padding
			size += 8 - size % 8;
			NewCrypt.appendChecksum(raw, offset, size);
			_crypt.crypt(raw, offset, size);
		}
		return size;
	}
}
