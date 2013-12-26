package fw.game;

import javolution.util.FastMap;

public class L2ClassList
{
	private final static FastMap<String, String> l2Class = new FastMap<String, String>();
	static
	{
		l2Class.put("0", "H_Fighter");
		l2Class.put("1", "H_Warrior");
		l2Class.put("2", "H_Gladiator");
		l2Class.put("3", "H_Warlord");
		l2Class.put("4", "H_Knight");
		l2Class.put("5", "H_Paladin");
		l2Class.put("6", "H_DarkAvenger");
		l2Class.put("7", "H_Rogue");
		l2Class.put("8", "H_TreasureHunter");
		l2Class.put("9", "H_Hawkeye");
		l2Class.put("10", "H_Mage");
		l2Class.put("11", "H_Wizard");
		l2Class.put("12", "H_Sorceror");
		l2Class.put("13", "H_Necromancer");
		l2Class.put("14", "H_Warlock");
		l2Class.put("15", "H_Cleric");
		l2Class.put("16", "H_Bishop");
		l2Class.put("17", "H_Prophet");
		l2Class.put("18", "E_Fighter");
		l2Class.put("19", "E_Knight");
		l2Class.put("20", "E_TempleKnight");
		l2Class.put("21", "E_SwordSinger");
		l2Class.put("22", "E_Scout");
		l2Class.put("23", "E_PlainsWalker");
		l2Class.put("24", "E_SilverRanger");
		l2Class.put("25", "E_Mage");
		l2Class.put("26", "E_Wizard");
		l2Class.put("27", "E_SpellSinger");
		l2Class.put("28", "E_ElementalSummoner");
		l2Class.put("29", "E_Oracle");
		l2Class.put("30", "E_Elder");
		l2Class.put("31", "DE_Fighter");
		l2Class.put("32", "DE_PaulusKnight");
		l2Class.put("33", "DE_ShillienKnight");
		l2Class.put("34", "DE_BladeDancer");
		l2Class.put("35", "DE_Assassin");
		l2Class.put("36", "DE_AbyssWalker");
		l2Class.put("37", "DE_PhantomRanger");
		l2Class.put("38", "DE_Mage");
		l2Class.put("39", "DE_DarkWizard");
		l2Class.put("40", "DE_Spellhowler");
		l2Class.put("41", "DE_PhantomSummoner");
		l2Class.put("42", "DE_ShillienOracle");
		l2Class.put("43", "DE_ShillienElder");
		l2Class.put("44", "O_Fighter");
		l2Class.put("45", "O_Raider");
		l2Class.put("46", "O_Destroyer");
		l2Class.put("47", "O_Monk");
		l2Class.put("48", "O_Tyrant");
		l2Class.put("49", "O_Mage");
		l2Class.put("50", "O_Shaman");
		l2Class.put("51", "O_Overlord");
		l2Class.put("52", "O_Warcryer");
		l2Class.put("53", "D_Fighter");
		l2Class.put("54", "D_Scavenger");
		l2Class.put("55", "D_BountyHunter");
		l2Class.put("56", "D_Artisan");
		l2Class.put("57", "D_Warsmith");
		l2Class.put("88", "H_Duelist");
		l2Class.put("89", "H_Dreadnought");
		l2Class.put("90", "H_PhoenixKnight");
		l2Class.put("91", "H_HellKnight");
		l2Class.put("92", "H_Sagittarius");
		l2Class.put("93", "H_Adventurer");
		l2Class.put("94", "H_Archmage");
		l2Class.put("95", "H_Soultaker");
		l2Class.put("96", "H_ArcanaLord");
		l2Class.put("97", "H_Cardinal");
		l2Class.put("98", "H_Hierophant");
		l2Class.put("99", "E_EvaTemplar");
		l2Class.put("100", "E_SwordMuse");
		l2Class.put("101", "E_WindRider");
		l2Class.put("102", "E_MoonlightSentinel");
		l2Class.put("103", "E_MysticMuse");
		l2Class.put("104", "E_ElementalMaster");
		l2Class.put("105", "E_EvaSaint");
		l2Class.put("106", "DE_ShillienTemplar");
		l2Class.put("107", "DE_SpectralDancer");
		l2Class.put("108", "DE_GhostHunter");
		l2Class.put("109", "DE_GhostSentinel");
		l2Class.put("110", "DE_StormScreamer");
		l2Class.put("111", "DE_SpectralMaster");
		l2Class.put("112", "DE_ShillienSaint");
		l2Class.put("113", "O_Titan");
		l2Class.put("114", "O_GrandKhauatari");
		l2Class.put("115", "O_Dominator");
		l2Class.put("116", "O_Doomcryer");
		l2Class.put("117", "D_FortuneSeeker");
		l2Class.put("118", "D_Maestro");
	}

	private final static FastMap<Integer, Integer> l2exp = new FastMap<Integer, Integer>();
	static
	{
		l2exp.put(1,1);
		l2exp.put(2, 68);
		l2exp.put(3, 363);
		l2exp.put(4, 1168);
		l2exp.put(5, 2884);
		l2exp.put(6, 6038);
		l2exp.put(7, 11287);
		l2exp.put(8, 19423);
		l2exp.put(9, 31378);
		l2exp.put(10, 48229);
		l2exp.put(11, 71202);
		l2exp.put(12, 101677);
		l2exp.put(13, 141193);
		l2exp.put(14, 191454);
		l2exp.put(15, 254330);
		l2exp.put(16, 331867);
		l2exp.put(17, 426288);
		l2exp.put(18, 540000);
		l2exp.put(19, 675596);
		l2exp.put(20, 835862);
		l2exp.put(21, 1023784);
		l2exp.put(22, 1242546);
		l2exp.put(23, 1495543);
		l2exp.put(24, 1786379);
		l2exp.put(25, 2118876);
		l2exp.put(26, 2497077);
		l2exp.put(27, 2925250);
		l2exp.put(28, 3407897);
		l2exp.put(29, 3949754);
		l2exp.put(30, 4555796);
		l2exp.put(31, 5231246);
		l2exp.put(32, 5981576);
		l2exp.put(33, 6812513);
		l2exp.put(34, 7730044);
		l2exp.put(35, 8740422);
		l2exp.put(36, 9850166);
		l2exp.put(37, 11066072);
		l2exp.put(38, 12395215);
		l2exp.put(39, 13844951);
		l2exp.put(40, 15422929);
		l2exp.put(41, 17137087);
		l2exp.put(42, 18995665);
		l2exp.put(43, 21007203);
		l2exp.put(44, 23180550);
		l2exp.put(45, 25524868);
		l2exp.put(46, 28049635);
		l2exp.put(47, 30764654);
		l2exp.put(48, 33680052);
		l2exp.put(49, 36806289);
		l2exp.put(50, 40154162);
		l2exp.put(51, 45525133);
		l2exp.put(52, 51262490);
		l2exp.put(53, 57383988);
		l2exp.put(54, 63907911);
		l2exp.put(55, 70853089);
		l2exp.put(56, 80700831);
		l2exp.put(57, 91162654);
		l2exp.put(58, 102265881);
		l2exp.put(59, 114038596);
		l2exp.put(60, 126509653);
		l2exp.put(61, 146308200);
		l2exp.put(62, 167244337);
		l2exp.put(63, 189364894);
		l2exp.put(64, 212717908);
		l2exp.put(65, 237352644);
		l2exp.put(66, 271975263);
		l2exp.put(67, 308443198);
		l2exp.put(68, 346827154);
		l2exp.put(69, 387199547);
		l2exp.put(70, 429634523);
		l2exp.put(71, 474207979);
		l2exp.put(72, 532694979);
		l2exp.put(73, 606322775);
		l2exp.put(74, 696381369);
		l2exp.put(75, 804225364);
		l2exp.put(76, 931275828);
		l2exp.put(77, 1108571463);
		l2exp.put(78, 1309482881);
		l2exp.put(79, 1535687304);
		l2exp.put(80, 1788937098);
		l2exp.put(81, 1988937098);
		l2exp.put(82, 2018937098);
		l2exp.put(83, 2058937098);
		l2exp.put(84, 2078937098);
		l2exp.put(85, 2098937098);
	}

	private L2ClassList(){};

	public static String getL2ClassName(int classId)
	{
		return l2Class.get(String.valueOf(classId));
	}

	public static Integer getL2ClassExp(int level)
	{
		return l2exp.get(level);
	}
}
