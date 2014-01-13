package fw.connection.game.clientpackets;

import fw.extensions.util.Location;

public class MoveBackwardToLocation extends L2GameClientPacket{

	private Location _to,_from=null;
	
	public MoveBackwardToLocation(int x,int y,int z){
		_to = new Location(x, y, z);
	}
	public MoveBackwardToLocation(Location to){
		_to = to;
	}
	public MoveBackwardToLocation(Location to, Location from){
		_to = to;
		_from = from;
	}
	
	@Override
	public void excecute() {
		writeC(0x01);
		writeD(_to.x);
		writeD(_to.y);
		_to.correctGeoZ();		
		writeD(_to.z);
		
		if(_from == null)
			_from = getClient().getGameEngine().getSelfChar().getLoc();
		
		writeD(_from.x);
		writeD(_from.y);
		writeD(_from.z);
		writeD(1);//moveByMouse
	}

}
