package fw.connection.socks_login.serverpackets;

import fw.connection.socks_login.LitenerInterludeLogin;

/**
 * Created by Мы on 05.08.14.
 */
public class GGAuth extends BaseLoginServerPacket  {
    @Override
    public void read() {
        getClient().ggResponce = readD();
        getClient().state = LitenerInterludeLogin.LOGIN_IT_STATE.AUTHED_GG;

    }

    @Override
    public boolean excecute() {
        return true;
    }
}
