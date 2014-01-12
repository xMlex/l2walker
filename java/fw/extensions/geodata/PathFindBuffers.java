package fw.extensions.geodata;

import java.util.HashMap;

import fw.extensions.util.GArray;
import fw.extensions.util.Location;
import fw.extensions.util.StrTable;

/**
 * @Author: Drin
 * @Date: 27/04/2009
 */
public class PathFindBuffers
{
	/**
	 * буффер размером 100x100 занимает примерно 0.5 мб
	 * буффер размером 128x128 занимает примерно 1.0 мб
	 * буффер размером 192x192 занимает примерно 1.5 мб
	 * буффер размером 256x256 занимает примерно 3.0 мб
	 * буффер размером 320x320 занимает примерно 4.5 мб
	 * буффер размером 384x384 занимает примерно 6.5 мб
	 */

	private static BufferInfo[] all_buffers;

	public static void initBuffers(String s)
	{
		HashMap<Integer, Integer> conf_data = new HashMap<Integer, Integer>();
		String[] k;
		for(String e : s.split(";"))
			if(!e.isEmpty() && (k = e.split("x")).length == 2)
				conf_data.put(Integer.valueOf(k[1]), Integer.valueOf(k[0]));

		BufferInfo[] _allbuffers = new BufferInfo[conf_data.size()];

		int idx = 0;
		Integer lowestKey;
		while(!conf_data.isEmpty())
		{
			lowestKey = null;
			for(Integer ke : conf_data.keySet())
				if(lowestKey == null || lowestKey > ke)
					lowestKey = ke;

			_allbuffers[idx] = new BufferInfo(lowestKey, idx, conf_data.remove(lowestKey));
			idx++;
		}

		all_buffers = _allbuffers;
	}

	public static boolean resizeBuffers(int MapSize, int newCapacity)
	{
		if(newCapacity < 1)
			return false;

		for(int i = 0; i < all_buffers.length; i++)
			if(MapSize == all_buffers[i].MapSize)
			{
				if(newCapacity == all_buffers[i].buffers.getCapacity())
					return true;

				GArray<PathFindBuffer> new_buffers = new GArray<PathFindBuffer>(newCapacity);
				synchronized (all_buffers[i])
				{
					while(all_buffers[i].buffers.size() > newCapacity)
						all_buffers[i].buffers.removeLast();
					new_buffers.addAll(all_buffers[i].buffers);
					all_buffers[i].buffers = new_buffers;
				}

				return true;
			}

		return false;
	}

	private static PathFindBuffer alloc(BufferInfo fine_buffer)
	{
		synchronized (fine_buffer)
		{
			// ищем свободный буффер
			for(PathFindBuffer b : fine_buffer.buffers)
				if(!b.inUse)
				{
					b.inUse = true;
					return b;
				}
			// если нет свободного буффера то создаем новый

			PathFindBuffer result = new PathFindBuffer(fine_buffer);
			// и если для него еще есть место то ложим его в список что бы заюзать потом снова
			if(fine_buffer.buffers.size() < fine_buffer.buffers.getCapacity())
			{
				result.inUse = true;
				fine_buffer.buffers.add(result);
			}
			else
				fine_buffer.overBuffers++;
			return result;
		}
	}

	public static PathFindBuffer alloc(int mapSize, boolean isPlayer, Location startpoint, Location endpoint, Location native_endpoint)
	{
		if(mapSize % 2 > 0)
			mapSize--; // для четности

		BufferInfo fine_buffer = null;
		for(int i = 0; i < all_buffers.length; i++)
			if(mapSize <= all_buffers[i].MapSize)
			{
				fine_buffer = all_buffers[i];
				mapSize = all_buffers[i].MapSize;
				break;
			}

		if(fine_buffer == null)
			return null; // запрошен слишком большой буффер

		PathFindBuffer result = alloc(fine_buffer);
		result.useStartedNanos = System.nanoTime();
		result.isPlayer = isPlayer;
		result.startpoint = startpoint;
		result.endpoint = endpoint;
		result.native_endpoint = native_endpoint;
		result.offsetX = startpoint.x - mapSize / 2;
		result.offsetY = startpoint.y - mapSize / 2;

		return result;
	}

	public static class PathFindBuffer
	{
		final short[] hNSWE = new short[2];
		final GeoNode[][] nodes;
		final BufferInfo info;

		boolean isPlayer, inUse;
		Location startpoint, endpoint, native_endpoint;
		int offsetX, offsetY;
		public long useStartedNanos;

		GeoNode firstNode, currentNode, tempNode;

		public PathFindBuffer(BufferInfo inf)
		{
			nodes = new GeoNode[inf.MapSize][inf.MapSize];
			tempNode = new GeoNode();
			info = inf;
		}

		public void free()
		{
			if(!inUse)
				return;

			for(int i = 0; i < nodes.length; i++)
				for(int j = 0; j < nodes[i].length; j++)
					if(nodes[i][j] != null)
						nodes[i][j].free();

			firstNode = null;
			currentNode = null;
			endpoint = null;
			currentNode = null;

			info.totalUses++;
			if(isPlayer)
				info.playableUses++;
			info.useTimeMillis += (System.nanoTime() - useStartedNanos) / 1000000.0;

			inUse = false;
		}
	}

	public static class GeoNode
	{
		public int _x, _y;
		public short _z, _nswe;
		public double score = 0., moveCost = 0.;
		public boolean closed = false;

		public GeoNode link = null, parent = null;

		public void free()
		{
			score = -1;
			link = null;
			parent = null;
			_z = 0;
		}

		public static GeoNode initNode(PathFindBuffer buff, int bx, int by, int x, int y, short z, GeoNode parentNode, int refIndex)
		{
			GeoNode result;

			if(buff == null)
			{
				result = new GeoNode();
				result._x = x;
				result._y = y;
				result._z = z;
				result.moveCost = 0.;
				result.parent = parentNode;
				result.score = 0;
				result.closed = false;
				return result;
			}

			if(buff.nodes[bx][by] == null)
				buff.nodes[bx][by] = new GeoNode();
			result = buff.nodes[bx][by];

			if(result._x != x || result._y != y || result._z == 0 || Math.abs(z - result._z) > 64)
			{
				GeoEngine.NgetHeightAndNSWE(x, y, z, buff.hNSWE, refIndex);
				result._x = x;
				result._y = y;
				result._z = buff.hNSWE[0];
				result._nswe = buff.hNSWE[1];
			}

			result.moveCost = 0.;
			result.parent = parentNode;
			result.score = 0;
			result.closed = false;
			return result;
		}

		public static GeoNode initNode(PathFindBuffer buff, int bx, int by, Location loc, int refIndex)
		{
			return initNode(buff, bx, by, loc.x, loc.y, (short) loc.z, null, refIndex);
		}

		public static boolean isNull(GeoNode node)
		{
			return node == null || node.score == -1;
		}

		public static GeoNode initNodeGeo(PathFindBuffer buff, int bx, int by, int x, int y, short z, int refIndex)
		{
			GeoNode result;

			if(buff.nodes[bx][by] == null)
				buff.nodes[bx][by] = new GeoNode();
			result = buff.nodes[bx][by];

			GeoEngine.NgetHeightAndNSWE(x, y, z, buff.hNSWE, refIndex);
			result._x = x;
			result._y = y;
			result._z = buff.hNSWE[0];
			result._nswe = buff.hNSWE[1];

			result.score = -1;

			return result;
		}

		public GeoNode reuse(GeoNode old, GeoNode parentNode)
		{
			_x = old._x;
			_y = old._y;
			_z = old._z;
			_nswe = old._nswe;
			moveCost = 0.;
			closed = old.closed;
			parent = parentNode;
			return this;
		}

		public void copy(GeoNode old)
		{
			_x = old._x;
			_y = old._y;
			_z = old._z;
			_nswe = old._nswe;
			moveCost = old.moveCost;
			score = old.score;
			closed = old.closed;
		}

		public Location getLoc()
		{
			return new Location(_x, _y, _z);
		}

		@Override
		public String toString()
		{
			return "GeoNode: " + _x + "\t" + _y + "\t" + _z;
		}
	}

	public static class BufferInfo
	{
		final int MapSize, sqMapSize, maxIterations, index;
		private int overBuffers = 0, totalUses = 0, playableUses = 0;
		private double useTimeMillis = 0;
		private GArray<PathFindBuffer> buffers;

		public BufferInfo(int mapSize, int idx, int buffersCount)
		{
			buffers = new GArray<PathFindBuffer>(buffersCount);
			MapSize = mapSize;
			sqMapSize = mapSize * mapSize;
			index = idx;
			if(sqMapSize <= 10000) //TODO оттюнить
				maxIterations = sqMapSize / 2;
			else if(sqMapSize < 30000)
				maxIterations = sqMapSize / 3;
			else
				maxIterations = sqMapSize / 4;
		}
	}

	public static StrTable getStats()
	{
		StrTable table = new StrTable("PathFind Buffers Stats");
		long inUse, pathFindsTotal = 0, pathFindsPlayable = 0;
		double allTimeMillis = 0;

		for(BufferInfo buff : all_buffers)
		{
			pathFindsTotal += buff.totalUses;
			pathFindsPlayable += buff.playableUses;
			allTimeMillis += buff.useTimeMillis;

			inUse = 0;
			synchronized (buff)
			{
				for(PathFindBuffer b : buff.buffers)
					if(b.inUse)
						inUse++;
			}
			table.set(buff.index, "Size", buff.MapSize);
			table.set(buff.index, "Use", inUse);
			table.set(buff.index, "Uses", buff.totalUses);
			table.set(buff.index, "Alloc", buff.buffers.size() + " of " + buff.buffers.getCapacity());
			table.set(buff.index, "unbuf", buff.overBuffers);
			if(buff.totalUses > 0)
				table.set(buff.index, "Avg ms", String.format("%1.3f", buff.useTimeMillis / buff.totalUses));
		}

		table.addTitle("Total / Playable  : " + pathFindsTotal + " / " + pathFindsPlayable);
		table.addTitle("Total(s) / Avg(ms): " + String.format("%1.2f", allTimeMillis / 1000) + " / " + String.format("%1.3f", allTimeMillis / pathFindsTotal));

		return table;
	}
}