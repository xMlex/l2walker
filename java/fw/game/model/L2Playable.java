package fw.game.model;

public abstract class L2Playable extends L2Character{

	private boolean _isSitting = false;
	
	public L2Playable(int objectId) {
		super(objectId);
	}
		
	public boolean isSitting()
	{
		return _isSitting;
	}
	public void setSitting(boolean val)
	{
		_isSitting = val;
	}

}
