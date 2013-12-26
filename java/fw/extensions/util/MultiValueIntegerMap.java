package fw.extensions.util;

import javolution.util.FastMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MultiValueIntegerMap
{
	private FastMap<Integer, GCSArray<Integer>> map;

	public MultiValueIntegerMap()
	{
		map = new FastMap<Integer, GCSArray<Integer>>(10).setShared(true);
	}

	public Set<Integer> keySet()
	{
		return map.keySet();
	}

	public Collection<GCSArray<Integer>> values()
	{
		return map.values();
	}

	public GCSArray<Integer> allValues()
	{
		GCSArray<Integer> result = new GCSArray<Integer>(10);
		for(Map.Entry<Integer, GCSArray<Integer>> entry : map.entrySet())
			result.addAll(entry.getValue());
		return result;
	}

	public Set<Entry<Integer, GCSArray<Integer>>> entrySet()
	{
		return map.entrySet();
	}

	public GCSArray<Integer> remove(Integer key)
	{
		return map.remove(key);
	}

	public GCSArray<Integer> get(Integer key)
	{
		return map.get(key);
	}

	public boolean containsKey(Integer key)
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

	public Integer remove(Integer key, Integer value)
	{
		GCSArray<Integer> valuesForKey = map.get(key);
		if(valuesForKey == null){ return null; }
		boolean removed = valuesForKey.remove(value);
		if(removed == false){ return null; }
		if(valuesForKey.isEmpty())
			remove(key);
		return value;
	}

	public Integer removeValue(Integer value)
	{
		GCSArray<Integer> toRemove = new GCSArray<Integer>(1);
		for(Map.Entry<Integer, GCSArray<Integer>> entry : map.entrySet())
		{
			entry.getValue().remove(value);
			if(entry.getValue().isEmpty())
				toRemove.add(entry.getKey());
		}
		for(Integer key : toRemove)
			remove(key);
		return value;
	}

	public Integer put(Integer key, Integer value)
	{
		GCSArray<Integer> coll = map.get(key);
		if(coll == null)
		{
			coll = new GCSArray<Integer>(1);
			map.put(key, coll);
		}
		coll.add(value);
		return value;
	}

	public boolean containsValue(Integer value)
	{
		for(Map.Entry<Integer, GCSArray<Integer>> entry : map.entrySet())
			if(entry.getValue().contains(value))
				return true;
		return false;
	}

	public boolean containsValue(Integer key, Integer value)
	{
		GCSArray<Integer> coll = map.get(key);
		if(coll == null){ return false; }
		return coll.contains(value);
	}

	public int size(Integer key)
	{
		GCSArray<Integer> coll = map.get(key);
		if(coll == null){ return 0; }
		return coll.size();
	}

	public boolean putAll(Integer key, Collection<? extends Integer> values)
	{
		if(values == null || values.size() == 0){ return false; }
		boolean result = false;
		GCSArray<Integer> coll = map.get(key);
		if(coll == null)
		{
			coll = new GCSArray<Integer>(values.size());
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
		for(Map.Entry<Integer, GCSArray<Integer>> entry : map.entrySet())
			total += entry.getValue().size();
		return total;
	}
}