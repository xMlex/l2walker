package fw.connection.socks_login.serverpackets;

import fw.connection.crypt.NewCrypt;

/**
 * Created by Мы on 04.08.14.
 */
public class Init extends BaseLoginServerPacket {
    @Override
    public void read() {

       _log.info(this.getClass().getSimpleName());

        getClient().sessionId = readD();
        getClient().protocolVersion = readD();
        readB(getClient().publicKey);
        getClient().setRSAKey(getClient().publicKey);
        getClient().ggList[0] = readD();
        getClient().ggList[1] = readD();
        getClient().ggList[2] = readD();
        getClient().ggList[3] = readD();
        byte[] bfkey = new byte[16];
        readB(bfkey);
        getClient().setBfKey(bfkey);
        readC();
    }

    @Override
    public boolean excecute() {
        return true;
    }
}
