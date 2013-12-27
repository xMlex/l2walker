package fw.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.logging.Logger;

import xmlex.jsc.ISocketClientListener;
import xmlex.jsc.SocketClient;
import javolution.util.FastList;
import javolution.util.FastMap;
import fw.common.ThreadPoolManager;
import fw.connection.crypt.NewCrypt;
import fw.connection.login.clientpackets.LoginClientPacket;
import fw.connection.login.serverpackets.*;

public class LoginConnection /*extends Thread*/ implements ISocketClientListener{
	
	private static Logger _log = Logger.getLogger(LoginConnection.class.getName());
	

	private String login, password;
	public FastList<String> listServers = new FastList<String>();
	NewCrypt crypt;
	RSAPublicKey _RSApublicKey;
	private int[] _loginOk = new int[2];
	private int _sessionId;
	int serverNum = 1;
	LoginResult loginResult = null;
	
	ConnectionEventReceiver connectionEventReceiver;
	
	// NETWORK
	private SocketClient _socket = new SocketClient();
	private ArrayList<GameServerInfo> _serverlist = new ArrayList<GameServerInfo>();	


	public LoginConnection(ConnectionEventReceiver connectionEventReceiver, String hostDestino, int portaDestino,
			int serverNum, String login, String password) {
		this.connectionEventReceiver = connectionEventReceiver;
		this.serverNum = serverNum;
		this.login = login;
		this.password = password;
		//this.staticCrypt = new NewCrypt(true);
		this.crypt = new NewCrypt(true);
		_socket.setHostPort(hostDestino, portaDestino);
		_socket.setListener(this);
	}

	public void start(){
		_socket.connect();
	}
	
	public LoginResult getLoginResult() {
		return loginResult;
	}
	
	public void onConnect2gameServer(int Play1,int Play2){
		GameServerInfo srv_found = null;
		
		for(GameServerInfo srv : _serverlist)
			if(srv._serverId == serverNum)
				srv_found = srv;
		if(srv_found == null){
			onErrorConnect("Server ID: "+serverNum+" not found");
			return;
		}
		
		LoginResult logRes = new LoginResult();
		logRes.ok = true;
		logRes.login = login;
		logRes.playId1 = Play1;
		logRes.playId2 = Play2;
		logRes.motivo = "PLAY ON SERVER [" + serverNum + "] OK";
		logRes.loginId1 = _loginOk[0];
		logRes.loginId2 = _loginOk[1];
		logRes.host = new Host(srv_found._serverId, srv_found._ip, srv_found._port);
		loginResult = logRes;
	}
	
	public void onErrorConnect(String msg){
		LoginResult logRes = new LoginResult();
		logRes.ok = false;
		logRes.motivo = "Error connect: "+msg;
		loginResult = logRes;
	}	
	
	private void packetHandler(ByteBuffer buf) throws IOException{
		
		crypt.decrypt(buf.array(),0,buf.remaining());
		
		byte id = (byte) (buf.get() & 0xFF);
		//System.out.println("Read packet id: " + Integer.toHexString(id));
		
		LoginServerPacket pkt = null;
		
		switch (id) {
		case 0x00:
			NewCrypt.decXORPass(buf.array());
			//System.out.println("Read packet INIT");
			pkt=new Init();
			break;
		case 0x0B:
			pkt=new GGAuth();
			break;
		case 0x01:
			pkt=new LoginFail();
			break;
		case 0x03:
			pkt=new LoginOk();
			break;
		case 0x04:
			pkt=new ServerList();
			break;
		case 0x06:
			pkt=new PlayFail();
			break;
		case 0x07:
			pkt=new PlayOk();
			break;
		default:
			System.out.println("Read unknow packet id: 0x0" + Integer.toHexString(id));
			break;
		}
		
		if(pkt != null){
			pkt.setClient(this);
			pkt.setByteBuffer(buf);
			pkt.read();
			ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
		}
		
	}
	
	public void crypt(byte[] raw, int offset, int len ){
		try {
			NewCrypt.appendChecksum(raw);
			crypt.crypt(raw,offset,len);
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public void sendPacket(LoginClientPacket pkt){
		pkt.setClient(this);
		ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
	}
	
	public void onConnected() {
		_log.info("onConnected");		
	}

	public void onDisconnected() {
		_log.info("onDisconnected");		
	}

	public void onDataRead(ByteBuffer buf) {
		//_log.info("onDataRead, size: "+buf.remaining());		
		try {
			packetHandler(buf);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void onDataWrite(ByteBuffer buf) {
		try {
			crypt.crypt(buf.array(),0,buf.array().length);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public final SocketClient getSocket() {
		return _socket;
	}
	
	public void setNewCryptKey(byte[] key){
		crypt = new NewCrypt(key);
	}
	
	public void setRSAKey(RSAPublicKey key) {
		_RSApublicKey = key;
	}
	public void setSessionId(int id) {
		_sessionId = id;
	}
	public int getSessionId() {
		return _sessionId;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public RSAPublicKey getRSAKey() {
		return _RSApublicKey;
	}

	public void setLoginOk12(int _loginOk1, int _loginOk2) {		
		_loginOk[0] = _loginOk1;
		_loginOk[1] = _loginOk2;
	}

	public int[] geLoginOk() {
		return _loginOk;
	}
	
	public void addGameServer(GameServerInfo srv) {
		_serverlist.add(srv);
	}

	public int getServerId() {		
		return serverNum;
	}

	public ArrayList<GameServerInfo> getServerList() {
		return _serverlist;
	}

}