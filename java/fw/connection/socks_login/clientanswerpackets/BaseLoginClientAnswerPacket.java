package fw.connection.socks_login.clientanswerpackets;

import fw.connection.socks.BaseSendableSocketPacket;
import fw.connection.socks_login.LitenerInterludeLogin;

import java.util.logging.Logger;

/**
 * Created by Мы on 04.08.14.
 */
public abstract class BaseLoginClientAnswerPacket  extends BaseSendableSocketPacket<LitenerInterludeLogin> {
    protected static Logger _log = Logger.getLogger(BaseLoginClientAnswerPacket.class.getName());
}
