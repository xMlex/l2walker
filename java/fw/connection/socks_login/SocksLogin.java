package fw.connection.socks_login;

import fw.connection.socks.ListenerIntelude;
import fw.util.Printer;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroSelector;
import jawnae.pyronet.PyroServer;
import jawnae.pyronet.addon.PyroRoundrobinSelectorProvider;
import jawnae.pyronet.addon.PyroSelectorProvider;
import jawnae.pyronet.events.PyroClientListener;
import jawnae.pyronet.events.PyroLazyBastardAdapter;
import jawnae.pyronet.traffic.ByteSink;
import jawnae.pyronet.traffic.ByteSinkEndsWith;
import jawnae.pyronet.traffic.ByteSinkLength;
import jawnae.pyronet.traffic.PyroByteSinkFeeder;
import xmlex.config.ConfigSystem;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Created by Maxim on 04.08.14.
 */
public class SocksLogin extends PyroLazyBastardAdapter {

    protected static Logger _log = Logger.getLogger(SocksLogin.class.getName());
    public static final boolean VERBOSE = true;
    public static final boolean SOCKS = false;
    public static final String SOCKS_HOST = "188.126.44.110";
    public static final int SOCKS_PORT = 1080;
    protected static SocksLogin _instance;

    public static void main(String[] args) throws IOException {
        ConfigSystem.load();

        _instance = new SocksLogin();
        PyroServer server = createProxyServer(_instance, "127.0.0.1", 2107);
        int selectorCount = 4;
        // make traffic-selector pool
        PyroSelector[] pool = new PyroSelector[selectorCount];
        for (int i = 0; i < pool.length; i++) {
            pool[i] = new PyroSelector(_instance);
            pool[i].spawnNetworkThread("traffic-thread #" + (i + 1));
        }
        PyroSelectorProvider roundrobin = new PyroRoundrobinSelectorProvider(pool);
        server.installSelectorProvider(roundrobin);
        server.selector().spawnNetworkThread("socks4-LoginHandler");
    }

    @Override
    public void acceptedClient(final PyroClient src) {
        _log.info("accepted-LoginClient: " + src.getRemoteAddress().getAddress() + " using " + Thread.currentThread().getName());
        final SocksRedirectRequest request = new SocksRedirectRequest(src);

        ByteSink header = new ByteSinkLength(8) {
            @Override
            public void onReady(ByteBuffer buffer) {
                if (VERBOSE)
                    System.out.println("socks4.header.onReady:" + buffer.remaining());
                request.version = buffer.get() & 0xFF;
                request.command = buffer.get() & 0xFF;
                request.port = buffer.getShort() & 0xFFFF;
                buffer.get(request.addr);
                if (buffer.hasRemaining())
                    throw new IllegalStateException();
            }
        };

        ByteSink userid = new ByteSinkEndsWith(zero_byte, 16, false) {
            @Override
            public void onReady(ByteBuffer buffer) {
                if (VERBOSE)
                    _log.info("socks4.userid.onReady:"+ buffer.remaining());
                request.userid = new byte[buffer.remaining()];
                buffer.get(request.userid);

                request.handshake(SocksLogin.this);
            }
        };

        PyroByteSinkFeeder feeder = new PyroByteSinkFeeder(src.selector());
        feeder.addByteSink(header);
        feeder.addByteSink(userid);
        src.attach(new SocksAttachment(feeder));
        src.addListener(this);
    }
    @Override
    public void connectedClient(PyroClient dst) {
        _log.info(" => connected-to-target: " + dst);
        // SOCK4 protocol
        ByteBuffer response = ByteBuffer.allocate(8);
        response.put((byte) 0x00);
        response.put((byte) 0x5a); // granted and succeeded! w00t!
        response.clear();

        SocksAttachment attachment = dst.attachment();
        attachment.target.write(response);
    }
    @Override
    public void receivedData(PyroClient client, ByteBuffer data) {
        if (VERBOSE){
            _log.info("receivedData:" + data.remaining()+" Limit: "+data.limit());
            //_log.info("\n"+ Printer.printData());
        }
        SocksAttachment attachment = client.attachment();
        if (attachment.header != null) {
            // we are still reading the SOCKS4 header...
            if (attachment.target == null) {
                // there is no target found, so feed
                // the data into the header handlers
                attachment.header.feed(data);
            } else {
                // we are connected to somebody else
                // whatever is pending in the header handlers should be
                // retrieved
                ByteBuffer pending = attachment.header.shutdown();

                // stop operating in header mode
                attachment.header = null;

                // handle the pending bytes, if any
                if (pending.hasRemaining())
                    this.receivedData(client, pending);

                // handle the bytes that we just received
                this.receivedData(client, data);
            }
        } else {
            if (VERBOSE) {
                // enqueue data just received to the target it was meant for
                String from = client.getRemoteAddress().getAddress()
                        .getHostAddress();
                String to = attachment.target.getRemoteAddress().getAddress()
                        .getHostAddress();
                _log.info("   traffic: [" + from + " => " + to + "]");
            }
            if (!attachment.socksAuthed && SOCKS) {

                if (attachment.src != null) {
                    attachment.socksAuthed = true;
                    _log.info("   data: " + data.remaining());
                    //if(attachment.src != null)
                    ((SocksAttachment) attachment.src.attachment()).init();
                    //else
                    //	((SocksAttachment)attachment.target.attachment()).init();
                } else {
                    attachment.target.writeCopy(data);
                }

            } else {
                //if(SOCKS && data.remaining() != 8)
                attachment.target.writeCopy(data);
            }
        }
    }
    @Override
    public void unconnectableClient(PyroClient dst) {
        _log.info(" => target-unreachable: " + dst);
        // SOCK4 protocol
        ByteBuffer response = ByteBuffer.allocate(8);
        response.put((byte) 0x00);
        response.put((byte) 0x5b); // failed
        response.clear();
        SocksAttachment attachment = dst.attachment();
        attachment.target.write(response);
        attachment.target.shutdown();
    }

    private static final byte[] zero_byte = new byte[]{(byte) 0x00};

    public static PyroServer createProxyServer(SocksLogin socks4, String host,
                                               int port) throws IOException {
        PyroSelector selector = new PyroSelector(socks4);
        InetSocketAddress bind = new InetSocketAddress(
                InetAddress.getByName(host), port);
        PyroServer server = selector.listen(bind);
        server.addListener(socks4);
        _log.info("launched SOCKS4 LoginServer: " + host + ":" + port);
        return server;
    }

    static class SocksAttachment {
        public PyroByteSinkFeeder header;
        public PyroClient target;
        public PyroClient src;

        private LitenerInterludeLogin _listener;
        public boolean socksAuthed = false;

        public int port;
        public byte[] addr = new byte[4];

        public SocksAttachment(PyroByteSinkFeeder header) {
            this.header = header;
            this.target = null;
        }

        public SocksAttachment(PyroClient target) {
            this.header = null;
            this.target = target;
        }

        public SocksAttachment(PyroClient target, PyroClient src) {
            this.header = null;
            this.target = target;
            this.src = src;
        }

        public SocksAttachment init() {
            _listener = new LitenerInterludeLogin();
            _listener.setServer(this.src);
            _listener.setClient(this.target);
            return this;
        }
    }

    static class SocksRedirectRequest {
        private final PyroClient src;

        public int version;
        public int command;
        public int port;
        public byte[] addr = new byte[4];
        public byte[] userid;

        public SocksRedirectRequest(PyroClient src) {
            this.src = src;
        }

        public void handshake(PyroClientListener listener) {
            if (SocksLogin.VERBOSE) {
                System.out.println("   request.version=" + this.version);
                System.out.println("   request.command=" + this.command);
                System.out.println("   request.port=" + this.port);
                System.out.println("   request.addr=" + (this.addr[0] & 0xFF)
                        + "." + (this.addr[1] & 0xFF) + "."
                        + (this.addr[2] & 0xFF) + "." + (this.addr[3] & 0xFF));
                System.out.println("   request.userid="
                        + new String(this.userid));
            }

            if (this.version != 4 || this.command != 1) {
                ByteBuffer response = ByteBuffer.allocate(8);
                response.put((byte) 0x00);
                response.put((byte) 0x5b); // rejected
                response.clear();
                src.write(response);
                src.shutdown();
                return;
            }

            try {
                InetAddress iaddr = InetAddress.getByAddress(this.addr);
                // System.out.println("request.host=" + iaddr.getHostName()); //
                // SLOW!
                if (SOCKS) {
                    //_socks = new Socks4Listener(src.selector(),src,listener,this.addr,this.port);
                    InetSocketAddress connect = new InetSocketAddress(SOCKS_HOST, SOCKS_PORT);
                    PyroClient dst = src.selector().connect(connect);
                    {
                        SocksAttachment srcAtt = src.attachment();
                        srcAtt.target = dst;
                        srcAtt.addr = this.addr;
                        srcAtt.port = this.port;

                        dst.attach(new SocksAttachment(src, dst).init());
                    }
                    //dst.addListener(listener);
                } else {
                    InetSocketAddress connect = new InetSocketAddress(iaddr, this.port);
                    PyroClient dst = src.selector().connect(connect);
                    {
                        SocksAttachment srcAtt = src.attachment();
                        srcAtt.target = dst;

                        dst.attach(new SocksAttachment(src, dst).init());
                    }
                    dst.addListener(listener);
                }
            } catch (UnknownHostException exc) {
                ByteBuffer response = ByteBuffer.allocate(8);
                response.put((byte) 0x00);
                response.put((byte) 0x5b); // failed
                response.clear();
                src.write(response);
                src.shutdown();
            } catch (IOException exc) {
                ByteBuffer response = ByteBuffer.allocate(8);
                response.put((byte) 0x00);
                response.put((byte) 0x5b); // failed
                response.clear();
                src.write(response);
                src.shutdown();
            }
        }
    }
}
