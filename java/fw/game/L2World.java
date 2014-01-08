package fw.game;

import java.util.Map.Entry;

import javolution.util.FastMap;
import fw.dbClasses.DbObjects;
import fw.extensions.util.Location;
import fw.game.model.*;
import fw.game.model.L2Char;
import fw.game.model.instances.*;

public class L2World {

	private DbObjects dbObjects = null;
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
	public L2Char getOrCreateChar(int objectId) {
		L2Char _obj = (L2Char) _objects.get(objectId);
		if(_obj != null)
			return _obj;
		_obj = new L2Char(objectId);
		_objects.put(objectId,_obj);
		return _obj;
	}
	public L2Drop getOrCreateDrop(int objectId) {
		L2Drop _obj = (L2Drop) _objects.get(objectId);
		if(_obj != null)
			return _obj;
		_obj = new L2Drop(objectId);
		_objects.put(objectId,_obj);
		return _obj;
	}
	
	public void remove(int objectId){
		_objects.remove(objectId);
	}

	public void clear() {
		_objects.clear();		
	}
	public L2Object getObject(int objectId){
		if(_objects.containsKey(objectId))
			return _objects.get(objectId);
		return null;		
	}
	
	public L2Object getObjectInRadius(Location loc,int radius)
	{				
		for (Entry<Integer,L2Object> obj: _objects.entrySet()) {
			if(obj.getValue().getDistance(loc) <= radius)
				return obj.getValue(); 
		}		
		return null;		
	}
	public L2NpcInstance getMobInRadius(Location loc,int radius)
	{				
		L2NpcInstance _mob = null;
		for (Entry<Integer,L2Object> obj: _objects.entrySet()) {
			if(obj.getValue().getDistance(loc) <= radius && (obj.getValue() instanceof L2NpcInstance))
				if( !((L2NpcInstance)obj.getValue()).isDead() && ((L2NpcInstance)obj.getValue()).isAttackable()){
					if(_mob == null)
						_mob = (L2NpcInstance)obj.getValue();
					
					if(_mob.getDistance(loc) > obj.getValue().getDistance(loc) )
						_mob = (L2NpcInstance)obj.getValue();
				}
				//return (L2NpcInstance)obj.getValue(); 
		}		
		return _mob;		
	}
	public L2Drop getDropInRadius(Location loc,int radius)
	{				
		for (Entry<Integer,L2Object> obj: _objects.entrySet()) {
			if(obj.getValue().getDistance(loc) <= radius && (obj.getValue().isDrop()))				
				return (L2Drop)obj.getValue(); 
		}		
		return null;		
	}
	
	
	/** <b>Read-only</b> Get all objects for iterate */
	public FastMap<Integer,L2Object> getObjects() {
		return _objects;
	}
	
	public DbObjects getDbObjects()
	{
		return dbObjects;
	}

	public void setDbObjects(DbObjects dbObjects)
	{
		this.dbObjects = dbObjects;
	}
}
