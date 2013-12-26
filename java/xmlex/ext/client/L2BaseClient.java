package xmlex.ext.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.logging.Logger;

import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.events.PyroClientListener;
import xmlex.ext.net.MMOClient;
import xmlex.ext.net.SendablePacket;
import xmlex.ext.net.SessionKey;

public class L2BaseClient extends MMOClient<L2BaseClient> implements Runnable{
	private static Logger _log = Logger.getLogger(L2BaseClient.class.getName());
	/* NETWORK */
	private String _host = "127.0.0.1";
	private int _port = 2106;
	private final ArrayDeque<SendablePacket<L2BaseClient>> _sendQueue = new ArrayDeque<SendablePacket<L2BaseClient>>();
	private PyroSelector _selector;
	private PyroClient _client = null;
	private L2LoginServerListener _loginListener;
	
	public SessionKey sessionId;
	
	public L2BaseClient(){
		_selector = new PyroSelector();
		_loginListener = new L2LoginServerListener(this);
		_selector.spawnNetworkThread("Base thread");
		//PyroSelector.DO_NOT_CHECK_NETWORK_THREAD = false;	
		_selector.scheduleTask(this);
		
	}
	
	private void setClient(PyroClient client){
		_client = client;
		_loginListener = new L2LoginServerListener(this);
		_client.addListener((PyroClientListener) _loginListener);
	}
	
	public void start(){
		//_selector.scheduleTask(this);		
	}
	
	void connect(){		
		_log.info("start...x");
		InetSocketAddress bind = new InetSocketAddress(_host,_port);
		try {
			_log.info("start: "+_host+" port: "+_port);
			this._client = _selector.connect(bind);
			_client.addListener(_loginListener);
			//setClient(_selector.connect(bind));
			_log.info("end...");
		} catch (IOException e) {
			_log.warning("Connection error!");
			e.printStackTrace();
		}
	}

	public void run() {
		connect();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendPacket(SendablePacket<L2BaseClient>... pkt) {
		synchronized (_sendQueue) {
			for (SendablePacket<L2BaseClient> sp : pkt)
				if (sp != null)
					_sendQueue.add(sp);
		}
	}

	@Override
	public void sendPackets(Collection<SendablePacket<L2BaseClient>> pkt) {
		synchronized (_sendQueue) {
			for (SendablePacket<L2BaseClient> sp : pkt)
				_sendQueue.add(sp);
		}			
	}
	
	
}
