package fw.connection.game.serverpackets;

import fw.connection.game.CLIENT_STATE;
import fw.connection.game.clientpackets.CharacterSelected;
import fw.game.model.CharSelectInfoPackage;

public class CharSelectInfo extends L2GameServerPacket{

	private CharSelectInfoPackage[] _chars;
	
	@Override
	public void read() {
		int size = readD(),oid;
		String name;
		_chars = new CharSelectInfoPackage[size];
		for (int i = 0; i < size; i++)
		{
			name=readS();
			oid=readD();
			_chars[i] = new CharSelectInfoPackage(oid,name);
			
			readS();//_loginName
			_chars[i].setSessionId(readD());
			_chars[i].setClanId(readD());
			readD(); // ??
			
			_chars[i].setSex(readD());
			_chars[i].setRace(readD());
			
			_chars[i].setClassId(readD());
			
			readD(); // active ??
			
			readD(); // x
			readD(); // y
			readD(); // z
			
			_chars[i].setCurrentHp(readF()); // hp cur
			_chars[i].setCurrentMp(readF()); // mp cur
			
			_chars[i].setSp(readD());
			_chars[i].setExp(readQ());
			_chars[i].setLevel(readD());
			
			_chars[i].setKarma(readD()); // karma
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
	
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			readD();
			
			_chars[i].setHairStyle(readD());
			_chars[i].setHairColor(readD());
			_chars[i].setFace(readD());
			
			_chars[i].setMaxHp((int)readF()); // hp max
			_chars[i].setMaxMp((int)readF()); // mp max
			
			_chars[i].setDeleteTimer(readD());
			
			_chars[i].setClassId(readD());
			
			readD(); // c3 auto-select char
			
			readC();//EnchantEffect
			
			_chars[i].setAugmentationId(readD());
			
		}
		
	}

	@Override
	public void excecute() {			
		getClient().setState(CLIENT_STATE.AUTHED);
		getClient().sendPacket(new CharacterSelected(getClient().getGameEngine().getCharNum()-1));
		//getClient().getVisualInterface().requestHeroesListDialog(_chars, _chars.length);
	}

}
