package xmlex.jsc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import xmlex.jsc.protocols.ProtocolL2;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.events.PyroClientListener;

public class SocketClient implements PyroClientListener, Runnable {

	private static Logger _log = Logger.getLogger(SocketClient.class.getName());

	private PyroSelector _selector;
	private PyroClient _client = null;
	private final ScheduledThreadPoolExecutor _worker;
	private boolean _shutdown = false;

	private String host = "127.0.0.1";
	private int port = 2106;
	private ISocketClientListener _listener = null;

	private final ArrayDeque<ISendablePacket> _sendList = new ArrayDeque<ISendablePacket>();

	private boolean _ev_connect = false, _ev_disconnect, _connected = false;

	public SocketClient() {
		super();
		_worker = new ScheduledThreadPoolExecutor(1);
		scheduleAtFixedRate(this, 100, 1);
	}

	public PyroClient getClient() {
		return _client;
	}

	public void addPacket(ISendablePacket pkt) {
		synchronized (_sendList) {
			_sendList.add(pkt);
		}
	}
	
	public void setHostPort(String host,int port){
		this.host = host;
		this.port = port;
	}

	public void setListener(ISocketClientListener listener) {
		_listener = listener;
	}

	private void setClient(PyroClient client) {
		if (client == null) {
			_log.warning("Error: setClient = null");
			return;
		}
		this._client = client;
		this._client.addListener(this);
	}

	@Override
	public void connectedClient(PyroClient client) {
		_connected = true;
		if (_listener != null)
			_listener.onConnected();

		PyroProtocolFeeder feeder = new PyroProtocolFeeder(this._client);
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {
				if (_listener != null) {
					_listener.onDataRead(buf);
				} else {
					_log.warning("onPacket - _listener == null");
				}
			}
		};
		feeder.addByteSink(handler);
		this._client.addListener(feeder);
	}

	@Override
	public void unconnectableClient(PyroClient client) {
		_connected = false;
		if (_listener != null)
			_listener.onDisconnected();
		// _log.info("unconnectableClient");
	}

	@Override
	public void droppedClient(PyroClient client, IOException cause) {
		_connected = false;
		if (_listener != null)
			_listener.onDisconnected();
		// _log.info("droppedClient");
	}

	@Override
	public void disconnectedClient(PyroClient client) {
		_connected = false;
		if (_listener != null)
			_listener.onDisconnected();
		// _log.info("disconnectedClient");
	}

	@Override
	public void receivedData(PyroClient client, ByteBuffer data) {
		// _log.info("receivedData");
		// data.clear();
	}

	@Override
	public void sentData(PyroClient client, int bytes) {
		// _log.info("sentData");
	}

	@Override
	public void run() {
		if (_selector == null)
			_selector = new PyroSelector();

		if (!_connected && _ev_connect) {
			try {
				//_log.info("Connect new client");
				_ev_connect = false;
				setClient(_selector.connect(getConnectAddres()));
			} catch (IOException e) {
				_log.warning("Error connect: " + e.getMessage());
				unconnectableClient(null);
			}
		}

		if (_connected) {
			synchronized (_sendList) {
				if (_sendList.size() > 0)
					for (ISendablePacket pkt : _sendList){
						if (_listener != null)
							_listener.onDataWrite(pkt.getData());
						int len =pkt.getData().remaining() + 2;
						byte[] lendata = new byte[2];
						lendata[0] = (byte) (len & 0xff);
						lendata[1] = (byte) (len >> 8 & 0xff);
						
						_client.write(ByteBuffer.wrap(lendata));
						_client.write(pkt.getData());
					}
				_sendList.clear();	
			}
		}

		if (_connected && _ev_disconnect) {
			// _log.info("Disconnect");
			_ev_disconnect = false;
			this._client.dropConnection();
			this._client.shutdown();
		}

		_selector.select(1);
	}

	public void connect() {
		_ev_connect = true;
		_ev_disconnect = false;
		// _log.info("Event connect");
	}

	public void disconnect() {
		_ev_connect = false;
		_ev_disconnect = true;
		// _log.info("Event disconnect");
	}

	private InetSocketAddress getConnectAddres() {
		try {
			return new InetSocketAddress(InetAddress.getByName(this.host),
					this.port);
		} catch (UnknownHostException e) {
			return new InetSocketAddress(this.host, this.port);
		}
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial,
			long delay) {
		try {
			if (delay <= 0)
				delay = 1;
			return _worker.scheduleAtFixedRate(r, initial, delay,
					TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			if (!isShutdown()) {
				_log.warning("GeneralThreadPool: Failed schedule task at fixed rate!");
				Thread.dumpStack();
			}
			return null; /* shutdown, ignore */
		}
	}

	private boolean isShutdown() {
		return _shutdown;
	}

	public void shutdown() {
		_shutdown = true;
		try {
			_worker.shutdown();
			_worker.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
