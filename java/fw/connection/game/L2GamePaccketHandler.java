package fw.connection.game;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import xmlex.jsc.BaseReceivablePacket;
import fw.connection.GameConnection;
import fw.connection.IPacketHandler;
import fw.connection.game.serverpackets.*;

public class L2GamePaccketHandler implements IPacketHandler<GameConnection> {

	private static final Logger _log = Logger.getLogger(L2GamePaccketHandler.class.getName());

	public BaseReceivablePacket<GameConnection> handlePacket(ByteBuffer buf,
			GameConnection client) {

		BaseReceivablePacket<GameConnection> msg = null;
		int id = buf.get() & 0xFF;

		switch (client.getState()) {
		case CONNECTED:
			switch (id) {
			case 0x00:
				msg = new KeyPacket();
				break;
			case 0x13:
				msg = new CharSelectInfo();
				break;
			default:
				System.out.println("Read uncknow packet: 0x"
						+ Integer.toHexString(id) + "State CONNECTED");
				break;
			}
			break;
		case AUTHED:
			switch (id) {
			case 0x15:
				msg = new CharSelected();
				break;
			case 0xFE: {
				int subid = buf.get() & 0xFF;
				switch (subid) {
				case 0x1B:
					msg = new ExSendManorList();
					break;
				default:
					_log.info("[R] uncknow sub packet(0xFE): 0x"
							+ Integer.toHexString(subid));
					break;
				}
			}
			}
		case IN_GAME:
			switch (id) {
			
			case 0x01:
				msg = new MoveToLocation();
				break;
			case 0x03:
				msg = new CharInfo();
				break;
			case 0x04:
				msg = new UserInfo();
				break;
			case 0x06:
				msg = new Die();
				break;
			case 0x12:
				msg = new DeleteObject();
				break;
			case 0x16:
				msg = new NpcInfo();
				break;
			case 0x1B:
				msg = new ItemList();
				break;
			case 0x0E:
				msg = new StatusUpdate();
				break;
			case 0x0C:
				msg = new DropItem();
				break;
			case 0x2A:
				msg = new TargetUnselected();
				break;
			case 0x29:
				msg = new TargetSelected();
				break;
			case 0x2F:
				msg = new ChangeWaitType();
				break;
			case 0x58:
				msg = new SkillList();
				break;
			case 0x39:
				msg = new AskJoinParty();
				break;
				
			default:
				//System.out.println("Uncknow packet(IN_GAME): 0x"+ Integer.toHexString(id));
				break;
			}
			break;
		}

		//if (msg != null)
		//	_log.info("[R] " + msg.getClass().getSimpleName());
		return msg;
	}

}
