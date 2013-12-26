package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Char;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TargetUnselectedPack extends ServerBasePacket
{
	static int x;
	static int y;
	static int z;

	private TargetUnselectedPack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);

		int objId = buf.getInt();
		x = buf.getInt();
		y = buf.getInt();
		z = buf.getInt();

		L2Char l2Char = gameEngine.getUserChar();
		if (l2Char != null)
		{
			if(l2Char.objId==objId)
			{
				gameEngine.removeCurrentTarget();
				return;
			}
		}



	}

}
