package fw.game.model.instances;

import fw.game.model.L2Character;

public class L2NpcInstance extends L2Character {

	private byte _level = 0;
	
	private double _currentCollisionRadius;
	private double _currentCollisionHeight;
	
	private int _currentLHandId;
	private int _currentRHandId;

	private int _npcId;
	private boolean _IsAttackable = false;
	
	public L2NpcInstance(int objectId) {
		super(objectId);
	}

	@Override
	public byte getLevel() {		
		return _level;
	}

	public int getRightHandItem()
	{
		return _currentRHandId;
	}

	public int getLeftHandItem()
	{
		return _currentLHandId;
	}
	public void setLHandId(int newWeaponId)
	{
		_currentLHandId = newWeaponId;
	}

	public void setRHandId(int newWeaponId)
	{
		_currentRHandId = newWeaponId;
	}

	public double getCollisionHeight()
	{
		return _currentCollisionHeight;
	}

	public void setCollisionHeight(double offset)
	{
		_currentCollisionHeight = offset;
	}

	public double getCollisionRadius()
	{
		return _currentCollisionRadius;
	}

	public void setCollisionRadius(double collisionRadius)
	{
		_currentCollisionRadius = collisionRadius;
	}
	
	@Override
	public int getNpcId()
	{
		return _npcId;
	}
	public void setNpcId(int id)
	{
		_npcId = id;
	}

	public boolean isAttackable() {
		return _IsAttackable;
	}

	public void setIsAttackable(boolean _IsAttackable) {
		this._IsAttackable = _IsAttackable;
	}
}
