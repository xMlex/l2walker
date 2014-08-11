package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;
import fw.extensions.util.Location;

/**
 * Created by Maxim on 11.08.14.
 */
public class Action extends L2GameSocksClientPacket {
    private int objId;
    private Location loc = new Location();
    @Override
    public void excecute() {

        objId = readD();
        loc.x = readD();
        loc.y = readD();
        loc.z = readD();

        writeC(0x04);
        writeD(objId);
        writeLoc(loc);
        writeC(0);
    }
}
