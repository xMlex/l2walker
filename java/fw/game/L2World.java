package fw.game;

import java.util.ArrayList;
import java.util.Map.Entry;

import javolution.util.FastMap;
import fw.dbClasses.DbObjects;
import fw.extensions.util.Location;
import fw.game.model.*;
import fw.game.model.L2Char;
import fw.game.model.L2Item;
import fw.game.model.instances.*;

public class L2World {

	private DbObjects dbObjects = null;
	/** Global list of objects in world */
	private FastMap<Integer, L2Object> _objects;
	private ArrayList<L2Object> _objectList;

	public L2World() {
		_objects = new FastMap<Integer, L2Object>();
		_objectList = new ArrayList<L2Object>();
	}

	public L2Player getOrCreatePlayer(int objectId) {
		/*synchronized (_objects) {
			L2Player _obj = (L2Player) _objects.get(objectId);
			if (_obj != null)
				return _obj;
			_obj = new L2Player(objectId);
			_objects.put(objectId, _obj);
			return _obj;
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId)
					return (L2Player)el;
			L2Player _obj = new L2Player(objectId);
			_objectList.add(_obj);
			return _obj;
		}
		
	}

	public L2NpcInstance getOrCreateNpc(int objectId) {
		/*synchronized (_objects) {
			L2NpcInstance _obj = (L2NpcInstance) _objects.get(objectId);
			if (_obj != null)
				return _obj;
			_obj = new L2NpcInstance(objectId);
			_objects.put(objectId, _obj);
			return _obj;
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId)
					return (L2NpcInstance)el;
			L2NpcInstance _obj = new L2NpcInstance(objectId);
			_objectList.add(_obj);
			return _obj;
		}
	}

	public L2Char getOrCreateChar(int objectId) {
		/*synchronized (_objects) {
			L2Char _obj = (L2Char) _objects.get(objectId);
			if (_obj != null)
				return _obj;
			_obj = new L2Char(objectId);

			_objects.put(objectId, _obj);
			return _obj;
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId)
					return (L2Char)el;
			L2Char _obj = new L2Char(objectId);
			_objectList.add(_obj);
			return _obj;
		}
	}

	public L2Drop getOrCreateDrop(int objectId) {
		/*synchronized (_objects) {
			L2Drop _obj = (L2Drop) _objects.get(objectId);
			if (_obj != null)
				return _obj;
			_obj = new L2Drop(objectId);

			_objects.put(objectId, _obj);
			return _obj;
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId)
					return (L2Drop)el;
			L2Drop _obj = new L2Drop(objectId);
			_objectList.add(_obj);
			return _obj;
		}
	}
	public L2Item getOrCreateItem(int objectId) {
		/*synchronized (_objects) {
			L2Item _obj = (L2Item) _objects.get(objectId);
			if (_obj != null)
				return _obj;
			_obj = new L2Item(objectId);

			_objects.put(objectId, _obj);
			return _obj;
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId)
					return (L2Item)el;
			L2Item _obj = new L2Item(objectId);
			_objectList.add(_obj);
			return _obj;
		}
	}

	public void remove(int objectId) {
		/*synchronized (_objects) {
			_objects.remove(objectId);
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId){
					_objectList.remove(el);
					return;
				}
		}
	}

	public void clear() {
		synchronized (_objects) {
			_objects.clear();
		}
		synchronized (_objectList) {
			_objectList.clear();
		}
	}

	public L2Object getObject(int objectId) {
		/*synchronized (_objects) {
			if (_objects.containsKey(objectId))
				return _objects.get(objectId);
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getObjectId() == objectId){					
					return el;
				}
		}
		return null;
	}

	public L2Object getObjectInRadius(Location loc, int radius) {
		/*synchronized (_objects) {
			for (Entry<Integer, L2Object> obj : _objects.entrySet()) {
				if (obj.getValue().getDistance(loc) <= radius)
					return obj.getValue();
			}
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getDistance(loc) <= radius){					
					return el;
				}
		}
		return null;
	}

	public L2NpcInstance getMobInRadius(Location loc, int radius) {
		L2NpcInstance _mob = null,_tmp;
		/*synchronized (_objects) {
			for (Entry<Integer, L2Object> obj : _objects.entrySet()) {
				if (obj.getValue().getDistance(loc) <= radius
						&& (obj.getValue() instanceof L2NpcInstance))
					if (!((L2NpcInstance) obj.getValue()).isDead()
							&& ((L2NpcInstance) obj.getValue()).isAttackable()) {
						if (_mob == null)
							_mob = (L2NpcInstance) obj.getValue();

						if (_mob.getDistance(loc) > obj.getValue().getDistance(
								loc))
							_mob = (L2NpcInstance) obj.getValue();
					}
			}
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList)
				if(el.getDistance(loc) <= radius){					
					if(el.isNpc()){
						_tmp = (L2NpcInstance)el;
						if(!_tmp.isDead())
							if(_mob == null){
								_mob = (L2NpcInstance)el;
							}else{
								if(_tmp.getDistance(loc) < _mob.getDistance(loc))
									_mob = (L2NpcInstance)el;
							}					
					}
				}
		}
		return _mob;
	}

	public L2Drop getDropInRadius(Location loc, int radius) {
		L2Drop _drop = null;
		/*synchronized (_objects) {
			for (Entry<Integer, L2Object> obj : _objects.entrySet()) {
				if (obj.getValue().getDistance(loc) <= radius
						&& (obj.getValue().isDrop()))
					return (L2Drop) obj.getValue();
			}
		}*/
		synchronized (_objectList) {
			for(L2Object el : _objectList){
				if(el.isDrop())
				if(el.getDistance(loc) <= radius){					
					if(_drop == null){
						_drop = (L2Drop)el;
					}else{
						if(el.getDistance(loc) < _drop.getDistance(loc))
							_drop = (L2Drop)el;
					}
				}
			}
		}
		return _drop;
	}

	/** <b>NOT SYNCHRONIZED</b> Get all object list */
	public ArrayList<L2Object> getObjectList() {
		return new ArrayList<L2Object>(_objectList);
	}
	public FastMap<Integer, L2Object> getObjects() {
		return _objects;
	}

	public DbObjects getDbObjects() {
		return dbObjects;
	}

	public void setDbObjects(DbObjects dbObjects) {
		this.dbObjects = dbObjects;
	}
}
