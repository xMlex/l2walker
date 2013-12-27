package fw.connection;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import xmlex.jsc.BaseReceivablePacket;
import xmlex.jsc.BaseSendablePacket;
import xmlex.jsc.ISocketClientListener;
import xmlex.jsc.SocketClient;
import fw.common.ThreadPoolManager;
import fw.connection.crypt.GameCrypt;
import fw.connection.game.L2GamePaccketHandler;
import fw.connection.game.clientpackets.*;
import fw.util.Printer;

public class GameConnection implements ISocketClientListener {
	private static Logger _log = Logger.getLogger(GameConnection.class.getName());

	public LoginResult loginResult = null;
	int charNum = 1;
	ConnectionEventReceiver connectionEventReceiver;
	
	// NETWORK
	private SocketClient _socket = new SocketClient();
	private GameCrypt _crypt = new GameCrypt();
	private final L2GamePaccketHandler _packetHandler = new L2GamePaccketHandler();
	
	private int ProtocolVersion = 152;

	public GameConnection(ConnectionEventReceiver connectionEventReceiver,
			LoginResult loginResult, int clientProtocolVersion, int charNum) {	
		this.connectionEventReceiver = connectionEventReceiver;
		this.loginResult = loginResult;
		if(loginResult == null) System.out.println("loginResult == null");
		setProtocolVersion(clientProtocolVersion);
		this.charNum = charNum;
		_socket = new SocketClient();
		_socket.setListener(this);
		_socket.setHostPort(loginResult.host.Addr, loginResult.host.port);
	}
	
	private void showMSG(String msg){
		connectionEventReceiver.procConnectionEvent(new Msg(
				Msg.MSG_TYPE.SUCESS, msg),
				ENUM_CONECTION_EVENT.EVT_MSG);
	}
	private void showWarning(String msg){
		connectionEventReceiver.procConnectionEvent(new Msg(
				Msg.MSG_TYPE.ATENTION, msg),
				ENUM_CONECTION_EVENT.EVT_MSG);
	}

	public LoginResult getLoginResult() {
		return loginResult;
	}
	
	public void sendPacket(L2GameClientPacket pkt){
		pkt.setClient(this);
		ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
	}

	public void onConnected() {
		showMSG("Connected to game server.");	
		sendPacket(new ProtocolVersion());
	}

	public void onDisconnected() {
		showWarning("Disconnected from game server.");		
	}

	public void onDataRead(ByteBuffer buf) {		
		_crypt.decrypt(buf.array(),0,buf.array().length);
		_log.info(Printer.printData(buf.array(), buf.array().length, "[R] Packet"));
		BaseReceivablePacket<GameConnection> pkt = _packetHandler.handlePacket(buf, this);
		if(pkt != null){
			pkt.setClient(this);
			pkt.setByteBuffer(buf);
			ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
		}
	}

	public void onDataWrite(ByteBuffer buf) {		
		_crypt.encrypt(buf.array(),0,buf.array().length);
	}

	public void start() {
		_socket.connect();
	}
	public void stop() {
		_socket.disconnect();
	}

	public SocketClient getSocket() {
		return _socket;
	}

	public int getProtocolVersion() {
		return ProtocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		ProtocolVersion = protocolVersion;
	}
	
	public void setCryptKey(byte[] _key){
		_crypt.setKey(_key);
	}

}