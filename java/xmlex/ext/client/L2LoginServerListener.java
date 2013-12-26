package xmlex.ext.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import xmlex.ext.crypt.LoginCrypt;
import xmlex.ext.crypt.NewCrypt;
import xmlex.ext.crypt.fwCrypt;
import xmlex.ext.net.ProtocolL2;
import xmlex.ext.net.PyroProtocolFeeder;
import xmlex.ext.net.ReceivablePacket;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.events.PyroClientAdapter;
import jext.common.ThreadPoolManager;

public class L2LoginServerListener extends PyroClientAdapter {

	private static Logger _log = Logger.getLogger(L2LoginServerListener.class
			.getName());

	private LoginCrypt _loginCrypt;
	private NewCrypt _staticCrypt;
	private fwCrypt _fwCryptStatic, _fwCrypt;
	private L2BaseClient _client;
	private PyroClient _socket;
	private boolean _firstpacket = true;

	public L2LoginServerListener(L2BaseClient client) {
		_client = client;
		_loginCrypt = new LoginCrypt();
		_staticCrypt = new NewCrypt(true);
		_fwCryptStatic = new fwCrypt(true);
	}

	// @Override
	public void connectedClient(PyroClient client) {
		_socket = client;
		_log.info("connectedClient");
		PyroProtocolFeeder feeder = new PyroProtocolFeeder(client);
		// lets create a packet handler
		ProtocolL2 handler = new ProtocolL2() {
			public void onReady(ByteBuffer buf) {

				System.out.println("buf pos: " + buf.position() + " Size: "
						+ buf.remaining());
				if (!decrypt(buf, buf.remaining())) {
					_log.warning("No decrypt login packet");
					_socket.dropConnection();
					return;
				}
				onPaket(buf);
			}
		};
		// add the packet handler to the
		feeder.addByteSink(handler);
		client.addListener(feeder);
		_log.info("connectedClient ok");
	}

	// @Override
	public void unconnectableClient(PyroClient client) {
		_log.info("unconnectableClient");
	}

	// @Override
	public void droppedClient(PyroClient client, IOException cause) {
		_log.info("droppedClient");
	}

	// @Override
	public void disconnectedClient(PyroClient client) {
		_log.info("disconnectedClient");
	}

	// @Override
	public void receivedData(PyroClient client, ByteBuffer data) {
		_log.info("receivedData");
	}

	// @Override
	public void sentData(PyroClient client, int bytes) {
		_log.info("sentData");
	}

	public void onPaket(ByteBuffer buf) {

		int id = buf.get() & 0xFF;
		ReceivablePacket<L2BaseClient> pkt = null;
		System.out
				.println("\tRecervable packet id: " + Integer.toHexString(id));

		switch (id) {
		case 0x00:
			System.out.println("\tSession id: " + buf.getInt());
			System.out.println("\tProtocol: " + Integer.toHexString(buf.getInt()));
			break;

		default:
			break;
		}
		
		if(pkt != null){
			pkt.setByteBuffer(buf);
			pkt.setClient(_client);		
			ThreadPoolManager.getInstance().executeGeneralPacket(pkt);
		}

	}

	private boolean decrypt(ByteBuffer buf, int size) {
		boolean ret = false;
		try {
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (!ret) {
			// byte[] dump = new byte[size];
			// System.arraycopy(buf.array(), buf.position(), dump, 0, size);
			_log.warning("Wrong checksum from server: " + toString());
			// dump = null;
		}

		return ret;
	}

	private boolean encrypt(ByteBuffer buf, int size) {
		final int offset = buf.position();
		try {
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		buf.position(offset + size);
		return true;
	}

}
