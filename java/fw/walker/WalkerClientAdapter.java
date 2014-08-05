package fw.walker;

import fw.connection.socks_login.serverpackets.Init;
import fw.extensions.util.Util;
import fw.walker.net.*;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.events.PyroClientListener;
import xmlex.ext.crypt.NewCrypt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by Maxim on 05.08.14.
 */
public class WalkerClientAdapter implements PyroClientListener {
    protected static Logger _log = Logger.getLogger(WalkerClientAdapter.class.getName());
    @Override
    public void connectedClient(PyroClient pyroClient) {
        _log.info("connectedClient");
        WalkerWPacket pkt = new TestPacket();
        pkt.run();
        NewCrypt _c = new NewCrypt(true);
        byte[] _d = pkt.getData().array();
        try {
            _c.crypt(_d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pyroClient.writeCopy(ByteBuffer.wrap(Util.writeLenght(_d)));
    }

    @Override
    public void unconnectableClient(PyroClient pyroClient) {

    }

    @Override
    public void droppedClient(PyroClient pyroClient, IOException e) {

    }

    @Override
    public void disconnectedClient(PyroClient pyroClient) {
        _log.info("disconnectedClient");
    }

    @Override
    public void receivedData(PyroClient pyroClient, ByteBuffer byteBuffer) {
        _log.info("receivedData: "+byteBuffer.limit());
    }

    @Override
    public void sentData(PyroClient pyroClient, int i) {
        _log.info("sentData: "+i);
    }
}
