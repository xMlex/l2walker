package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class AnswerTradeRequest extends ClientBasePacket{
	private GameEngine _clientThread;
	private int _response; //1-ok, 0-cancel
	private int _unk1 = 0;
	private int _unk2 = 0;
	private int _unk3 = 0;
	
	public AnswerTradeRequest(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_response = readD();
		_unk1 = readD();
		_unk2 = readD();
		_unk3 = readD();
	}
	
	public AnswerTradeRequest(GameEngine clientThread, int response) {
		_clientThread = clientThread;
		_response = response;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}
	
	@Override
	protected void writeImpl() {
		writeC(0x44);
		writeD(_response);
		writeD(_unk1);
		writeD(_unk2);
		writeD(_unk3);
	}

}
