package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.game.L2PartyChar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PartySmallWindowAdd extends ServerBasePacket
{
	private GameEngine _clientThread;
	private String _partyCharName;
	@SuppressWarnings("unused")
	private int _objId, protocol, _objIdPartyChar, _cp, _cpMax, _hp, _hpMax, _mp, _mpMax, _partyCharLevel, _partyCharClassId;

	public PartySmallWindowAdd(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();				//read message lenght
		readC();				// (1) message type

		_objId = readD();
		protocol = readD();
		_objIdPartyChar = readD();
		_partyCharName = readS();
		_cp = readD();
		_cpMax = readD();
		_hp = readD();
		_hpMax = readD();
		_mp = readD();
		_mpMax = readD();
		_partyCharLevel = readD();
		_partyCharClassId = readD();
	}

	public void runImpl()
	{
		L2PartyChar l2PartyChar=_clientThread.getMaps().getOrCreatePartyChar(_objIdPartyChar);

		l2PartyChar.partyCharName = _partyCharName;
		l2PartyChar.cp = _cp;
		l2PartyChar.cpMax = _cpMax;
		l2PartyChar.hp = _hp;
		l2PartyChar.hpMax = _hpMax;
		l2PartyChar.mp = _mp;
		l2PartyChar.mpMax = _mpMax;
		l2PartyChar.partyCharLevel = _partyCharLevel;
		l2PartyChar.partyCharClassId = _partyCharClassId;

		_clientThread.addPartyChars(new L2PartyChar[]{l2PartyChar});
	}
}
