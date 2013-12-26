package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fw.game.GameEngine;

public class SendTradeDone extends ServerBasePacket{
	private int _num;
	private GameEngine _clientThread;

	public SendTradeDone(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type 0x22
		_num = readD();
	}

	public void runImpl()
	{
		_clientThread.getVisualInterface().procSendTradeDone();
	}

	final void writeImpl()
	{
		writeC(0x22);
		writeD(_num);
	}
}
