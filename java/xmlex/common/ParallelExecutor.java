package xmlex.common;

import xmlex.common.ThreadPoolManager.PriorityThreadFactory;

import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelExecutor {
	private ThreadPoolExecutor executor;

	public ParallelExecutor(String name, int prio, int cores) {
		executor = new ThreadPoolExecutor(cores, cores, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(name, prio));
	}

	public ParallelExecutor(String name, int prio) {
		this(name, prio, Runtime.getRuntime().availableProcessors());
	}

	public ParallelExecutor(String name) {
		this(name, Thread.NORM_PRIORITY);
	}

	public ParallelExecutor(int prio) {
		this("Paralel Executor", prio);
	}

	public ParallelExecutor() {
		this("Paralel Executor", Thread.NORM_PRIORITY);
	}

	public boolean waitForFinishAndDestroy(long timeout, TimeUnit unit) throws InterruptedException {
		executor.shutdown();
		if (executor.awaitTermination(timeout, unit)) {
			executor = null;
			return true;
		}
		return false;
	}

	public boolean waitForFinishAndDestroy() throws InterruptedException {
		return waitForFinishAndDestroy(1, TimeUnit.DAYS);
	}

	public void execute(Runnable r) {
		executor.execute(r);
	}

	public void execute(Object obj, Method method, Object... args) {
		execute(TaskCreator.createTask(obj, method, args));
	}

	public void execute(Object obj, Class<?> clazz, String methodName, Object... args)
			throws SecurityException, NoSuchMethodException {
		execute(TaskCreator.createTask(obj, clazz, methodName, args));
	}

	public void execute(Object obj, String className, String methodName, Object... args)
			throws SecurityException, NoSuchMethodException, ClassNotFoundException {
		execute(TaskCreator.createTask(obj, className, methodName, args));
	}

	public void execute(Method method, Object... args) {
		execute(TaskCreator.createTask(method, args));
	}

	public void execute(Class<?> clazz, String methodName, Object... args) throws SecurityException,
			NoSuchMethodException {
		execute(TaskCreator.createTask(clazz, methodName, args));
	}

	public void execute(String className, String methodName, Object... args) throws SecurityException,
			NoSuchMethodException, ClassNotFoundException {
		execute(TaskCreator.createTask(className, methodName, args));
	}
}