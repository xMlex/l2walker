package fw.connection.game;

import java.nio.ByteBuffer;

import xmlex.jsc.BaseReceivablePacket;
import fw.connection.GameConnection;
import fw.connection.IPacketHandler;
import fw.connection.game.serverpackets.*;

public class L2GamePaccketHandler implements IPacketHandler<GameConnection>{

	public BaseReceivablePacket<GameConnection> handlePacket(ByteBuffer buf, GameConnection client) {
		
		BaseReceivablePacket<GameConnection>  msg = null;
		int id = buf.get() & 0xFF;
		
		switch (id) {
		case 0x00:
			msg=new KeyPacket();
			break;

		default:
			System.out.println("Read uncknow packet: 0x"+Integer.toHexString(id));
			break;
		}	
		System.out.println("Pos: "+buf.position());
		return msg;
	}

}
