package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.NpcChar;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NpcInfo extends ServerBasePacket
{
	private GameEngine _clientThread;

	public int _objectId;
	public int _x, _y, _z, _heading;
	private int _idTemplate;
	public int _isAttackable, _isSummoned;
	private int _mAtkSpd, _pAtkSpd, _pAtkSpd2;
	private int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd, _flRunSpd, _flWalkSpd, _flyRunSpd, _flyWalkSpd;
	private int _rhand, _lhand;
    private double _collisionHeight, _collisionRadius,_attackSpeedMultiplier;
    public String _name = "";
    private String _title = "";
    @SuppressWarnings("unused")
	private int  _abnormalEffect;
    private byte _isRunning, _isInCombat, _isAlikeDead;
	@SuppressWarnings("unused")
	private int _tmp1, _tmp3, _tmp4, _tmp5, _tmp6, _tmp7, _tmp8, _tmp9, _tmp10, _tmp11,
				_tmp12, _tmp13, _tmp16;
	@SuppressWarnings("unused")
	private double _tmp14, _tmp15;

	public NpcInfo(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();
		readC();

		_objectId = readD();
		_idTemplate = readD();  // npctype id
		_isAttackable = readD();
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
		_tmp1 = readD();
		_mAtkSpd = readD();
		_pAtkSpd = readD();
		_runSpd = readD();
		_walkSpd = readD();
		_swimRunSpd = readD();  // swimspeed
		_swimWalkSpd = readD();  // swimspeed
		_flRunSpd = readD();
		_flWalkSpd = readD();
		_flyRunSpd = readD();
		_flyWalkSpd = readD();
		_attackSpeedMultiplier = readF();
		_pAtkSpd2 = readD();
		_collisionRadius = readF();
		_collisionHeight = readF();
		_rhand = readD(); // right hand weapon
		_tmp3 = readD();
		_lhand = readD(); // left hand weapon
		_tmp4 = readC();	// name above char 1=true ... ??
		_isRunning = (byte)readC();
		_isInCombat = (byte)readC();
		_isAlikeDead = (byte)readC();
		_isSummoned = readC(); // invisible ?? 0=false  1=true   2=summoned (only works if model has a summon animation)
		_name = readS();
		_title = readS();
		_tmp5 = readD();
		_tmp6 = readD();
		_tmp7 = readD();  // hmm karma ??

		_abnormalEffect = readD();  // C2
		_tmp8 = readD();  // C2
		_tmp9 = readD();  // C2
		_tmp10 = readD();  // C2
		_tmp11 = readD();  // C2
		_tmp12 = readC();  // C2

		_tmp13 = readC();  // C3  team circle 1-blue, 2-red
		_tmp14 = readF();  // C4 i think it is collisionRadius a second time
		_tmp15 = readF();  // C4      "        collisionHeight     "
		_tmp16 = readD();  // C4
	}

	public void runImpl()
	{
		NpcChar npcChar = _clientThread.getMaps().getOrCreateNpcChar(_objectId, _idTemplate-1000000);

		npcChar.isAttackable = _isAttackable;
		npcChar.x = _x;
		npcChar.y = _y;
		npcChar.z = _z;
		npcChar.heading = _heading;
		npcChar.mAtkSpd = _mAtkSpd;
		npcChar.pAtkSpd = _pAtkSpd;
		npcChar.runSpd = _runSpd;
		npcChar.walkSpd = _walkSpd;
		npcChar.swimRunSpd = _swimRunSpd;
		npcChar.swimWalkSpd = _swimWalkSpd;
		npcChar.flRunSpd = _flRunSpd;
		npcChar.flWalkSpd = _flWalkSpd;
		npcChar.flyRunSpd = _flyRunSpd;
		npcChar.flyWalkSpd = _flyWalkSpd;
		npcChar.attackSpeedMultiplier = _attackSpeedMultiplier;
		npcChar.pAtkSpd2 = _pAtkSpd2;
		npcChar.collisionRadius = _collisionRadius;
		npcChar.collisionHeight = _collisionHeight;
		npcChar.rhand = _rhand;
		npcChar.lrhand = _lhand;
		npcChar.isRunning = _isRunning;
		npcChar.isInCombat = _isInCombat;
		npcChar.isAlikeDead = _isAlikeDead;
		npcChar.isSummoned = _isSummoned;
		//npcChar.realName = _name;
		npcChar.title = _title;

		_clientThread.getVisualInterface().procNpcChar(npcChar);
	}
}
