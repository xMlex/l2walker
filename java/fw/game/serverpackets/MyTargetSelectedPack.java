package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Char;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MyTargetSelectedPack extends ServerBasePacket
{
	private static short color;

	private MyTargetSelectedPack()
	{
	}

	public static void runImplementatiton(GameEngine gameEngine, byte data[])
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);

		int objId = buf.getInt();
		setColor(buf.getShort());

		L2Char targetChar =null;

		if(gameEngine.getUserChar().objId==objId)
			targetChar=	gameEngine.getUserChar();
		else
			targetChar=gameEngine.getMaps().getChar(objId);

		if (targetChar != null)
			gameEngine.setCurrentTarget(targetChar);

	}

	public static void setColor(short color) {
		MyTargetSelectedPack.color = color;
	}

	public static short getColor() {
		return color;
	}

}
