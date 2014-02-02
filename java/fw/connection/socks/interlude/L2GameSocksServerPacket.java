package fw.connection.socks.interlude;

import java.util.logging.Logger;

import fw.connection.socks.BaseReceivableSocksPacket;
import fw.connection.socks.ISocksListener;

public abstract class L2GameSocksServerPacket extends BaseReceivableSocksPacket<ISocksListener> {	

	protected static Logger _log = Logger.getLogger(L2GameSocksServerPacket.class.getName());
}