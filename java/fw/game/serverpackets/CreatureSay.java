package fw.game.serverpackets;

import fw.game.GameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CreatureSay extends ServerBasePacket
{
	private GameEngine _clientThread;
	@SuppressWarnings("unused")
	private int _objectId;
	private int _textType;
	private String _charName;
	private String _text;

	public CreatureSay(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH(); // msg length
		readC(); // msg type
		_objectId = readD();
		_textType = readD();
		_charName = readS();
		_charName += ":";
		_text = readS();
	}

	public void runImpl()
	{

		int  msg_type = GameEngine.MSG_CHAT_NORMAL;

		switch (_textType)
		{
		case 0:
			msg_type =  GameEngine.MSG_CHAT_NORMAL;
			break;
		case 1:
			msg_type =  GameEngine.MSG_CHAT_ALL;
			break;
		case 2:
			msg_type =  GameEngine.MSG_TELL;
			break;
		case 4:
			msg_type =  GameEngine.MSG_CLAN;
			break;
		case 8:
			msg_type =  GameEngine.MSG_TRADE;
			break;
		case 9:
			msg_type =  GameEngine.MSG_ALLY;
			break;
		case 10:
			_charName = "Announcement:";
			msg_type =  GameEngine.MSG_ANUNCEMENT;
			break;
		case 17:
			msg_type =  GameEngine.MSG_HERO;
		default:
			break;
		}

		_clientThread.getVisualInterface().putMessage(_charName + _text, msg_type, false);
	}

}
