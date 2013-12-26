package fw.extensions.util;

import javolution.util.FastMap;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

public class Files
{
	private static Logger _log = Logger.getLogger(Strings.class.getName());

	private static final FastMap<String, String> cache = new FastMap<String, String>().setShared(true);

	public static String read(String name)
	{
		if(name == null)
			return null;

		if(true && cache.containsKey(name))
			return cache.get(name);

		File file = new File("./" + name);

		//		_log.info("Get file "+file.getPath());

		if(!file.exists())
			return null;

		String content = null;

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String s = "";
			while((s = br.readLine()) != null)
				sb.append(s).append("\n");
			content = sb.toString();
			sb = null;
		}
		catch(Exception e)
		{ /* problem are ignored */}
		finally
		{
			try
			{
				if(br != null)
					br.close();
			}
			catch(Exception e1)
			{ /* problems ignored */}
		}

		if(true)
			cache.put(name, content);

		return content;
	}

	public static void cacheClean()
	{
		cache.clear();
	}

	public static long lastModified(String name)
	{
		if(name == null)
			return 0;

		return new File(name).lastModified();
	}

	/*public static String read(String name, L2Player player)
	{
		if(player == null)
			return "";
		return read(name, player.getLang());
	}*/

	public static String langFileName(String name, String lang)
	{
		if(lang == null || lang.equalsIgnoreCase("en"))
			lang = "";

		String tmp;

		tmp = name.replaceAll("(.+)(\\.htm)", "$1-" + lang + "$2");
		if(!tmp.equals(name) && lastModified(tmp) > 0)
			return tmp;

		tmp = name.replaceAll("(.+)(/[^/].+\\.htm)", "$1/" + lang + "$2");
		if(!tmp.equals(name) && lastModified(tmp) > 0)
			return tmp;

		tmp = name.replaceAll("(.+?/html)/", "$1-" + lang + "/");
		if(!tmp.equals(name) && lastModified(tmp) > 0)
			return tmp;

		if(lastModified(name) > 0)
			return name;

		return null;
	}

	public static String read(String name, String lang)
	{
		String tmp = langFileName(name, lang);

		long last_modif = lastModified(tmp); // время модификации локализованного файла
		if(last_modif > 0) // если он существует
		{
			if(last_modif >= lastModified(name) || !true) // и новее оригинального файла
				return Strings.bbParse(read(tmp)); // то вернуть локализованный

			_log.warning("Last modify of " + name + " more then " + tmp); // если он существует но устарел - выругаться в лог
		}

		return Strings.bbParse(read(name)); // если локализованный файл отсутствует вернуть оригинальный
	}

	/**
	 * Сохраняет строку в файл в кодировке UTF-8.<br>
	 * Если такой файл существует, то перезаписывает его.
	 * @param path путь к файлу
	 * @param string сохраняемая строка
	 */
	public static void writeFile(String path, String string)
	{
		if(string == null || string.length() == 0)
			return;

		File target = new File(path);

		if(!target.exists())
			try
			{
				target.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace(System.err);
			}

		try
		{
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(string.getBytes("UTF-8"));
			fos.close();
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	@SuppressWarnings("resource")
	public static boolean copyFile(String pathSource, String pathDest)
	{
		try
		{
			FileChannel source = new FileInputStream(pathSource).getChannel();
			FileChannel destination = new FileOutputStream(pathDest).getChannel();

			destination.transferFrom(source, 0, source.size());

			source.close();
			destination.close();
		}
		catch(IOException e)
		{
			return false;
		}
		return true;
	}
}