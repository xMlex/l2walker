package fw.connection.socks.interlude;

import java.util.logging.Logger;

import fw.connection.socks.BaseSendableSocketPacket;
import fw.connection.socks.ISocksListener;
import fw.connection.socks.ListenerIntelude;


public abstract class L2GameSocksClientPacket extends BaseSendableSocketPacket<ListenerIntelude> {

	protected static Logger _log = Logger.getLogger(L2GameSocksClientPacket.class.getName());
}