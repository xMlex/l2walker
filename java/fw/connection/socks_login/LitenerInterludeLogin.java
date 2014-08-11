package fw.connection.socks_login;

import fw.connection.crypt.GameCryptInterlude;
import fw.connection.crypt.NewCrypt;
import fw.connection.socks.BaseSendableSocketPacket;
import fw.connection.socks.ISocksListener;
import fw.connection.socks_login.clientanswerpackets.BaseLoginClientAnswerPacket;
import fw.connection.socks_login.clientpackets.BaseLoginClientPacket;
import fw.util.Printer;
import jawnae.pyronet.PyroClient;
import xmlex.jsc.PyroProtocolFeeder;
import xmlex.jsc.protocols.ProtocolL2;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by Maxim on 04.08.14.
 */
public class LitenerInterludeLogin extends ISocksListener {
    protected static Logger _log = Logger.getLogger(LitenerInterludeLogin.class.getName());

    protected InterludeLoginPackethandler _pHandler = new InterludeLoginPackethandler();
    public LOGIN_IT_STATE state = LOGIN_IT_STATE.CONNECTED;
    public LoginClientCrypt cryptServer = new LoginClientCrypt();
    public LoginClientCrypt cryptClient = new LoginClientCrypt();

    public int sessionId = 0;
    public int protocolVersion = 0x0000c621;
    public int ggResponce = 0;
    public byte[] publicKey = new byte[128];
    public int[] ggList = new int[4];
    public int[] SessionKey1 = new int[2];
    public int[] SessionKey2 = new int[2];
    private BigInteger rsaKey;

    @Override
    public void onDataWrite(ByteBuffer buf) {
        _log.info("onDataWrite");
    }

    @Override
    public void sendToServer(BaseSendableSocketPacket pkt) {
        if (pkt != null) {
            pkt.setClient(this);
            pkt.run();
            ByteBuffer buf = pkt.getData();
            sendToServerCrypt(buf);
        }
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
    public void receivedData(PyroClient client, ByteBuffer buf) {
        if (client == getClient()) {
            _log.info(" receivedData from client: "+buf.remaining());
        }
        if (client == getServer()) {
            _log.info(" receivedData from server: "+buf.remaining());
        }
    }

    public void sendToServerPkt(BaseLoginClientPacket pkt) {
        if (pkt != null) {
            pkt.setClient(this);
            pkt.run();
            ByteBuffer buf = pkt.getData();
            sendToServerCrypt(buf);
        }
    }

    public void sendToServerCrypt(ByteBuffer buf) {
        try {
            _log.info("\n"+ Printer.printData(buf.array()));
            cryptServer.getCrypt().crypt(buf.array(), 0, buf.array().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendToServer(buf);
    }

    public synchronized void onPacketFromServer(ByteBuffer buf) {
        // _log.info("From Server: "+buf.limit());
        if (_pHandler.handlePacketServer(buf, this))
            sendToClient(buf);
    }

    public synchronized void onPacketFromClient(ByteBuffer buf) {
        //_log.info("From Client: "+buf.limit());
        if (_pHandler.handlePacketClient(buf, this))
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

    public void setBfKey(byte[] key) {
        cryptServer.setKey(key);
        cryptServer._static = false;
        cryptClient._static = false;
        cryptClient.setKey(key);
        _log.info("Set BF Key");
    }

    public void setRSAKey(byte[] key) {
        rsaKey = NewCrypt.descrambleModulus(key);
    }

    public enum LOGIN_IT_STATE {
        CONNECTED, AUTHED_GG, AUTHED_LOGIN
    }
}
