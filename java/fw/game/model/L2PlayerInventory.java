package fw.game.model;

import java.util.ArrayList;

public class L2PlayerInventory {

	private long _lastUpdate = System.currentTimeMillis();
	private ArrayList<L2Item> _objects = new ArrayList<L2Item>();

	public synchronized int getCountById(int id) {
		for (L2Item _el : _objects) {
			if (_el.getId() == id)
				return _el.getCount();
		}
		return 0;
	}

	public synchronized int getCount(int objectId) {
		for (L2Item _el : _objects) {
			if (_el.getObjectId() == objectId)
				return _el.getCount();
		}
		return 0;
	}

	public synchronized L2Item getOrCreate(int objectId) {
		update();
		for (L2Item _el : _objects)
			if (_el.getObjectId() == objectId)
				return _el;

		L2Item _r = new L2Item(objectId);
		_objects.add(_r);
		return _r;
	}

	public synchronized void remove(L2Item el) {
		_objects.remove(el);
		update();
	}

	public synchronized void removeById(int id) {
		update();
		for (L2Item _el : _objects)
			if (_el.getId() == id) {
				int i = _objects.indexOf(_el);
				_objects.remove(i);
				return;
			}		
	}

	public synchronized void removeByOID(int objectId) {
		update();
		for (L2Item _el : _objects)
			if (_el.getObjectId() == objectId) {
				int i = _objects.indexOf(_el);
				_objects.remove(i);
				return;
			}
	}

	public synchronized void clear() {
		_objects.clear();
		update();
	}

	public synchronized ArrayList<L2Item> getObjectList() {
		return new ArrayList<L2Item>(_objects);
	}

	private void update() {
		_lastUpdate = System.currentTimeMillis();
	}

	public synchronized final long getLastUpdate() {
		return _lastUpdate;
	}
}
