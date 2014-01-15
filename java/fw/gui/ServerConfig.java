package fw.gui;

public class ServerConfig
{
	public String name;
	public String hostLogin;
	public int port;
	public int protocol;
	public String gameServers[];
	public String token;
	@Override
	public String toString() {
		return name;
	}
}
