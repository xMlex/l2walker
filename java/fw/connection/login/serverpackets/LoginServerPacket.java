package fw.connection.login.serverpackets;

import java.util.logging.Logger;

import fw.connection.LoginConnection;

public abstract class LoginServerPacket extends BaseLoginServerPacket<LoginConnection> {
	protected static Logger _log = Logger.getLogger(LoginServerPacket.class.getName());
}
