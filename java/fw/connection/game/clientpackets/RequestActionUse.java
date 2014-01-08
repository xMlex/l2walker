package fw.connection.game.clientpackets;

public class RequestActionUse extends L2GameClientPacket {

	private int _id;
	private boolean _ctr;
	private boolean _shift;
	
	public RequestActionUse(int id, boolean ctr, boolean shift){
		_id = id;
		_ctr = ctr;
		_shift = shift;
	}
	public RequestActionUse(int id, boolean ctr){
		this(id,ctr,false);
	}
	public RequestActionUse(int id){
		this(id,false,false);
	}
	
	@Override
	public void excecute() {
		writeC(0x45);
		writeD(_id);
		writeD( (_ctr)?1:0 );
		writeC( (_shift)?0x01:0x00 );
	}

}
