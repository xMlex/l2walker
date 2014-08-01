package fw.connection.socks;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

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
        switch (client.getState()) {
            case CONNECTED:
                switch (id) {
                    case 0x00:
                        // msg = new KeyPacket();
                        break;
                    case 0x13:
                        // msg = new CharSelectInfo();
                        break;
                    default:
                        System.out.println("[CONNECTED]Read uncknow packet: 0x"
                                + Integer.toHexString(id));
                        break;
                }
                break;
            case AUTHED:
                switch (id) {
                    case 0x15:
                        // msg = new CharSelected();
                        break;
                    case 0xFE: {
                        int subid = _buf.get() & 0xFF;
                        switch (subid) {
                            case 0x1B:
                                // msg = new ExSendManorList();
                                break;
                            default:
                                // _log.info("[AUTHED] uncknow sub packet(0xFE): 0x"+
                                // Integer.toHexString(subid));
                                break;
                        }
                    }
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
                    default:
                        // System.out.println("Uncknow packet(IN_GAME): 0x"+
                        // Integer.toHexString(id));
                        break;
                }
                break;
        }
        if (msg != null) {
            //_log.info(Util.printData(_buf.array()));
            //_log.info("Pos read: "+_buf.position()+" x: "+_buf.getInt());
            msg.setClient(client);
            msg.setBufRead(_buf);
            msg.run();
            client.addToServer(msg);
            client.run();
            return false;
        } else
            return true;
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
                        System.out.println("[R] CharSelectInfo");
                        client.setState(CLIENT_STATE.AUTHED);
                        break;
                    default:
                        // System.out.println("[CONNECTED]R: 0x"+
                        // Integer.toHexString(id));
                        break;
                }
                break;
            case AUTHED:
                switch (id) {
                    case 0x15:
                        // msg = new CharSelected();
                        client.setState(CLIENT_STATE.IN_GAME);
                        break;
                    case 0xFE: {
                        int subid = _buf.get() & 0xFF;
                        switch (subid) {
                            case 0x1B:
                                // msg = new ExSendManorList();

                                break;
                            default:
                                // _log.info("[AUTHED] uncknow sub packet(0xFE): 0x"+
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
            // System.out.println("[R] "+msg.getClass().getSimpleName());
            msg.setClient(client);
            msg.setByteBuffer(_buf);
            msg.read();
            return msg.run();
        }
        return true;
    }

}
