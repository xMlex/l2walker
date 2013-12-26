package fw.extensions.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Аналог ArrayList, но намного быстрее удаляет. <br />
 * Вместо сдвига всего массива перемещает последний элемент списка на место удаленного. <br />
 * Побочный эффект - размер массива при удалении элемента не меняется, но можно использовать clear. <br />
 * 
 * Конкуррентнобезопасная синхронизированная версия, для итерации создается копия списка. <br />
 * Метод remove для итератора не работает и возвращает IllegalStateException. 
 * 
 * @see l2open.util.GArray - базовая версия
 * @see l2open.util.GCArray - конкуррентнобезопасная версия
 * @see l2open.util.GSArray - синхронизированная версия
 */
public class GCSArray<E> implements Collection<E>
{
	private transient E[] elementData;
	private int size;

	@SuppressWarnings("unchecked")
	public GCSArray(int initialCapacity)
	{
		super();
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		this.elementData = (E[]) new Object[initialCapacity];
	}

	public GCSArray()
	{
		this(10);
	}

	public synchronized void ensureCapacity(int minCapacity)
	{
		int oldCapacity = elementData.length;
		if(minCapacity > oldCapacity)
		{
			int newCapacity = oldCapacity * 3 / 2 + 1;
			if(newCapacity < minCapacity)
				newCapacity = minCapacity;
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}

	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public synchronized E[] toNativeArray()
	{
		return Arrays.copyOf(elementData, size);
	}

	public synchronized Object[] toArray()
	{
		return Arrays.copyOf(elementData, size);
	}

	@SuppressWarnings("unchecked")
	public synchronized <T> T[] toArray(T[] a)
	{
		if(a.length < size)
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		System.arraycopy(elementData, 0, a, 0, size);
		if(a.length > size)
			a[size] = null;
		return a;
	}

	public synchronized E get(int index)
	{
		RangeCheck(index);
		return elementData[index];
	}

	public synchronized boolean add(E e)
	{
		ensureCapacity(size + 1);
		elementData[size++] = e;
		return true;
	}

	public synchronized boolean remove(Object o)
	{
		if(o == null)
		{
			for(int index = 0; index < size; index++)
				if(elementData[index] == null)
				{
					remove(index);
					return true;
				}
		}
		else
			for(int index = 0; index < size; index++)
				if(o.equals(elementData[index]))
				{
					remove(index);
					return true;
				}
		return false;
	}

	public synchronized E remove(int index)
	{
		RangeCheck(index);
		E old = elementData[index];
		elementData[index] = elementData[size - 1];
		elementData[--size] = null;
		return old;
	}

	public synchronized E set(int index, E element)
	{
		RangeCheck(index);
		E oldValue = elementData[index];
		elementData[index] = element;
		return oldValue;
	}

	public synchronized int indexOf(Object o)
	{
		if(o == null)
		{
			for(int i = 0; i < size; i++)
				if(elementData[i] == null)
					return i;
		}
		else
			for(int i = 0; i < size; i++)
				if(o.equals(elementData[i]))
					return i;
		return -1;
	}

	public synchronized boolean contains(Object o)
	{
		if(o == null)
		{
			for(int i = 0; i < size; i++)
				if(elementData[i] == null)
					return true;
		}
		else
			for(int i = 0; i < size; i++)
				if(o.equals(elementData[i]))
					return true;
		return false;
	}

	public synchronized boolean addAll(Collection<? extends E> c)
	{
		boolean modified = false;
		Iterator<? extends E> e = c.iterator();
		while(e.hasNext())
			if(add(e.next()))
				modified = true;
		return modified;
	}

	public synchronized boolean removeAll(Collection<?> c)
	{
		boolean modified = false;
		for(int i = 0; i < size; i++)
			if(c.contains(elementData[i]))
			{
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		return modified;
	}

	public synchronized boolean retainAll(Collection<?> c)
	{
		boolean modified = false;
		for(int i = 0; i < size; i++)
			if(!c.contains(elementData[i]))
			{
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		return modified;
	}

	public synchronized boolean containsAll(Collection<?> c)
	{
		for(int i = 0; i < size; i++)
			if(!contains(elementData[i]))
				return false;
		return true;
	}

	private void RangeCheck(int index)
	{
		if(index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}

	@SuppressWarnings("unchecked")
	public synchronized void clear()
	{
		int oldSize = size;
		size = 0;
		if(oldSize > 1000)
			elementData = (E[]) new Object[10];
		else
			for(int i = 0; i < oldSize; i++)
				elementData[i] = null;
		size = 0;
	}

	/**
	 * Осторожно, при таком очищении в массиве могут оставаться ссылки на обьекты, 
	 * удерживающие эти обьекты в памяти!
	 */
	public synchronized void clearSize()
	{
		size = 0;
	}

	public synchronized Iterator<E> iterator()
	{
		return new Itr();
	}

	private class Itr implements Iterator<E>
	{
		E[] data = toNativeArray();
		int size = data.length;
		int cursor = 0;

		public boolean hasNext()
		{
			return cursor != size;
		}

		public E next()
		{
			try
			{
				return data[cursor++];
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new NoSuchElementException();
			}
		}

		/**
		 * НЕ РАБОТАЕТ!
		 */
		public void remove()
		{
			throw new IllegalStateException();
		}
	}
}