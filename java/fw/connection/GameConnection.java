package fw.connection;

import java.nio.ByteBuffer;
import java.util.logging.Logger;





import xmlex.jsc.BaseReceivablePacket;
import xmlex.jsc.ISocketClientListener;
import xmlex.jsc.SocketClient;
import fw.common.ThreadPoolManager;
import fw.connection.crypt.GameCryptInterlude;
import fw.connection.game.CLIENT_STATE;
import fw.connection.game.IGameConnectionLitener;
import fw.connection.game.L2GamePaccketHandler;
import fw.connection.game.clientpackets.*;
import fw.game.GameEngine;
import fw.game.GameVisualInterface;

public class GameConnection implements ISocketClientListener {
	private static Logger _log = Logger.getLogger(GameConnection.class.getName());
	
	// NETWORK
	private SocketClient _socket = new SocketClient();
	private GameCryptInterlude _crypt = new GameCryptInterlude();
	private final L2GamePaccketHandler _packetHandler = new L2GamePaccketHandler();
	private final IGameConnectionLitener _listener;	
	private CLIENT_STATE _state = CLIENT_STATE.CONNECTED;	
	
	public GameConnection(IGameConnectionLitener listener) {	
		this._listener = listener;
		if(this._listener == null){
			_log.warning("GameConnection listener == null");
			return;
		}
		_socket = new SocketClient();
		_socket.setListener(this);
		_socket.setHostPort(_listener.GetLoginResult().host.Addr, _listener.GetLoginResult().host.port);
	}
	
	public LoginResult getLoginResult() {
		return _listener.GetLoginResult();
	}
	
	public void sendPacket(L2GameClientPacket pkt){
		pkt.setClient(this);
		_log.info("[W] "+pkt.getClass().getSimpleName());
		ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
	}

	public void onConnected() {
		_state = CLIENT_STATE.CONNECTED;
		_listener.GameConnectionOnConnect();
		sendPacket(new ProtocolVersion());
	}

	public void onDisconnected() {
		_listener.GameConnectionOnDisconnect();
	}

	public void onDataRead(ByteBuffer buf) {		
		_crypt.decrypt(buf.array(),0,buf.array().length);
		//_log.info(Printer.printData(buf.array(), buf.array().length, "[R] Packet"));
		BaseReceivablePacket<GameConnection> pkt = _packetHandler.handlePacket(buf, this);
		if(pkt != null){
			pkt.setClient(this);
			pkt.setByteBuffer(buf);
			pkt.read();
			ThreadPoolManager.getInstance().executeLSGSPacket(pkt);
		}
	}

	public void onDataWrite(ByteBuffer buf) {		
		_crypt.encrypt(buf.array(),0,buf.array().length);
		//_log.info(Printer.printData(buf.array(), buf.array().length, "[W] Packet"));
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
		return _listener.GameConnectionGetProtocolVersion();
	}
	
	public void setCryptKey(byte[] _key){
		_crypt.setKey(_key);
	}

	public String getLogin() {
		return  _listener.GetLoginResult().login;// loginResult.login;
	}
	
	public GameVisualInterface getVisualInterface() {
		return  _listener.getVisualInterface();// loginResult.login;
	}
	
	public GameEngine getGameEngine() {
		return  _listener.getGameEngine();// loginResult.login;
	}
	
	public CLIENT_STATE getState() {
		return _state;
	}

	public void setState(CLIENT_STATE _state) {
		this._state = _state;
	}

}