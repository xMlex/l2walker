package fw.connection.game.clientpackets;

public class RequestJoinParty extends L2GameClientPacket {

	private String _name = "";
	private int ItemDistribution = 0;
	
	public RequestJoinParty(String name){
		_name = name;
	}
	
	@Override
	public void excecute() {
		writeC(0x29);
		writeS(_name);
		writeD(ItemDistribution);
	}

}
