package fw.loginserver.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import javolution.util.FastList;

/**
 * ServerList
 * Format: cc [cddcchhcdc]
 *
 * c: server list size (number of servers)
 * c: ?
 * [ (repeat for each servers)
 * c: server id (ignored by client?)
 * d: server ip
 * d: server port
 * c: age limit (used by client?)
 * c: pvp or not (used by client?)
 * h: current number of players
 * h: max number of players
 * c: 0 if server is down
 * d: 2nd bit: clock
 *    3rd bit: wont dsiplay server name
 *    4th bit: test server (used by client?)
 * c: 0 if you dont want to display brackets in front of sever name
 * ]
 *
 * Server will be considered as Good when the number of  online players
 * is less than half the maximum. as Normal between half and 4/5
 * and Full when there's more than 4/5 of the maximum number of players
 */
public final class ServerList extends ServerBasePacket
{
	private List<ServerData> _servers;
	private int _lastServer;
	private int _servercount;

	public class ServerData
	{
		public String _ip = "127.0.0.99";
		public int _port = 7777;
		public boolean _pvp = false;
		public int _currentPlayers = -1;
		public int _maxPlayers = -1;
		public boolean _testServer = false;
		public boolean _brackets = false;
		public boolean _clock = false;
		public int _status = -1;
		public int _serverId = -1;
		
	}

	public ServerList(byte data[])
	{
		_servers = new FastList<ServerData>();
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); 	//read message lenght
		readC(); 		// (1) message type
		_servercount = readC();
		readC();
		for (int i = 0; i < _servercount; i++) {
			ServerData sd = new ServerData();
			sd._serverId = readC();
			sd._ip = readC()+"."+readC()+"."+readC()+"."+readC();
			sd._port = readD();
			readC();//age
			readC();//is pvp
			sd._currentPlayers = readH();
			sd._maxPlayers = readH();	
			sd._status = readC();
			readD();
			sd._brackets =(readC() == 0x01) ? true :false;
			_servers.add(sd);
		}
	}
	public List<ServerData> getList(){
		return _servers;
	}

	

	public void write()
	{
		writeC(0x04);
		writeC(_servers.size());
		writeC(_lastServer);
		for (ServerData server : _servers)
		{
			writeC(server._serverId); // server id

			try
			{
				InetAddress i4 = InetAddress.getByName(server._ip);
				byte[] raw = i4.getAddress();
				writeC(raw[0] & 0xff);
				writeC(raw[1] & 0xff);
				writeC(raw[2] & 0xff);
				writeC(raw[3] & 0xff);
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
				writeC(127);
				writeC(0);
				writeC(0);
				writeC(1);
			}

			writeD(server._port);
			writeC(0x00); // age limit
			writeC(server._pvp ? 0x01 : 0x00);
			writeH(server._currentPlayers);
			writeH(server._maxPlayers);
			writeC(server._status);//0x00 - down, 0x01 - up
			int bits = 0;
			if (server._testServer)
			{
				bits |= 0x04;
			}
			if (server._clock)
			{
				bits |= 0x02;
			}
			writeD(bits);
			writeC(server._brackets ? 0x01 : 0x00);
		}
	}
}
