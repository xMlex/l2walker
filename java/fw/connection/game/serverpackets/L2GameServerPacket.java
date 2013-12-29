package fw.connection.game.serverpackets;

import java.util.logging.Logger;

import fw.connection.GameConnection;
import xmlex.jsc.BaseReceivablePacket;

public abstract class L2GameServerPacket extends BaseReceivablePacket<GameConnection>{
	protected static Logger _log = Logger.getLogger(L2GameServerPacket.class.getName());
}
