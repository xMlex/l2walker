package fw.connection.game.serverpackets;

public class KeyPacket extends L2GameServerPacket {

	private byte[] _key = new byte[16];
	//private int status = 0;
	
	@Override
	public void read() {		
		//status=readC();
		readC();
		readB(_key);
	}

	@Override
	public void excecute() {
		/*if(status == 0){
			System.out.println("Error: status = 0 in "+this.getClass().getCanonicalName());			
			return;
		}	*/
		getClient().setCryptKey(_key);
	}

}
