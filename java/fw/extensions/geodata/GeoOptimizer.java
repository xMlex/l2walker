package fw.extensions.geodata;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;
import java.util.zip.CRC32;

import fw.extensions.util.GArray;

public class GeoOptimizer
{
	private static final Logger log = Logger.getLogger(GeoEngine.class.getName());
	public static int[][][] checkSums;
	private static final byte version = 1;

	public static class GeoBlocksMatchFinder implements Runnable
	{
		private final int geoX, geoY, rx, ry, maxScanRegions;
		private final String fileName;

		public GeoBlocksMatchFinder(int _geoX, int _geoY, int _maxScanRegions)
		{
			super();
			geoX = _geoX;
			geoY = _geoY;
			rx = geoX + L2World.GEO_X_FIRST;
			ry = geoY + L2World.GEO_Y_FIRST;
			maxScanRegions = _maxScanRegions;
			fileName = "./geodata/matches/" + rx + "_" + ry + ".matches";
		}

		private boolean exists()
		{
			return new File(fileName).exists();
		}

		private void saveToFile(BlockLink[] links)
		{
			log.info("Saving matches to: " + fileName);
			try
			{
				File f = new File(fileName);
				if(f.exists())
					f.delete();
				FileChannel wChannel = new RandomAccessFile(f, "rw").getChannel();
				ByteBuffer buffer = wChannel.map(FileChannel.MapMode.READ_WRITE, 0, links.length * 6 + 1);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				buffer.put(version);
				for(int i = 0; i < links.length; i++)
				{
					buffer.putShort((short) links[i].blockIndex);
					buffer.put(links[i].linkMapX);
					buffer.put(links[i].linkMapY);
					buffer.putShort((short) links[i].linkBlockIndex);
				}
				wChannel.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		private void calcMatches(int[] curr_checkSums, int mapX, int mapY, GArray<BlockLink> putlinks, boolean[] notready)
		{
			int[] next_checkSums = checkSums[mapX][mapY];
			if(next_checkSums == null)
				return;

			int startIdx2;
			for(int blockIdx = 0; blockIdx < GeoEngine.BlocksInMap; blockIdx++)
				if(notready[blockIdx])
				{
					startIdx2 = next_checkSums == curr_checkSums ? blockIdx + 1 : 0;
					for(int blockIdx2 = startIdx2; blockIdx2 < GeoEngine.BlocksInMap; blockIdx2++)
						if(curr_checkSums[blockIdx] == next_checkSums[blockIdx2])
							if(GeoEngine.compareGeoBlocks(geoX, geoY, blockIdx, mapX, mapY, blockIdx2))
							{
								putlinks.add(new BlockLink(blockIdx, (byte) mapX, (byte) mapY, blockIdx2));
								notready[blockIdx] = false;
								break;
							}
				}
		}

		private BlockLink[] gen()
		{
			log.info("Searching matches for " + rx + "_" + ry);
			long started = System.currentTimeMillis();

			boolean[] notready = new boolean[GeoEngine.BlocksInMap];
			for(int i = 0; i < GeoEngine.BlocksInMap; i++)
				notready[i] = true;

			GArray<BlockLink> links = new GArray<BlockLink>();
			int[] _checkSums = checkSums[geoX][geoY];

			int n = 0;
			for(int mapX = geoX; mapX < L2World.WORLD_SIZE_X; mapX++)
			{
				int startgeoY = mapX == geoX ? geoY : 0;
				for(int mapY = startgeoY; mapY < L2World.WORLD_SIZE_Y; mapY++)
				{
					//log.info("Searching matches for " + rx + "_" + ry + " in " + (mapX + GeoEngine.MAP_X_FIRST) + "_" + (mapY + GeoEngine.MAP_Y_FIRST) + ", already found matches: " + links.size());
					calcMatches(_checkSums, mapX, mapY, links, notready);
					n++;
					if(maxScanRegions > 0 && maxScanRegions == n)
						return links.toArray(new BlockLink[links.size()]);
				}
			}

			started = System.currentTimeMillis() - started;
			log.info("Founded " + links.size() + " matches for " + rx + "_" + ry + " in " + started / 1000f + "s");
			return links.toArray(new BlockLink[links.size()]);
		}

		public void run()
		{
			if(!exists())
			{
				BlockLink[] links = gen();
				saveToFile(links);
			}
		}
	}

	public static class CheckSumLoader implements Runnable
	{
		private final int geoX, geoY, rx, ry;
		private final byte[][][] region;
		private final String fileName;

		public CheckSumLoader(int _geoX, int _geoY, byte[][][] _region)
		{
			super();
			geoX = _geoX;
			geoY = _geoY;
			rx = geoX + L2World.GEO_X_FIRST;
			ry = _geoY + L2World.GEO_Y_FIRST;
			region = _region;
			fileName = "./geodata/checksum/" + rx + "_" + ry + ".crc";
		}

		private boolean loadFromFile()
		{
			File GeoCrc = new File(fileName);
			if(!GeoCrc.exists())
				return false;
			try
			{
				FileChannel roChannel = new RandomAccessFile(GeoCrc, "r").getChannel();
				if(roChannel.size() != GeoEngine.BlocksInMap * 4)
				{
					roChannel.close();
					return false;
				}

				ByteBuffer buffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, roChannel.size());
				roChannel.close();
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				int[] _checkSums = new int[GeoEngine.BlocksInMap];
				for(int i = 0; i < GeoEngine.BlocksInMap; i++)
					_checkSums[i] = buffer.getInt();
				checkSums[geoX][geoY] = _checkSums;
				return true;

			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		private void saveToFile()
		{
			log.info("Saving checksums to: " + fileName);
			FileChannel wChannel;
			try
			{
				File f = new File(fileName);
				if(f.exists())
					f.delete();
				wChannel = new RandomAccessFile(f, "rw").getChannel();
				ByteBuffer buffer = wChannel.map(FileChannel.MapMode.READ_WRITE, 0, GeoEngine.BlocksInMap * 4);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				int[] _checkSums = checkSums[geoX][geoY];
				for(int i = 0; i < GeoEngine.BlocksInMap; i++)
					buffer.putInt(_checkSums[i]);
				wChannel.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		private void gen()
		{
			log.info("Generating checksums for " + rx + "_" + ry);
			int[] _checkSums = new int[GeoEngine.BlocksInMap];
			CRC32 crc32 = new CRC32();
			for(int i = 0; i < GeoEngine.BlocksInMap; i++)
			{
				crc32.update(region[i][0]);
				_checkSums[i] = (int) (crc32.getValue() ^ 0xFFFFFFFF);
				crc32.reset();
			}
			checkSums[geoX][geoY] = _checkSums;
		}

		public void run()
		{
			if(!loadFromFile())
			{
				gen();
				saveToFile();
			}
		}
	}

	public static class BlockLink
	{
		public final int blockIndex, linkBlockIndex;
		public final byte linkMapX, linkMapY;

		public BlockLink(short _blockIndex, byte _linkMapX, byte _linkMapY, short _linkBlockIndex)
		{
			blockIndex = _blockIndex & 0xFFFF;
			linkMapX = _linkMapX;
			linkMapY = _linkMapY;
			linkBlockIndex = _linkBlockIndex & 0xFFFF;
		}

		public BlockLink(int _blockIndex, byte _linkMapX, byte _linkMapY, int _linkBlockIndex)
		{
			blockIndex = _blockIndex & 0xFFFF;
			linkMapX = _linkMapX;
			linkMapY = _linkMapY;
			linkBlockIndex = _linkBlockIndex & 0xFFFF;
		}
	}

	public static BlockLink[] loadBlockMatches(String fileName)
	{
		File f = new File(fileName);
		if(!f.exists())
			return null;
		try
		{
			FileChannel roChannel = new RandomAccessFile(f, "r").getChannel();

			int count = (int) ((roChannel.size() - 1) / 6);
			ByteBuffer buffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, roChannel.size());
			roChannel.close();
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			if(buffer.get() != version)
				return null;

			BlockLink[] links = new BlockLink[count];
			for(int i = 0; i < links.length; i++)
				links[i] = new BlockLink(buffer.getShort(), buffer.get(), buffer.get(), buffer.getShort());

			return links;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}