package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class RequestBypassToServer extends ClientBasePacket
{
	private String _command;
	private GameEngine _clientThread;


	public RequestBypassToServer(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type
		_command = readS();
	}

	public RequestBypassToServer(GameEngine clientThread, String command)
	{
		_clientThread = clientThread;

//		System.out.println(command);
		_command = replace(command,"bypass%20","");
		_command = replace(_command,"-h%20","");
		_command = replace(_command,"blank","");
		System.out.println(_command);
	}


	public void runImpl()
	{

		_clientThread.sendPacket(getMessage());
	}


	@Override
	protected void writeImpl() {
		writeC(0x21);
		writeS(_command);

	}

	static String replace(String str, String pattern, String replace) {
		int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	            result.append(str.substring(s, e));
	            result.append(replace);
	            s = e+pattern.length();
	    }

	    result.append(str.substring(s));
	    return result.toString();
	}
}
