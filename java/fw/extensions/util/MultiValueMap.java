package fw.extensions.util;

import javolution.util.FastMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class MultiValueMap<K, V> implements Map
{
	protected transient FastMap<K, GArray<V>> map;

	public MultiValueMap()
	{
		map = new FastMap<K, GArray<V>>(10).setShared(true);
	}

	public Set<K> keySet()
	{
		return map.keySet();
	}

	public Collection<GArray<V>> values()
	{
		return map.values();
	}

	public GArray<V> allValues()
	{
		GArray<V> result = new GArray<V>(10);
		for(Map.Entry<K, GArray<V>> entry : map.entrySet())
			result.addAll(entry.getValue());
		return result;
	}

	public Set<Entry<K, GArray<V>>> entrySet()
	{
		return map.entrySet();
	}

	public GArray<V> remove(Object key)
	{
		return map.remove(key);
	}

	public GArray<V> get(Object key)
	{
		return map.get(key);
	}

	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	public void clear()
	{
		map.clear();
	}

	public int size()
	{
		return map.size();
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public Object remove(Object key, Object value)
	{
		GArray<V> valuesForKey = map.get(key);
		if(valuesForKey == null){ return null; }
		boolean removed = valuesForKey.remove(value);
		if(removed == false){ return null; }
		if(valuesForKey.isEmpty())
			remove(key);
		return value;
	}

	public V removeValue(V value)
	{
		GArray<K> toRemove = new GArray<K>(1);
		for(Map.Entry<K, GArray<V>> entry : map.entrySet())
		{
			entry.getValue().remove(value);
			if(entry.getValue().isEmpty())
				toRemove.add(entry.getKey());
		}
		for(K key : toRemove)
			remove(key);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Object put(Object key, Object value)
	{
		GArray<V> coll = map.get(key);
		if(coll == null)
		{
			coll = new GArray<V>(1);
			map.put((K) key, coll);
		}
		coll.add((V) value);
		return value;
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map map)
	{
		if(map instanceof MultiValueMap)
			for(Map.Entry<K, GArray<V>> entry : ((Map<K, GArray<V>>) map).entrySet())
				putAll(entry.getKey(), entry.getValue());
		else
			for(Map.Entry<K, GArray<V>> entry : ((Map<K, GArray<V>>) map).entrySet())
				put(entry.getKey(), entry.getValue());
	}

	public boolean containsValue(Object value)
	{
		for(Map.Entry<K, GArray<V>> entry : map.entrySet())
			if(entry.getValue().contains(value))
				return true;
		return false;
	}

	public boolean containsValue(Object key, Object value)
	{
		GArray<V> coll = map.get(key);
		if(coll == null){ return false; }
		return coll.contains(value);
	}

	public int size(Object key)
	{
		GArray<V> coll = map.get(key);
		if(coll == null){ return 0; }
		return coll.size();
	}

	public boolean putAll(K key, Collection<? extends V> values)
	{
		if(values == null || values.size() == 0){ return false; }
		boolean result = false;
		GArray<V> coll = map.get(key);
		if(coll == null)
		{
			coll = new GArray<V>(values.size());
			coll.addAll(values);
			if(coll.size() > 0)
			{
				map.put(key, coll);
				result = true;
			}
		}
		else
			result = coll.addAll(values);
		return result;
	}

	public int totalSize()
	{
		int total = 0;
		for(Map.Entry<K, GArray<V>> entry : map.entrySet())
			total += entry.getValue().size();
		return total;
	}
}