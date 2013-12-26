package fw.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import fw.connection.game.*;
import fw.extensions.GameCryptHB;
import fw.util.*;

public class GameConnection extends Thread {
	private byte[] cryptkey = { (byte) 0x94, (byte) 0x35, (byte) 0x00,
			(byte) 0x00, (byte) 0xa1, (byte) 0x6c, (byte) 0x54, (byte) 0x87 };

	public LoginResult loginResult = null;
	Socket sock;
	BufferedInputStream in;
	BufferedOutputStream out;
	boolean terminate = false;
	public int clientProtocolVersion = 152;
	int charNum = 1;
	ConnectionEventReceiver connectionEventReceiver;
	GamePackageEventReceiver gamePackageEventReceiver;
	GameCryptHB _Crypt = new GameCryptHB();

	public GameConnection(GamePackageEventReceiver gamePackageEventReceiver,
			ConnectionEventReceiver connectionEventReceiver,
			LoginResult loginResult, int clientProtocolVersion, int charNum) {
		this.gamePackageEventReceiver = gamePackageEventReceiver;
		this.connectionEventReceiver = connectionEventReceiver;
		this.loginResult = loginResult;
		if(loginResult == null) System.out.println("loginResult == null");
		this.clientProtocolVersion = clientProtocolVersion;
		this.charNum = charNum;
	}

	public void fireGame() throws IOException {
		sock = new Socket(loginResult.host.Addr, loginResult.host.port);
		in = new BufferedInputStream(sock.getInputStream());
		out = new BufferedOutputStream(sock.getOutputStream());
		connectionEventReceiver.procConnectionEvent(new Msg(
				Msg.MSG_TYPE.SUCESS, "GAME CONNECTION OK"),
				ENUM_CONECTION_EVENT.EVT_MSG);
	}


	@Override
	public void run() {
		byte packetData[];
		PacketStream.setName("Game server");
		try {

			if (terminate)return;

			GameSendablePacket _packet = null;
			_packet = new ProtocolVersion();
			_packet.setClient(this);
			packetData = _packet.getBytes();
			PacketStream.writePacket(out, packetData);
			
			packetData = PacketStream.readPacket(in);
			if (packetData == null) {
				System.out.println("Unknow error in protocolVersionPack");
				setTerminate();
				return;
			}				
			if(packetData[1] != 0x01){
				System.out.println("Error: wrong protocol version");
				setTerminate();
				return;
			}
			
			System.arraycopy(packetData, 2, cryptkey, 0, 8);
			_Crypt.setClientKey(cryptkey);
			//outCrypt.setClientKey(cryptkey);			
			
			sendPacket(new GameAuthLogin());			
			
			while (!terminate && (packetData = readPacket()) != null) {
				System.out.println("Read encrypt: "+packetData.length);
				gamePackageEventReceiver.procGamePackage(packetData);
			}

			setTerminate();
		} catch (IOException e) {
			setTerminate();
		} catch (Exception e) {
			setTerminate();
			e.printStackTrace();
		}
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

	public boolean isConnected() {
		return sock.isConnected() && !terminate;
	}

	/**
	 * Read encrypted packet
	 * 
	 * @return
	 * @throws IOException
	 */

	protected void writeS(ByteBuffer buf, String text) {
		try {
			if (text != null) {
				buf.put(text.getBytes("UTF-16LE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		buf.put((byte) 0x00);
		buf.put((byte) 0x00);
	}

	public final String readS(ByteBuffer buf) {
		StringBuilder sb = new StringBuilder();
		char ch;
		try {
			while ((ch = buf.getChar()) != 0)
				sb.append(ch);
		} catch (Exception e) {
		}
		return sb.toString();
	}

	public byte[] readPacket() throws IOException {
		byte packetData[] = PacketStream.readPacket(in);
		if (packetData == null)
			return null;
		_Crypt.decrypt(packetData,0,packetData.length);
		return packetData;
	}
	public void sendPacket(GameSendablePacket _pkt) throws IOException{
		_pkt.setClient(this);
		byte[] data = _pkt.getBytes();
		System.out.print(Printer.printData(data,data.length,"WRITE PACKET"));
		_Crypt.encrypt(data,0,data.length);
		PacketStream.writePacket(out, data);
	}
	public void sendPacket(byte data[]) throws IOException {
		// System.out.println(Printer.printData(data,data.length,Packets.getClientMessageType(data[0],
		// data[1])));
		if (out == null)
			return;

		_Crypt.encrypt(data,0,data.length);
		PacketStream.writePacket(out, data);
	}

}