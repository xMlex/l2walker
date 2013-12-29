package fw.game.model;

public final class L2Player extends L2Playable {

	private String _accountName;
	private int _karma, _pkKills, _pvpKills;
	private int _face, _hairStyle, _hairColor;
	private int _recomHave, _recomLeft, _fame,
	_ClassId,clanId;
	
	private boolean _hero = false;
	private int _lvl = 1;
	
	private int _curWeightPenalty = 0;
	private boolean _isSitting;
	private int _sp;
	private long _exp;
	
	public L2Player(int objectId) {
		super(objectId);
	}

	@Override
	public byte getLevel() {
		// TODO Auto-generated method stub
		return (byte)_lvl;
	}
	public final void setLevel(final int lvl)
	{
		_lvl = lvl;
	}
	
	public int getFace()
	{
		return _face;
	}

	public void setFace(int face)
	{
		_face = face;
	}

	public int getHairColor()
	{
		return _hairColor;
	}

	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}

	public int getHairStyle()
	{
		return _hairStyle;
	}

	public void setHairStyle(int hairStyle)
	{
		_hairStyle = hairStyle;
	}
	public int getPkKills()
	{
		return _pkKills;
	}

	public void setPkKills(final int pkKills)
	{
		_pkKills = pkKills;
	}
	public int getRecomHave()
	{
		return _recomHave;
	}

	public void setRecomHave(int value)
	{
		if(value > 255)
			_recomHave = 255;
		else if(value < 0)
			_recomHave = 0;
		else
			_recomHave = value;
	}

	public int getRecomLeft()
	{
		return _recomLeft;
	}

	public void setRecomLeft(final int value)
	{
		_recomLeft = value;
	}
	@Override
	public int getKarma()
	{
		return _karma;
	}
	public void setKarma(int karma)
	{
		if(karma < 0)
			karma = 0;

		if(_karma == karma)
			return;

		_karma = karma;
	}
	
	public int getWeightPenalty()
	{
		return _curWeightPenalty;
	}
	public int getPvpKills()
	{
		return _pvpKills;
	}

	public void setPvpKills(int pvpKills)
	{
		_pvpKills = pvpKills;
	}
	public int getClassId()
	{
		return _ClassId;
	}
	//@Override
	public boolean isSitting()
	{
		return _isSitting;
	}
	public void setSitting(boolean val)
	{
		_isSitting = val;
	}
	public int getClanId() {
		return clanId;
	}

	public void setClanId(int clanId) {
		this.clanId = clanId;
	}

	public void setSp(int sp) {
		_sp = 	sp;	
	}

	public void setExp(long Exp) {		
		_exp = Exp;
	}

}
