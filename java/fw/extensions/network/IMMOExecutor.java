package fw.extensions.network;


@SuppressWarnings("rawtypes")
public interface IMMOExecutor<T extends MMOClient>
{
	public void execute(ReceivablePacket<T> packet);
}