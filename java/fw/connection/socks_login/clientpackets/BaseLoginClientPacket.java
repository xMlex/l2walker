package fw.connection.socks_login.clientpackets;

import fw.connection.socks_login.BaseSendableLoginSocketPacket;
import fw.connection.socks_login.LitenerInterludeLogin;

import java.util.logging.Logger;

/**
 * Created by Мы on 04.08.14.
 */
public abstract class BaseLoginClientPacket  extends BaseSendableLoginSocketPacket<LitenerInterludeLogin> {
    protected static Logger _log = Logger.getLogger(BaseLoginClientPacket.class.getName());
}