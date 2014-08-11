package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Мы on 10.08.14.
 */
public class RequestManorList extends L2GameSocksClientPacket {
    @Override
    public void excecute() {
        writeC(0xD0);
        writeH(8);
    }
}
