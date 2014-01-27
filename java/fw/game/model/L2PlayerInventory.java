package fw.game.model;

import java.util.ArrayList;

public class L2PlayerInventory {

	private final L2Player _self;
	/** weapon in hand */
	private L2Item RHand = null;
	/** Shield in hand */
	private L2Item LHand = null;
	/** Shield in hand */
	private L2Item LRHand = null;

	private long _lastUpdate = System.currentTimeMillis();
	private ArrayList<L2Item> _objects = new ArrayList<L2Item>();

	public L2PlayerInventory(L2Player self) {
		super();
		_self = self;
	}
	
	public synchronized int getCountById(int id) {
		for (L2Item _el : _objects) {
			if (_el.getId() == id)
				return _el.getCount();
		}
		return 0;
	}
	public synchronized L2Item getById(int id) {
		for (L2Item _el : _objects) {
			if (_el.getId() == id)
				return _el;
		}
		return null;
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

	private L2Item getByObjectId(int oid) {
		for (L2Item _el : _objects)
			if (_el.getObjectId() == oid) {
				return _el;
			}
		return null;
	}

	public L2Item getRHand() {
		return RHand;
	}

	public void setRHand(L2Item rHand) {
		if(!rHand.isEquipped())return;
		RHand = rHand;
	}
	public void setRHand(int id) {
		L2Item rHand = getById(id);
		if(rHand == null) return;
		if(!rHand.isEquipped())return;
		RHand = rHand;
	}

	public L2Item getLHand() {
		return LHand;
	}

	public void setLHand(L2Item lHand) {
		if(!lHand.isEquipped())return;
		LHand = lHand;
	}
	public void setLHand(int id) {
		L2Item lHand = getById(id);
		if(lHand == null) return;
		if(!lHand.isEquipped())return;
		LHand = lHand;
	}

	public synchronized void updateSlots() {
		for (L2Item _el : _objects) {
			if(!_el.isEquipped()) continue;
			switch (_el.getBodyPart()) {
			case L2Item.SLOT_L_HAND:
				setLHand(_el);
				break;
			case L2Item.SLOT_R_HAND:
				setRHand(_el);
				break;
			case L2Item.SLOT_LR_HAND:
				//setRHand(_el);
				//setLHand(_el);
				break;
			default:
				break;
			}
		}
	}
	
	public synchronized void equipWeapon(){
		for (L2Item _it:_objects) {
			if(_it.isWeapon() && !_it.isEquipped()){
				_self.useItemId(_it.getId());
				//_log.info("Одеваем "+_it);
				break;
			}
		}
	}
	
}
