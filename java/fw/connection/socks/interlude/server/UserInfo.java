package fw.connection.socks.interlude.server;

import fw.connection.socks.interlude.L2GameSocksServerPacket;

/**
 * Created by Мы on 11.08.14.
 */
public class UserInfo extends L2GameSocksServerPacket {
    @Override
    public void read() {
        getClient().user.x = readD();
        getClient().user.y = readD();
        getClient().user.z = readD();
        readD();
        getClient().user.objectId = readD();
        getClient().user.name = readS();
    }

    @Override
    public boolean excecute() {
        return true;
    }
}
