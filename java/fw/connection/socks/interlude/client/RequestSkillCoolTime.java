package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Maxim on 11.08.14.
 */
public class RequestSkillCoolTime extends L2GameSocksClientPacket{
    @Override
    public void excecute() {
        writeC(0x9D);
    }
}
