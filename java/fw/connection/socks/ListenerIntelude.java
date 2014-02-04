package fw.connection.socks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import fw.connection.crypt.GameCryptInterlude;
import fw.connection.game.CLIENT_STATE;
import fw.connection.socks.SOCKS4.SocksAttachment;
import fw.util.Printer;
import xmlex.jsc.PyroProtocolFeeder;
import xmlex.jsc.protocols.ProtocolL2;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.events.PyroClientAdapter;
import jext.util.Util;

public class ListenerIntelude extends ISocksListener {

	private static final Logger _log = Logger.getLogger(ListenerIntelude.class
			.getName());

	public GameCryptInterlude _cryptClient,_cryptServer;
	private PacketHandlerInterlude _phandler = new PacketHandlerInterlude();

	private CLIENT_STATE _state = CLIENT_STATE.CONNECTED;
	private boolean _game = false, _login = false;
	private boolean _init = false;

	public ListenerIntelude() {
		// _log.info("Create");
	}

	public void connectedClient(PyroClient dst) {
		if(SOCKS4.SOCKS){
			byte[] addr = ((SocksAttachment)((SocksAttachment)dst.attachment()).target.attachment()).addr;
			short port = (short)((SocksAttachment)((SocksAttachment)dst.attachment()).target.attachment()).port;
			ByteBuffer data = ByteBuffer.allocate(9);
		    data.put((byte) 0x04); //version 4 or 5
		   			//0x01 = установка TCP/IP соединения
		    		//0x02 = назначение TCP/IP порта (binding)
		    data.put((byte) 0x01); 
		    	//номер порта, 2 байта
		    data.putShort(port);
		    	// IP-адрес, 4 байта
		    data.put(addr);
		    data.put((byte) 0x00); 
		    data.clear();
			dst.write(data);
			
			ByteBuffer response = ByteBuffer.allocate(8);
			response.put((byte) 0x00);
			response.put((byte) 0x5a); // granted and succeeded! w00t!
			response.clear();

			SocksAttachment attachment = dst.attachment();
			attachment.target.write(response);	
		}
	}

	public void disconnectedClient(PyroClient arg0) {
	}

	public void droppedClient(PyroClient arg0, IOException arg1) {
	}

	public void receivedData(PyroClient client, ByteBuffer buf) {
		
		
		PyroProtocolFeeder feeder = new PyroProtocolFeeder(client);
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {
				onPacketFromServer(buf);
			}
		};
		feeder.addByteSink(handler);
		client.addListener(feeder);
		client.removeListener(this);
		
		if (client == getClient()) {
			//_log.info(" receivedData from client: "+buf.remaining());
			//getServer().writeCopy(buf);
		}
		if (client == getServer()) {
			//_log.info(" receivedData from server: "+buf.remaining());
			//getClient().writeCopy(buf);
		}

	}

	public void sentData(PyroClient client, int buf) {
	}

	public void unconnectableClient(PyroClient arg0) {
	}

	private void sendToClientCrypt(ByteBuffer buf){
		_cryptClient.encrypt(buf.array(), 0, buf.array().length);
		sendToClient(buf);
	}
	
	public synchronized void onPacketFromServer(ByteBuffer buf) {
		// _log.info("From Server: "+buf.limit());			
		if (!_init) {
			if (buf.limit() == 184)
				_login = true;
			_init = true;
		}
		if (_game) {
			//ByteBuffer _buf = ISocksListener.copy(buf);
			_cryptServer.decrypt(buf.array(), 0, buf.array().length);
			if (_phandler.handlePacketServer(buf, this))				
				sendToClientCrypt(buf);
		} else {
			sendToClient(buf);
		}

	}

	private void sendToServerCrypt(ByteBuffer buf){
		_cryptServer.encrypt(buf.array(), 0, buf.array().length);
		sendToServer(buf);
	}
	public synchronized void onPacketFromClient(ByteBuffer buf) {
		// _log.info("From Client: "+buf.limit());
		if (!_init) {
			_login = false;
			_game = true;
			_init = true;
			_log.info("Game connect init: " + buf.remaining());
			_cryptServer = new GameCryptInterlude();
			_cryptClient = new GameCryptInterlude();
		}
		if (_game) {
			_cryptClient.decrypt(buf.array(), 0, buf.array().length);
			if (_phandler.handlePacketClient(buf, this))
				sendToServerCrypt(buf);
		} else
			sendToServer(buf);
	}

	@Override
	public void setClient(PyroClient client) {
		super.setClient(client);
		client.removeListeners();

		PyroProtocolFeeder feeder = new PyroProtocolFeeder(client);
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {
				onPacketFromClient(buf);
			}
		};
		feeder.addByteSink(handler);
		client.addListener(feeder);

		// client.addListener(this);
		// _log.info("Set Client: "+client);
	}

	@Override
	public void setServer(PyroClient server) {
		super.setServer(server);
		server.removeListeners();

		if(SOCKS4.SOCKS){
			server.addListener(this);
		}else{		
		PyroProtocolFeeder feeder = new PyroProtocolFeeder(server);
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {
				onPacketFromServer(buf);
			}
		};
		feeder.addByteSink(handler);
		server.addListener(feeder);
		//_log.info("Set Server: "+server);
		}
	}

	public CLIENT_STATE getState() {
		return _state;
	}

	public void setState(CLIENT_STATE _state) {
		this._state = _state;
	}

	@Override
	public void onDataWrite(ByteBuffer buf) {
		//_log.info(Util.printData(buf.array()));
		_cryptServer.encrypt(buf.array(), 0, buf.array().length);
	}

	@Override
	public void setGameCrypt(byte[] key) {
		_cryptClient.setKey(key);
		_cryptClient._toClient = true;
		_cryptServer.setKey(key);
	}

}
