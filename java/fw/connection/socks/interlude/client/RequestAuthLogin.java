package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Мы on 10.08.14.
 */
public class RequestAuthLogin extends L2GameSocksClientPacket {

    private int[] _keys = new int[4];
    private String login;

    @Override
    public void excecute() {
        login = readS();
        _keys[0] = readD();
        _keys[1] = readD();
        _keys[2] = readD();
        _keys[3] = readD();
        _log.info("Login: "+login);

        writeC(0x08);
        writeS(login);
        writeDD(_keys);
        writeD(1);
    }
}
