package fw.game.clientpackets;

import fw.game.GameEngine;
import fw.game.UserChar;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Action extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _objectId;
	private int _x;
	private int _y;
	private int _z;
	private int _actionId;

	public Action(byte[] message, GameEngine clientThread) {
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_objectId = readD();
		_x = readD();
		_y = readD();
		_z = readD();
		_actionId = readD();
	}

	public Action(GameEngine clientThread, UserChar userChar, int objectIdTarget, int actionId){
		_clientThread = clientThread;
		_objectId = objectIdTarget;
		_x = userChar.x;
		_y = userChar.y;
		_z = userChar.z;
		_actionId = actionId;
	}


	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x04);
		writeD(_objectId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeC(_actionId);
	}
}
