package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestSkillList extends ClientBasePacket {
    private GameEngine _clientThread;
    private final int _unk1;
    private final int _unk2;
    private final int _unk3;

    public RequestSkillList(byte[] message, GameEngine clientThread) {
	_clientThread = clientThread;
	_buf = ByteBuffer.wrap(message);
	_buf.order(ByteOrder.LITTLE_ENDIAN);

	readH(); // msg len
	readC(); // msg type
	_unk1 = readD();
	_unk2 = readD();
	_unk3 = readD();
    }

    public RequestSkillList(GameEngine clientThread) {
	_clientThread = clientThread;
	_unk1 = 0;
	_unk2 = 0;
	_unk3 = 0;
    }

    public void runImpl() {
	_clientThread.sendPacket(getMessage());
    }

    @Override
	protected void writeImpl() {
	writeC(0x3f);
	writeD(_unk1);
	writeD(_unk2);
	writeD(_unk3);
    }
}
