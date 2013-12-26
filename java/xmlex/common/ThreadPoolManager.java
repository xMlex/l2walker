package xmlex.common;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ThreadPoolManager {
	private static final Logger _log = Logger.getLogger(ThreadPoolManager.class.getName());

	private static ThreadPoolManager _instance;

	private final ScheduledThreadPoolExecutor _generalScheduledThreadPool;

	private ThreadPoolExecutor _ioPacketsThreadPool;
	private ExecutorService _generalThreads;
	public ThreadPoolExecutor _generalPacketsThreadPool;

	private boolean _shutdown;

	public static ThreadPoolManager getInstance() {
		if (_instance == null)
			_instance = new ThreadPoolManager();
		return _instance;
	}

	private ThreadPoolManager() {
		_generalScheduledThreadPool = new ScheduledThreadPoolExecutor(2, new PriorityThreadFactory(
				"GerenalSTPool", Thread.NORM_PRIORITY + 1));
		_generalScheduledThreadPool.setKeepAliveTime(1, TimeUnit.SECONDS);
		_generalScheduledThreadPool.allowCoreThreadTimeOut(true);

		_ioPacketsThreadPool = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("High Packet Pool",
						Thread.NORM_PRIORITY + 3));

		_generalPacketsThreadPool = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 15L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new PriorityThreadFactory("Normal Packet Pool",
						Thread.NORM_PRIORITY + 3));
		_generalThreads = Executors.newFixedThreadPool(2);
	}

	public ScheduledFuture<?> scheduleGeneral(Runnable r, long delay) {
		try {
			if (delay < 0)
				delay = 0;
			return _generalScheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			if (!isShutdown()) {
				_log.warning("GeneralThreadPool: Failed schedule task!");
				Thread.dumpStack();
			}
			return null; /* shutdown, ignore */
		}
	}

	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable r, long initial, long delay) {
		try {
			if (delay <= 0)
				delay = 1;
			return _generalScheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			if (!isShutdown()) {
				_log.warning("GeneralThreadPool: Failed schedule task at fixed rate!");
				Thread.dumpStack();
			}
			return null; /* shutdown, ignore */
		}
	}

	public void executeGeneral(Runnable r) {
		_generalScheduledThreadPool.execute(r);
	}

	public void executeGeneralPacket(Runnable r) {
		_generalPacketsThreadPool.execute(r);
	}

	public void executeIoPacket(Runnable r) {
		_ioPacketsThreadPool.execute(r);
	}

	public void executeGeneralThread(Runnable r) {
		_generalThreads.submit(r);
	}

	public String[] getStats() {
		return new String[] {
				" ╒══ General:",
				" │ ActiveThreads:   " + _generalScheduledThreadPool.getActiveCount(),
				" │ CorePoolSize:    " + _generalScheduledThreadPool.getCorePoolSize(),
				" │ CompletedTasks:  " + _generalScheduledThreadPool.getCompletedTaskCount(),
				" │ ScheduledTasks:  "
						+ (_generalScheduledThreadPool.getTaskCount() - _generalScheduledThreadPool
								.getCompletedTaskCount()),/*
														 * " ╞══ Move:",
														 * " │ ActiveThreads:   "
														 * +
														 * _moveScheduledThreadPool
														 * .getActiveCount(),
														 * " │ CorePoolSize:    "
														 * +
														 * _moveScheduledThreadPool
														 * .getCorePoolSize(),
														 * " │ CompletedTasks:  "
														 * +
														 * _moveScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * (),
														 * " │ ScheduledTasks:  "
														 * +
														 * (_moveScheduledThreadPool
														 * .getTaskCount() -
														 * _moveScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * ()),
														 * " ╞══ Pathfind:",
														 * " │ ActiveThreads:   "
														 * +
														 * _pathfindThreadPool.
														 * getActiveCount(),
														 * " │ CorePoolSize:    "
														 * +
														 * _pathfindThreadPool.
														 * getCorePoolSize(),
														 * " │ CompletedTasks:  "
														 * +
														 * _pathfindThreadPool.
														 * getCompletedTaskCount
														 * (),
														 * " │ ScheduledTasks:  "
														 * +
														 * (_pathfindThreadPool
														 * .getTaskCount() -
														 * _pathfindThreadPool
														 * .getCompletedTaskCount
														 * ()), " ╞══ Npc AI:",
														 * " │ ActiveThreads:   "
														 * +
														 * _npcAiScheduledThreadPool
														 * .getActiveCount(),
														 * " │ CorePoolSize:    "
														 * +
														 * _npcAiScheduledThreadPool
														 * .getCorePoolSize(),
														 * " │ CompletedTasks:  "
														 * +
														 * _npcAiScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * (),
														 * " │ ScheduledTasks:  "
														 * + (
														 * _npcAiScheduledThreadPool
														 * .getTaskCount() -
														 * _npcAiScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * ()),
														 * " ╞══ Player AI:",
														 * " │ ActiveThreads:   "
														 * +
														 * _playerAiScheduledThreadPool
														 * .getActiveCount(),
														 * " │ CorePoolSize:    "
														 * +
														 * _playerAiScheduledThreadPool
														 * .getCorePoolSize(),
														 * " │ CompletedTasks:  "
														 * +
														 * _playerAiScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * (),
														 * " │ ScheduledTasks:  "
														 * + (
														 * _playerAiScheduledThreadPool
														 * .getTaskCount() -
														 * _playerAiScheduledThreadPool
														 * .
														 * getCompletedTaskCount
														 * ()),
														 */
				/*
				 * " ╞══ Interest", " │ ActiveThreads:   " +
				 * MMOConnection.getPool().getActiveCount(),
				 * " │ CorePoolSize:    " +
				 * MMOConnection.getPool().getCorePoolSize(),
				 * " │ CompletedTasks:  " +
				 * MMOConnection.getPool().getCompletedTaskCount(),
				 * " │ ScheduledTasks:  " +
				 * (MMOConnection.getPool().getTaskCount() -
				 * MMOConnection.getPool().getCompletedTaskCount()),
				 */
				" ╞══ Packets:", " │ ActiveThreads:   " + _generalPacketsThreadPool.getActiveCount(),
				" │ CorePoolSize:    " + _generalPacketsThreadPool.getCorePoolSize(),
				" │ MaximumPoolSize: " + _generalPacketsThreadPool.getMaximumPoolSize(),
				" │ LargestPoolSize: " + _generalPacketsThreadPool.getLargestPoolSize(),
				" │ PoolSize:        " + _generalPacketsThreadPool.getPoolSize(),
				" │ CompletedTasks:  " + _generalPacketsThreadPool.getCompletedTaskCount(),
				" │ QueuedTasks:     " + _generalPacketsThreadPool.getQueue().size(), " ╞══ IO Packets:",
				" │ ActiveThreads:   " + _ioPacketsThreadPool.getActiveCount(),
				" │ CorePoolSize:    " + _ioPacketsThreadPool.getCorePoolSize(),
				" │ LargestPoolSize: " + _ioPacketsThreadPool.getLargestPoolSize(),
				" │ PoolSize:        " + _ioPacketsThreadPool.getPoolSize(),
				" │ CompletedTasks:  " + _ioPacketsThreadPool.getCompletedTaskCount(),
				" │ QueuedTasks:     " + _ioPacketsThreadPool.getQueue().size()/*
																				 * ,
																				 * " ╞══ LS/GS Packets:"
																				 * ,
																				 * " │ ActiveThreads:   "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getActiveCount
																				 * (
																				 * )
																				 * ,
																				 * " │ CorePoolSize:    "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getCorePoolSize
																				 * (
																				 * )
																				 * ,
																				 * " │ MaximumPoolSize: "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getMaximumPoolSize
																				 * (
																				 * )
																				 * ,
																				 * " │ LargestPoolSize: "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getLargestPoolSize
																				 * (
																				 * )
																				 * ,
																				 * " │ PoolSize:        "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getPoolSize
																				 * (
																				 * )
																				 * ,
																				 * " │ CompletedTasks:  "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getCompletedTaskCount
																				 * (
																				 * )
																				 * ,
																				 * " │ QueuedTasks:     "
																				 * +
																				 * _LsGsExecutor
																				 * .
																				 * getQueue
																				 * (
																				 * )
																				 * .
																				 * size
																				 * (
																				 * )
																				 * ,
																				 * " ╘══════════════════"
																				 * ,
																				 */};
	}

	public static class PriorityThreadFactory implements ThreadFactory {
		private int _prio;
		private String _name;
		private AtomicInteger _threadNumber = new AtomicInteger(1);
		private ThreadGroup _group;

		public PriorityThreadFactory(String name, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(_name);
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			return t;
		}

		public ThreadGroup getGroup() {
			return _group;
		}
	}

	/**
	 *
	 */
	public void shutdown() {
		_shutdown = true;
		try {
			// в обратном порядке шатдауним потоки
			if (_generalThreads != null)
				_generalThreads.shutdown();
			if (_ioPacketsThreadPool != null)
				_ioPacketsThreadPool.shutdown();
			if (_generalPacketsThreadPool != null)
				_generalPacketsThreadPool.shutdown();
			if (_generalScheduledThreadPool != null)
				_generalScheduledThreadPool.shutdown();

			// и ждем их завершения
			if (_generalThreads != null)
				_generalThreads.awaitTermination(1, TimeUnit.SECONDS);
			if (_ioPacketsThreadPool != null)
				_ioPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			if (_generalPacketsThreadPool != null)
				_generalPacketsThreadPool.awaitTermination(1, TimeUnit.SECONDS);
			if (_generalScheduledThreadPool != null)
				_generalScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);

			System.out.println("All ThreadPools are now stoped.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getGPacketStats() {
		return getThreadPoolStats(_generalPacketsThreadPool, "general packets");
	}

	public String getIOPacketStats() {
		return getThreadPoolStats(_ioPacketsThreadPool, "IO packets");
	}

	public String getGeneralPoolStats() {
		return getThreadPoolStats(_generalScheduledThreadPool, "general");
	}

	public static String getThreadPoolStats(ThreadPoolExecutor pool, String poolname) {
		ThreadFactory tf = pool.getThreadFactory();
		if (!(tf instanceof PriorityThreadFactory))
			return "This should not be seen, pool " + poolname;

		StringBuilder res = new StringBuilder();
		PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
		int count = ptf.getGroup().activeCount();
		Thread[] threads = new Thread[count + 2];
		ptf.getGroup().enumerate(threads);

		res.append("\r\nThread Pool: ").append(poolname);
		res.append("\r\nTasks in the queue: ").append(pool.getQueue().size());
		res.append("\r\nThreads stack trace:");
		res.append("\r\nThere should be ").append(count).append(" threads\r\n");

		for (Thread t : threads) {
			if (t == null)
				continue;
			res.append("\r\n").append(t.getName());
			// StackTraceElement[] trace = t.getStackTrace();
			// if(trace.length == 0 || trace[0] == null ||
			// trace[0].toString().contains("sun.misc.Unsafe.park"))
			// continue; // Пропускаем пустые
			for (StackTraceElement ste : t.getStackTrace())
				res.append("\r\n\t").append(ste);
		}

		return res.toString();
	}

	public boolean isShutdown() {
		return _shutdown;
	}

	public ScheduledThreadPoolExecutor getGeneralScheduledThreadPool() {
		return _generalScheduledThreadPool;
	}

	public ThreadPoolExecutor getIoPacketsThreadPool() {
		return _ioPacketsThreadPool;
	}

	public ThreadPoolExecutor getGeneralPacketsThreadPool() {
		return _generalPacketsThreadPool;
	}
}