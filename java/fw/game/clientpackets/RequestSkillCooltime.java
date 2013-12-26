package fw.game.clientpackets;

import fw.game.GameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestSkillCooltime extends ClientBasePacket {
	private GameEngine _clientThread;

	public RequestSkillCooltime(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

	}

	public RequestSkillCooltime(GameEngine clientThread)
	{
		_clientThread = clientThread;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x9d);
	}
}
