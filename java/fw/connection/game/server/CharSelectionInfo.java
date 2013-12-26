package fw.connection.game.server;

public class CharSelectionInfo extends GameServerPacket {

	public class Heroes {
		public String Name, LoginName;
		public int Sex, Race, Level, Karma,x,y,z,_heroCount;
		public int CharID, SessionID, ClanID,ClassID;
		public double curHP,curMP;

		public Heroes() {
		}
	}

	private Heroes[] myHeroes;
	private int myHeroesCount;

	@Override
	public void xrun() {
		getClient().getVisualInterface().requestHeroesListDialog(myHeroes,myHeroesCount);
	}

	@Override
	public void read() {
		myHeroesCount = readD();
		System.out.println("myHeroesCount: "+myHeroesCount);
		readD();
		readC();

		myHeroes = new Heroes[myHeroesCount];

		for (int i = 0; i < myHeroesCount; i++) {
			myHeroes[i] = new Heroes();
			myHeroes[i]._heroCount = myHeroesCount; 
			myHeroes[i].Name = readS();
			myHeroes[i].CharID = readD();
			myHeroes[i].LoginName = readS();
			myHeroes[i].SessionID = readD();
			myHeroes[i].ClanID = readD();
			System.out.println("Char id: "+i);
			//int one = readD();
			myHeroes[i].Sex = readD();
			myHeroes[i].Race = readD();
			myHeroes[i].ClassID = readD();
			readD();
			myHeroes[i].x = readD();
			myHeroes[i].y = readD();
			myHeroes[i].z = readD();
			myHeroes[i].curHP = readF();
			myHeroes[i].curMP = readF();
		}
	}

}
