package fw.connection.socks_login;

import fw.connection.socks_login.clientpackets.*;
import fw.connection.socks_login.serverpackets.*;
import fw.connection.socks.IPacketHandler;
import fw.connection.socks.ListenerIntelude;
import fw.connection.socks_login.serverpackets.*;
import fw.util.Printer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by Maxim on 04.08.14.
 */
public class InterludeLoginPackethandler implements IPacketHandler<LitenerInterludeLogin> {

    private static final Logger _log = Logger.getLogger(InterludeLoginPackethandler.class.getName());

    @Override
    public boolean handlePacketClient(ByteBuffer buf, LitenerInterludeLogin client) {
        _log.info("From Client: "+buf.limit());
        ByteBuffer _buf = client.copy(buf);
        try {
            client.cryptClient._static = false;
            client.cryptClient.decrypt(_buf.array(),0,_buf.array().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseLoginClientPacket pkt = null;
        int id = (byte) (_buf.get() & 0xFF);
        switch (id){
            case 0x07:
                _log.info("AuthGameGuard");
                _log.info("\n"+Printer.printData(_buf.array()));
                pkt = new AuthGameGuard();
                break;
            case 0x00:
                _log.info("RequestAuthLogin: "+_buf.array().length);
                _log.info("\n"+Printer.printData(_buf.array()));
                pkt = new RequestAuthLogin();
                break;
            case 0x05://RequestServerList
                _log.info("RequestServerList");
                _log.info("\n"+Printer.printData(_buf.array()));
                pkt = new RequestServerList();
                break;
            default:
                _log.info("Uncknow packet: 0x"+Printer.fillHex(id,2));
                break;
        }
        if(pkt != null){
            pkt.setBufRead(_buf);
            client.sendToServerPkt(pkt);
        }
        return false;
    }

    @Override
    public boolean handlePacketServer(ByteBuffer buf, LitenerInterludeLogin client) {
        ByteBuffer _buf = client.copy(buf);

        try {
            client.cryptServer.decrypt(_buf.array(),0,_buf.array().length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BaseLoginServerPacket pkt = null;
        int id = (byte) (_buf.get() & 0xFF);
        switch (client.state){
            case CONNECTED:
                switch (id){
                    case 0x00:
                        _log.info("init packet");
                        pkt = new Init();
                        break;
                    case 0x0B:
                        _log.info("GGAuth");
                        pkt = new GGAuth();
                        break;
                    case 0x01:
                        _log.info("LoginFail");
                        break;
                    default:
                        _log.warning("UNKNOW packet: 0x" + Printer.fillHex(id,4));
                        break;
                }
                break;
            case AUTHED_GG:
                switch (id){
                    case 0x03://LoginOk
                        _log.info("LoginOk");
                        pkt = new LoginOk();
                        break;
                    case 0x04://ServerList
                        _log.info("ServerList");
                        //pkt = new LoginOk();
                        break;
                    default:
                        _log.info("AUTHED_GG: packet: 0x"+ Printer.fillHex(id,4));
                        break;
                }
                break;
        }
        _log.info("From Server: "+buf.limit());
        if(pkt != null){
            pkt.setByteBuffer(_buf);
            pkt.setClient(client);
            pkt.read();
            return pkt.excecute();
        }
        return true;
    }

}
