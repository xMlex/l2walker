package fw.connection;

import java.nio.ByteBuffer;

public interface GamePackageEventReceiver
{
	public void procGamePackage(byte data[]);
	public void procGamePackage(ByteBuffer buf);
}
