package fw.connection.game.serverpackets;

import fw.connection.game.clientpackets.RequestSkillCoolTime;
import fw.connection.game.clientpackets.RequestSkillList;
import fw.extensions.util.Location;
import fw.game.model.L2Player;
import fw.game.model.L2PlayerEvent;

public class UserInfo extends L2GameServerPacket {

	private L2Player _char;
	
	@Override
	public void read() {
		Location loc = new Location(readD(), readD(), readD(),readD());
		_char = getClient().getGameEngine().getWorld().getOrCreatePlayer(readD());
		_char.setLoc(loc);
		_char.setName(readS());
		
		readD(); // d  Race: 4 (0x00000004)
		readD(); // d  Sex: 1 (0x00000001)
		readD(); // d  ClassID: Dwarven Fighter (Ïîäìàñòåðüå) ID:53 (0x0035)
		_char.setLevel(readD()); // d  Level: 4 (0x00000004)
		_char.setExp(readQ()); // q  Exp: 2631
		readD(); // d  STR: 39 (0x00000027)
		readD(); // d  DEX: 29 (0x0000001D)
		readD(); // d  CON: 45 (0x0000002D)
		readD(); // d  INT: 20 (0x00000014)
		readD(); // d  WIT: 10 (0x0000000A)
		readD(); // d  MEN: 27 (0x0000001B)
		_char.setMax_hp(readD()); // d  MaxHP: 200 (0x000000C8)
		_char.setCurrentHp(readD()); // d  CurHP: 200 (0x000000C8)
		_char.setMax_mp(readD()); // d  MaxMP: 61 (0x0000003D)
		_char.setCurrentMp(readD()); // d  CurMP: 61 (0x0000003D)
		_char.setSp(readD()); // d  SP: 69 (0x00000045)
		_char.setCurLoad(readD()); // d  CurLoad: 37688 (0x00009338)
		_char.setMaxLoad(readD()); // d  MaxLoad: 115291 (0x0001C25B)
		readD(); // d  d: 40 (0x00000028)
		readD(); // d  DHair: 0 (0x00000000)
		readD(); // d  Rear: 0 (0x00000000)
		readD(); // d  Lear: 0 (0x00000000)
		readD(); // d  Neck: 0 (0x00000000)
		readD(); // d  RFinger: 0 (0x00000000)
		readD(); // d  LFinger: 0 (0x00000000)
		readD(); // d  Head: 268479993 (0x1000ADF9)
		readD();//_char.getInventory().setRHand(readD()); // d  RHand: 268478230 (0x1000A716)
		readD();//_char.getInventory().setLHand(readD()); // d  LHand: 0 (0x00000000)
		readD(); // d  Gloves: 268479990 (0x1000ADF6)
		readD(); // d  Chest: 268479992 (0x1000ADF8)
		readD(); // d  Legs: 268479991 (0x1000ADF7)
		readD(); // d  Feet: 268479989 (0x1000ADF5)
		readD(); // d  Back: 0 (0x00000000)
		readD(); // d  LRHand: 0 (0x00000000)
		readD(); // d  Hair: 0 (0x00000000)
		readD(); // d  Face: 0 (0x00000000)
		readD(); // d  DHair: 0
		readD(); // d  Rear: 0
		readD(); // d  Lear: 0
		readD(); // d  Neck: 0
		readD(); // d  RFinger: 0
		readD(); // d  LFinger: 0
		readD(); // d  Head: Êîæàíûé Øëåì ID:44 (0x002C)
		_char.getInventory().setRHand(readD()); // d  RHand: Äóáèíà Ãèëüäèè ID:2370 (0x0942)
		_char.getInventory().setLHand(readD()); // d  LHand: 0
		readD(); // d  Gloves: Êîæàíûå Ïåð÷àòêè ID:50 (0x0032)
		readD(); // d  Chest: Êîñòÿíàÿ Êèðàñà ID:24 (0x0018)
		readD(); // d  Legs: Êîñòÿíûå Íàáåäðåííèêè ID:31 (0x001F)
		readD(); // d  Feet: Ïðîñòûå Ñàïîãè ID:38 (0x0026)
		readD(); // d  Back: 0
		readD(); // d  LRHand: 0
		readD(); // d  Hair: 0
		readD(); // d  Face: 0 (0x00000000)
		readB(new byte[68]);// skeep z  0068: Ïðîïóñêàåì 68 áàéò(à)
		readD(); // d  Patk: 6 (0x00000006)
		readD(); // d  Paspd: 413 (0x0000019D)
		readD(); // d  Pdef: 129 (0x00000081)
		readD(); // d  evasion: 36 (0x00000024)
		readD(); // d  accur: 40 (0x00000028)
		readD(); // d  crithit: 43 (0x0000002B)
		readD(); // d  Matk: 2 (0x00000002)
		readD(); // d  Maspd: 203 (0x000000CB)
		readD(); // d  Paspd: 413 (0x0000019D)
		readD(); // d  Mdef: 49 (0x00000031)
		readD(); // d  PvpFlag: 0 (0x00000000)
		_char.setKarma(readD()); // d  Karma: 0 (0x00000000)
		_char.setRunspd(readD()); // d  runSpd: 115 (0x00000073)
		_char.setWalkspd(readD()); // d  walkSpd: 80 (0x00000050)
		readD(); // d  swimRSpd: 115 (0x00000073)
		readD(); // d  swimWSpd: 80 (0x00000050)
		readD(); // d  flRSpd: 115 (0x00000073)
		readD(); // d  flWSpd: 80 (0x00000050)
		readD(); // d  flyRSpd: 115 (0x00000073)
		readD(); // d  flyWSpd: 80 (0x00000050)
		_char.setMovementSpeedMultiplier(readF()); // f  MoveMul: 1,08695650100708
		readF(); // f  aspdMul: 1,51433336734772
		readF(); // f  collisRadius: 9
		readF(); // f  collisHeight: 18
		readD(); // d  HairStyle: 1 (0x00000001)
		readD(); // d  HairColor: 2
		readD(); // d  Face: 0 (0x00000000)
		readD(); // d  AccessLvl: 0 (0x00000000)
		readS(); // s  Title: 
		readD(); // d  clanID: 0 (0x00000000)
		readD(); // d  clanCrestId: 0 (0x00000000)
		readD(); // d  AllyID: 0 (0x00000000)
		readD(); // d  AllyCrestId: 0 (0x00000000)
		readD(); // d  ClanLeader: 0 (0x00000000)
		readC(); // c  Mount: 0 (0x00)
		readC(); // c  shop: 0 (0x00)
		readC(); // c  DwarfCraft: 0 (0x00)
		readD(); // d  PKkills: 0 (0x00000000)
		readD(); // d  PVPkills: 0 (0x00000000)
		readH(); // h  cubics: 0 (0x0000)
		readC(); // c  findparty: 0 (0x00)
		readD(); // d  abnEffects: 0 (0x00000000)
		readC(); // c  c: 0 (0x00)
		readD(); // d  clanPrivil: 0 (0x00000000)
		readH(); // h  RecomLeft: 3 (0x0003)
		readH(); // h  RecomHave: 0 (0x0000)
		readD(); // d  d: 0 (0x00000000)
		readH(); // h  InventLimit: 100 (0x0064)
		readD(); // d  classId: Dwarven Fighter (Ïîäìàñòåðüå) ID:53 (0x0035)
		readD(); // d  sEff: 0 (0x00000000)
		_char.setMax_cp(readD()); // d  maxCP: 139 (0x0000008B)
		_char.setCurrentCp(readD()); // d  curCP: 139 (0x0000008B)
		readC(); // c  Mount: 0 (0x00)
		readC(); // c  Team: 0 (0x00)
		readD(); // d  clanBigCrestId: 0 (0x00000000)
		readC(); // c  Noble: 0 (0x00)
		readC(); // c  Hero: 0 (0x00)
		readC(); // c  Fishing: 0 (0x00)
		readD(); // d  fishX: 0 (0x00000000)
		readD(); // d  fishY: 0 (0x00000000)
		readD(); // d  fishZ: 0 (0x00000000)
		readD(); // d  NameColor: 16777215
		_char.setRunning( readC(1) );  // c  isRun: 1 (0x01)
		readD(); // d  PledgeClass: 0 (0x00000000)
		readD(); // d  d: 0 (0x00000000)
		readD(); // d  TitleColor: 16777079
		readD(); // d  d: 0 (0x00000000)
	}

	@Override
	public void excecute() {
		_char.setGameEngine(getClient().getGameEngine());
		getClient().getGameEngine().setSelfChar(_char);	
		getPlayer().getInventory().updateSlots();
		getClient().getVisualInterface().procSetUserChar(_char);
		
		getClient().sendPacket(new RequestSkillList());
		getPlayer().onEvent(L2PlayerEvent.UserInfo);
		
	}

}
