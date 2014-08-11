package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Мы on 10.08.14.
 */
public class CharSelected extends L2GameSocksClientPacket {
    @Override
    public void excecute() {
        int charSlot = readD();

        if(charSlot < 0 || charSlot > 6){
            _log.info("CharSelected correct to 0 from: "+charSlot);
            charSlot = 0;
        }
        writeC(0x0D);
        writeD(charSlot);
        writeH(0);
        writeD(0);
        writeD(0);
        writeD(0);
    }
}
