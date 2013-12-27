package fw.connection.game.serverpackets;

public class CharSelectionInfo extends L2GameServerPacket {

	public class Heroes{
		public String Name;
		public int ClassID=0;
		public String _clanName;
		public int _clanCrest=0;
		public String _allyName;
		public int _allyCrest;
		public int _heroCount;

		public Heroes(String charName,int classId, String clanName, String allyName, int heroCount)
		{
			Name = charName;
			ClassID = classId;
			_clanName = clanName;
			_allyName = allyName;
			_heroCount = heroCount;
		}
	}
	
	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		
	}

}
