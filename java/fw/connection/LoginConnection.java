package fw.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.logging.Logger;

import xmlex.jsc.ISocketClientListener;
import xmlex.jsc.SocketClient;
import javolution.util.FastList;
import fw.common.ThreadPoolManager;
import fw.connection.crypt.NewCrypt;
import fw.connection.login.clientpackets.ILoginConnectionListener;
import fw.connection.login.clientpackets.LoginClientPacket;
import fw.connection.login.serverpackets.*;

public class LoginConnection implements ISocketClientListener, Runnable{
	
	private static Logger _log = Logger.getLogger(LoginConnection.class.getName());
	
	public FastList<String> listServers = new FastList<String>();
	NewCrypt crypt;
	RSAPublicKey _RSApublicKey;
	private int[] _loginOk = new int[2];
	private int _sessionId;
	
	
	// NETWORK
	private final SocketClient _socket = new SocketClient();
	private final ArrayList<GameServerInfo> _serverlist = new ArrayList<GameServerInfo>();
	private final ILoginConnectionListener _listener;
	private long _startConnection = -1;


	public LoginConnection(ILoginConnectionListener listener) {
		this._listener = listener;
		if(this._listener == null){
			_log.warning("LoginConnection listener == null");
			return;
		}
		this.crypt = new NewCrypt(true);
		_socket.setHostPort(_listener.getLoginHost(), _listener.getLoginPort());
		_socket.setListener(this);
		_socket.scheduleAtFixedRate(this, 500, 1000);
	}

	public void start(){
		_socket.connect();
	}
	
	public void onConnect2gameServer(int Play1,int Play2){
		GameServerInfo srv_found = null;
		
		for(GameServerInfo srv : _serverlist)
			if(srv._serverId == _listener.getLoginServerId())
				srv_found = srv;
		if(srv_found == null){
			onErrorConnect("Server ID: "+_listener.getLoginServerId()+" not found");
			return;
		}
		
		LoginResult logRes = new LoginResult();
		logRes.ok = true;
		logRes.login = _listener.getLogin();
		logRes.playId1 = Play1;
		logRes.playId2 = Play2;
		logRes.motivo = "PLAY ON SERVER [" + _listener.getLoginServerId() + "] OK";
		logRes.loginId1 = _loginOk[0];
		logRes.loginId2 = _loginOk[1];
		logRes.host = new Host(srv_found._serverId, srv_found._ip, srv_found._port);
		_listener.setLoginResult(logRes);
		this.getSocket().disconnect();
	}
	
	public void onErrorConnect(String msg){
		LoginResult logRes = new LoginResult();
		logRes.ok = false;
		logRes.motivo = "Error connect: "+msg;
		_listener.setLoginResult(logRes);		
	}	
	
	private void packetHandler(ByteBuffer buf) throws IOException{
		
		_startConnection=System.currentTimeMillis();
		
		crypt.decrypt(buf.array(),0,buf.remaining());
		
		byte id = (byte) (buf.get() & 0xFF);
		//System.out.println("Read packet id: " + Integer.toHexString(id));
		
		LoginServerPacket pkt = null;
		
		switch (id) {
		case 0x00:
			NewCrypt.decXORPass(buf.array());
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
			_startConnection=-1;
			break;
		default:
			_log.warning("Read unknow packet id: 0x0" + Integer.toHexString(id));
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
		this.crypt = new NewCrypt(true);
		_startConnection=System.currentTimeMillis();
		_listener.LoginConnectionOnConnected();	
	}

	public void onDisconnected() {
		_listener.LoginConnectionOnDisconnect();	
	}

	public void onDataRead(ByteBuffer buf) {	
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
		return _listener.getLogin();
	}

	public String getPassword() {
		return _listener.getPassword();
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
		return _listener.getLoginServerId();
	}

	public ArrayList<GameServerInfo> getServerList() {
		return _serverlist;
	}

	public void run() {
		if(_startConnection == -1) return;
		
		if( (System.currentTimeMillis() - _startConnection) > 5*1000 ){
			getSocket().disconnect();
			onErrorConnect("Timeout connect");			
			_startConnection = -1;
		}
	}

}