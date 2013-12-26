package fw.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.interfaces.RSAPublicKey;

import javolution.util.FastList;
import javolution.util.FastMap;
import fw.loginserver.clientpackets.*;
import fw.loginserver.crypt.NewCrypt;
import fw.loginserver.serverpackets.*;
import fw.util.PacketStream;
import fw.util.Printer;

public class LoginConnection extends Thread {
	private String hostDestino;
	private int portaDestino;
	private String login, password;
	Socket sock;
	public FastList<String> listServers = new FastList<String>();
	BufferedInputStream in;
	BufferedOutputStream out;
	NewCrypt crypt;
	//NewCrypt staticCrypt;
	RSAPublicKey _RSApublicKey;
	private int[] _unkGG = new int[4];
	private int[] _loginOk = new int[4];
	private byte[] _blowfishKey = new byte[16];
	int _protocolVersion;
	private int _sessionId;
	int serverNum = 1;
	boolean terminate = false;
	LoginResult loginResult = null;
	byte loginId[] = new byte[8];
	FastMap<String, Host> serverList = null;
	ConnectionEventReceiver connectionEventReceiver;

	public LoginConnection(ConnectionEventReceiver connectionEventReceiver, String hostDestino, int portaDestino,
			int serverNum, String login, String password) {
		this.connectionEventReceiver = connectionEventReceiver;
		this.hostDestino = hostDestino;
		this.portaDestino = portaDestino;
		this.serverNum = serverNum;
		this.login = login;
		this.password = password;
		//this.staticCrypt = new NewCrypt(true);
		this.crypt = new NewCrypt(true);
	}

	public void fireLogin() throws IOException {
		System.out.println("Login Started.");
		sock = new Socket(hostDestino, portaDestino);
		in = new BufferedInputStream(sock.getInputStream());
		out = new BufferedOutputStream(sock.getOutputStream());
		connectionEventReceiver.procConnectionEvent(new Msg(Msg.MSG_TYPE.SUCESS, "LOGIN CONNECTION OK"),
				ENUM_CONECTION_EVENT.EVT_MSG);
	}

	private void processPlayOkPacket(byte data[]) throws IOException {
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);
		LoginResult logRes = new LoginResult();
		logRes.ok = true;
		logRes.login = login;
		logRes.playId1 = buf.getInt();
		logRes.playId2 = buf.getInt();
		logRes.motivo = "PLAY ON SERVER [" + serverNum + "] OK";
		logRes.host = serverList.get(String.valueOf(serverNum));

		buf = ByteBuffer.wrap(loginId);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(0);
		logRes.loginId1 = _loginOk[0];
		logRes.loginId2 = _loginOk[1];

		loginResult = logRes;
	}

	private void processPlayFailPacket(byte data[]) throws IOException {
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);
		LoginResult logRes = new LoginResult();

		byte reason = buf.get();
		switch (reason) {
		case 0x0f:
			logRes.motivo = "PLAY FAIL (TOO MANY PLAYERS IN SERVER)";
			break;
		case 0x01:
			logRes.motivo = "PLAY FAIL (SYSTEM ERROR)";
			break;
		case 0x02:
			logRes.motivo = "PLAY FAIL (USER OR PASSWORD WRONG)";
			break;
		default:
			logRes.motivo = "PLAY FAIL (UNKNOW)";
			break;
		}

		loginResult = logRes;
	}

	private void processLoginFailPacket(byte data[]) throws IOException {
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(1);
		LoginResult logRes = new LoginResult();

		byte reason = buf.get();

		switch (reason) {
		case 0x09:
			logRes.motivo = "LOGIN FAIL (ACCOUNT BANNED)";
			break;
		case 0x07:
			logRes.motivo = "LOGIN FAIL (ACCOUNT IN USE)";
			break;
		case 0x04:
			logRes.motivo = "LOGIN FAIL (ACCESS FAILED)";
			break;
		case 0x03:
			logRes.motivo = "LOGIN FAIL (USER OR PASSWORD IS WRONG)";
			break;
		case 0x02:
			logRes.motivo = "LOGIN FAIL (PASSWORD WRONG)";
			break;
		case 0x01:
			logRes.motivo = "LOGIN FAIL (SYSTEM ERROR)";
			break;
		default:
			logRes.motivo = "LOGIN FAIL (UNKNOW)";
			break;
		}

		loginResult = logRes;
	}

	public void processPacket(byte data[]) throws Exception {
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.position(0);
		byte[] packet,pack;
		byte id = (byte) (buf.get() & 0xFF);
		System.out.println("Read packet id: " + Integer.toHexString(id));
		switch (id) {
		case 0x00:
			System.out.println("Read packet INIT");
			NewCrypt.decXORPass(data);
			Init myInit = new Init(data);
			myInit.runImpl();
			// Set new BF Key
			_blowfishKey = myInit.get_blowfishKey();
			crypt = new NewCrypt(_blowfishKey);
			_protocolVersion = myInit.get_sessionId();
			_RSApublicKey = myInit.get_RSApublicKey();
			_unkGG = myInit.get_unkGG();
			_sessionId = myInit.get_sessionId();
			// Request GameGuard Auth
			AuthGameGuard myGG = new AuthGameGuard(_sessionId, _unkGG[0], _unkGG[1], _unkGG[2], _unkGG[3]);
			byte[] packGG = new byte[myGG.getMessage().length];
			packGG = myGG.getMessage();
			NewCrypt.appendChecksum(packGG);
			packet = crypt.crypt(packGG);
			System.out.print(Printer.printData(data, data.length, "READ PACKET"));
			PacketStream.writePacket(out, packet);
			break;
		case 0x0B:
			System.out.println("Read packet GGAuth");
			GGAuth ggauth = new GGAuth(data);		
			
			RequestAuthLogin ral = new RequestAuthLogin(_RSApublicKey, login, password,ggauth.getResponse());
			pack = new byte[ral.getMessage().length];
			pack = ral.getMessage();
			NewCrypt.appendChecksum(pack);
			packet = crypt.crypt(pack);
			PacketStream.writePacket(out, packet);
			//GGAuth rpacket = new GGAuth(data);			
			//byte[] packGG = new byte[myGG.getMessage().length];
			break;
		case 0x01: // LOGIN_FAIL
			processLoginFailPacket(data);
			break;
		case 0x06: // PLAY_FAIL
			processPlayFailPacket(data);
			break;
		case 0x03:
			//RequestServerListPack
			System.out.println("Read packet LoginOk"); 
			LoginOk loginok = new LoginOk(data);
			_loginOk[0] = loginok.getOk1();
			_loginOk[1] = loginok.getOk2();
			//loginResult.loginId1 = loginok.getOk1();
			//loginResult.loginId2 = loginok.getOk2();
			
			RequestServerList rslist = new RequestServerList(_loginOk[0], _loginOk[1], 4);
			pack = new byte[rslist.getMessage().length];
			pack = rslist.getMessage();
			NewCrypt.appendChecksum(pack);
			packet = crypt.crypt(pack);
			PacketStream.writePacket(out, packet);
			
			break;
		case 0x04:
			System.out.println("Read packet Server list"); 
			ServerList srvlist = new ServerList(data);			
			serverList = new FastMap<String, Host>();
			
			for (int i = 0; i < srvlist.getList().size(); i++) {
				Host host = new Host(srvlist.getList().get(i)._serverId, srvlist.getList().get(i)._ip, srvlist.getList().get(i)._port);
				System.out.println("Server id: "+srvlist.getList().get(i)._serverId+
						" Host: "+srvlist.getList().get(i)._ip+
						" Online: "+srvlist.getList().get(i)._currentPlayers);
				serverList.put(String.valueOf(srvlist.getList().get(i)._serverId), host);
			}
			if (!serverList.containsKey(String.valueOf(serverNum))) {
				connectionEventReceiver.procConnectionEvent(new Msg(Msg.MSG_TYPE.ATENTION, "GAME ENTER WORLD"),
						ENUM_CONECTION_EVENT.EVT_MSG);
				LoginResult logRes = new LoginResult();
				//logRes.loginId1 = loginok.getOk1();
				//logRes.loginId2 = loginok.getOk2();
				logRes.motivo = "PLAY FAIL (SERVER NUMBER [" + serverNum + "] INVALID)";
				loginResult = logRes;
				setTerminate();
				return;
			}
			RequestServerLogin rsl = new RequestServerLogin(_loginOk[0], _loginOk[1], serverNum);
			pack = new byte[rsl.getMessage().length];
			pack = rsl.getMessage();
			NewCrypt.appendChecksum(pack);
			packet = crypt.crypt(pack);
			PacketStream.writePacket(out, packet);
			break;
		case 0x07:
			processPlayOkPacket(data);
			break;
		default:
			System.out.println("Uncknow packet");
			System.out.print(Printer.printData(data, data.length, "READ PACKET"));
			setTerminate();
			break;
		}		

	}

	public void run() {
		byte packetData[];
		byte sessionKeyPacket[] = null;
		PacketStream.setName("Login server");
		try {
			while (!terminate) {
				if (terminate)
					return;

				sessionKeyPacket = PacketStream.readPacket(in);
				if(sessionKeyPacket == null ){ 
					System.out.println("Error on read - disconnected." );
					setTerminate();
					return;			
				}
				packetData = crypt.decrypt(sessionKeyPacket);
				processPacket(packetData);			

				Thread.sleep(10);
			}			

		} catch (IOException e) {
			// NADA
		} catch (Exception e) {
			e.printStackTrace();
		}

		LoginResult logRes = new LoginResult();
		logRes.motivo = "Connection error";
		loginResult = logRes;
	}

	public LoginResult getLoginResult() {
		return loginResult;
	}

	public void setTerminate() {
		this.terminate = true;
		try {
			sock.close();
			in.close();
			out.close();
		} catch (IOException e) {
			// NADA
		}
	}

}