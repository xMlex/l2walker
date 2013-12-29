package fw.connection.game.clientpackets;

import java.util.logging.Logger;

import xmlex.jsc.BaseSendablePacket;
import fw.connection.GameConnection;

public abstract class L2GameClientPacket extends BaseSendablePacket<GameConnection> {
	protected static Logger _log = Logger.getLogger(L2GameClientPacket.class.getName());
}
