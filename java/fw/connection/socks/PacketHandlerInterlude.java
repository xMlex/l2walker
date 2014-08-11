package fw.connection.socks;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import com.sun.org.apache.regexp.internal.RE;
import fw.connection.game.CLIENT_STATE;
import fw.connection.socks.interlude.L2GameSocksClientPacket;
import fw.connection.socks.interlude.L2GameSocksServerPacket;
import fw.connection.socks.interlude.client.*;
import fw.connection.socks.interlude.server.*;

public class PacketHandlerInterlude implements IPacketHandler<ListenerIntelude> {

    private static final Logger _log = Logger
            .getLogger(PacketHandlerInterlude.class.getName());

    public boolean handlePacketClient(ByteBuffer bufx, ListenerIntelude client) {
        ByteBuffer _buf = client.copy(bufx);
        // client._cryptClient.decrypt(_buf.array(), 0, _buf.array().length);

        int id = _buf.get() & 0xFF;

        L2GameSocksClientPacket msg = null;
        boolean sendPacket = true;
        //_log.info("[C] -- 0x" + Integer.toHexString(id) + " State: " + client.getState() + " int: " + id);
        switch (client.getState()) {
            case CONNECTED:
            case AUTHED:
                switch (id) {
                    case 0x00:
                        msg = new ProtocolVersion();
                        break;
                    case 0x0D:
                        _log.info("Selected char");
                        msg = new CharSelected();
                        break;
                    case 0xD0:
                        _log.info("RequestManorList ORIG");
                        //msg = new RequestManorList();
                        sendPacket = false;
                        break;
                    case 0x03://EnterWorld
                        _log.info("RequestManorList");
                        /*client.sendToServer(new RequestManorList());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {}
                        _log.info("EnterWorld");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {}
                        msg = new EnterWorld();
                        //client.sendToServer(new EnterWorld());
                        client.setState(CLIENT_STATE.IN_GAME);
                        */
                        sendPacket = false;
                        break;
                    case 0x08:
                        _log.info("RequestAuthLogin");
                        msg = new RequestAuthLogin();
                        break;
                    default:
                        _log.info("[C][AUTHED] unk packet: 0x" + Integer.toHexString(id));
                        sendPacket=false;
                        break;
                }
                break;
            case IN_GAME:
                switch (id) {
                    case 0x01:
                        msg = new MoveBackwardToLocation();
                        break;
                    case 0x81:
                        msg = new RequestGMList();
                        break;
                    case 0xFE:
                        int subid = _buf.getShort() & 0xFFFF;
                        switch (subid) {
                            case 0x13:
                                // msg = new ExFishingStart();
                                break;
                            default:
                                // System.out.println("Uncknow subpacket(IN_GAME[0xFE]): 0x"+
                                // Integer.toHexString(subid));
                                break;
                        }
                        break;
                    case 0x21://RequestBypassToServer
                        msg = new RequestBypassToServer();
                        break;
                    case 0x03:
                        sendPacket = false;
                        break;
                    default:
                        //_log.info("[C]Uncknow packet(IN_GAME): 0x" + Integer.toHexString(id));
                        break;
                }
                break;
        }
        if (msg != null) {
            _log.info("[C] " + msg.getClass().getSimpleName());
            //_log.info(Util.printData(_buf.array()));
            //_log.info("Pos read: "+_buf.position()+" x: "+_buf.getInt());
            msg.setClient(client);
            msg.setBufRead(_buf);
            msg.run();
            client.addToServer(msg);
            client.run();
            return false;
        } else
            return sendPacket;
    }

    public boolean handlePacketServer(ByteBuffer bufx, ListenerIntelude client) {
        ByteBuffer _buf = client.copy(bufx);

        // client._cryptServer.decrypt(_buf.array(), 0, _buf.array().length);
        int id = _buf.get() & 0xFF;
        // int id2 = _buf.get() & 0xFF;
        // _log.info("Pos: "+_buf.position()+" limit: "+_buf.limit());
        // _log.info(Printer.printData(_buf.array()));
        L2GameSocksServerPacket msg = null;
        switch (client.getState()) {
            case CONNECTED:
                switch (id) {
                    case 0x00:
                        // System.out.println("[CONNECTED] KeyPacket");
                        msg = new KeyPacket();
                        break;
                    case 0x13:
                        // msg = new CharSelectInfo();
                        System.out.println("[S] CharSelectInfo");
                        client.setState(CLIENT_STATE.AUTHED);
                        break;
                    default:
                        //System.out.println("[CONNECTED]S: 0x"+
                        //Integer.toHexString(id));
                        break;
                }
                break;
            case AUTHED:
                switch (id) {
                    case 0x15://CharSelected
                        client.sendToServer(new RequestManorList());
                        break;
                    case 0xFE: {
                        int subid = _buf.get() & 0xFF;
                        switch (subid) {
                            case 0x1B:
                                // msg = new ExSendManorList();
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {}
                                client.sendToServer(new EnterWorld());
                                client.setState(CLIENT_STATE.IN_GAME);
                                break;
                            default:
                                //_log.info("[AUTHED] uncknow sub packet(0xFE): 0x"+
                                // Integer.toHexString(subid));
                                break;
                        }
                    }
                }
                break;
            case IN_GAME:
                switch (id) {
                    case 0x0B:
                        msg = new SpawnItem();
                        break;
                    case 0x0C:
                        msg = new DropItem();
                        break;
                    case 0x04:
                        msg = new UserInfo();
                        break;
                    case 0x0E:
                        msg = new StatusUpdate();
                        break;
                    case 0xFE:
                        int subid = _buf.getShort() & 0xFFFF;
                        switch (subid) {
                            case 0x16:
                                // msg = new ExFishingHpRegen();
                                break;
                            default:
                                // System.out.println("Uncknow subpacket(IN_GAME[0xFE]): 0x"+
                                // Integer.toHexString(subid));
                                break;
                        }
                        break;
                    case 0x0F: //NpcHtmlMessage
                        msg = new NpcHtmlMessage();
                        break;
                    default:
                        // System.out.println("Uncknow packet(IN_GAME): 0x"+
                        // Integer.toHexString(id));
                        break;
                }
                break;
        }

        //_log.info("["+client.getState()+"][R]: 0x"+ Integer.toHexString(id));
        if (msg != null) {
            //_log.info("[S] " + msg.getClass().getSimpleName());
            // System.out.println("[R] "+msg.getClass().getSimpleName());
            msg.setClient(client);
            msg.setByteBuffer(_buf);
            msg.read();
            return msg.run();
        }
        return true;
    }

}
