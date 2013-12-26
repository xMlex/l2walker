package fw.extensions.util;

public class RndSelector<E>
{
	private int total_weight = 0;
	private final GArray<E> values;
	private final GArray<Integer> weights;

	public RndSelector()
	{
		values = new GArray<E>();
		weights = new GArray<Integer>();
	}

	public RndSelector(int initialCapacity)
	{
		values = new GArray<E>(initialCapacity);
		weights = new GArray<Integer>(initialCapacity);
	}

	public synchronized void add(E value, int weight)
	{
		if(value == null || weight <= 0)
			return;
		total_weight += weight;
		values.add(value);
		weights.add(total_weight);
	}

	/**
	 * Вернет один из елементов или null, null возможен только если сумма весов всех елементов меньше max_weight 
	 */
	public synchronized E chance(int max_weight)
	{
		if(total_weight == 0)
			return null;
		int r = Rnd.get(max_weight);
		for(int i = 0; i < weights.size(); i++)
			if(weights.get(i) > r)
				return values.get(i);
		return null;
	}

	/**
	 * Вернет один из елементов или null, null возможен только если сумма весов всех елементов меньше 100 
	 */
	public synchronized E chance()
	{
		return chance(100);
	}

	/**
	 * Вернет один из елементов
	 */
	public synchronized E select()
	{
		return chance(total_weight);
	}

	public synchronized void clear()
	{
		total_weight = 0;
		values.clear();
		weights.clear();
	}

	@Override
	public synchronized String toString()
	{
		String ret = "";
		for(int i = 0; i < weights.size(); i++)
			ret += values.get(i).toString() + " - " + weights.get(i) + "\r\n";
		return ret;
	}
}