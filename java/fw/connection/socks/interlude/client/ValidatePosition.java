package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;
import fw.extensions.util.Location;

/**
 * Created by Maxim on 11.08.14.
 */
public class ValidatePosition extends L2GameSocksClientPacket {
    private Location loc = new Location();
    @Override
    public void excecute() {
        loc.x = readD();
        loc.y = readD();
        loc.z = readD();
        loc.h = readD();

        writeC(0x48);
        writeLoc(loc);
        writeD(loc.h);
        writeD(0);
    }
}
