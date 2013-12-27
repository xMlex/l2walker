package xmlex.jsc;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class TestClient implements ISocketClientListener {
	private static Logger _log = Logger.getLogger(TestClient.class.getName());
	private SocketClient _client = new SocketClient();
	
	public TestClient(){
		_client.setListener(this);
	}
	
	public void connect() {
		_client.connect();
	}
	
	public void disconnect() {
		_client.disconnect();
	}

	public void onConnected() {
		// TODO Auto-generated method stub
		_log.info("onConnected");
	}

	public void onDisconnected() {
		// TODO Auto-generated method stub
		_log.info("onDisconnected");
	}

	public void onDataRead(ByteBuffer buf) {
		// TODO Auto-generated method stub
		_log.info("onDataRead");
		_client.addPacket(new TestPacket());
		
	}

	public void onDataWrite(ByteBuffer buf) {
		// TODO Auto-generated method stub
		_log.info("onDataWrite");
	}

	public SocketClient getSocket() {
		// TODO Auto-generated method stub
		return _client;
	}

}
