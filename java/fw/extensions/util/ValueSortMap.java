package fw.extensions.util;

import java.util.*;

/**
 * This class is used to show how you can sort a java.uti.Map for values. This also 
 * takes care of null and duplicate values present in the map.
 */
@SuppressWarnings("unchecked")
public class ValueSortMap
{
	public Map<Integer, Integer> sortThis(Map<Integer, Integer> map, boolean asc)
	{
		return sortMapByValue(map, asc);
	}

	/**
	 * This method returns the new LinkedHashMap sorted with values for passed Comparater. 
	 * If null values exist they will be put in the last of the returned LinkedHashMap. 
	 * If there are duplicate values they will come together at the values ordering order 
	 * but ordering between same multiple values is ramdom. Passed Map will be intect.
	 * @param inMap Map to be sorted
	 * @param comparator Values will be sorted as per passed Comparater
	 * @return LinkedHashMap Sorted new LinkedHashMap
	 */
	@SuppressWarnings("rawtypes")
	public static LinkedHashMap sortMapByValue(Map inMap, Comparator comparator)
	{
		return sortMapByValue(inMap, comparator, null);
	}

	/**
	 * This method returns the new LinkedHashMap sorted with values for passed ascendingOrder. 
	 * If null values exist they will be put in the last for true value of ascendingOrder or 
	 * will be put on top of the returned LinkedHashMap for false value of ascendingOrder. 
	 * If there are duplicate values they will come together at the values ordering order 
	 * but ordering between same multiple values is ramdom. Passed Map will be intect.
	 * @param inMap Map to be sorted
	 * @param ascendingOrder Values will be sorted as per value of ascendingOrder
	 * @return LinkedHashMap Sorted new LinkedHashMap
	 */
	@SuppressWarnings("rawtypes")
	public static LinkedHashMap sortMapByValue(Map inMap, boolean ascendingOrder)
	{
		return sortMapByValue(inMap, null, new Boolean(ascendingOrder));
	}

	/**
	 * This method returns the new LinkedHashMap sorted with values in ascending order. 
	 * If null values exist they will be put in the last of the returned LinkedHashMap. 
	 * If there are duplicate values they will come together at the values ordering order
	 * but ordering between same multiple values is ramdom. Passed Map will be intect.
	 * @param inMap Map to be sorted
	 * @return LinkedHashMap Sorted new LinkedHashMap
	 */
	@SuppressWarnings("rawtypes")
	public static LinkedHashMap sortMapByValue(Map inMap)
	{
		return sortMapByValue(inMap, null, null);
	}

	/**
	 * This method returns the new LinkedHashMap sorted with values. Values will be sorted
	 * as value of passed comparator if ascendingOrder is null or in order of passed 
	 * ascendingOrder if it is not null.
	 * If null values exist they will be put in the last for true value of ascendingOrder or 
	 * will be put on top of the returned LinkedHashMap for false value of ascendingOrder. 
	 * If there are duplicate values they will come together at the values ordering order 
	 * but ordering between same multiple values is ramdom. Passed Map will be intect.
	 * @param inMap Map to be sorted
	 * @param comparator Values will be sorted as per passed Comparater
	 * @param ascendingOrder Values will be sorted as per value of ascendingOrder
	 * @return LinkedHashMap Sorted new LinkedHashMap
	 */
	@SuppressWarnings("rawtypes")
	private static LinkedHashMap sortMapByValue(Map inMap, Comparator comparator, Boolean ascendingOrder)
	{
		int iSize = inMap.size();

		// Create new LinkedHashMap that need to be returned
		LinkedHashMap sortedMap = new LinkedHashMap(iSize);

		Collection values = inMap.values();
		ArrayList valueList = new ArrayList(values); // To get List of all values in passed Map
		HashSet distinctValues = new HashSet(values); // To know the distinct values in passed Map

		// Do handing for null values. remove them from the list that will be used for sorting
		int iNullValueCount = 0; // Total number of null values present in passed Map
		if(distinctValues.contains(null))
		{
			distinctValues.remove(null);
			for(int i = 0; i < valueList.size(); i++)
			{
				if(valueList.get(i) == null)
				{
					valueList.remove(i);
					iNullValueCount++;
					i--;
					continue;
				}
			}
		}

		// Sort the values of the passed Map
		if(ascendingOrder == null)
		{
			// If Boolean ascendingOrder is null, use passed comparator for order of sorting values
			Collections.sort(valueList, comparator);
		}
		else if(ascendingOrder.booleanValue())
		{
			// If Boolean ascendingOrder is not null and is true, sort values in ascending order  
			Collections.sort(valueList);
		}
		else
		{
			// If Boolean ascendingOrder is not null and is false, sort values in descending order  
			Collections.sort(valueList);
			Collections.reverse(valueList);
		}

		// Check if there are multiple same values exist in passed Map (not considering null values)
		boolean bAllDistinct = true;
		if(iSize != (distinctValues.size() + iNullValueCount))
			bAllDistinct = false;

		Object key = null, value = null, sortedValue;
		Set keySet = null;
		Iterator itKeyList = null;
		HashMap hmTmpMap = new HashMap(iSize);
		HashMap hmNullValueMap = new HashMap();

		if(bAllDistinct)
		{
			// There are no multiple same values in the passed map (without consedring null)
			keySet = inMap.keySet();
			itKeyList = keySet.iterator();
			while(itKeyList.hasNext())
			{
				key = itKeyList.next();
				value = inMap.get(key);

				if(value != null)
					hmTmpMap.put(value, key); // Prepare new temp HashMap with value=key combination
				else
					hmNullValueMap.put(key, value); // Keep all null values in a new temp Map
			}

			if(ascendingOrder != null && !ascendingOrder.booleanValue())
			{
				// As it is descending order, Add Null Values in first place of the LinkedHasMap
				sortedMap.putAll(hmNullValueMap);
			}

			// Put all not null values in returning LinkedHashMap
			for(int i = 0; i < valueList.size(); i++)
			{
				value = valueList.get(i);
				key = hmTmpMap.get(value);

				sortedMap.put(key, value);
			}

			if(ascendingOrder == null || ascendingOrder.booleanValue())
			{
				// Add Null Values in the last of the LinkedHasMap
				sortedMap.putAll(hmNullValueMap);
			}
		}
		else
		{
			// There are some multiple values (with out considering null)
			keySet = inMap.keySet();
			itKeyList = keySet.iterator();
			while(itKeyList.hasNext())
			{
				key = itKeyList.next();
				value = inMap.get(key);

				if(value != null)
					hmTmpMap.put(key, value); // Prepare new temp HashMap with key=value combination
				else
					hmNullValueMap.put(key, value); // Keep all null values in a new temp Map
			}

			if(ascendingOrder != null && !ascendingOrder.booleanValue())
			{
				// As it is descending order, Add Null Values in first place of the LinkedHasMap
				sortedMap.putAll(hmNullValueMap);
			}

			// Put all not null values in returning LinkedHashMap
			for(int i = 0; i < valueList.size(); i++)
			{
				sortedValue = valueList.get(i);

				// Search this value in temp HashMap and if found remove it 
				keySet = hmTmpMap.keySet();
				itKeyList = keySet.iterator();
				while(itKeyList.hasNext())
				{
					key = itKeyList.next();
					value = hmTmpMap.get(key);
					if(value.equals(sortedValue))
					{
						sortedMap.put(key, value);
						hmTmpMap.remove(key);
						break;
					}
				}
			}

			if(ascendingOrder == null || ascendingOrder.booleanValue())
			{
				// Add Null Values in the last of the LinkedHasMap
				sortedMap.putAll(hmNullValueMap);
			}
		}

		return sortedMap;
	}
}
