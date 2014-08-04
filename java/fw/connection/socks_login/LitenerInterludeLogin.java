package fw.connection.socks_login;

import fw.connection.crypt.GameCryptInterlude;
import fw.connection.socks.ISocksListener;
import jawnae.pyronet.PyroClient;
import xmlex.jsc.PyroProtocolFeeder;
import xmlex.jsc.protocols.ProtocolL2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by Maxim on 04.08.14.
 */
public class LitenerInterludeLogin extends ISocksListener {
    protected static Logger _log = Logger.getLogger(LitenerInterludeLogin.class.getName());

    protected InterludeLoginPackethandler _pHandler = new InterludeLoginPackethandler();
    public LOGIN_IT_STATE state = LOGIN_IT_STATE.CONNECTED;
    public LoginClientCrypt crypt = new LoginClientCrypt();

    @Override
    public void onDataWrite(ByteBuffer buf) {
        _log.info("onDataWrite");
    }

    @Override
    public void setGameCrypt(byte[] _key) {
        _log.info("setGameCrypt");
    }

    @Override
    public void connectedClient(PyroClient pyroClient) {
        _log.info("connectedClient");
    }

    @Override
    public void unconnectableClient(PyroClient pyroClient) {
        _log.info("unconnectableClient");
    }

    @Override
    public void droppedClient(PyroClient pyroClient, IOException e) {
        _log.info("droppedClient");
    }

    @Override
    public void disconnectedClient(PyroClient pyroClient) {
        _log.info("disconnectedClient");
    }

    @Override
    public void receivedData(PyroClient client, ByteBuffer byteBuffer) {
        _log.info("receivedData");
    }
    public synchronized void onPacketFromServer(ByteBuffer buf) {
       // _log.info("From Server: "+buf.limit());
        if(_pHandler.handlePacketServer(buf, this))
            sendToClient(buf);
    }
    public synchronized void onPacketFromClient(ByteBuffer buf) {
        //_log.info("From Client: "+buf.limit());
        if(_pHandler.handlePacketClient(buf, this))
            sendToServer(buf);
    }

    @Override
    public void setClient(PyroClient client) {
        super.setClient(client);
        client.removeListeners();

        PyroProtocolFeeder feeder = new PyroProtocolFeeder(client);
        ProtocolL2 handler = new ProtocolL2() {
            public void onReady(ByteBuffer buf) {
                onPacketFromClient(buf);
            }
        };
        feeder.addByteSink(handler);
        client.addListener(feeder);
    }

    @Override
    public void setServer(PyroClient server) {
        super.setServer(server);
        server.removeListeners();
            PyroProtocolFeeder feeder = new PyroProtocolFeeder(server);
            ProtocolL2 handler = new ProtocolL2() {
                public void onReady(ByteBuffer buf) {
                    onPacketFromServer(buf);
                }
            };
            feeder.addByteSink(handler);
            server.addListener(feeder);
        server.removeListener(this);
            //_log.info("Set Server: "+server);
    }
    @Override
    public void sentData(PyroClient pyroClient, int i) {
        _log.info("sentData");
    }

    public enum LOGIN_IT_STATE {
        CONNECTED,AUTHED
    }
}
