package fw.walker;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.PyroServer;
import jawnae.pyronet.events.PyroClientAdapter;
import jawnae.pyronet.events.PyroServerAdapter;
import jawnae.pyronet.events.PyroServerListener;
import xmlex.config.ConfigSystem;

/**
 * Created by Maxim on 05.08.14.
 */
public class WalkerServer implements PyroServerListener{
    public static final String HOST = "127.0.0.1";
    public static final int    PORT = 2108;

    public static void main(String[] args) throws IOException
    {
        ConfigSystem.load();
        /*PyroSelector selector = new PyroSelector();
        PyroServer server = selector.listen(new InetSocketAddress(HOST, PORT));
        System.out.println("listening: " + server);
        server.addListener(new WalkerServer());
        selector.spawnNetworkThread("main-walker-server");*/

        SOCKS4Logger.main();
    }

    @Override
    public void acceptedClient(PyroClient pyroClient)
    {
        System.out.println("accepted-client.");
        WalkerClientAdapter _l = new WalkerClientAdapter();
        pyroClient.addListener(_l);
        _l.connectedClient(pyroClient);
    }
}
