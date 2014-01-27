package fw.extensions.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Аналог ArrayList. <br />
 *
 * Конкуррентнобезопасная синхронизированная версия, для итерации создается копия списка. <br />
 * Метод remove для итератора работает но не изменяет основной список
 * 
 * @see fw.extensions.util.SArrayList - синхронизированная версия
 */
public class SArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;
	private long _lastUpdate = -1;
	
	@Override
	public synchronized boolean add(E el) {	
		_lastUpdate = System.currentTimeMillis();
		return super.add(el);
	}
	
	@Override
	public synchronized E remove(int index) {
		_lastUpdate = System.currentTimeMillis();
		return super.remove(index);
	}

	@Override
	public synchronized boolean remove(Object o) {
		_lastUpdate = System.currentTimeMillis();
		return super.remove(o);
	}
	@Override
	public synchronized boolean isEmpty() {
		return super.isEmpty();
	}	
	@Override
	public synchronized int size() {
		return super.size();
	}
	@Override
	public synchronized void clear() {
		_lastUpdate = System.currentTimeMillis();
		super.clear();
	}
	public synchronized long getLastUpdate() {
		return _lastUpdate;
	}
	@Override
	public synchronized Iterator<E> iterator() {
		return super.iterator();
	}
	/** используется только для перебора - никакие изменения над списком не отразятся на оригинале */
	public synchronized ArrayList<E> getCopy() {
		return new ArrayList<E>(this);
	}

}
