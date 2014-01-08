package fw.connection.game.clientpackets;

public class ValidatePosition extends L2GameClientPacket {

	@Override
	public void excecute() {
		writeC(0x48);
		writeLoc(getClient().getGameEngine().getSelfChar().getLoc());
		writeD(getClient().getGameEngine().getSelfChar().getHeading());
		writeD(0);		
	}

}
