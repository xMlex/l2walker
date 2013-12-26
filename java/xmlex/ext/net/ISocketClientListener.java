package xmlex.ext.net;

public interface ISocketClientListener {

	public abstract void onConnected();
	public abstract void onDisconnected();
	public abstract void onData(byte[] data);
}
