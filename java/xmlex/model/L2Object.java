package xmlex.model;

public abstract class L2Object {
	
	protected long _reflection = Long.MIN_VALUE;
	
	/** Object identifier */
	protected int _objectId;
	
	/** Object location : Used for items/chars that are seen in the world */
	private int _x;
	private int _y;
	private int _z;
	
	
	public int getX()
	{
		return _x;
	}

	public int getY()
	{
		return _y;
	}

	public int getZ()
	{
		return _z;
	}
	
	public String getName()
	{
		return getClass().getSimpleName() + ":" + _objectId;
	}
}
