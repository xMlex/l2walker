package jext.util;

import java.util.Arrays;

/**
 * Концепт компактного ассоциативного массива. Использует хеши String как ключи.
 */
public class ArrayMap {
	/**
	 * Добавляет элемент в массив. При этом массив может быть пересоздан.
	 */
	public static int[] set(int[] array, String param, int value) {
		if (array == null)
			return new int[] { param.hashCode(), value };

		int loc = find(array, param);
		if (loc == -1) // если такого элемента нет то расширяем массив
		{
			loc = array.length;
			array = Arrays.copyOf(array, array.length + 2);
		}
		array[loc] = param.hashCode();
		array[loc + 1] = value;
		return array;
	}

	/**
	 * Ищет элемент в массиве. Возвращает номер элемента.
	 */
	public static int find(int[] array, String param) {
		if (array == null)
			return -1;
		for (int i = 0; i < array.length; i += 2)
			if (array[i] == param.hashCode())
				return i;
		return -1;
	}

	/**
	 * Врзвращает элемент из массива.
	 */
	public static int get(int[] array, String param) {
		int loc = find(array, param);
		if (loc == -1)
			return 0;
		return array[loc + 1];
	}
}