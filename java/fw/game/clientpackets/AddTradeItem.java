package fw.game.clientpackets;

import fw.game.GameEngine;
import fw.game.L2Item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class AddTradeItem extends ClientBasePacket
{
	private GameEngine _clientThread;
    private final int _tradeId;
    private final int _objectId;
    private final int _count;

    public AddTradeItem(byte[] message, GameEngine clientThread)
    {
    	_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
        _tradeId = readD();
        _objectId = readD();
        _count = readD();
    }

    public AddTradeItem(GameEngine clientThread, L2Item tradeItem, int count)
    {
    	_clientThread = clientThread;
        _tradeId = 1;
        _objectId = tradeItem.objectId;
        _count = count;
    }

    public void runImpl()
    {
    	_clientThread.sendPacket(getMessage());
    }

	@Override
	protected void writeImpl() {
		writeC(0x16);
		writeD(_tradeId);
		writeD(_objectId);
		writeD(_count);
	}

}
