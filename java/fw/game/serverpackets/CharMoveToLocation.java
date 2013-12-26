package fw.game.serverpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * 0000: 01  7a 73 10 4c  b2 0b 00 00  a3 fc 00 00  e8 f1 ff    .zs.L...........
 * 0010: ff  bd 0b 00 00  b3 fc 00 00  e8 f1 ff ff             .............
 * 0x01
 * ddddddd
 **/
public class CharMoveToLocation extends ServerBasePacket
{
	@SuppressWarnings("unused")
	private int _objectId, _x, _y, _z, _xDst, _yDst, _zDst;
	@SuppressWarnings("unused")
	private GameEngine _clientThread;

	CharMoveToLocation(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type
		_objectId = readD(); 	//Read object
		_xDst = readD();   		//To X
		_yDst = readD();   		//To Y
		_zDst = readD();   		//To Z
		_x = readD();			//From X
		_y = readD();			//From Y
		_z = readD();			//From Z
	}

	public void runImpl() {

	}
}
