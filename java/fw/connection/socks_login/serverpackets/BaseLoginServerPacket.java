package fw.connection.socks_login.serverpackets;

import fw.connection.socks.BaseReceivableSocksPacket;
import fw.connection.socks.ISocksListener;
import fw.connection.socks_login.LitenerInterludeLogin;

import java.util.logging.Logger;

/**
 * Created by Мы on 04.08.14.
 */
public abstract class BaseLoginServerPacket extends BaseReceivableSocksPacket<LitenerInterludeLogin> {
    protected static Logger _log = Logger.getLogger(BaseLoginServerPacket.class.getName());
}
