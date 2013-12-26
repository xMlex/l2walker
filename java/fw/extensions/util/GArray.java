package fw.extensions.util;

import java.util.*;

/**
 * Полный аналог ArrayList, но намного быстрее удаляет. <br />
 * Вместо сдвига всего массива перемещает последний элемент списка на место удаленного. <br />
 * Побочный эффект - размер массива при удалении элемента не меняется, но можно использовать clear. <br /> <br />
 * 
 * Базовая версия.
 * 
 * @see l2open.util.GCArray - конкуррентнобезопасная версия
 * @see l2open.util.GSArray - синхронизированная версия
 * @see l2open.util.GCSArray - конкуррентнобезопасная синхронизированная версия
 */
public class GArray<E> implements Collection<E>
{
	protected transient E[] elementData;
	protected transient int modCount = 0;
	protected int size;

	@SuppressWarnings("unchecked")
	public GArray(int initialCapacity)
	{
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		elementData = (E[]) new Object[initialCapacity];
	}

	public GArray()
	{
		this(10);
	}

	public int getCapacity()
	{
		return elementData.length;
	}

	public void ensureCapacity(int minCapacity)
	{
		modCount++;
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

	public E[] toNativeArray()
	{
		return Arrays.copyOf(elementData, size);
	}

	public Object[] toArray()
	{
		return Arrays.copyOf(elementData, size);
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a)
	{
		if(a.length < size)
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		System.arraycopy(elementData, 0, a, 0, size);
		if(a.length > size)
			a[size] = null;
		return a;
	}

	public E get(int index)
	{
		RangeCheck(index);
		return elementData[index];
	}

	public boolean add(E e)
	{
		ensureCapacity(size + 1);
		elementData[size++] = e;
		return true;
	}

	public boolean remove(Object o)
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

	public E remove(int index)
	{
		modCount++;
		RangeCheck(index);
		E old = elementData[index];
		elementData[index] = elementData[size - 1];
		elementData[--size] = null;
		return old;
	}

	public E removeFirst()
	{
		return size > 0 ? remove(0) : null;
	}

	public E removeLast()
	{
		if(size > 0)
		{
			modCount++;
			size--;
			E old = elementData[size];
			elementData[size] = null;
			return old;
		}
		return null;
	}

	public E set(int index, E element)
	{
		RangeCheck(index);
		E oldValue = elementData[index];
		elementData[index] = element;
		return oldValue;
	}

	public int indexOf(Object o)
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

	public boolean contains(Object o)
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

	public boolean addAll(Collection<? extends E> c)
	{
		if(c == null)
			return false;
		boolean modified = false;
		Iterator<? extends E> e = c.iterator();
		while(e.hasNext())
			if(add(e.next()))
				modified = true;
		return modified;
	}

	public boolean removeAll(Collection<?> c)
	{
		boolean modified = false;
		for(int i = 0; i < size; i++)
			if(c.contains(elementData[i]))
			{
				modCount++;
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		return modified;
	}

	public boolean retainAll(Collection<?> c)
	{
		boolean modified = false;
		for(int i = 0; i < size; i++)
			if(!c.contains(elementData[i]))
			{
				modCount++;
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		return modified;
	}

	public boolean containsAll(Collection<?> c)
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
	public void clear()
	{
		modCount++;
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
	public void clearSize()
	{
		modCount++;
		size = 0;
	}

	public Iterator<E> iterator()
	{
		return new Itr();
	}

	private class Itr implements Iterator<E>
	{
		int cursor = 0;
		int lastRet = -1;
		int expectedModCount = modCount;

		public boolean hasNext()
		{
			return cursor < size();
		}

		public E next()
		{
			checkForComodification();
			try
			{
				E next = get(cursor);
				lastRet = cursor++;
				return next;
			}
			catch(IndexOutOfBoundsException e)
			{
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		public void remove()
		{
			if(lastRet == -1)
				throw new IllegalStateException();
			checkForComodification();

			try
			{
				GArray.this.remove(lastRet);
				if(lastRet < cursor)
					cursor--;
				lastRet = -1;
				expectedModCount = modCount;
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification()
		{
			if(modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	@Override
	public String toString()
	{
		StringBuffer bufer = new StringBuffer();
		for(int i = 0; i < size; i++)
		{
			if(i != 0)
				bufer.append(", ");
			bufer.append(elementData[i]);
		}
		return "<" + bufer + ">";
	}
}