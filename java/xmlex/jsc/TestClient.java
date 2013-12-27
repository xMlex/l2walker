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
	
	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		_log.info("onConnected");
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		_log.info("onDisconnected");
	}

	@Override
	public void onDataRead(ByteBuffer buf) {
		// TODO Auto-generated method stub
		_log.info("onDataRead");
		_client.addPacket(new TestPacket());
		
	}

	@Override
	public void onDataWrite(ByteBuffer buf) {
		// TODO Auto-generated method stub
		_log.info("onDataWrite");
	}

}
