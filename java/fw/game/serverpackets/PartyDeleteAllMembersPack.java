package fw.game.serverpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PartyDeleteAllMembersPack extends ServerBasePacket
{
	private PartyDeleteAllMembersPack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);

		gameEngine.getMaps().getMapL2PartyChar().clear(); 
	    gameEngine.deleteAllPartyChars();		 

	}
}
