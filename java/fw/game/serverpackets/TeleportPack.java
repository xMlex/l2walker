package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.UserChar;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TeleportPack extends ServerBasePacket
{
	private TeleportPack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);

		int objId = buf.getInt();
		int x = buf.getInt();
		int y = buf.getInt();
		int z = buf.getInt();

		if (gameEngine.getUserChar().objId == objId)
		{
			UserChar userChar = gameEngine.getUserChar();
			userChar.x = x;
			userChar.y = y;
			userChar.z = z;
			userChar.cancelMovments();

			gameEngine.removeCurrentTarget();
			gameEngine.getVisualInterface().procTeleportClear();
			gameEngine.getMaps().teleportClear();
			gameEngine.sendEnterWorld();
		}

	}

}
