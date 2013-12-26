package fw.game.clientpackets;

import fw.game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Say extends ClientBasePacket
{
	public final static int ALL = 0;
	public final static int SHOUT = 1; //!
	public final static int TELL = 2;
	public final static int PARTY = 3; //#
	public final static int CLAN = 4;  //@
	public final static int GM = 5;
	public final static int PETITION_PLAYER = 6; // used for petition
	public final static int PETITION_GM = 7; //* used for petition
	public final static int TRADE = 8; //+
	public final static int ALLIANCE = 9;
	public final static int ANNOUNCEMENT = 10;
	public final static int PARTYROOM_ALL = 15; //(yellow)
	public final static int PARTYROOM_COMMANDER = 16; //(blue)
	public final static int HERO_VOICE = 17;

	private GameEngine _clientThread;
	private String _text;
	private int _type;
	private String _target;


	public final static String[] chatNames = {
        "ALL",
        "SHOUT",
        "TELL",
        "PARTY",
        "CLAN",
//        "GM   ",
//        "PETITION_PLAYER",
//        "PETITION_GM",
        "TRADE",
        "ALLIANCE",
//        "ANNOUNCEMENT", //10
//        "WILLCRASHCLIENT:)",
//        "FAKEALL?",
//        "FAKEALL?",
//        "FAKEALL?",
//        "PARTYROOM_ALL",
//        "PARTYROOM_COMMANDER",
        "HERO_VOICE"
};

	public final static int[] chatIds =
	{
			0, 1, 2, 3, 4, 8, 9, 17
	};

	public Say(byte[] message, GameEngine clientThread)
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(message);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

		readH();	// msg len
		readC();	// msg type

		_text = readS();
		_type = readD();
		_target = (_type == TELL) ? readS() : null;
	}

	public Say(GameEngine clientThread,String type, String text, String target)
	{
		_clientThread = clientThread;
		_type = ALL;
		_text = text;
		_target = target;

		for (int i = 0; i < chatNames.length; i++)
		{
			if(type.equals(chatNames[i]))
			{
				_type=chatIds[i];
				break;
			}
		}

	}

	public void runImpl()
	{
		_clientThread.sendPacket(getMessage());
	}

	@Override
	protected void writeImpl() {
		writeC(0x38);
		writeS(_text);
		writeD(_type);
		if (_type==TELL) {
			writeS(_target);
		}
	}
}
