package fw.connection.socks_login.clientpackets;

/**
 * Created by Мы on 05.08.14.
 */
public class RequestAuthLogin extends BaseLoginClientPacket {
    @Override
    public void excecute() {
        byte[] sraLogin = new byte[128];
        readB(sraLogin);

        writeC(0x00);
        writeB(sraLogin);
    }
}
