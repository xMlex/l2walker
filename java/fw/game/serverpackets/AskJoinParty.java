package fw.game.serverpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AskJoinParty extends ServerBasePacket
{
	private String _requestorName;
	private int _itemDistribution;
	private GameEngine _clientThread;

	public AskJoinParty(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;

		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type
		_requestorName=readS();
		_itemDistribution=readD();
	}

	public void runImpl()
	{
		String partyType=null;

		if(_itemDistribution == 0)
			partyType = "Finders keepers";
		else if(_itemDistribution == 1)
			partyType = "Random";
		else if(_itemDistribution == 2)
			partyType = "Random including spoil";
		else if(_itemDistribution == 3)
			partyType = "By turn";
		else if(_itemDistribution == 4)
			partyType = "By turn including spoil";


		_clientThread.getVisualInterface().requestPartyDialog(_requestorName, partyType);
	}

}
