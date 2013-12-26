package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2PartyChar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PartySmallWindowDelete extends ServerBasePacket
{
	private GameEngine _clientThread;
	private int _objIdPartyChar;
	@SuppressWarnings("unused")
	private String _partyCharName;

	public PartySmallWindowDelete(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type
		_objIdPartyChar = readD();
		_partyCharName = readS();

	}

	public void runImpl()
	{
		L2PartyChar l2PartyChar=_clientThread.getMaps().getPartyChar(_objIdPartyChar);

		 if(l2PartyChar!=null)
		 {
			 _clientThread.getMaps().removePartyChar(_objIdPartyChar);
			 _clientThread.deletePartyChar(l2PartyChar);
		 }
	}
}
