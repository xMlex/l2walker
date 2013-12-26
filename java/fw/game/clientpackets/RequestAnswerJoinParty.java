package fw.game.clientpackets;

import fw.game.GameEngine;

public class RequestAnswerJoinParty extends ClientBasePacket
{
	private GameEngine _clientThread;
	private int _response;

	public RequestAnswerJoinParty(byte[] message, GameEngine clientThread)
	{
		this._clientThread = clientThread;
		readH();	// msg len
		readC();	// msg type
		_response = readD();
	}

	public RequestAnswerJoinParty(GameEngine clientThread,int response)
	{
		this._clientThread = clientThread;
		_response = response;
	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x2a);
		writeD(_response);
	}
}
