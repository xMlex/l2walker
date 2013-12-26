package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2PartyChar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PartySmallWindowAll extends ServerBasePacket
{
	private GameEngine _clientThread;
	L2PartyChar l2PartyChar;
	L2PartyChar l2PartyChars[];

	public  PartySmallWindowAll(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type


		@SuppressWarnings("unused")
		int objIdPartyLeader = readD();
		@SuppressWarnings("unused")
		int partyType = readD();
		int size = readD();

		l2PartyChars = new L2PartyChar[size];

		for (int i = 0; i < size; i++)
		{
			int objIdPartyChar = readD();

			l2PartyChar = _clientThread.getMaps().getOrCreatePartyChar(objIdPartyChar);

			l2PartyChar.partyCharName = readS();
			l2PartyChar.cp = readD();
			l2PartyChar.cpMax = readD();
			l2PartyChar.hp = readD();
			l2PartyChar.hpMax = readD();
			l2PartyChar.mp = readD();
			l2PartyChar.mpMax = readD();
			l2PartyChar.partyCharLevel = readD();
			l2PartyChar.partyCharClassId = readD();
			readD();// protocol
			readD();// protocol

			l2PartyChars[i]=l2PartyChar;

		}
	}

	public void runImpl()
	{
		_clientThread.addPartyChars(l2PartyChars);
	}
}
