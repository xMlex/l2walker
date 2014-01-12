package fw.extensions.geodata;

import java.util.ArrayList;

import fw.extensions.geodata.PathFindBuffers.GeoNode;
import fw.extensions.geodata.PathFindBuffers.PathFindBuffer;
import fw.extensions.util.Location;
import fw.game.model.L2Object;
import fw.game.model.instances.L2NpcInstance;

/*import l2p.Config;
import l2p.gameserver.geodata.PathFindBuffers.GeoNode;
import l2p.gameserver.geodata.PathFindBuffers.PathFindBuffer;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.tables.ItemTable;
import l2p.util.GArray;
import l2p.util.Location;*/

/**
 * @Author: Diamond & Drin
 * @Date: 27/04/2009
 */
public class PathFind
{
	private static final byte NSWE_NONE = 0, EAST = 1, WEST = 2, SOUTH = 4, NORTH = 8, NSWE_ALL = 15;

	private int refIndex = 0;

	private PathFindBuffer buff;

	private ArrayList<Location> path;

	private boolean custom_debug;
	
	public static final boolean PATH_CLEAN = false;
	
	// Сильно ускоряет поиск, как влияет на качество неизвестно
	// 0 - отключить, 1 - только начало пути, 2 - начало и конец пути
	private int PATHFIND_BOOST = 2 ;
	private boolean PATHFIND_DEBUG = true;
	private boolean GEODATA_DEBUG = true;			
	// Диагональный поиск. Возможно тратит больше ресурсов.
	private boolean PATHFIND_DIAGONAL = true;
	// Базовый вес ячейки
	private double WEIGHT0 = 0.5;
	// Вес "плохих" клеток второго плана
	private double WEIGHT2 = 1;
	// Вес "плохих" клеток первого плана
	private double WEIGHT1 = 2;
	// Минимальная разница между слоями
	private int PATHFIND_MAX_Z_DIFF = 64;

	private boolean SIMPLE_PATHFIND_FOR_MOBS = true;

	public PathFind(int x, int y, int z, int destX, int destY, int destZ, L2Object obj, int refIndex)
	{
		this.refIndex = refIndex;

		Location startpoint = PATHFIND_BOOST == 0 ? new Location(x, y, z) : GeoEngine.moveCheckWithCollision(x, y, z, destX, destY, true, refIndex);
		Location native_endpoint = new Location(destX, destY, destZ);
		Location endpoint = PATHFIND_BOOST != 2 || Math.abs(destZ - z) > 200 ? native_endpoint.clone() : GeoEngine.moveCheckBackwardWithCollision(destX, destY, destZ, startpoint.x, startpoint.y, true, refIndex);

		startpoint.world2geo();
		native_endpoint.world2geo();
		endpoint.world2geo();

		startpoint.z = GeoEngine.NgetHeight(startpoint.x, startpoint.y, startpoint.z, refIndex);
		endpoint.z = GeoEngine.NgetHeight(endpoint.x, endpoint.y, endpoint.z, refIndex);

		int xdiff = Math.abs(endpoint.x - startpoint.x);
		int ydiff = Math.abs(endpoint.y - startpoint.y);

		if(xdiff == 0 && ydiff == 0)
		{
			if(Math.abs(endpoint.z - startpoint.z) < 32)
			{
				path = new ArrayList<Location>();
				path.add(0, startpoint);
			}
			return;
		}

		if((buff = PathFindBuffers.alloc(64 + 2 * Math.max(xdiff, ydiff), true, startpoint, endpoint, native_endpoint)) != null)
		{
			/*if(Config.PATHFIND_DEBUG && obj.isPlayer() && ((L2Player) obj).isGM())
			{
				if(debugItems != null)
				{
					for(L2ItemInstance item : debugItems)
						item.deleteMe();
					debugItems.clear();
				}
				custom_debug = true;
			}*/

			path = findPath();

			//if(Config.GEODATA_DEBUG && obj.isPlayer())
			//	debugPath((L2Player) obj, buff, path); // TODO дебаг не для лайва!

			buff.free();

			if(obj != null && obj.isNpc())
			{
				L2NpcInstance npc = (L2NpcInstance) obj;
				npc.pathfindCount++;
				npc.pathfindTime += (System.nanoTime() - buff.useStartedNanos) / 1000000.0;
			}
		}
	}

	public ArrayList<Location> findPath()
	{
		buff.firstNode = GeoNode.initNode(buff, buff.startpoint.x - buff.offsetX, buff.startpoint.y - buff.offsetY, buff.startpoint, refIndex);
		buff.firstNode.closed = true;

		GeoNode nextNode = buff.firstNode, finish = null;
		int i = buff.info.maxIterations;

		while(nextNode != null && i-- > 0)
		{
			if((finish = handleNode(nextNode)) != null)
				return tracePath(finish);
			nextNode = getBestOpenNode();
			if(custom_debug && PATHFIND_DEBUG  && nextNode != null)
			{
				//addDebugItem(3436, 1, nextNode.getLoc().geo2world());
				try
				{
					Thread.sleep(250);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		if(GEODATA_DEBUG  && buff.isPlayer)
			System.out.println("Pathfind failed, maxIterations = " + buff.info.maxIterations);

		return null;
	}

	private GeoNode getBestOpenNode()
	{
		GeoNode bestNodeLink = null;
		GeoNode oldNode = buff.firstNode;
		GeoNode nextNode = buff.firstNode.link;

		while(nextNode != null)
		{
			if(bestNodeLink == null || nextNode.score < bestNodeLink.link.score)
				bestNodeLink = oldNode;
			oldNode = nextNode;
			nextNode = oldNode.link;
		}

		if(bestNodeLink != null)
		{
			bestNodeLink.link.closed = true;
			GeoNode bestNode = bestNodeLink.link;
			bestNodeLink.link = bestNode.link;
			if(bestNode == buff.currentNode)
				buff.currentNode = bestNodeLink;
			return bestNode;
		}

		return null;
	}

	private ArrayList<Location> tracePath(GeoNode f)
	{
		ArrayList<Location> locations = new ArrayList<Location>();
		do
		{
			locations.add(0, f.getLoc());
			f = f.parent;
		} while(f.parent != null);
		return locations;
	}

	public GeoNode handleNode(GeoNode node)
	{
		GeoNode result = null;

		int clX = node._x;
		int clY = node._y;
		short clZ = node._z;

		getHeightAndNSWE(clX, clY, clZ);
		short NSWE = buff.hNSWE[1];

		if(PATHFIND_DIAGONAL )
		{
			// Юго-восток
			if((NSWE & SOUTH) == SOUTH && (NSWE & EAST) == EAST)
			{
				getHeightAndNSWE(clX + 1, clY, clZ);
				if((buff.hNSWE[1] & SOUTH) == SOUTH)
				{
					getHeightAndNSWE(clX, clY + 1, clZ);
					if((buff.hNSWE[1] & EAST) == EAST)
					{
						result = getNeighbour(clX + 1, clY + 1, node, true);
						if(result != null)
							return result;
					}
				}
			}

			// Юго-запад
			if((NSWE & SOUTH) == SOUTH && (NSWE & WEST) == WEST)
			{
				getHeightAndNSWE(clX - 1, clY, clZ);
				if((buff.hNSWE[1] & SOUTH) == SOUTH)
				{
					getHeightAndNSWE(clX, clY + 1, clZ);
					if((buff.hNSWE[1] & WEST) == WEST)
					{
						result = getNeighbour(clX - 1, clY + 1, node, true);
						if(result != null)
							return result;
					}
				}
			}

			// Северо-восток
			if((NSWE & NORTH) == NORTH && (NSWE & EAST) == EAST)
			{
				getHeightAndNSWE(clX + 1, clY, clZ);
				if((buff.hNSWE[1] & NORTH) == NORTH)
				{
					getHeightAndNSWE(clX, clY - 1, clZ);
					if((buff.hNSWE[1] & EAST) == EAST)
					{
						result = getNeighbour(clX + 1, clY - 1, node, true);
						if(result != null)
							return result;
					}
				}
			}

			// Северо-запад
			if((NSWE & NORTH) == NORTH && (NSWE & WEST) == WEST)
			{
				getHeightAndNSWE(clX - 1, clY, clZ);
				if((buff.hNSWE[1] & NORTH) == NORTH)
				{
					getHeightAndNSWE(clX, clY - 1, clZ);
					if((buff.hNSWE[1] & WEST) == WEST)
					{
						result = getNeighbour(clX - 1, clY - 1, node, true);
						if(result != null)
							return result;
					}
				}
			}
		}

		// Восток
		if((NSWE & EAST) == EAST)
		{
			result = getNeighbour(clX + 1, clY, node, false);
			if(result != null)
				return result;
		}

		// Запад
		if((NSWE & WEST) == WEST)
		{
			result = getNeighbour(clX - 1, clY, node, false);
			if(result != null)
				return result;
		}

		// Юг
		if((NSWE & SOUTH) == SOUTH)
		{
			result = getNeighbour(clX, clY + 1, node, false);
			if(result != null)
				return result;
		}

		// Север
		if((NSWE & NORTH) == NORTH)
			result = getNeighbour(clX, clY - 1, node, false);

		return result;
	}

	public GeoNode getNeighbour(int x, int y, GeoNode from, boolean d)
	{
		int nX = x - buff.offsetX, nY = y - buff.offsetY;
		if(nX >= buff.info.MapSize || nX < 0 || nY >= buff.info.MapSize || nY < 0)
			return null;

		boolean isOldNull = GeoNode.isNull(buff.nodes[nX][nY]);
		if(!isOldNull && buff.nodes[nX][nY].closed)
			return null;

		GeoNode n = isOldNull ? GeoNode.initNode(buff, nX, nY, x, y, from._z, from, refIndex) : buff.tempNode.reuse(buff.nodes[nX][nY], from);

		int height = Math.abs(n._z - from._z);

		if(height > PATHFIND_MAX_Z_DIFF || n._nswe == NSWE_NONE)
			return null;

		double weight;
		if (d)
			weight = 1.414213562373095 * WEIGHT0;
		else
			weight = WEIGHT0;

		if(n._nswe != NSWE_ALL || height > 16)
			weight = WEIGHT1;
		else
			// Цикл только для удобства
			while(buff.isPlayer || SIMPLE_PATHFIND_FOR_MOBS)
			{
				getHeightAndNSWE(x + 1, y, n._z);
				if(buff.hNSWE[1] != NSWE_ALL || Math.abs(n._z - buff.hNSWE[0]) > 16)
				{
					weight = WEIGHT2;
					break;
				}

				getHeightAndNSWE(x - 1, y, n._z);
				if(buff.hNSWE[1] != NSWE_ALL || Math.abs(n._z - buff.hNSWE[0]) > 16)
				{
					weight = WEIGHT2;
					break;
				}

				getHeightAndNSWE(x, y + 1, n._z);
				if(buff.hNSWE[1] != NSWE_ALL || Math.abs(n._z - buff.hNSWE[0]) > 16)
				{
					weight = WEIGHT2;
					break;
				}

				getHeightAndNSWE(x, y - 1, n._z);
				if(buff.hNSWE[1] != NSWE_ALL || Math.abs(n._z - buff.hNSWE[0]) > 16)
				{
					weight = WEIGHT2;
					break;
				}

				break;
			}

		int diffx = buff.endpoint.x - x;
		int diffy = buff.endpoint.y - y;
		//int diffx = Math.abs(buff.endpoint.x - x);
		//int diffy = Math.abs(buff.endpoint.y - y);
		int dz = Math.abs(buff.endpoint.z - n._z);

		n.moveCost += from.moveCost + weight;
		n.score = n.moveCost + (PATHFIND_DIAGONAL ? Math.sqrt(diffx * diffx + diffy * diffy + dz * dz / 256) : Math.abs(diffx) + Math.abs(diffy) + dz / 16); // 256 = 16*16
		//n.score = n.moveCost + diffx + diffy + dz / 16;

		if(x == buff.endpoint.x && y == buff.endpoint.y && dz < 64)
			return n; // ура, мы дошли до точки назначения :)

		if(isOldNull)
		{
			if(buff.currentNode == null)
				buff.firstNode.link = n;
			else
				buff.currentNode.link = n;
			buff.currentNode = n;

			//if(custom_debug && PATHFIND_DEBUG)
			//	addDebugItem(57, 1, n.getLoc().geo2world());

		} // если !isOldNull, значит эта клетка уже присутствует, в n находится временный Node содержимое которого нужно скопировать 
		else if(n.moveCost < buff.nodes[nX][nY].moveCost)
			buff.nodes[nX][nY].copy(n);

		return null;
	}

	private void getHeightAndNSWE(int x, int y, short z)
	{
		int nX = x - buff.offsetX, nY = y - buff.offsetY;
		if(nX >= buff.info.MapSize || nX < 0 || nY >= buff.info.MapSize || nY < 0)
		{
			buff.hNSWE[1] = NSWE_NONE; // Затычка
			return;
		}
		GeoNode n = buff.nodes[nX][nY];
		if(n == null)
			n = GeoNode.initNodeGeo(buff, nX, nY, x, y, z, refIndex);
		buff.hNSWE[0] = n._z;
		buff.hNSWE[1] = n._nswe;
	}

	public ArrayList<Location> getPath()
	{
		return path;
	}

	/*private static GArray<L2ItemInstance> debugItems;

	private static void addDebugItem(int item_id, int item_count, Location loc)
	{
		L2ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if(item_count > 1)
			item.setCount(item_count);
		debugItems.add(item);
		item.dropMe(null, loc);
	}

	protected static void debugPath(L2Player player, PathFindBuffer pf_buff, ArrayList<Location> _path)
	{
		if(!player.isGM())
			return;

		double dist = pf_buff.startpoint.clone().geo2world().distance(pf_buff.endpoint.clone().geo2world());
		System.out.println(String.format("Path for %s, distance: %.0f, MapSize: %s, size: %s", player.getName(), dist, pf_buff.info.MapSize, (_path == null ? "null" : _path.size())));

		if(debugItems == null)
			debugItems = new GArray<L2ItemInstance>();
		else
		{
			for(L2ItemInstance item : debugItems)
				item.deleteMe();
			debugItems.clear();
		}

		for(int i = 0; i < pf_buff.nodes.length; i++)
			for(int j = 0; j < pf_buff.nodes[i].length; j++)
			{
				if(GeoNode.isNull(pf_buff.nodes[i][j]) || pf_buff.nodes[i][j] == pf_buff.firstNode || _path == null && pf_buff.nodes[i][j].closed)
					continue;

				addDebugItem(pf_buff.nodes[i][j].closed ? 65 : 57, (int) pf_buff.nodes[i][j].moveCost, new Location(pf_buff.nodes[i][j]._x, pf_buff.nodes[i][j]._y, pf_buff.nodes[i][j]._z).clone().geo2world());
			}

		addDebugItem(3433, 1, pf_buff.startpoint.clone().geo2world());
		addDebugItem(3436, 1, pf_buff.endpoint.clone().geo2world());
	}*/
}