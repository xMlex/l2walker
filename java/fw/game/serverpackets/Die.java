package fw.game.serverpackets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import fw.game.GameEngine;

public class Die extends ServerBasePacket
{
	/**
	 * sample
	 * 0x06
	 * 952a1048     objectId
	 * 00000000 00000000 00000000 00000000 00000000 00000000

	 * format  dddddd   rev 377
	 * format  ddddddd   rev 417
	 */
	private GameEngine _clientThread;
    private int _objectId;
    private int _sweepable;
    private int _access;
    private int _gohomeType;

    public Die(GameEngine clientThread, byte data[])
    {
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH();				//read message lenght
		readC();				// (1) message type
		set_objectId(readD());
		set_gohomeType(readD());
		readD();
		readD();
		readD();
		// NOTE: _gohomeType
	    // 6d 00 00 00 00 - to nearest village
	    // 6d 01 00 00 00 - to hide away
	    // 6d 02 00 00 00 - to castle
	    // 6d 03 00 00 00 - to siege HQ
	    // sweepable
	    // 6d 04 00 00 00 - FIXED
		set_sweepable(readD());
		set_access(readD());
    }

    public void runImpl()
    {
    	_clientThread.getVisualInterface().putMessage("Die detected",GameEngine.MSG_SYSTEM_NORMAL,true);
    }

	public void set_objectId(int _objectId) {
		this._objectId = _objectId;
	}

	public int get_objectId() {
		return _objectId;
	}

	public void set_sweepable(int _sweepable) {
		this._sweepable = _sweepable;
	}

	public int get_sweepable() {
		return _sweepable;
	}

	public void set_access(int _access) {
		this._access = _access;
	}

	public int get_access() {
		return _access;
	}

	public void set_gohomeType(int _gohomeType) {
		this._gohomeType = _gohomeType;
	}

	public int get_gohomeType() {
		return _gohomeType;
	}
}