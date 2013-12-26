package fw.game.serverpackets;

import fw.game.ClanInfo;
import fw.game.GameEngine;
import fw.game.PlayerChar;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

public class PledgeInfo extends ServerBasePacket
{
	private GameEngine _clientThread;
	public int _ClanId;
	public String _ClanName;
	public String _AllyName;

	public PledgeInfo(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();	// msg len
		readC();	// msg type

		_ClanId = readD();
		_ClanName = readS();
		_AllyName = readS();
	}

	public void runImpl()
	{
		ClanInfo clanInfo=_clientThread.getMaps().getOrCreateClanInfo(_ClanId);
		clanInfo.clanName=_ClanName;
		clanInfo.allyName=_AllyName;
		clanInfo.initialized = true;

		for (Iterator<?> iter = clanInfo.listWaitForClanInfo.iterator(); iter.hasNext();)
		{
			PlayerChar playerChar = (PlayerChar) iter.next();
			_clientThread.getVisualInterface().procPlayerCharClanInfo(playerChar);
		}

		clanInfo.listWaitForClanInfo.clear();
	}
}
