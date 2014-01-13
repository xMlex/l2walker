package xmlex.config;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import fw.extensions.util.Util;

/**
 * @author : Ragnarok
 * @date : 19.12.10 10:53
 */
public class ConfigSystem {
	private static final Logger log = Logger.getLogger(ConfigSystem.class.getName());
	private static final String dir = "./data/config";
	private static ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<String, String>();

	public static void load() {

		File files = new File(dir);
		if (!files.exists())
			log.warning("WARNING! " + dir + " not exists! Config not loaded!");
		else {
			loadLogConfig();
			parseFiles(files.listFiles());
		}
		Util.getOs();
		//System.setProperty("org.lwjgl.librarypath","/lib/");
		System.setProperty( "org.lwjgl.librarypath", System.getProperty("user.dir")+"/libs/slik2d" );
	}

	public static void loadLogConfig() {
		InputStream is;
		try {
			is = new FileInputStream(dir + "/log.properties");
			LogManager.getLogManager().readConfiguration(is);
			is.close();
			log.info("Console configured.");
		} catch (Exception e) {
			log.warning("WARNING! " + dir + " not exists! Config Log not loaded! " + e.getMessage());
		}
	}

	public static void reload() {
		synchronized (properties) {
			properties = new ConcurrentHashMap<String, String>();
			load();
		}
	}

	private static void parseFiles(File[] files) {
		for (File f : files) {
			if (f.isHidden())
				continue;
			if (f.isDirectory() && !f.getName().contains("defaults"))
				parseFiles(f.listFiles());
			if (f.getName().endsWith(".properties")) {
				try {
					InputStream is = new FileInputStream(f);
					Properties p = new Properties();
					p.load(is);
					loadProperties(p);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void loadProperties(Properties p) {
		for (String name : p.stringPropertyNames()) {
			if (properties.get(name) != null) {
				properties.replace(name, p.getProperty(name).trim());
				log.info("Duplicate properties name \"" + name + "\" replaced with new value.");
			} else if (p.getProperty(name) == null)
				log.info("Null property for key " + name);
			else
				properties.put(name, p.getProperty(name).trim());
		}
		p.clear();
	}

	public static String get(String name) {
		if (properties.get(name) == null)
			log.warning("ConfigSystem: Null value for key: " + name);
		return properties.get(name);
	}

	public static float getFloat(String name) {
		return getFloat(name, Float.MAX_VALUE);
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет false!
	 */
	public static boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет 0x7fffffff
	 */
	public static int getInt(String name) {
		return getInt(name, Integer.MAX_VALUE);
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет пустой массив
	 * размером 1
	 */
	/*
	 * public static int[] getIntArray(String name) { return getIntArray(name,
	 * new int[0]); }
	 */

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет значение
	 * 0xFFFFFF
	 */
	public static int getIntHex(String name) {
		return getIntHex(name, Integer.decode("0xFFFFFF"));
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет 127
	 */
	public static byte getByte(String name) {
		return getByte(name, Byte.MAX_VALUE);
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет
	 * 9223372036854775807
	 */
	public static long getLong(String name) {
		return getLong(name, Long.MAX_VALUE);
	}

	/**
	 * Если такой строчки в конфигах нет - то данный метод вернет
	 * 1.7976931348623157e+308
	 */
	public static double getDouble(String name) {
		return getDouble(name, Double.MAX_VALUE);
	}

	public static String get(String name, String def) {
		return get(name) == null ? def : get(name);
	}

	public static float getFloat(String name, float def) {
		return Float.parseFloat(get(name, String.valueOf(def)));
	}

	public static boolean getBoolean(String name, boolean def) {
		return Boolean.parseBoolean(get(name, String.valueOf(def)));
	}

	public static int getInt(String name, int def) {
		return Integer.parseInt(get(name, String.valueOf(def)));
	}

	/*
	 * public static int[] getIntArray(String name, int[] def) { return
	 * get(name, null) == null ? def :
	 * Util.parseCommaSeparatedIntegerArray(get(name, null)); }
	 */

	public static int getIntHex(String name, int def) {
		if (!get(name, String.valueOf(def)).trim().startsWith("0x"))
			return Integer.decode("0x" + get(name, String.valueOf(def)));
		else
			return Integer.decode(get(name, String.valueOf(def)));
	}

	public static byte getByte(String name, byte def) {
		return Byte.parseByte(get(name, String.valueOf(def)));
	}

	public static double getDouble(String name, double def) {
		return Double.parseDouble(get(name, String.valueOf(def)));
	}

	public static long getLong(String name, long def) {
		return Long.parseLong(get(name, String.valueOf(def)));
	}

	public static void set(String name, String param) {
		properties.replace(name, param);
	}

	public static void set(String name, Object obj) {
		set(name, String.valueOf(obj));
	}

	public static String concat(final String... strings) {
		final StringBuilder sbString = new StringBuilder(getLength(strings));
		for (final String string : strings) {
			sbString.append(string);
		}
		return sbString.toString();
	}

	private static int getLength(final String[] strings) {
		int length = 0;
		for (final String string : strings) {
			length += string.length();
		}
		return length;
	}
}
