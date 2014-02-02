package fw.connection.socks.interlude;

import java.util.logging.Logger;

import fw.connection.socks.BaseSendableSocketPacket;
import fw.connection.socks.ISocksListener;


public abstract class L2GameSocksClientPacket extends BaseSendableSocketPacket<ISocksListener> {	

	protected static Logger _log = Logger.getLogger(L2GameSocksClientPacket.class.getName());
}