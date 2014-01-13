package fw.extensions.geodata;

import java.util.concurrent.locks.ReentrantLock;

import fw.extensions.util.GArray;
import fw.game.model.L2Object;

/*import l2p.Config;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.DeleteObject;
import l2p.gameserver.serverpackets.L2GameServerPacket;
import l2p.gameserver.tables.SpawnTable;
import l2p.gameserver.tables.TerritoryTable;
import l2p.util.GArray;
import l2p.util.Location;*/


/**
 * @author Diamond
 * @Date: 15/5/2007
 * @Time: 10:06:34
 */
public final class L2WorldRegion
{
	private L2Object[] _objects = null;
	private GArray<L2Territory> territories = null;
	private int tileX, tileY, tileZ, _objectsSize = 0, _playersSize = 0;
	private boolean _small = false;
	private boolean _spawned = false;
	private final ReentrantLock objects_lock = new ReentrantLock();
	private final ReentrantLock spawn_lock = new ReentrantLock();
	private final ReentrantLock territories_lock = new ReentrantLock();

	public L2WorldRegion(int pTileX, int pTileY, int pTileZ, boolean small)
	{
		tileX = pTileX;
		tileY = pTileY;
		tileZ = pTileZ;
		_small = small;
	}


	public L2Object[] getObjects()
	{
		objects_lock.lock();
		try
		{
			if(_objects == null)
			{
				_objects = new L2Object[50];
				_objectsSize = 0;
				_playersSize = 0;
			}
			return _objects;
		}
		finally
		{
			objects_lock.unlock();
		}
	}

	public void addObject(L2Object obj)
	{
		if(obj == null)
			return;

		objects_lock.lock();
		try
		{
			if(_objects == null)
			{
				_objects = new L2Object[50];
				_objectsSize = 0;
			}
			else if(_objectsSize >= _objects.length)
			{
				L2Object[] temp = new L2Object[_objects.length * 2];
				for(int i = 0; i < _objectsSize; i++)
					temp[i] = _objects[i];
				_objects = temp;
			}

			_objects[_objectsSize] = obj;
			_objectsSize++;

			if(obj.isPlayer())
			{
				_playersSize++;				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			objects_lock.unlock();
		}

		//if(obj.isNpc() && obj.getAI() instanceof DefaultAI && obj.getAI().isGlobalAI() && !obj.getAI().isActive())
		//	obj.getAI().startAITask();
	}

	public void removeObject(L2Object obj, boolean move)
	{
		if(obj == null)
			return;

		objects_lock.lock();
		try
		{
			if(_objects == null)
			{
				_objectsSize = 0;
				_playersSize = 0;
				return;
			}

			if(_objectsSize > 1)
			{
				int k = -1;
				for(int i = 0; i < _objectsSize; i++)
					if(_objects[i] == obj)
					{
						k = i;
						break;
					}
				if(k > -1)
				{
					_objects[k] = _objects[_objectsSize - 1];
					_objects[_objectsSize - 1] = null;
					_objectsSize--;
				}
			}
			else if(_objectsSize == 1 && _objects[0] == obj)
			{
				_objects[0] = null;
				_objects = null;
				_objectsSize = 0;
				_playersSize = 0;
			}

			if(obj.isPlayer())
			{
				_playersSize--;
				if(_playersSize <= 0)
					_playersSize = 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			objects_lock.unlock();
		}

		//if(!move && obj.isNpc() && obj.getAI() instanceof DefaultAI && !obj.getAI().isGlobalAI())
		//	obj.getAI().stopAITask();
	}

	public boolean areNeighborsEmpty()
	{
		if(!isEmpty())
			return false;
		for(L2WorldRegion neighbor : getNeighbors())
			if(!neighbor.isEmpty())
				return false;
		return true;
	}

	public GArray<L2WorldRegion> getNeighbors()
	{
		return L2World.getNeighbors(tileX, tileY, tileZ, _small);
	}

	public int getObjectsSize()
	{
		return _objectsSize;
	}

	public int getPlayersSize()
	{
		return _playersSize;
	}

	public boolean isEmpty()
	{
		return _playersSize <= 0;
	}

	public boolean isNull()
	{
		return _objectsSize <= 0;
	}

	public String getName()
	{
		return "(" + tileX + ", " + tileY + ", " + tileZ + ")";
	}

	public void addTerritory(L2Territory territory)
	{
		territories_lock.lock();
		try
		{
			if(territories == null)
				territories = new GArray<L2Territory>(5);
			if(!territories.contains(territory))
				territories.add(territory);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			territories_lock.unlock();
		}
	}

	public void removeTerritory(L2Territory territory)
	{
		territories_lock.lock();
		try
		{
			if(territories == null)
				return;
			territories.remove(territory);
			if(territories.isEmpty())
				territories = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			territories_lock.unlock();
		}
	}

	public GArray<L2Territory> getTerritories(int x, int y, int z)
	{
		territories_lock.lock();
		try
		{
			if(territories == null)
				return null;
			GArray<L2Territory> result = new GArray<L2Territory>(territories.size());
			for(L2Territory terr : territories)
				if(terr != null && terr.isInside(x, y, z))
					result.add(terr);
			return result.isEmpty() ? null : result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			territories_lock.unlock();
		}
		return null;
	}
}