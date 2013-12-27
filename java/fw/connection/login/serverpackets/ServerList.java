package fw.connection.login.serverpackets;

import fw.connection.GameServerInfo;
import fw.connection.login.clientpackets.RequestServerLogin;

public class ServerList extends LoginServerPacket {

	private int _lastServer;
	private int _servercount;
	
	@Override
	public void read() {
		_servercount = readC();
		_lastServer = readC();
		for (int i = 0; i < _servercount; i++) {
			GameServerInfo srv = new GameServerInfo();
			srv._serverId = readC();
			srv._isLastServer = (srv._serverId == _lastServer) ? true :false;
			srv._ip = readC()+"."+readC()+"."+readC()+"."+readC();
			srv._port = readD();
			srv._ageLimit=readC();//age
			srv._isPVP=(readC() == 0x01) ? true :false;//is pvp
			srv._currentPlayers = readH();
			srv._maxPlayers = readH();	
			srv._status = readC();
			readD();
			srv._brackets =(readC() == 0x01) ? true :false;
			getClient().addGameServer(srv);
		}
	}

	@Override
	public void excecute() {
		GameServerInfo srv_found = null;
		
		for(GameServerInfo srv : getClient().getServerList())
			if(srv._serverId == getClient().getServerId())
				srv_found = srv;
		if(srv_found == null){
			getClient().onErrorConnect("Server ID: "+getClient().getServerId()+" not found");
			return;
		}
		getClient().sendPacket(new RequestServerLogin());
	}

}
