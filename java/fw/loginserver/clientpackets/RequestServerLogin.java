package fw.loginserver.clientpackets;

/**
 * Fromat is ddc d: first part of session id d: second part of session id c:
 * server ID
 */
public class RequestServerLogin extends ClientBasePacket {
	private int _skey1;
	private int _skey2;
	private int _serverId;

	public RequestServerLogin(int skey1, int skey2, int serverId) {
		_skey1 = skey1;
		_skey2 = skey2;
		_serverId = serverId;
	}

	@Override
	protected void writeImpl() {
		writeC(0x02);
		writeD(_skey1);
		writeD(_skey2);
		writeC(_serverId);
		writeB(new byte[14]);
	}
}
