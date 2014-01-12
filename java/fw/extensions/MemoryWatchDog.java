package fw.extensions;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryWatchDog extends Thread
{
	private static final MemoryMXBean mem_bean = ManagementFactory.getMemoryMXBean();
	private static MemoryWatchDog instance = null;

	public static long getMemUsed()
	{
		return mem_bean.getHeapMemoryUsage().getUsed();
	}

	public static double getMemUsedPercentage()
	{
		MemoryUsage heapMemoryUsage = mem_bean.getHeapMemoryUsage();
		return 100f * heapMemoryUsage.getUsed() / heapMemoryUsage.getMax();
	}

	public static String getMemUsedPerc()
	{
		return String.format("%.2f%%", getMemUsedPercentage());
	}

	public static String getMemUsedMb()
	{
		return getMemUsed() / 0x100000 + " Mb";
	}

	public static long getMemMax()
	{
		return mem_bean.getHeapMemoryUsage().getMax();
	}

	public static String getMemMaxMb()
	{
		return getMemMax() / 0x100000 + " Mb";
	}

	public static long getMemFree()
	{
		MemoryUsage heapMemoryUsage = mem_bean.getHeapMemoryUsage();
		return heapMemoryUsage.getMax() - heapMemoryUsage.getUsed();
	}

	public static double getMemFreePercentage()
	{
		return 100f - getMemUsedPercentage();
	}

	public static String getMemFreePerc()
	{
		return String.format("%.2f%%", getMemFreePercentage());
	}

	public static String getMemFreeMb()
	{
		return getMemFree() / 0x100000 + " Mb";
	}

	public static MemoryWatchDog getInstance()
	{
		if(instance == null)
			instance = new MemoryWatchDog(1000);
		return instance;
	}

	private final long sleepInterval;

	public MemoryWatchDog(long interval)
	{
		sleepInterval = interval;
	}

	@Override
	public void run()
	{
		try
		{
			//TODO 
			Thread.sleep(sleepInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}