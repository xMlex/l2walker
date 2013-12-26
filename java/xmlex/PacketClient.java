package xmlex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import sun.security.krb5.Config;
import xmlex.common.ThreadPoolManager;
import xmlex.config.ConfigSystem;
import xmlex.ext.client.L2BaseClient;
import xmlex.ext.crypt.LoginCrypt;
import xmlex.ext.net.PyroProtocolFeeder;
import xmlex.ext.net.ProtocolL2;
import xmlex.ext.net.SessionKey;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.events.PyroClientAdapter;

public class PacketClient extends PyroClientAdapter implements Runnable {
	
	private static Logger _log = Logger.getLogger(PacketClient.class.getName());
	
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 2106;

	PyroSelector selector;
	
	private LoginCrypt _loginCrypt;
	private SessionKey _sessionKey;
	private long _connectionStartTime;
	
	public PacketClient(){
		selector = new PyroSelector();
		_loginCrypt = new LoginCrypt();
		_loginCrypt.setKey(LoginCrypt.STATIC_BLOWFISH_KEY);
	}
	
	ArrayList<PyroClient> clientlist = new ArrayList<PyroClient>();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		ConfigSystem.load();
		ThreadPoolManager.getInstance();	
		
		//_log.info("Protocol version: "+(int)0x0000c621);
		
		//final SelectorThread<L2LoginClient> st = new SelectorThread<L2LoginClient>(new L2LoginPacketHandler());
		
		//ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(st, 2, 2);
		//for (int i = 0; i < 5; i++) {
			//st.createClient(HOST, PORT);
		//}
		
		//st.run();
	
		//selector = new PyroSelector();
		//if(true) return;
		_log.info("yyyy");
		L2BaseClient _client = new L2BaseClient();
		_log.info("xxxxx");
		Thread.sleep(500);
		_client.start();
		
		
		
		//
		/*PacketClient main = new PacketClient();
		
		main.selector.DO_NOT_CHECK_NETWORK_THREAD = true;
		main.selector.spawnNetworkThread("Base thread");		
		main.selector.scheduleTask(main);*/
		
		
		
		//client2.addListener(main);

		while (true) { Thread.sleep(2000); }
		
	}

	@Override
	public void connectedClient(PyroClient client) {
		
		_connectionStartTime = System.currentTimeMillis();
		// for this example the following is overkill...
		System.out.println("Connected ");
		// for this example the following is overkill...
		/*
		 * ByteBuffer _buf = ByteBuffer.allocate(5); _buf.putShort((short) 3);
		 * _buf.put((byte) 0x00); _buf.put((byte) 0x02); _buf.put((byte) 0x01);
		 * _buf.flip(); client.writeCopy(_buf);
		 */
		// first create
		final PyroProtocolFeeder feeder = new PyroProtocolFeeder(client);

		// lets create a packet handler
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {

				System.out.println("buf pos: " + buf.position() + " Size: " + buf.remaining());
				onPaket(buf);

			}
		};
		// add the packet handler to the
		feeder.addByteSink(handler);
		client.addListener(feeder);
		// ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this,5,500);
	}

	public void onPaket(ByteBuffer buf) {
		System.out.println("*** Read packet *** "+buf.position());
		int size = buf.remaining();
		
		if(!decrypt(buf, size)){
			_log.info("Disconnected.");
		}
		System.out.println("*** Read packet *** "+buf.position());
		int id = buf.get() & 0xFF;	
		System.out.println("\tid: " + Integer.toHexString(id) + " Size: " + size);

		switch (id) {
		case 0x00:
			System.out.println("\tSession id: " + buf.getInt());
			System.out.println("*** Read packet *** "+buf.position());
			System.out.println("\tProtocol: " + buf.getInt());
			System.out.println("*** Read packet *** "+buf.position());
			break;

		default:
			break;
		}
		
		
		ByteBuffer _buf = ByteBuffer.allocate(5);
		_buf.putShort((short) 3);
		_buf.put((byte) 0x00);
		_buf.put((byte) 0x02);
		_buf.put((byte) 0x01);
		_buf.flip();
		try {
			//_client.write(_buf);

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * for(String str : ThreadPoolManager.getInstance().getStats())
		 * System.out.println(str);
		 */
	}

	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret = false;
		try
		{
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			//super.getConnection().close((SendablePacket<L2LoginClient>)null);
			//_client.dropConnection();
			return false;
		}

		if(!ret)
		{
			byte[] dump = new byte[size];
			System.arraycopy(buf.array(), buf.position(), dump, 0, size);
			_log.warning("Wrong checksum from server: " + toString());
			//super.getConnection().close((SendablePacket<L2LoginClient>)null);
			//_client.shutdown();
			dump = null;
		}

		return ret;
	}
	
	public boolean encrypt(ByteBuffer buf, int size){
		final int offset = buf.position();
		try
		{
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		buf.position(offset + size);
		return true;
	}
	ArrayList<PyroClient> pc = new ArrayList<PyroClient>();
	
	public void run() {
		
		int count = 1;
		
		
		
		for (int i = 0; i < count; i++) {
			
		
		InetSocketAddress bind = new InetSocketAddress(HOST, PORT);
		try {
			pc.add( selector.connect(bind));
			pc.get(i).addListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		System.out.println("Send");

	}
}