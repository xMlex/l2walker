package fw.connection.socks_login;

import fw.connection.socks.IPacketHandler;
import fw.connection.socks.ListenerIntelude;
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
        return false;
    }

    @Override
    public boolean handlePacketServer(ByteBuffer buf, LitenerInterludeLogin client) {
        ByteBuffer _buf = client.copy(buf);

        switch (client.state){
            case CONNECTED:
                try {
                    if(!client.crypt.decrypt(_buf.array(),0,_buf.array().length))
                        _log.info("Wrong checksumm: "+_buf.array().length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int id = (byte) (_buf.get() & 0xFF);
                switch (id){
                    case 0x0B:
                        _log.info("init packet");
                        break;
                    default:
                        _log.warning("UNKNOW packet: 0x" + Printer.fillHex(id,4));
                        break;
                }
                break;
        }

        _log.info("From Server: "+buf.limit());
        return false;
    }

}
