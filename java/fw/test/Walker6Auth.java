package fw.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.PyroServer;
import jawnae.pyronet.events.PyroClientAdapter;
import jawnae.pyronet.events.PyroServerListener;
import jext.util.Util;

public class Walker6Auth {
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 6007;

	public static void main(String[] args) throws IOException {
		PyroSelector selector = new PyroSelector();
		PyroServer server = selector.listen(new InetSocketAddress(HOST, PORT));
		System.out.println("listening: " + server);

		server.addListener(new PyroServerListener() {
			@Override
			public void acceptedClient(PyroClient client) {
				System.out.println("accepted-client: " + client);
				
				client.addListener(new PyroClientAdapter(){
					@Override
					public void receivedData(PyroClient client, ByteBuffer buffer) {
						byte[] data = new byte[buffer.remaining()];
						buffer.get(data);
						System.out.println("received:\n"+Util.printData(data));
						try {
							client.setTimeout(1000);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				// echoBytesForTwoSeconds(client);

			}
		});

		while (true) {
			selector.select(100);
		}
	}
	
	public class Auth6 extends PyroClientAdapter{
		@Override
		public void receivedData(PyroClient client, ByteBuffer buffer) {
			System.out.println("received:\n"+Util.printData(buffer.array(),buffer.remaining()));
		}
	}

	static void echoBytesForTwoSeconds(PyroClient client) {
		try {
			client.setTimeout(2 * 1000);
		} catch (IOException exc) {
			exc.printStackTrace();
			return;
		}

		client.addListener(new PyroClientAdapter() {
			@Override
			public void receivedData(PyroClient client, ByteBuffer buffer) {
				ByteBuffer echo = buffer.slice();

				// convert data to text
				byte[] data = new byte[buffer.remaining()];
				buffer.get(data);
				String text = new String(data);

				// dump to console
				System.out.println("received \"" + text + "\" from " + client);

				client.write(echo);
			}

			@Override
			public void disconnectedClient(PyroClient client) {
				System.out.println("disconnected");
			}
		});
	}
}
