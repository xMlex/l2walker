package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Maxim on 11.08.14.
 */
public class RequestActionUse extends L2GameSocksClientPacket {


    @Override
    public void excecute() {

        writeC(0x45);
        writeD(readD());
        writeD(readD());
        writeC(0);
    }

}
