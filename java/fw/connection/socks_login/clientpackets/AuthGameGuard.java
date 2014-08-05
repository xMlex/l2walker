package fw.connection.socks_login.clientpackets;

/**
 * Created by Мы on 04.08.14.
 */
public class AuthGameGuard extends BaseLoginClientPacket{
    @Override
    public void excecute() {
        int session = readD();
        if(session != getClient().sessionId)
            _log.info("session id incorrect: "+getClient().sessionId+" != "+session);
        writeC(0x07);
        writeD(getClient().sessionId);
        writeDD(getClient().ggList);
    }
}
