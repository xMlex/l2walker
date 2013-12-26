package fw.extensions.network;

import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.RunnableScheduledFuture;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MMOConnection<T extends MMOClient>
{

	//private final SelectorThread<T> _selectorThread;
	private T _client;

	private ISocket _socket;
	private WritableByteChannel _writableByteChannel;
	private ReadableByteChannel _readableByteChannel;

	private final ArrayDeque<SendablePacket<T>> _sendQueue = new ArrayDeque<SendablePacket<T>>();
	private SelectionKey _selectionKey;
	private RunnableScheduledFuture<ScheduleInterest> interest;

	private ByteBuffer _readBuffer, _primaryWriteBuffer, _secondaryWriteBuffer;

	private boolean _pendingClose;
	private long lastWrite = System.currentTimeMillis();
	private boolean buffersReleased = false, buffersNulled = false; //TODO remove debug

	public MMOConnection(/*SelectorThread<T> selectorThread, */ISocket socket, SelectionKey key)
	{
		//_selectorThread = selectorThread;
		setSocket(socket);
		setWritableByteChannel(socket.getWritableByteChannel());
		setReadableByteChannel(socket.getReadableByteChannel());
		setSelectionKey(key);
	}

	/*public MMOConnection(SelectorThread<T> selectorThread)
	{
		_selectorThread = selectorThread;
	}*/

	public boolean isTimeToWrite()
	{
		return true;
	}

	protected void setClient(T client)
	{
		_client = client;
	}

	public T getClient()
	{
		return _client;
	}

	@SuppressWarnings("rawtypes")
	public void sendPacket(SendablePacket... args)
	{
		if(!_pendingClose && _selectionKey != null && args != null && args.length > 0)
			try
			{
				synchronized (_sendQueue)
				{
					for(SendablePacket sp : args)
						if(sp != null)
							_sendQueue.add(sp);
				}
				if(!useInterestScheduler())
					_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
			}
			catch(CancelledKeyException e)
			{
				// ignore
			}
	}

	@SuppressWarnings("rawtypes")
	public void sendPackets(Collection<SendablePacket> args)
	{
		if(!_pendingClose && _selectionKey != null && args != null && !args.isEmpty())
			try
			{
				synchronized (_sendQueue)
				{
					for(SendablePacket sp : args)
						_sendQueue.add(sp);
				}
				if(!useInterestScheduler())
					_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
			}
			catch(CancelledKeyException e)
			{
				// ignore
			}
	}

	private class ScheduleInterest implements Runnable
	{
		@Override
		public void run()
		{
			if(useInterestScheduler() && !_sendQueue.isEmpty())
				_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
		}
	}

	/*protected SelectorThread<T> getSelectorThread()
	{
		return _selectorThread;
	}*/

	protected void setSelectionKey(SelectionKey key)
	{
		_selectionKey = key;
	}

	protected SelectionKey getSelectionKey()
	{
		return _selectionKey;
	}

	/**
	 * Немедленно включает Read Interest
	 */
	protected void enableReadInterest()
	{
		try
		{
			if(_selectionKey != null)
				_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_READ);
		}
		catch(CancelledKeyException e)
		{
			// ignore
		}
	}

	/**
	 * Немедленно выключает Read Interest
	 */
	protected void disableReadInterest()
	{
		try
		{
			if(_selectionKey != null)
				_selectionKey.interestOps(_selectionKey.interestOps() & ~SelectionKey.OP_READ);
		}
		catch(CancelledKeyException e)
		{
			// ignore
		}
	}

	/**
	 * Немедленно включает Write Interest
	 */
	protected void enableWriteInterest()
	{
		try
		{
			if(_selectionKey != null)
				_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
		}
		catch(CancelledKeyException e)
		{
			// ignore
		}
	}

	/**
	 * Немедленно выключает Write Interest
	 */
	protected void disableWriteInterest()
	{
		try
		{
			if(_selectionKey != null)
				_selectionKey.interestOps(_selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
		}
		catch(CancelledKeyException e)
		{
			// ignore
		}
	}

	protected void setSocket(ISocket socket)
	{
		_socket = socket;
	}

	/**
	 * @return the socket
	 */
	public ISocket getSocket()
	{
		return _socket;
	}

	protected void setWritableByteChannel(WritableByteChannel wbc)
	{
		_writableByteChannel = wbc;
	}

	public WritableByteChannel getWritableChannel()
	{
		return _writableByteChannel;
	}

	protected void setReadableByteChannel(ReadableByteChannel rbc)
	{
		_readableByteChannel = rbc;
	}

	public ReadableByteChannel getReadableByteChannel()
	{
		return _readableByteChannel;
	}

	protected ArrayDeque<SendablePacket<T>> getSendQueue()
	{
		return _sendQueue;
	}

	/*protected void createWriteBuffer(ByteBuffer buf)
	{
		if(_primaryWriteBuffer == null)
		{
			_primaryWriteBuffer = getSelectorThread().getPooledBuffer();
			_primaryWriteBuffer.put(buf);
		}
		else
		{
			ByteBuffer temp = getSelectorThread().getPooledBuffer();
			temp.put(buf);

			int remaining = temp.remaining();
			_primaryWriteBuffer.flip();
			int limit = _primaryWriteBuffer.limit();

			if(remaining >= _primaryWriteBuffer.remaining())
			{
				temp.put(_primaryWriteBuffer);
				getSelectorThread().recycleBuffer(_primaryWriteBuffer);
				_primaryWriteBuffer = temp;
			}
			else
			{
				_primaryWriteBuffer.limit(remaining);
				temp.put(_primaryWriteBuffer);
				_primaryWriteBuffer.limit(limit);
				_primaryWriteBuffer.compact();
				_secondaryWriteBuffer = _primaryWriteBuffer;
				_primaryWriteBuffer = temp;
			}
		}
	}

	protected boolean hasPendingWriteBuffer()
	{
		return _primaryWriteBuffer != null;
	}

	protected void movePendingWriteBufferTo(ByteBuffer dest)
	{
		_primaryWriteBuffer.flip();
		dest.put(_primaryWriteBuffer);
		getSelectorThread().recycleBuffer(_primaryWriteBuffer);
		_primaryWriteBuffer = _secondaryWriteBuffer;
		_secondaryWriteBuffer = null;
	}

	protected void setReadBuffer(ByteBuffer buf)
	{
		if(buf == null)
			buffersNulled = true;
		_readBuffer = buf;
	}

	public ByteBuffer getReadBuffer()
	{
		return _readBuffer;
	}*/

	public boolean isClosed()
	{
		return _pendingClose;
	}

	protected void closeNow(boolean error)
	{
		boolean selectorThreadClose = false;
		synchronized (_sendQueue)
		{
			if(!isClosed())
			{
				_pendingClose = true;
				_sendQueue.clear();
				if(!error)
					disableWriteInterest();
				/*if(_selectorThread != null)
					selectorThreadClose = true;*/
			}
		}

		/*if(selectorThreadClose && _selectorThread != null)
			_selectorThread.closeConnection(this);*/

		//cancelInterest();
	}

	public void close(SendablePacket<T> sp)
	{
		synchronized (_sendQueue)
		{
			if(isClosed())
				return;
			_sendQueue.clear();
		}
		if(!isClosed() && _selectionKey.isValid())
		{
			sendPacket(sp);
			if(useInterestScheduler() && !_sendQueue.isEmpty())
				_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
		}
		synchronized (_sendQueue)
		{
			_pendingClose = true;
			/*if(_selectorThread != null)
				_selectorThread.closeConnection(this);*/
			//cancelInterest();
		}
	}

	protected void closeLater()
	{
		synchronized (_sendQueue)
		{
			if(!isClosed())
			{
				_pendingClose = true;
				/*if(_selectorThread != null)
					_selectorThread.closeConnection(this);*/
				//cancelInterest();
			}
		}
	}

	protected void releaseBuffers()
	{
		if(_primaryWriteBuffer != null)
		{
			//getSelectorThread().recycleBuffer(_primaryWriteBuffer);
			_primaryWriteBuffer = null;
			if(_secondaryWriteBuffer != null)
			{
				//getSelectorThread().recycleBuffer(_secondaryWriteBuffer);
				_secondaryWriteBuffer = null;
			}
		}
		if(_readBuffer != null)
		{
			//getSelectorThread().recycleBuffer(_readBuffer);
			_readBuffer = null;
		}
		buffersReleased = true;
	}

	protected void onDisconnection()
	{
		getClient().onDisconnection();
		//cancelInterest();
	}

	protected void onForcedDisconnection()
	{
		getClient().onForcedDisconnection();
		//cancelInterest();
	}


	protected boolean useInterestScheduler()
	{
		return false; //Config.INTEREST_ALT && _client != null && _client instanceof L2GameClient;
	}

	@Override
	public String toString()
	{
		return "MMOConnection: client=" + getClient() + "; buffersReleased=" + buffersReleased + "; buffersNulled=" + buffersNulled;
	}
}