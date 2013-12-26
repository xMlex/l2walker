package fw.game.clientpackets;

import fw.game.GameEngine;

//  D0:05
public class RequestAutoSoulShot extends ClientBasePacket {
	private GameEngine _clientThread;
	private final int _itemId;
    private final int _type;		// 1 = on : 0 = off;

    public RequestAutoSoulShot(GameEngine clientThread, int _itemId, int _type) {
    	_clientThread = clientThread;
    	this._itemId = _itemId;
		this._type = _type;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0xd0);
		writeH(0x05);
		writeD(_itemId);
		writeD(_type);
	}
}
