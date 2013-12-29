package fw.game;

import javolution.util.FastMap;
import fw.game.model.*;
import fw.game.model.instances.*;

public class L2World {

	/** Global list of objects in world */
	private FastMap<Integer,L2Object> _objects;
	
	public L2World() {
		_objects = new FastMap<Integer,L2Object>();
	}
	
	public L2Player getOrCreatePlayer(int objectId) {
		L2Player _obj = (L2Player) _objects.get(objectId);
		if(_obj != null)
			return _obj;
		_obj = new L2Player(objectId);
		_objects.put(objectId,_obj);
		return _obj;
	}
	
	public L2NpcInstance getOrCreateNpc(int objectId) {
		L2NpcInstance _obj = (L2NpcInstance) _objects.get(objectId);
		if(_obj != null)
			return _obj;
		_obj = new L2NpcInstance(objectId);
		_objects.put(objectId,_obj);
		return _obj;
	}
	
	public void remove(int objectId){
		_objects.remove(objectId);
	}

	public void clear() {
		_objects.clear();		
	}
	
}
