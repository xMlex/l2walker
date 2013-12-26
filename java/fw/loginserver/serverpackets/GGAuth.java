package fw.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class GGAuth extends ServerBasePacket {
	private int _response;
	private int _unk1 = 0;
	private int _unk2 = 0;
	private int _unk3 = 0;
	private int _unk4 = 0;

	public GGAuth(byte data[]) {
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); 	//read message lenght
		readC(); 		// (1) message type
		_response = readD(); 
		_unk1 = readD();
		_unk2 = readD();
		_unk3 = readD();
		_unk4 = readD();
	}

	public void write() {
		writeC(0x0b);
		writeD(_response);
		writeD(_unk1);
		writeD(_unk2);
		writeD(_unk3);
		writeD(_unk4);
	}
	public int getResponse(){
		return _response;
	}
}
