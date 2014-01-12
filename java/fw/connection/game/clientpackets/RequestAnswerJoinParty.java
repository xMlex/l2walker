package fw.connection.game.clientpackets;

public class RequestAnswerJoinParty extends L2GameClientPacket {
	
	private boolean _ok = true;
	
	public RequestAnswerJoinParty(boolean ok){
		_ok = ok;
	}
	public RequestAnswerJoinParty(){
		this(true);
	}
	@Override
	public void excecute() {
		writeC(0x2A);
		writeD((_ok)?1:0);
	}

}
