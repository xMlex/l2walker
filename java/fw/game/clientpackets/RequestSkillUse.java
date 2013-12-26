package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestSkillUse extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _skillId;
	private int _control;
	private int _shift;

	public RequestSkillUse(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_skillId = readD();
		_control = readD();
		_shift = readD();
	}

	public RequestSkillUse(GameEngine clientThread,int skill_id,int ctrlPressed,int shiftPressed)
	{
		_clientThread = clientThread;
		_skillId = skill_id;
		_control = ctrlPressed;
		_shift = shiftPressed;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x2f);
		writeD(_skillId);
		writeD(_control);
		writeC(_shift);
	}
}
