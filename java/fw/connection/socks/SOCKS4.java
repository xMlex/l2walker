/*
 * Created on 24 nov 2009
 */

package fw.connection.socks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import xmlex.config.ConfigSystem;
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

public class SOCKS4 extends PyroLazyBastardAdapter {
    protected static Logger _log = Logger.getLogger(SOCKS4.class.getName());
    public static final boolean VERBOSE = false;

    public static final boolean SOCKS = false;
    public static final String SOCKS_HOST = "70.175.230.124";
    public static final int SOCKS_PORT = 30288;

    private PyroSelector selector;

    public static void main(String[] args) throws IOException {
        ConfigSystem.load();
        args = new String[]{"127.0.0.1", "1081", "4"};
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage:");
            System.out.println("   {hostname} {port} [{threadCount}]");
            System.exit(0);
        }

        // where to listen...?
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // create the SOCKS4 handler and the (NIO) server
        SOCKS4 socks4 = new SOCKS4();
        PyroServer server = createProxyServer(socks4, host, port);
        System.out.println("launched SOCKS4 server: " + server);

        if (args.length == 3) {
            // how many networking threads on the pool...?
            int selectorCount = Integer.parseInt(args[2]);
            if (selectorCount < 1)
                throw new IllegalStateException();

            // make traffic-selector pool
            PyroSelector[] pool = new PyroSelector[selectorCount];
            for (int i = 0; i < pool.length; i++) {
                pool[i] = new PyroSelector(socks4);

                // launch the threads that will handle network I/O (1 NIO
                // selector per thread)
                pool[i].spawnNetworkThread("traffic-thread #" + (i + 1));
            }

            PyroSelectorProvider roundrobin = new PyroRoundrobinSelectorProvider(
                    pool);
            server.installSelectorProvider(roundrobin);
        }

        // pick your favorite
        /*if (Math.random() < 0.5) {
			System.out.println("going to spawn a new thread for network I/O");
			server.selector().spawnNetworkThread("socks4-handler");
		} else {*/
        System.out.println("going to use the current thread for network I/O");
        while (true) {
            server.selector().select();
        }
        //}
    }

    public static PyroServer createProxyServer(SOCKS4 socks4, String host,
                                               int port) throws IOException {
        PyroSelector selector = new PyroSelector(socks4);
        InetSocketAddress bind = new InetSocketAddress(
                InetAddress.getByName(host), port);
        PyroServer server = selector.listen(bind);
        server.addListener(socks4);
        return server;
    }

    // PyroServerListener

    private static final byte[] zero_byte = new byte[]{(byte) 0x00};

    @Override
    public void acceptedClient(final PyroClient src) {
        System.out.println("accepted-client: " + src + " using "
                + Thread.currentThread().getName());

        final SocksRedirectRequest request = new SocksRedirectRequest(src);

        ByteSink header = new ByteSinkLength(8) {
            // @Override
            public void onReady(ByteBuffer buffer) {
                if (VERBOSE)
                    System.out.println("socks4.header.onReady:"
                            + buffer.remaining());
                request.version = buffer.get() & 0xFF;
                request.command = buffer.get() & 0xFF;
                request.port = buffer.getShort() & 0xFFFF;
                buffer.get(request.addr);
                if (buffer.hasRemaining())
                    throw new IllegalStateException();
            }
        };

        ByteSink userid = new ByteSinkEndsWith(zero_byte, 16, false) {
            // @Override
            public void onReady(ByteBuffer buffer) {
                if (VERBOSE)
                    System.out.println("socks4.userid.onReady:"
                            + buffer.remaining());
                request.userid = new byte[buffer.remaining()];
                buffer.get(request.userid);

                request.handshake(SOCKS4.this);
            }
        };

        PyroByteSinkFeeder feeder = new PyroByteSinkFeeder(src.selector());
        feeder.addByteSink(header);
        feeder.addByteSink(userid);

        src.attach(new SocksAttachment(feeder));

        src.addListener(this);
    }

    // PyroClientListener

    @Override
    public void connectedClient(PyroClient dst) {
        System.out.println(" => connected-to-target: " + dst);

        if (SOCKS) {
            System.out.println("SOCKS");

            byte[] addr = ((SocksAttachment) ((SocksAttachment) dst.attachment()).target.attachment()).addr;
            short port = (short) ((SocksAttachment) ((SocksAttachment) dst.attachment()).target.attachment()).port;

            InetAddress iaddr = null;
            try {
                iaddr = InetAddress.getByAddress(addr);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            System.out.println("request.host=" + iaddr.getHostName() + " port: " + port);

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
        }else{
        // SOCK4 protocol
        ByteBuffer response = ByteBuffer.allocate(8);
        response.put((byte) 0x00);
        response.put((byte) 0x5a); // granted and succeeded! w00t!
        response.clear();

        SocksAttachment attachment = dst.attachment();
        attachment.target.write(response);
        }
    }

    @Override
    public void unconnectableClient(PyroClient dst) {
        System.out.println(" => target-unreachable: " + dst);

        // SOCK4 protocol
        ByteBuffer response = ByteBuffer.allocate(8);
        response.put((byte) 0x00);
        response.put((byte) 0x5b); // failed
        response.clear();

        SocksAttachment attachment = dst.attachment();
        attachment.target.write(response);
        attachment.target.shutdown();
    }

    //

    @Override
    public void receivedData(PyroClient client, ByteBuffer data) {
        if (VERBOSE)
            System.out.println("receivedData:" + data.remaining());

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
                System.out.println("   traffic: [" + from + " => " + to + "]");
            }
            if (!attachment.socksAuthed && SOCKS) {

                if (attachment.src != null) {
                    attachment.socksAuthed = true;
                    System.out.println("   data: " + data.remaining());
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

    //

    @Override
    public void droppedClient(PyroClient client, IOException cause) {
        if (VERBOSE) {
            System.out.println("droppedClient=" + client);
            if (cause != null)
                cause.printStackTrace(System.out);
        }

        SocksAttachment attachment = client.attachment();

        if (attachment != null && attachment.target != null
                && !attachment.target.isDisconnected()) {
            attachment.target.shutdown();
            attachment.target.attach(null);
            client.attach(null);
        }
    }

    @Override
    public void disconnectedClient(PyroClient client) {
        if (VERBOSE)
            System.out.println("disconnectedClient=" + client);

        SocksAttachment attachment = client.attachment();

        if (attachment != null && attachment.target != null
                && !attachment.target.isDisconnected()) {
            attachment.target.shutdown();
            attachment.target.attach(null);
            client.attach(null);
        }
    }

    public PyroSelector getSelector() {
        return selector;
    }

    public void setSelector(PyroSelector selector) {
        this.selector = selector;
    }

    public static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static class SocksAttachment {
        public PyroByteSinkFeeder header;
        public PyroClient target;
        public PyroClient src;

        private ListenerIntelude _listener;
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
            _listener = new ListenerIntelude();
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
            if (SOCKS4.VERBOSE) {
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
