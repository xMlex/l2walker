package fw.game;

import java.util.ArrayList;

import fw.dbClasses.DbObjects;
import fw.extensions.util.Location;
import fw.extensions.util.SArrayList;
import fw.game.model.*;
import fw.game.model.instances.*;

public class L2World {

	private DbObjects dbObjects = null;
	/** Global list of objects in world */
	private SArrayList<L2Object> _objectList = new SArrayList<L2Object>();

	public L2World() {}

	public L2Player getOrCreatePlayer(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId)
				return (L2Player) el;
		L2Player _obj = new L2Player(objectId);
		_objectList.add(_obj);
		return _obj;
	}

	public L2NpcInstance getOrCreateNpc(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId)
				return (L2NpcInstance) el;
		L2NpcInstance _obj = new L2NpcInstance(objectId);
		_objectList.add(_obj);
		return _obj;

	}

	public L2Char getOrCreateChar(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId)
				return (L2Char) el;
		L2Char _obj = new L2Char(objectId);
		_objectList.add(_obj);
		return _obj;

	}

	public L2Drop getOrCreateDrop(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId)
				return (L2Drop) el;
		L2Drop _obj = new L2Drop(objectId);
		_objectList.add(_obj);
		return _obj;

	}

	public L2Item getOrCreateItem(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId)
				return (L2Item) el;
		L2Item _obj = new L2Item(objectId);
		_objectList.add(_obj);
		return _obj;

	}

	public void remove(int objectId) {
		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId) {
				_objectList.remove(el);
				return;
			}
	}

	public void clear() {
		_objectList.clear();
	}

	public L2Object getObject(int objectId) {

		for (L2Object el : _objectList.getCopy())
			if (el.getObjectId() == objectId) {
				return el;
			}

		return null;
	}

	public L2Object getObjectInRadius(Location loc, int radius) {

		for (L2Object el : _objectList.getCopy())
			if (el.getDistance(loc) <= radius) {
				return el;
			}

		return null;
	}

	public L2NpcInstance getMobInRadius(Location loc, int radius) {
		L2NpcInstance _mob = null, _tmp;
		for (L2Object el : _objectList.getCopy())
			if (el.getDistance(loc) <= radius) {
				if (el.isNpc()) {
					_tmp = (L2NpcInstance) el;
					if (!_tmp.isDead() && _tmp.isAttackable())
						if (_mob == null) {
							_mob = (L2NpcInstance) el;
						} else {
							if (_tmp.getDistance(loc) < _mob.getDistance(loc))
								_mob = (L2NpcInstance) el;
						}
				}
			}
		return _mob;
	}

	public L2Drop getDropInRadius(Location loc, int radius) {
		L2Drop _drop = null;
		for (L2Object el : _objectList.getCopy()) {
			if (el.isDrop())
				if (el.getDistance(loc) <= radius) {
					if (_drop == null) {
						_drop = (L2Drop) el;
					} else {
						if (el.getDistance(loc) < _drop.getDistance(loc))
							_drop = (L2Drop) el;
					}
				}
		}
		return _drop;
	}

	/** <b>NOT SYNCHRONIZED</b> Get all object list */
	public synchronized ArrayList<L2Object> getObjectList() {
		return _objectList.getCopy();
	}

	public DbObjects getDbObjects() {
		return dbObjects;
	}

	public void setDbObjects(DbObjects dbObjects) {
		this.dbObjects = dbObjects;
	}
}
