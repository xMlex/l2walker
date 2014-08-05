package fw.walker.net;

import fw.extensions.util.Rnd;

/**
 * Created by Maxim on 05.08.14.
 */
public class TestPacket extends WalkerWPacket {
    @Override
    public void excecute()
    {
        writeC(0x00);
        writeD(Rnd.nextInt());
        writeD(0x0000c621);
        byte[] _rsa = new byte[128];
        writeB(_rsa);

        writeD(Rnd.nextInt());
        writeD(Rnd.nextInt());
        writeD(Rnd.nextInt());
        writeD(Rnd.nextInt());

        byte[] bfkey = new byte[16];
        writeB(bfkey);
        writeC(0x00);
    }
}
