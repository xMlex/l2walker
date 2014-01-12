package fw.connection.game.clientpackets;

public class RequestMagicSkillUse extends L2GameClientPacket {

	private int _id;
	private boolean _ctr,_shift;
	
	public RequestMagicSkillUse(int id,boolean ctr,boolean shift){
		_id = id;
		_ctr = ctr;
		_shift = shift;
	}
	public RequestMagicSkillUse(int id,boolean ctr){
		this(id,ctr,false);
	}
	public RequestMagicSkillUse(int id){
		this(id,false,false);
	}
	
	@Override
	public void excecute() {
		writeC(0x2F);
		writeD(_id);
		writeD((_ctr)?1:0);
		writeC((_shift)?0x01:0x00);
	}

}
