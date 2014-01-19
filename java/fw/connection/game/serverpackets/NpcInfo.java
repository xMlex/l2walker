package fw.connection.game.serverpackets;

import fw.game.model.instances.L2NpcInstance;

public class NpcInfo extends L2GameServerPacket {

	private L2NpcInstance _npc;
	
	@Override
	public void read() {
		int objectId = readD();
		_npc = getClient().getGameEngine().getWorld().getOrCreateNpc(objectId);
		_npc.setNpcId(readD());
		_npc.setIsAttackable(((readD() == 1)?true:false));
		_npc.setXYZ(readD(), readD(), readD());
		_npc.setHeading(readD());
		readD();
		_npc.setmAtkSpd(readD());
		readD(); //patk-spd
		_npc.setRunspd(readD()); //run-spd
		_npc.setWalkspd(readD()); //walkSpd: 26 (0x0000001A)
		readD(); //  swimRSpd: 121 (0x00000079)
		readD(); //swimWSpd: 26 (0x0000001A)
		readD(); //flRSpd: 121 (0x00000079)
		readD(); //flWSpd: 26 (0x0000001A)
		readD(); //FlyRSpd: 121 (0x00000079)
		readD(); //FlyWSpd: 26 (0x0000001A)
		_npc.setMovementSpeedMultiplier(readF());  //ProperMul: 1,1
		readF(); //Paspd: 1,00188000000162
		readF(); //CollisRadius: 13
		readF(); //CollisHeight: 9
		readD(); //RHand: 0
		readD(); //d: 0 (0x00000000)
		readD(); //LHand: 0
		readC(); //c  nameabove: 1 (0x01)
		
		_npc.setRunning( ((readC()==0)?false:true) ); //c  isRun: 0 (0x00)
		_npc.setInFight(((readC()==0)?false:true)); //c  isInFight: 0 (0x00)
		_npc.setAlikeDead(((readC()==0)?false:true)); //c  isAlikeDead: 0 (0x00)
		_npc.setSummoned(((readC()==0)?false:true)); //c  isSummoned: 0 (0x00)
		
		_npc.setName(readS()); //s  Name: 
		_npc.setTitle(readS()); //  Title: 
		readD(); //d: 0 (0x00000000)
		readD(); //PvpFlag: 0 (0x00000000)
		readD(); //karma?: 0 (0x00000000)
		readH(); // abnEffect: 0 (0x0000)
		readH(); //h: 0 (0x0000)
		readD(); //d: 0 (0x00000000)
		readD(); //d: 0 (0x00000000)
		readD(); //d: 0 (0x00000000)
		readD(); //d: 0 (0x00000000)
		readC(); //c  c: 0 (0x00)
		readC(); //c  Team: 0 (0x00)
		readF(); //collisRadius: 13
		readF(); //collisHeight: 9
		readD(); //  d: 0 (0x00000000
	}

	@Override
	public void excecute() {
			getClient().getVisualInterface().procNpcChar(_npc);
	}

}
