package fw.extensions.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @Author: Drin
 * @Date: 27/04/2009
 */
public class StrTable
{
	/**
	пример
		public static void main(String[] args)
		{
			StrTable table = new StrTable("Test Table :)");
			table.set(0, "#", 1).set(0, "val", 23.5).set(0, "desc", " value #1");
			table.set(1, "#", 2).set(1, "v", 22.5).set(1, "desc", " value #2");
			table.set(3, "#", 3).set(3, "val", true).set(3, "desc", " bool #3 1334");
			table.set(2, "#", -1).set(2, "v", 22.5).set(2, "desc", "#######");
			System.out.print(table);
		}
	выведет:
	         Test Table :)            
	 ---------------------------------- 
	| #  | val  |     desc      |  v   |
	|----|------|---------------|------|
	| 1  | 23.5 |    value #1   |  -   |
	| 2  |  -   |    value #2   | 22.5 |
	| -1 |  -   |    #######    | 22.5 |
	| 3  | true |  bool #3 1334 |  -   |
	 ---------------------------------- 
	*/

	private final HashMap<Integer, HashMap<String, String>> rows = new HashMap<Integer, HashMap<String, String>>();
	private final LinkedHashMap<String, Integer> columns = new LinkedHashMap<String, Integer>();
	private final GArray<String> titles = new GArray<String>();

	public StrTable(String title)
	{
		if(title != null)
			titles.add(title);
	}

	public StrTable()
	{
		this(null);
	}

	public StrTable set(int rowIndex, String colName, Object value)
	{
		String val = value.toString();
		HashMap<String, String> row;

		synchronized (rows)
		{
			if(rows.containsKey(rowIndex))
				row = rows.get(rowIndex);
			else
			{
				row = new HashMap<String, String>();
				rows.put(rowIndex, row);
			}
		}

		synchronized (row)
		{
			row.put(colName, val);
		}

		synchronized (columns)
		{
			int columnSize;
			if(!columns.containsKey(colName))
				columnSize = Math.max(colName.length(), val.length());
			else if(columns.get(colName) >= (columnSize = val.length()))
				return this;
			columns.put(colName, columnSize);
		}

		return this;
	}

	public StrTable addTitle(String s)
	{
		synchronized (rows)
		{
			titles.add(s);
		}
		return this;
	}

	public static String pad_right(String s, int sz)
	{
		String result = s;
		if((sz -= s.length()) > 0)
			result += repeat(" ", sz);
		return result;
	}

	public static String pad_left(String s, int sz)
	{
		String result = s;
		if((sz -= s.length()) > 0)
			result = repeat(" ", sz) + result;
		return result;
	}

	public static String pad_center(String s, int sz)
	{
		String result = s;
		int i;
		while((i = sz - result.length()) > 0)
			if(i == 1)
				result += " ";
			else
				result = " " + result + " ";
		return result;
	}

	public static String repeat(String s, int sz)
	{
		String result = "";
		for(int i = 0; i < sz; i++)
			result += s;
		return result;
	}

	@Override
	public String toString()
	{
		String[] result;
		synchronized (rows)
		{
			if(columns.isEmpty())
				return "";

			String header = "|";
			String line = "|";
			for(String c : columns.keySet())
			{
				header += pad_center(c, columns.get(c) + 2) + "|";
				line += repeat("-", columns.get(c) + 2) + "|";
			}

			result = new String[rows.size() + 4 + (titles.isEmpty() ? 0 : titles.size() + 1)];
			int i = 0;
			if(!titles.isEmpty())
			{
				result[i++] = " " + repeat("-", header.length() - 2) + " ";
				for(String title : titles)
					result[i++] = "| " + pad_right(title, header.length() - 3) + "|";
			}

			result[i++] = result[result.length - 1] = " " + repeat("-", header.length() - 2) + " ";
			result[i++] = header;
			result[i++] = line;

			for(HashMap<String, String> row : rows.values())
			{
				line = "|";
				for(String c : columns.keySet())
					line += pad_center(row.containsKey(c) ? row.get(c) : "-", columns.get(c) + 2) + "|";
				result[i++] = line;
			}
		}

		return Strings.joinStrings("\r\n", result);
	}

	public String toL2Html()
	{
		return toString().replaceAll("\r\n", "<br1>");
	}
}