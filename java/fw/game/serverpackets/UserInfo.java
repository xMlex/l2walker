package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.UserChar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UserInfo extends ServerBasePacket
{
	private GameEngine _clientThread;
	private int _x;
	private int _y;
	private int _z;

	private int _heading;
	private int _objectId;

	private int _race;
	private int _sex;
	private int _class;
	private int _lvl;
	private int _exp;

	private int _str;
	private int _dex;
	private int _con;
	private int _int;
	private int _wit;
	private int _men;

	private int _sp;
	private int _currLoad;
	private int _maxLoad;

	int _res1;
	int _res2;
	int _res3;
	int _res4;
	int _res5;
	int _res6;
	int _res7;
	int _res8;
	int _res9;
	int _res10;
	int _res11;
	int _res12;
	int _res13;

	private int _patck;
	private int _patcks;
	private int _pdef;
	private int _evasion;
	private int _accur;
	private int _crithit;
	private int _matck;
	private int _matcks;
	private int _patcks2;
	private int _mdef;

	private int _karma;

	private int _runspeed;
	private int _wspd;
	private int _srspd;
	private int _swspd;
	private int _flrun;
	private int _flwalk;
	private int _flyrun;
	private int _flywalk;
	private double _movmul;
	private double _atcksmul;

	private double _colRadius;
	private double _colHeight;

	private int _hairStyle;
	private int _hairColor;
	private int _face;
	private int _accessLevel;
	private String _title;

	private int _clanId;
	private int _clanCrestId;
	private int _allyId;
	private int _allyCrestId;
	int _clanLeader;

	int _mountType;
	int _privateSS;
	int _dwarvenCraft;
	int _pkCount;
	int _pvpCount;

	private int[] _cubics;

	int _abnormal;
	int _clanPriv;

	int _recomLeft;
	int _recomRecv;
	int _invLimit;

	int _class2;
	int _enchEffect;

	int _largeCrestId;
	int _hero1;
	int _hero2;
	int _fishMode;
	int _fishX;
	int _fishY;
	int _fishZ;
	int _nameColor;

	private int[] _paperdolls = new int[32];

	private int _currHP;
	private int _maxHP;
	int _currCP;
	int _maxCP;
	private int _currMP;
	private int _maxMP;
	private String _name;
	private int _pvpFlag;

	public UserInfo(GameEngine clientThread,  byte data[])
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type

		_x = readD();		// X
		_y = readD();		// Y
		_z = readD();		// Z
		_heading = readD();	// heading
		_objectId = readD();	// object id
		_name = readS();		// name
		_race = readD();		// race
		_sex = readD();		// sex
		_class = readD();	// class
		_lvl = readD();		// level
		_exp = readD();		// exp

		_str = readD();		// str
		_dex = readD();		// dex
		_con = readD();		// con
		_int = readD();		// int
		_wit = readD();		// wit
		_men = readD();		// men

		_maxHP = readD();	// max HP
		_currHP = readD();	// curr HP
		_maxMP = readD();	// max MP
		_currMP = readD();	// curr MP

		_sp = readD();		// sp
		_currLoad = readD();	// curr load
		_maxLoad = readD();	// max load

		_res1 = readD();	// unknown

		// 32 paperdolls
		for (int i=0; i<32; i++) {
			_paperdolls[i] = readD();
		}

		_patck = readD();	// patck
		_patcks = readD();	// patck speed
		_pdef = readD();		// pdef
		_evasion = readD();	// evasion
		_accur = readD();	// accuracy
		_crithit = readD();	// critical hit
		_matck = readD();	// matck
		_matcks = readD();	// matck speed
		_patcks2 = readD();	// patck speed
		_mdef = readD();		// mdef

		_pvpFlag = readD();	// pvp flag
		_karma = readD();	// karma

		_runspeed = readD();			// run spd
		_wspd = readD();			// walk spd
		_srspd = readD();			// sweem speed
		_swspd = readD();			// sweem speed 2
		_flrun = readD();			// fl run
		_flwalk = readD();			// fl walk
		_flyrun = readD();			// fly run
		_flywalk = readD();			// fly walk
		_movmul = readF();			// mov multiplier
		_atcksmul = readF();			// atk spd multiplier

		_colRadius = readF();			// collision radius
		_colHeight = readF();			// collision heidht

		_hairStyle = readD();			// hair style
		_hairColor = readD();			// hair color
		_face = readD();			// face
		_accessLevel = readD();			// access level
		_title = readS();			// title

		_clanId = readD();			// clan id
		_clanCrestId = readD();			// clan crest id
		_allyId = readD();			// ally id
		_allyCrestId = readD();			// ally crest id
		_clanLeader = readD();			// clanleader
		_mountType = readC();			// mount type
		_privateSS = readC();			// private store sell
		_dwarvenCraft = readC();			// dwarven craft
		_pkCount = readD();			// pk count
		_pvpCount = readD();			// pvp count

		// cubics
		int n = readH();
		_cubics = new int[n];
		for (int i=0;i<n;i++) {
			_cubics[i] = readH();
		}

		_res2 = readC();			// 0x00 smth about party
		_abnormal = readD();			// abnormal effect
		_res3 = readC();			// 0x00
		_clanPriv = readD();			// clan privileges
		_res4 = readD();			// 0x00
		_res5 = readD();			// 0x00
		_res6 = readD();			// 0x00
		_res7 = readD();			// 0x00
		_res8 = readD();			// 0x00
		_res9 = readD();			// 0x00
		_res10 = readD();			// 0x00

		_recomLeft = readH();			// recom left
		_recomRecv = readH();			// recom recv
		_res11 = readD();			// 0x00
		_invLimit = readH();			// inventory limit

		_class2 = readD();			// class id
		_res12 = readD();			// 0x00 (special effects)
		_maxCP = readD();	// max CP
		_currCP = readD();	// curr CP
		_enchEffect = readC();

		_res13 = readC();		// circles (1-blue, 2-red)

		_largeCrestId = readD();
		_hero1 = readC();
		_hero2 = readC();

		_fishMode = readC();
		_fishX = readD();
		_fishY = readD();
		_fishZ = readD();

		_nameColor = readD();
	}

	public void runImpl()
	{
		UserChar userInfo=_clientThread.getUserChar();

		userInfo.isNew = false;
		userInfo.x = _x;
		userInfo.y = _y;
		userInfo.z = _z;
		userInfo.heading = _heading;
		userInfo.objId = _objectId;
		userInfo.realName = _name;
		userInfo.race = _race;
		userInfo.sex = _sex;
		userInfo.classId = _class;
		userInfo.level = _lvl;
		userInfo.xp = _exp;
		userInfo.s_t_r = _str;
		userInfo.d_e_x = _dex;
		userInfo.c_o_n = _con;
		userInfo.i_n_t = _int;
		userInfo.w_i_t = _wit;
		userInfo.m_e_n = _men;
		userInfo.hpMax = _maxHP;
		userInfo.hp = _currHP;
		userInfo.mpMax = _maxMP;
		userInfo.mp = _currMP;
		userInfo.sp = _sp;
		userInfo.load = _currLoad;
		userInfo.loadMax = _maxLoad;
		userInfo.unknow1 = _res1;
		userInfo.under = _paperdolls[0];
		userInfo.rear = _paperdolls[1];
		userInfo.lear = _paperdolls[2];
		userInfo.neck = _paperdolls[3];
		userInfo.rFinger = _paperdolls[4];
		userInfo.lFinger = _paperdolls[5];
		userInfo.head = _paperdolls[6];
		userInfo.rhand = _paperdolls[7];
		userInfo.lhand = _paperdolls[8];
		userInfo.gloves = _paperdolls[9];
		userInfo.chest = _paperdolls[10];
		userInfo.legs = _paperdolls[11];
		userInfo.feet = _paperdolls[12];
		userInfo.back = _paperdolls[13];
		userInfo.lrhand = _paperdolls[14];
		userInfo.hair = _paperdolls[15];
		userInfo.under2 = _paperdolls[16];
		userInfo.rear2 = _paperdolls[17];
		userInfo.lear2 = _paperdolls[18];
		userInfo.neck2 = _paperdolls[19];
		userInfo.rFinger2 = _paperdolls[20];
		userInfo.lFinger2 = _paperdolls[21];
		userInfo.head2 = _paperdolls[22];
		userInfo.rhand2 = _paperdolls[23];
		userInfo.lhand2 = _paperdolls[24];
		userInfo.gloves2 = _paperdolls[25];
		userInfo.chest2 = _paperdolls[26];
		userInfo.legs2 = _paperdolls[27];
		userInfo.feet2 = _paperdolls[28];
		userInfo.back2 = _paperdolls[29];
		userInfo.lrhand2 = _paperdolls[30];
		userInfo.hair2 = _paperdolls[31];
		userInfo.pAtk = _patck;
		userInfo.pAtkSpd = _patcks;
		userInfo.pDef = _pdef;
		userInfo.evasion = _evasion;
		userInfo.accuracy = _accur;
		userInfo.critical = _crithit;
		userInfo.mAtk = _matck;
		userInfo.mAtkSpd = _matcks;
		userInfo.pAtkSpd2 = _patcks2;
		userInfo.mDef = _mdef;
		userInfo.pvpFlag1 = _pvpFlag;
		userInfo.karma1 = _karma;
		userInfo.runSpd = _runspeed;
		userInfo.walkSpd = _wspd;
		userInfo.swimRunSpd = _srspd;
		userInfo.swimWalkSpd = _swspd;
		userInfo.flRunSpd = _flrun;
		userInfo.flWalkSpd = _flwalk;
		userInfo.flyRunSpd = _flyrun;
		userInfo.flyWalkSpd = _flywalk;
		userInfo.movementSpeedMultiplier = _movmul;
		userInfo.attackSpeedMultiplier = _atcksmul;
		userInfo.collisionRadius = _colRadius;
		userInfo.collisionHeight = _colHeight;
		userInfo.hairStyle = _hairStyle;
		userInfo.hairColor = _hairColor;
		userInfo.face = _face;
		userInfo.accessLevel = _accessLevel;
		userInfo.title = _title;
		userInfo.clanId = _clanId;
		userInfo.clanCrestId = _clanCrestId;
		userInfo.allyId = _allyId;
		userInfo.allyCrestId = _allyCrestId;
		//debug
		//_clientThread.getVisualInterface().putMessage(userInfo.realName+" = "+userInfo.objId,GameEngine.MSG_SYSTEM_NORMAL,true);		
	}

}
