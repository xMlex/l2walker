package fw.connection.game.serverpackets;

import fw.extensions.util.Location;
import fw.game.model.L2Char;

public class CharInfo extends L2GameServerPacket {

	private L2Char _char;
	
	@Override
	public void read() {
		Location loc = new Location(readD(), readD(), readD(), readD());
		_char = getClient().getGameEngine().getWorld().getOrCreateChar(readD());
		_char.setLoc(loc);
		_char.setName(readS());
		readD();// d  Race: 0 (0x00000000)
		readD();// d  Sex: 1 (0x00000001)
		readD();// d  ClassID: Human Fighter (Âîèí) ID:0 (0x0000)
		readD();// d  DHair: 0 (0x00000000)
		readD();// d  Head: 0
		readD();// d  RHand: Ìå÷ Îðóæåíîñöà ID:2369 (0x0941)
		readD();// d  LHand: 0
		readD();// d  Gloves: 0
		readD();// d  Chest: Òóíèêà  ID:426 (0x01AA)
		readD();// d  Legs: Øòàíû  ID:462 (0x01CE)
		readD();// d  Feet: 0
		readD();// d  Back: 0
		readD();// d  LRHand: 0
		readD();// d  Hair: 0
		readD();// d  Face: 0 (0x00000000)
		readB(new byte[48]);//0060 z  0048: Ïðîïóñêàåì 48 áàéò(à)
		readD();// d  PvpFlag: 0 (0x00000000)
		readD();// d  Karma: 0 (0x00000000)
		readD();// d  MSpeed: 213 (0x000000D5)
		readD();// d  PSpeed: 416 (0x000001A0)
		readD();// d  PvpFlag: 0 (0x00000000)
		readD();// d  Karma: 0 (0x00000000)
		_char.setRunspd(readD());// d  runSpd: 115 (0x00000073)
		_char.setWalkspd(readD());// d  walkSpd: 80 (0x00000050)
		readD();// d  swimRSpd: 115 (0x00000073)
		readD();// d  swimWSpd: 80 (0x00000050)
		readD();// d  flRunSpd: 115 (0x00000073)
		readD();// d  flWalkSpd: 80 (0x00000050)
		readD();// d  flyRSpd: 115 (0x00000073)
		readD();// d  flyWSpd: 80 (0x00000050)
		readF();// f  SpdMul: 1,26956522464752
		readF();// f  ASpdMul: 1,52533328533173
		readF();// f  collisRadius: 9
		readF();// f  collisHeight: 23
		readD();// d  HairStyle: 1 (0x00000001)
		readD();// d  HairColor: 0
		readD();// d  Face: 0 (0x00000000)
		readS();// s  Title: 
		readD();// d  clanID: 0 (0x00000000)
		readD();// d  clanCrest: 0 (0x00000000)
		readD();// d  allyID: 0 (0x00000000)
		readD();// d  allyCrest: 0 (0x00000000)
		readD();// d  siegeFlag: 0 (0x00000000)
		readC();// c  isStand: 1 (0x01)
		_char.setRunning( ((readC()==0)?false:true) ); //c  isRun: 0 (0x00)
		_char.setInFight(((readC()==0)?false:true)); //c  isInFight: 0 (0x00)
		_char.setAlikeDead(((readC()==0)?false:true)); //c  isAlikeDead: 0 (0x00)
		readC();// c  Invis: 0 (0x00)
		readC();// c  Mount: 0 (0x00)
		readC();// c  shop: 0 (0x00)
		readH();//  cubics: 0 (0x0000)
		readC();// c  findparty: 0 (0x00)
		readD();// d  abnEffects: 0 (0x00000000)
		readC();// c  RecomLeft: 3 (0x03)
		readH();// h  RecomHave: 0 (0x0000)
		readD();// d  classID: Human Fighter (Âîèí) ID:0 (0x0000)
		readD();// d  maxCP: 144 (0x00000090)
		readD();// d  curCP: 144 (0x00000090)
		readC();// c  isMounted: 0 (0x00)
		readC();// c  Team: 0 (0x00)
		readD();// d  clanBigCrestId: 0 (0x00000000)
		readC();// c  isNoble: 0 (0x00)
		readC();// c  isHero: 0 (0x00)
		readC();// c  isFishing: 0 (0x00)
		readD();// d  fishX: 0 (0x00000000)
		readD();// d  fishY: 0 (0x00000000)
		readD();// d  fishZ: 0 (0x00000000)
		readD();// d  NameColor: 16777215
		readD();// d  isRun: 0 (0x00000000)
		readD();// d  PledgeClass: 0 (0x00000000)
		readD();// d  PledgeColor: 0
		readD();// d  TitleColor: 16777079
		readD();// d  d: 0 (0x00000000)
	}

	@Override
	public void excecute() {

	}

}
