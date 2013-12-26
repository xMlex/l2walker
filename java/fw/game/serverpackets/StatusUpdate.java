package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2Char;
import fw.game.L2PartyChar;
import fw.game.PlayerChar;
import fw.game.UserChar;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class StatusUpdate extends ServerBasePacket
{
	public static int LEVEL = 0x01;
	public static int EXP = 0x02;
	public static int STR = 0x03;
	public static int DEX = 0x04;
	public static int CON = 0x05;
	public static int INT = 0x06;
	public static int WIT = 0x07;
	public static int MEN = 0x08;
	public static int CUR_HP = 0x09;
	public static int MAX_HP = 0x0a;
	public static int CUR_MP = 0x0b;
	public static int MAX_MP = 0x0c;
	public static int SP = 0x0d;
	public static int CUR_LOAD = 0x0e;
	public static int MAX_LOAD = 0x0f;
	public static int P_ATK = 0x11;
	public static int ATK_SPD = 0x12;
	public static int P_DEF = 0x13;
	public static int EVASION = 0x14;
	public static int ACCURACY = 0x15;
	public static int CRITICAL = 0x16;
	public static int M_ATK = 0x17;
	public static int CAST_SPD = 0x18;
	public static int M_DEF = 0x19;
	public static int PVP_FLAG = 0x1a;
	public static int KARMA = 0x1b;
	public static int CUR_CP = 0x21;
	public static int MAX_CP = 0x22;

	private GameEngine _clientThread;
	private int _objectId;
	private List<Attribute> attributes;

	class Attribute {
		public int id;
		public int value;
	};

	public StatusUpdate(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();	// msg len
		readC();	// msg type

		_objectId = readD();
		int n = readD();
		attributes = new ArrayList<Attribute>(n);
		for(int i=0;i<n;i++) {
			Attribute attr = new Attribute();
			attr.id = readD();
			attr.value = readD();
			attributes.add(attr);
		}
	}

	public void runImpl()
	{
		L2Char l2Char = null;
		L2PartyChar l2PartyChar = null;

		if (_clientThread.getUserChar().objId == _objectId)
		{
			l2Char = _clientThread.getUserChar();
		} else
		{
			l2Char = _clientThread.getMaps().getChar(_objectId);
		}

		l2PartyChar = _clientThread.getMaps().getPartyChar(_objectId);

		if (l2Char == null && l2PartyChar == null)
		{
			System.out.println("StatusUpdatePack.runImplementatiton() objId=" + _objectId + " not found.");
			return;
		}



		if (l2Char != null)
		{
			for(int i=0;i<attributes.size();i++) {
				Attribute attr = attributes.get(i);
				if (attr.id == CUR_HP) {
					l2Char.hp = attr.value;
				}
				if (attr.id == MAX_HP) {
					l2Char.hpMax = attr.value;
				}
				if (attr.id == CUR_CP) {
					l2Char.cp = attr.value;
				}
				if (attr.id == MAX_CP) {
					l2Char.cpMax = attr.value;
				}
				if (attr.id == CUR_MP) {
					l2Char.mp = attr.value;
				}
				if (attr.id == MAX_MP) {
					l2Char.mpMax = attr.value;
				}
			}

			if (l2Char instanceof PlayerChar)
			{
				PlayerChar playerChar = (PlayerChar) l2Char;
				for(int i=0;i<attributes.size();i++) {
					Attribute attr = attributes.get(i);
					if (attr.id == PVP_FLAG) {
						playerChar.pvpFlag1 = attr.value;
					}
					if (attr.id == KARMA) {
						playerChar.karma1 = attr.value;
					}
				}

			} else if (l2Char instanceof UserChar)
			{
				UserChar userChar = (UserChar) l2Char;
				for(int i=0;i<attributes.size();i++) {
					Attribute attr = attributes.get(i);
					if (attr.id == PVP_FLAG) {
						userChar.pvpFlag1 = attr.value;
					}
					if (attr.id == KARMA) {
						userChar.karma1 = attr.value;
					}
					if (attr.id == LEVEL) {
						userChar.level = attr.value;
					}
					if (attr.id == EXP) {
						userChar.xp = attr.value;
					}
				}

				_clientThread.getVisualInterface().procUpdateUsercharStatus(userChar);
			}

			L2Char targetChar = _clientThread.getCurrentTarget();
			if (targetChar != null && targetChar.objId == _objectId)
			{
				_clientThread.getVisualInterface().procUpdateTargetcharStatus(targetChar);
			}

		}

		if(l2PartyChar!=null)
		{
			for(int i=0;i<attributes.size();i++) {
				Attribute attr = attributes.get(i);
				if (attr.id == CUR_HP) {
					l2PartyChar.hp = attr.value;
				}
				if (attr.id == MAX_HP) {
					l2PartyChar.hpMax = attr.value;
				}
				if (attr.id == CUR_CP) {
					l2PartyChar.cp = attr.value;
				}
				if (attr.id == MAX_CP) {
					l2PartyChar.cpMax = attr.value;
				}
				if (attr.id == CUR_MP) {
					l2PartyChar.mp = attr.value;
				}
				if (attr.id == MAX_MP) {
					l2PartyChar.mpMax = attr.value;
				}
			}
			_clientThread.updatePartyChar(l2PartyChar);
		}

	}
}
