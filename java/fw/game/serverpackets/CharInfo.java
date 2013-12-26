package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2ClassList;
import fw.game.PlayerChar;
import fw.game.clientpackets.RequestPledgeInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CharInfo extends ServerBasePacket
{
	private GameEngine _clientThread;

	public int _x;
	public int _y;
	public int _z;
	private int _heading;
	public int _objectId;
	public String _name;
	private int _race;
	private int _sex;
	private int _classId;
	private int _res0;
	private int _pd0;
	private int _pd1;
	private int _pd2;
	private int _pd3;
	private int _pd4;
	private int _pd5;
	private int _pd6;
	private int _pd7;
	private int _pd8;
	private int _pd9;
	private int _karma;
	private int _pvp;
	private int _matkspd;
	private int _patkspd;
	private int _runSpd;
	private int _walkSpd;
	private int _swimRunSpd;
	private int _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private double _movSpdMul;
	private double _atkSpdMul;
	private double _colRadius;
	private double _colHeight;
	private int _hairStyle;
	private int _hairColor;
	private int _face;
	private String _title;
	public int _clanId;
	private int _clanCrestId;
	private int _allyId;
	private int _allyCrestId;
	private int _siege;
	private byte _sitting;
	private byte _running;
	private byte _inCombat;
	private byte _alikeDead;
	private byte _invisible;
	private byte _mountType;
	private byte _privateStoreType;
	private int _cubicsSize;
	private int[] _cubics;
	private byte _findPartyMembers;
	public int _abnormalEffect;
	private byte _res3;
	private short _recomHave;
	private int _res4;
	private int _res5;
	private int _res6;
	private byte _enchantEffect;
	private byte _res7;
	private int _clanCrestLargeId;
	private byte _hero1;
	private byte _hero2;
	private byte _fishing;
	private int _fishX;
	private int _fishY;
	@SuppressWarnings("unused")
	private int _fishZ;
	public int _nameColor;
	private int _pvp1;
	private int _karma1;
	@SuppressWarnings("unused")
	private byte[] _orgMessage;

	public CharInfo(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();	// msg len
		readC();	// msg type

		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
		_objectId = readD();
		_name = readS();
		_race = readD();
		_sex = readD();
		_classId = readD();
		_res0 = readD();
		_pd0 = readD();
		_pd1 = readD();
		_pd2 = readD();
		_pd3 = readD();
		_pd4 = readD();
		_pd5 = readD();
		_pd6 = readD();
		_pd7 = readD();
		_pd8 = readD();
		_pd9 = readD();
		_pvp = readD();
		_karma = readD();
		_matkspd = readD();
		_patkspd = readD();
		_pvp1 = readD();
		_karma1 = readD();
		_runSpd = readD();
		_walkSpd = readD();
		_swimRunSpd = readD();
		_swimWalkSpd = readD();
		_flRunSpd = readD();
		_flWalkSpd = readD();
		_flyRunSpd = readD();
		_flyWalkSpd = readD();
		_movSpdMul = readF();
		_atkSpdMul = readF();
		_colRadius = readF();
		_colHeight = readF();
		_hairStyle = readD();
		_hairColor = readD();
		_face = readD();
		_title = readS();
		_clanId = readD();
		_clanCrestId = readD();
		_allyId = readD();
		_allyCrestId = readD();
		_siege = readD();
		_sitting = (byte)readC();
		_running = (byte)readC();
		_inCombat = (byte)readC();
		_alikeDead = (byte)readC();
		_invisible = (byte)readC();
		_mountType = (byte)readC();
		_privateStoreType = (byte)readC();

		_cubicsSize = readH();
		_cubics = new int[_cubicsSize];
		for(int i=0;i<_cubicsSize;i++) {
			_cubics[i] = readH();
		}

		_findPartyMembers = (byte)readC();
		_abnormalEffect = readD();
		_res3 = (byte)readC();
		_recomHave = (short)readH();
		_res4 = readD();
		_res5 = readD();
		_res6 = readD();
		_enchantEffect = (byte)readC();
		_res7 = (byte)readC();
		_clanCrestLargeId = readD();
		_hero1 = (byte)readC();
		_hero2 = (byte)readC();
		_fishing = (byte)readC();
		_fishX = readD();
		_fishY = readD();
		_fishZ = readD();
		_nameColor = readD();
	}

	public void runImpl()
	{
		PlayerChar playerChar = null;

		playerChar = _clientThread.getMaps().getOrCreatePlayerChar(_objectId);

		playerChar.x = _x;
		playerChar.y = _y;
		playerChar.z = _z;
		playerChar.heading = _heading;
		// -//

		playerChar.realName = _name;
		playerChar.race = _race;
		playerChar.sex = _sex;
		playerChar.classId = _classId;
		playerChar.unkNow1 = _res0;
		playerChar.head = _pd0;
		playerChar.rhand =_pd1;
		playerChar.lhand = _pd2;
		playerChar.gloves = _pd3;
		playerChar.chest = _pd4;
		playerChar.legs = _pd5;
		playerChar.feet = _pd6;
		playerChar.back = _pd7;
		playerChar.lrhand = _pd8;
		playerChar.hair = _pd9;
		playerChar.pvpFlag1 = _pvp;
		playerChar.karma1 = _karma;
		playerChar.mAtkSpd = _matkspd;
		playerChar.pAtkSpd = _patkspd;
		playerChar.pvpFlag2 = _pvp1;
		playerChar.karma2 = _karma1;
		playerChar.runSpd = _runSpd;
		playerChar.walkSpd = _walkSpd;
		playerChar.swimRunSpd = _swimRunSpd;
		playerChar.swimWalkSpd = _swimWalkSpd;
		playerChar.flRunSpd = _flRunSpd;
		playerChar.flWalkSpd = _flWalkSpd;
		playerChar.flyRunSpd = _flyRunSpd;
		playerChar.flyWalkSpd = _flyWalkSpd;

		playerChar.movementSpeedMultiplier = _movSpdMul;
		playerChar.attackSpeedMultiplier = _atkSpdMul;
		playerChar.collisionRadius = _colRadius;
		playerChar.collisionHeight = _colHeight;
		playerChar.hairStyle = _hairStyle;
		playerChar.hairColor = _hairColor;
		playerChar.face = _face;
		playerChar.title = _title;
		playerChar.clanId = _clanId;
		playerChar.clanCrestId = _clanCrestId;
		playerChar.allyId = _allyId;
		playerChar.allyCrestId = _allyCrestId;
		playerChar.siegFlags = _siege;
		playerChar.isSitting = _sitting;
		playerChar.isRunning = _running;
		playerChar.isInCombat = _inCombat;
		playerChar.isAlikeDead = _alikeDead;
		playerChar.getInvisible = _invisible;
		playerChar.getMountType = _mountType;
		playerChar.getPrivateStoreType = _privateStoreType;

		if (_cubicsSize > 0)
		{
			playerChar.cubicsSet = new short[_cubicsSize];
			for (int i = 0; i < _cubicsSize; i++)
			{
				playerChar.cubicsSet[i] = (short)_cubics[i];
			}
		}

		playerChar.findPartyMembers = _findPartyMembers;
		playerChar.abnormalEffect = _abnormalEffect;
		playerChar.unknow1 = _res3;
		playerChar.recomHave = _recomHave;
		playerChar.unknow2 = _res4;
		playerChar.unknow3 = _res5;
		playerChar.unknow4 = _res6;
		playerChar.isMounted = _enchantEffect;
		playerChar.unknow5 = _res7;
		playerChar.clanCrestLargeId = _clanCrestLargeId;
		playerChar.haveSymbol = _hero1;
		playerChar.haveAura = _hero2;
		playerChar.fishingMode = _fishing;
		playerChar.unknow6 = _fishX;
		playerChar.unknow7 = _fishY;
		playerChar.unknow8 = _fishY;
		playerChar.nameColor = _nameColor;

		String charClass[] = L2ClassList.getL2ClassName(playerChar.classId).split("_");
		playerChar.name = playerChar.realName;
		playerChar.classRace = charClass[0];
		playerChar.className = charClass[1];

		processCharInfo(_clientThread, playerChar);
	}

	private static void processCharInfo(final GameEngine gameEngine, final PlayerChar playerChar)
	{
		// Update movement of char
		playerChar.updateMovementValues();

		if (playerChar.clanInfo == null)
			playerChar.clanInfo = gameEngine.getMaps().getOrCreateClanInfo(playerChar.clanId);

		if (!playerChar.clanInfo.initialized)
		{
			playerChar.clanInfo.listWaitForClanInfo.add(playerChar);
			RequestPledgeInfo myRequestPledgeInfo = new RequestPledgeInfo(gameEngine,playerChar.clanId);
			myRequestPledgeInfo.runImpl();
		}

		gameEngine.getVisualInterface().procPlayerChar(playerChar);
	}

}
