package fw.connection.socks;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;

import jawnae.pyronet.PyroClient;
import jawnae.pyronet.events.PyroClientListener;

public abstract class ISocksListener implements PyroClientListener,Runnable {

    public ArrayList<String> bypass_list = new ArrayList<String>();

	private PyroClient client, server;
	private final ArrayDeque<BaseSendableSocketPacket<?>> _sendListServer = new ArrayDeque<BaseSendableSocketPacket<?>>();
	
	public void addToServer(BaseSendableSocketPacket<?> baseSendableSocketPacket){
		synchronized (_sendListServer) {
			_sendListServer.add(baseSendableSocketPacket);
		}
	}
	
	public void run() {
		synchronized (_sendListServer) {
			if (_sendListServer.size() > 0)
				for (BaseSendableSocketPacket<?> pkt : _sendListServer){
					ByteBuffer buf = pkt.getData();
					onDataWrite(buf);
					int len =buf.remaining() + 2;
                    if(len == 2)
                        continue;
					byte[] lendata = new byte[2];
					lendata[0] = (byte) (len & 0xff);
					lendata[1] = (byte) (len >> 8 & 0xff);
					//System.out.println("Sen packet size: "+len);
					server.write(ByteBuffer.wrap(lendata));
					server.write(buf);
				}
			_sendListServer.clear();	
		}		
	}
	
	public abstract void onDataWrite(ByteBuffer buf);

    public abstract void sendToServer(BaseSendableSocketPacket pkt);

	public void sendToClient(ByteBuffer buf) {
		int len = buf.remaining() + 2;
		byte[] lendata = new byte[2];
		lendata[0] = (byte) (len & 0xff);
		lendata[1] = (byte) (len >> 8 & 0xff);
		// _log.info("Sen packet size: "+len);
		client.write(ByteBuffer.wrap(lendata));
		client.write(buf);
	}

	public void sendToServer(ByteBuffer buf) {
		int len = buf.remaining() + 2;
		byte[] lendata = new byte[2];
		lendata[0] = (byte) (len & 0xff);
		lendata[1] = (byte) (len >> 8 & 0xff);
		// _log.info("Sen packet size: "+len);
		server.write(ByteBuffer.wrap(lendata));
		server.writeCopy(buf);
	}

	public PyroClient getClient() {
		return client;
	}

	public void setClient(PyroClient client) {
		this.client = client;
	}

	public PyroClient getServer() {
		return server;
	}

	public void setServer(PyroClient server) {
		this.server = server;
	}

	public ByteBuffer malloc(int size) {
		return ByteBuffer.wrap(new byte[size]).order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer malloc(byte[] array) {
		ByteBuffer copy = this.malloc(array.length);
		copy.put(array);
		copy.flip();
		return copy;
	}

	public ByteBuffer copy(ByteBuffer buffer) {
		ByteBuffer copy = this.malloc(buffer.remaining());
		copy.put(buffer);
		buffer.position(0/*buffer.position() - copy.remaining()*/);
		copy.flip();
		return copy;
	}

	public abstract void setGameCrypt(byte[] _key);
}
