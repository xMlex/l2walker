package fw.loginserver.clientpackets;

/**
 * Format: ddc
 * d: fist part of session id
 * d: second part of session id
 * c: ?
 */
public class RequestServerList extends ClientBasePacket
{
	private int _skey1;
	private int _skey2;
	private int _data3;

	public RequestServerList(int skey1, int skey2, int data)
	{
			_skey1  = skey1; // loginOk 1
			_skey2  = skey2; // loginOk 2
			_data3 = data;
	}

	@Override
	protected void writeImpl() {
		writeC(0x05);
		writeD(_skey1);
		writeD(_skey2);
		writeC(_data3);
		writeB(new byte[14]);
	}
}
