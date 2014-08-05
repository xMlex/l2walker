package fw.connection.socks_login.clientpackets;

/**
 * Created by Мы on 05.08.14.
 */
public class RequestServerList extends BaseLoginClientPacket {
    @Override
    public void excecute() {
        writeC(0x05);
        writeDD(getClient().SessionKey1);
        writeD(0x04000000);
    }
}
