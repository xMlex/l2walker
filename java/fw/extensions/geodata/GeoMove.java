package fw.extensions.geodata;

import java.util.ArrayList;

import fw.extensions.util.Location;
import fw.game.model.L2Object;

/**
 * @Author: Diamond
 * @Date: 27/04/2009
 */
public class GeoMove
{
	private static final ArrayList<Location> emptyTargetRecorder = new ArrayList<Location>(0);
	private static final ArrayList<ArrayList<Location>> emptyMovePath = new ArrayList<ArrayList<Location>>(0);

	public static ArrayList<Location> findPath(int x, int y, int z, Location target, L2Object obj, boolean showTrace, int refIndex)
	{
		if(Math.abs(z - target.z) > 256)
			return emptyTargetRecorder;

		z = GeoEngine.getHeight(x, y, z, refIndex);
		target.z = GeoEngine.getHeight(target, refIndex);

		PathFind n = new PathFind(x, y, z, target.x, target.y, target.z, obj, refIndex);

		if(n.getPath() == null || n.getPath().isEmpty())
			return emptyTargetRecorder;

		ArrayList<Location> targetRecorder = new ArrayList<Location>(n.getPath().size() + 2);

		// добавляем первую точку в список (начальная позиция чара)
		targetRecorder.add(new Location(x, y, z));

		for(Location p : n.getPath())
			targetRecorder.add(p.geo2world());

		// добавляем последнюю точку в список (цель)
		targetRecorder.add(target);

		if(PathFind.PATH_CLEAN)
			pathClean(targetRecorder, refIndex);

		/*if(showTrace && obj.isPlayer() && ((L2Player) obj).getVarB("trace"))
		{
			L2Player player = (L2Player) obj;
			ExShowTrace trace = new ExShowTrace();
			int i = 0;
			for(Location loc : targetRecorder)
			{
				i++;
				if(i == 1 || i == targetRecorder.size())
					continue;
				trace.addTrace(loc.x, loc.y, loc.z + 15, 30000);
			}
			player.sendPacket(trace);
		}*/

		return targetRecorder;
	}

	public static ArrayList<ArrayList<Location>> findMovePath(int x, int y, int z, Location target, L2Object obj, boolean showTrace, int refIndex)
	{
		return getNodePath(findPath(x, y, z, target, obj, showTrace, refIndex), refIndex);
	}

	public static ArrayList<ArrayList<Location>> getNodePath(ArrayList<Location> path, int refIndex)
	{
		int size = path.size();
		if(size <= 1)
			return emptyMovePath;
		ArrayList<ArrayList<Location>> result = new ArrayList<ArrayList<Location>>();
		for(int i = 1; i < size; i++)
		{
			Location p2 = path.get(i);
			Location p1 = path.get(i - 1);
			ArrayList<Location> moveList = GeoEngine.MoveList(p1.x, p1.y, p1.z, p2.x, p2.y, refIndex, true); // onlyFullPath = true - проверяем весь путь до конца
			if(moveList == null) // если хотя-бы через один из участков нельзя пройти, забраковываем весь путь 
				return emptyMovePath;
			if(!moveList.isEmpty()) // это может случиться только если 2 одинаковых точки подряд
				result.add(moveList);
		}
		return result;
	}

	public static ArrayList<Location> constructMoveList(Location begin, Location end)
	{
		begin.world2geo();
		end.world2geo();

		ArrayList<Location> result = new ArrayList<Location>();

		int diff_x = end.x - begin.x, diff_y = end.y - begin.y, diff_z = end.z - begin.z;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y), dz = Math.abs(diff_z);
		float steps = Math.max(Math.max(dx, dy), dz);
		if(steps == 0) // Никуда не идем
			return result;

		float step_x = diff_x / steps, step_y = diff_y / steps, step_z = diff_z / steps;
		float next_x = begin.x, next_y = begin.y, next_z = begin.z;

		result.add(new Location(begin.x, begin.y, begin.z)); // Первая точка

		for(int i = 0; i < steps; i++)
		{
			next_x += step_x;
			next_y += step_y;
			next_z += step_z;

			result.add(new Location((int) (next_x + 0.5f), (int) (next_y + 0.5f), (int) (next_z + 0.5f)));
		}

		return result;
	}

	/**
	 * Очищает путь от ненужных точек.
	 * @param path путь который следует очистить
	 */
	private static void pathClean(ArrayList<Location> path, int refIndex)
	{
		int size = path.size();
		if(size > 2)
			for(int i = 2; i < size; i++)
			{
				Location p3 = path.get(i); // точка конца движения
				Location p2 = path.get(i - 1); // точка в середине, кандидат на вышибание
				Location p1 = path.get(i - 2); // точка начала движения
				if(p1.equals(p2) || p3.equals(p2) || IsPointInLine(p1, p2, p3)) // если вторая точка совпадает с первой/третьей или на одной линии с ними - она не нужна
				{
					path.remove(i - 1); // удаляем ее
					size--; // отмечаем это в размере массива
					i = Math.max(2, i - 2); // сдвигаемся назад, FIXME: может я тут не совсем прав
				}
			}

		int current = 0;
		int sub;
		while(current < path.size() - 2)
		{
			sub = current + 2;
			while(sub < path.size())
			{
				Location one = path.get(current);
				Location two = path.get(sub);
				if(one.equals(two) || GeoEngine.canMoveWithCollision(one.x, one.y, one.z, two.x, two.y, two.z, refIndex)) //canMoveWithCollision  /  canMoveToCoord
					while(current + 1 < sub)
					{
						path.remove(current + 1);
						sub--;
					}
				sub++;
			}
			current++;
		}
	}

	private static boolean IsPointInLine(Location p1, Location p2, Location p3)
	{
		// Все 3 точки на одной из осей X или Y.
		if(p1.x == p3.x && p3.x == p2.x || p1.y == p3.y && p3.y == p2.y)
			return true;
		// Условие ниже выполнится если все 3 точки выстроены по диагонали.
		// Это работает потому, что сравниваем мы соседние точки (расстояния между ними равны, важен только знак).
		// Для случая с произвольными точками работать не будет.
		if((p1.x - p2.x) * (p1.y - p2.y) == (p2.x - p3.x) * (p2.y - p3.y))
			return true;
		return false;
	}
}