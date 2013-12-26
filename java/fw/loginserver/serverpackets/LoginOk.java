package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Format: dddddddd
 * f: the session key
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * b: 16 bytes - unknown
 */
public final class LoginOk extends ServerBasePacket
{
	private int _loginOk1, _loginOk2;

	public LoginOk(byte data[])
	{
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); //read message lenght
		readC(); // (1) message type
		_loginOk1 = readD();
		_loginOk2 = readD();
	}

	protected void write()
	{
		writeC(0x03);
		writeD(_loginOk1);
		writeD(_loginOk2);
		writeD(0x00);
		writeD(0x00);
		writeD(0x000003ea);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeB(new byte[16]);
	}
	public int getOk1(){
		return _loginOk1;
	}
	public int getOk2(){
		return _loginOk2;
	}
}
