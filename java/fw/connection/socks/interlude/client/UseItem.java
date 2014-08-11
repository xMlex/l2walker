package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Maxim on 11.08.14.
 */
public class UseItem extends L2GameSocksClientPacket {

    private int objId;

    @Override
    public void excecute() {

        objId = readD();

        writeC(0x14);
        writeD(objId);
        writeD(0);
    }
}
