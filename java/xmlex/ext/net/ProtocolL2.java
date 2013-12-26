package xmlex.ext.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jawnae.pyronet.traffic.ByteSink;

public abstract class ProtocolL2 implements ByteSink {

	private ByteBuffer result;
	private ByteBuffer _sizear;
	private int _headerSize, _curpsize;

	public ProtocolL2(int headerSize, int maxSizePacket) {
		if (headerSize == 0)
			throw new IllegalArgumentException();
		// this.result = ByteBuffer.wrap(new
		// byte[maxSizePacket]).order(ByteOrder.LITTLE_ENDIAN);
		this._sizear = ByteBuffer.wrap(new byte[headerSize]).order(ByteOrder.LITTLE_ENDIAN);
		this._headerSize = headerSize;
		this.reset();
	}

	public ProtocolL2(int headerSize) {
		this(headerSize, 8 * 1024);
	}

	public ProtocolL2() {
		this(2);
	}

	public int feed(byte b) {

		// System.out.println("\tRead: "+Integer.toHexString(b));
		if (_curpsize == -1) {
			_sizear.put(b);
			if (_headerSize == _sizear.position()) {
				_sizear.flip();
				_curpsize = getPacketLength(_sizear.get(0), _sizear.get(1)) - 2;// getShortFromLittleEndianRange(_sizear.array());
				result = ByteBuffer.wrap(new byte[_curpsize]).order(ByteOrder.LITTLE_ENDIAN);
				System.out.println("Read packet size: " + _curpsize);
				return FEED_ACCEPTED;
			}
		} else {

			result.put(b);
			// System.out.println("\tRead: "+Integer.toHexString(b)+" Pos: "+result.position());

			if (result.position() == _curpsize) {
				result.flip();
				this.onReady(result);
				this.reset();
				return FEED_ACCEPTED_LAST;
			}
		}

		return ByteSink.FEED_ACCEPTED;
	}

	public void reset() {
		this._sizear.clear();
		this._curpsize = -1;
	}

	/**
	 * Получает длинну пакета из 2-х первых байт.<BR>
	 * Используется для общения между LS и GS.
	 * 
	 * @param first
	 *            первый байт пакета
	 * @param second
	 *            второй байт пакета
	 * @return длинна пакета
	 */
	public static int getPacketLength(byte first, byte second) {
		int lenght = first & 0xff;
		return lenght |= second << 8 & 0xff00;
	}
}
