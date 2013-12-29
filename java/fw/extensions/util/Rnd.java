package fw.extensions.util;

import fw.clientbase.model.L2Object;

public class Rnd
{
	private static MTRandom _rnd = new MTRandom();

	public static float get() // get random number from 0 to 1
	{
		return _rnd.nextFloat();
	}

	/**
	 * Gets a random number from 0(inclusive) to n(exclusive)
	 *
	 * @param n The superior limit (exclusive)
	 * @return A number from 0 to n-1
	 */
	public static int get(int n)
	{
		return (int) Math.floor(_rnd.nextDouble() * n);
	}

	public static int get(int min, int max) // get random number from min to max (not max-1 !)
	{
		return min + (int) Math.floor(_rnd.nextDouble() * (max - min + 1));
	}

	public static long get(long min, long max) // get random number from min to max (not max-1 !)
	{
		return min + (long) Math.floor(_rnd.nextDouble() * (max - min + 1));
	}

	public static int nextInt()
	{
		return _rnd.nextInt();
	}

	public static double nextDouble()
	{
		return _rnd.nextDouble();
	}

	public static double nextGaussian()
	{
		return _rnd.nextGaussian();
	}

	public static boolean nextBoolean()
	{
		return _rnd.nextBoolean();
	}

	/**
	 * Рандомайзер для подсчета шансов.<br>
	 * Рекомендуется к использованию вместо Rnd.get()
	 * @param chance в процентах от 0 до 100
	 * @return true в случае успешного выпадания.
	 * <li>Если chance <= 0, вернет false
	 * <li>Если chance >= 100, вернет true
	 */
	public static boolean chance(int chance)
	{
		return chance < 1 ? false : chance > 99 ? true : _rnd.nextInt(99) + 1 <= chance;
	}

	/**
	 * Рандомайзер для подсчета шансов.<br>
	 * Рекомендуется к использованию вместо Rnd.get() если нужны очень маленькие шансы
	 * @param chance в процентах от 0 до 100
	 * @return true в случае успешного выпадания.
	 * <li>Если chance <= 0, вернет false
	 * <li>Если chance >= 100, вернет true
	 */
	public static boolean chance(double chance)
	{
		return _rnd.nextDouble() <= chance / 100.;
	}

	public static Location coordsRandomize(L2Object obj, int min, int max)
	{
		return obj.getLoc().rnd(min, max, false);
	}

	public static Location coordsRandomize(L2Object obj, int radius)
	{
		return coordsRandomize(obj, 0, radius);
	}

	public static Location coordsRandomize(int x, int y, int z, int heading, int radius_min, int radius_max)
	{
		if(radius_max == 0 || radius_max < radius_min)
			return new Location(x, y, z, heading);
		int radius = get(radius_min, radius_max);
		double angle = _rnd.nextDouble() * 2 * Math.PI;
		return new Location((int) (x + radius * Math.cos(angle)), (int) (y + radius * Math.sin(angle)), z, heading);
	}

	public static Location coordsRandomize(Location pos, int radius_min, int radius_max)
	{
		return coordsRandomize(pos.x, pos.y, pos.z, pos.h, radius_min, radius_max);
	}

}