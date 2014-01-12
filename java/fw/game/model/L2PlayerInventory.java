package fw.game.model;

import java.util.Map.Entry;

import javolution.util.FastMap;

public class L2PlayerInventory {

	private FastMap<Integer, L2Item> _items = new FastMap<Integer, L2Item>();
	
	public int getCount(int id) {
		synchronized (_items) {
			for (Entry<Integer, L2Item> obj : _items.entrySet()) {
				if(obj.getValue().getId() == id)
					return  obj.getValue().getCount();
			}
		}
		return 0;
	}
	
	public void add(L2Item el){
		synchronized (_items) {
			_items.put(el.getObjectId(), el);
		}
	}
	public void remove(L2Item el){
		synchronized (_items) {
			_items.remove(el.getObjectId());
		}
	}	
	public void clear(){
		synchronized (_items) {
			_items.clear();
		}
	}
	
}
