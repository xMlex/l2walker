package fw.connection.socks_login.serverpackets;

/**
 * Created by Мы on 05.08.14.
 */
public class LoginOk  extends BaseLoginServerPacket {
    @Override
    public void read() {
        getClient().SessionKey1[0] = readD();
        getClient().SessionKey1[1] = readD();
    }

    @Override
    public boolean excecute() {
        return true;
    }
}
