package fw.game.serverpackets;
import fw.game.GameEngine;
import fw.game.L2Char;
import fw.game.L2Item;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DeleteObject extends ServerBasePacket
{
	private GameEngine _clientThread;
	int _objId;

	public DeleteObject(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type
		_objId=readD();
	}

	public void runImpl()
	{
		L2Char l2Char = _clientThread.getMaps().getChar(_objId);
		if (l2Char != null)
		{
			_clientThread.getMaps().removeChar(_objId);
			_clientThread.deleteL2char(l2Char);
			return;
		}
		
		L2Item item = _clientThread.getMaps().getItem(_objId);
		if (item != null)
		{
			_clientThread.getMaps().removeItem(_objId);
			_clientThread.deleteItem(item);
			return;
		}
	}
}
