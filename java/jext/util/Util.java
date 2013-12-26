package jext.util;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Util {
	private static Logger _log = Logger.getLogger(Util.class.getName());

	static final String PATTERN = "0.0000000000E00";
	static final DecimalFormat df;

	/**
	 * Форматтер для адены.<br>
	 * Locale.KOREA заставляет его фортматировать через ",".<br>
	 * Locale.FRANCE форматирует через " "<br>
	 * Для форматирования через "." убрать с аргументов Locale.FRANCE
	 */
	private static NumberFormat adenaFormatter;

	static {
		adenaFormatter = NumberFormat.getIntegerInstance(Locale.FRANCE);
		df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.applyPattern(PATTERN);
		df.setPositivePrefix("+");
	}

	static boolean checkIfIpInRange(String ip, String ipRange) {
		// DATA FIELD
		// Ip format. 110.20.30.40 - 50.50.30.40
		int userIp1 = -1;
		int userIp2 = -1;
		int userIp3 = -1;
		int userIp4 = -1;
		String firstIp;
		String lastIp;
		// Data field end.

		ip = ip.replace(".", ",");
		for (String s : ip.split(","))
			if (userIp1 == -1)
				userIp1 = Integer.parseInt(s);
			else if (userIp2 == -1)
				userIp2 = Integer.parseInt(s);
			else if (userIp3 == -1)
				userIp3 = Integer.parseInt(s);
			else
				userIp4 = Integer.parseInt(s);

		int ipMin1 = -1;
		int ipMin2 = -1;
		int ipMin3 = -1;
		int ipMin4 = -1; // IP values for min ip
		int ipMax1 = -1;
		int ipMax2 = -1;
		int ipMax3 = -1;
		int ipMax4 = -1; // Ip values for max ip

		StringTokenizer st = new StringTokenizer(ipRange, "-"); // Try to split
																// them by "-"
																// symbol

		if (st.countTokens() == 2) {
			firstIp = st.nextToken(); // get our first ip string
			lastIp = st.nextToken(); // get out second ip string.

			firstIp = firstIp.replace(".", ",");
			lastIp = lastIp.replace(".", ",");

			// Set our minimum ip
			for (String s1 : firstIp.split(","))
				if (ipMin1 == -1)
					ipMin1 = Integer.parseInt(s1);
				else if (ipMin2 == -1)
					ipMin2 = Integer.parseInt(s1);
				else if (ipMin3 == -1)
					ipMin3 = Integer.parseInt(s1);
				else
					ipMin4 = Integer.parseInt(s1);

			// set our maximum ip
			for (String s2 : lastIp.split(","))
				if (ipMax1 == -1)
					ipMax1 = Integer.parseInt(s2);
				else if (ipMax2 == -1)
					ipMax2 = Integer.parseInt(s2);
				else if (ipMax3 == -1)
					ipMax3 = Integer.parseInt(s2);
				else
					ipMax4 = Integer.parseInt(s2);

			// Now we are making some checks with our ips.
			if (userIp1 > ipMin1 && userIp1 < ipMax1)
				return true; // it's internal
			else if (userIp1 < ipMin1 || userIp1 > ipMax1)
				return false; // it's external
			else if (userIp1 == ipMin1 && userIp1 != ipMax1) {
				if (userIp2 > ipMin2)
					return true;
				else if (userIp2 < ipMin2)
					return false;
				else if (userIp3 > ipMin3)
					return true;
				else if (userIp3 < ipMin3)
					return false;
				else
					return userIp4 >= ipMin4;
			} else if (userIp1 != ipMin1 && userIp1 == ipMax1) {
				if (userIp2 < ipMax2)
					return true;
				else if (userIp2 > ipMax2)
					return false;
				else if (userIp3 < ipMax3)
					return true;
				else if (userIp3 > ipMax3)
					return false;
				else
					return userIp4 <= ipMax4;
			} else if (userIp2 > ipMin2 && userIp2 < ipMax2)
				return true; // it's internal
			else if (userIp2 < ipMin2 || userIp2 > ipMax2)
				return false; // it's external
			else if (userIp2 == ipMin2 && userIp2 != ipMax2) {
				if (userIp3 > ipMin3)
					return true;
				else if (userIp3 < ipMin3)
					return false;
				else
					return userIp4 >= ipMin4;
			} else if (userIp2 != ipMin2 && userIp2 == ipMax2) {
				if (userIp3 < ipMax3)
					return true;
				else if (userIp3 > ipMax3)
					return false;
				else
					return userIp4 <= ipMax4;
			} else if (userIp3 > ipMin3 && userIp3 < ipMax3)
				return true; // it's internal
			else if (userIp3 < ipMin3 || userIp3 > ipMax3)
				return false; // it's external
			else if (userIp3 == ipMin3 && userIp3 != ipMax3)
				return userIp4 >= ipMin4;
			else if (userIp3 != ipMin3 && userIp3 == ipMax3)
				return userIp4 <= ipMax4;
			else if (userIp4 >= ipMin4 && userIp4 <= ipMax4)
				return true; // it's internal
			else if (userIp4 < ipMin4 || userIp4 > ipMax4)
				return false; // it's external
		} else if (st.countTokens() == 1) {
			if (ip.equalsIgnoreCase(ipRange))
				return true;
		} else
			_log.warning("Error in internal ip detection: " + ipRange);
		return false;
	}

	/**
	 * Проверяет строку на соответсвие регулярному выражению
	 * 
	 * @param text
	 *            Строка-источник
	 * @param template
	 *            Шаблон для поиска
	 * @return true в случае соответвия строки шаблону
	 */
	public static boolean isMatchingRegexp(String text, String template) {
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(template);
		} catch (PatternSyntaxException e) // invalid template
		{
			e.printStackTrace();
		}
		if (pattern == null)
			return false;
		Matcher regexp = pattern.matcher(text);
		return regexp.matches();
	}

	/**
	 * Производит замену в строке по регулярному выражению
	 * 
	 * @param source
	 *            Строка-источник
	 * @param template
	 *            Шаблон для замены
	 * @param replacement
	 *            Строка замена
	 * @return Замененную строку
	 */
	public static String replaceRegexp(String source, String template,
			String replacement) {
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(template);
		} catch (PatternSyntaxException e) // invalid template
		{
			e.printStackTrace();
		}
		if (pattern != null) {
			Matcher regexp = pattern.matcher(source);
			source = regexp.replaceAll(replacement);
		}
		return source;
	}

	public static String printData(byte[] data, int len) {
		StringBuffer result = new StringBuffer();

		int counter = 0;

		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0)
				result.append(fillHex(i, 4) + ": ");

			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
						result.append((char) t1);
					else
						result.append('.');
				}
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++)
				result.append("   ");

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
					result.append((char) t1);
				else
					result.append('.');
			}
			result.append("\n");
		}

		return result.toString();
	}

	public static String fillHex(int data, int digits) {
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++)
			number = "0" + number;

		return number;
	}

	/**
	 * @param raw
	 * @return
	 */
	public static String printData(byte[] raw) {
		return printData(raw, raw.length);
	}

	/**
	 * Returns current timestamp in seconds (without milliseconds). Returned
	 * timestamp is obtained with the following expression:
	 * <p>
	 * <p/>
	 * <code>(System.currentTimeMillis() + 500L) / 1000L</code>
	 * 
	 * @return Current timestamp
	 */
	public static long getTime() {
		return (System.currentTimeMillis() + 500L) / 1000L;
	}

	public static String formatDouble(double x, String nanString,
			boolean forceExponents) {
		if (Double.isNaN(x))
			return nanString;
		if (forceExponents)
			return df.format(x);
		if ((long) x == x)
			return String.valueOf((long) x);
		return String.valueOf(x);
	}

	public static String getRelativePath(File base, File file) {
		return file.toURI().getPath()
				.substring(base.toURI().getPath().length());
	}

	/**
	 * Return degree value of object 2 to the horizontal line with object 1
	 * being the origin
	 */
	public static double calculateAngleFrom(int obj1X, int obj1Y, int obj2X,
			int obj2Y) {
		double angleTarget = Math.toDegrees(Math.atan2(obj1Y - obj2Y, obj1X
				- obj2X));
		if (angleTarget <= 0)
			angleTarget += 360;
		return angleTarget;
	}

	public static boolean checkIfInRange(int range, int x1, int y1, int x2,
			int y2) {
		return checkIfInRange(range, x1, y1, 0, x2, y2, 0, false);
	}

	public static boolean checkIfInRange(int range, int x1, int y1, int z1,
			int x2, int y2, int z2, boolean includeZAxis) {
		long dx = x1 - x2;
		long dy = y1 - y2;

		if (includeZAxis) {
			long dz = z1 - z2;
			return dx * dx + dy * dy + dz * dz <= range * range;
		}
		return dx * dx + dy * dy <= range * range;
	}

	public static double convertHeadingToDegree(int heading) {
		double angle = heading / 182.044444444;
		if (angle == 0)
			angle = 360;
		return angle;
	}

	public static double convertHeadingToRadian(int heading) {
		return Math.toRadians(convertHeadingToDegree(heading) - 90);
	}

	public final static int convertDegreeToClientHeading(double degree) {
		if (degree < 0)
			degree = 360 + degree;
		return (int) (degree * 182.044444444);
	}

	public static double calculateDistance(int x1, int y1, int z1, int x2,
			int y2) {
		return calculateDistance(x1, y1, 0, x2, y2, 0, false);
	}

	public static double calculateDistance(int x1, int y1, int z1, int x2,
			int y2, int z2, boolean includeZAxis) {
		long dx = x1 - x2;
		long dy = y1 - y2;

		if (includeZAxis) {
			long dz = z1 - z2;
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static short getShort(byte[] bs, int offset) {
		return (short) (bs[offset + 1] << 8 | bs[offset] & 0xff);
	}

	public static double getDistance(int x1, int y1, int x2, int y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}

	/**
	 * Return amount of adena formatted with " " delimiter
	 * 
	 * @param amount
	 * @return String formatted adena amount
	 */
	public static String formatAdena(long amount) {
		/*
		 * String s = ""; int rem = amount % 1000; s = Integer.toString(rem);
		 * amount = (amount - rem) / 1000; while(amount > 0) { if(rem < 99) s =
		 * '0' + s; if(rem < 9) s = '0' + s; rem = amount % 1000; s =
		 * Integer.toString(rem) + " " + s; amount = (amount - rem) / 1000; }
		 * return s;
		 */
		return adenaFormatter.format(amount);
	}

	public static String formatSize(long size) {
		if (size > 0x3FFFFFFF)
			return String.format("%.2f G", 1.0 * size / 0x40000000);
		if (size > 0xFFFFF)
			return String.format("%.2f M", 1.0 * size / 0x100000);
		if (size > 0x3FF)
			return String.format("%.2f K", 1.0 * size / 0x400);
		return String.format("%d", size);
	}

	/**
	 * Получает длинну пакета из 2-х первых байт.<BR>
	 * Используется для общения между LS и GS.
	 * 
	 * @param first
	 *            первый байт пакета
	 * @param second
	 *            второй байт пакета
	 * @return длинна пакета
	 */
	public static int getPacketLength(byte first, byte second) {
		int lenght = first & 0xff;
		return lenght |= second << 8 & 0xff00;
	}

	/**
	 * Дописывает длинну пакета.<BR>
	 * Используется для общения между LS и GS.
	 * 
	 * @param data
	 *            пакет для отправления (зашифрован уже)
	 * @return готовый пакет для отправления
	 */
	public static byte[] writeLenght(byte[] data) {
		int newLenght = data.length + 2;
		byte[] result = new byte[newLenght];
		result[0] = (byte) (newLenght & 0xFF);
		result[1] = (byte) (newLenght >> 8 & 0xFF);
		System.arraycopy(data, 0, result, 2, data.length);
		return result;
	}

	public static byte[] generateHex(int size) {
		byte[] array = new byte[size];
		Random rnd = new Random();
		for (int i = 0; i < size; i++)
			array[i] = (byte) rnd.nextInt(256);
		return array;
	}

	/**
	 * форматирует время в секундах в дни/часы/минуты/секунды
	 */
	public static String formatTime(long time) {
		if (time == 0)
			return "now";
		time = Math.abs(time);
		String ret = "";
		long numDays = time / 86400;
		time -= numDays * 86400;
		long numHours = time / 3600;
		time -= numHours * 3600;
		long numMins = time / 60;
		time -= numMins * 60;
		long numSeconds = time;
		if (numDays > 0)
			ret += numDays + "d ";
		if (numHours > 0)
			ret += numHours + "h ";
		if (numMins > 0)
			ret += numMins + "m ";
		if (numSeconds > 0)
			ret += numSeconds + "s";
		return ret.trim();
	}

	public static int packInt(int[] a, int bits) throws Exception {
		int m = 32 / bits;
		if (a.length > m)
			throw new Exception("Overflow");

		int result = 0;
		int next;
		int mval = (int) Math.pow(2, bits);
		for (int i = 0; i < m; i++) {
			result <<= bits;
			if (a.length > i) {
				next = a[i];
				if (next >= mval || next < 0)
					throw new Exception("Overload, value is out of range");
			} else
				next = 0;
			result += next;
		}
		return result;
	}

	public static long packLong(int[] a, int bits) throws Exception {
		int m = 64 / bits;
		if (a.length > m)
			throw new Exception("Overflow");

		long result = 0;
		int next;
		int mval = (int) Math.pow(2, bits);
		for (int i = 0; i < m; i++) {
			result <<= bits;
			if (a.length > i) {
				next = a[i];
				if (next >= mval || next < 0)
					throw new Exception("Overload, value is out of range");
			} else
				next = 0;
			result += next;
		}
		return result;
	}

	public static int[] unpackInt(int a, int bits) {
		int m = 32 / bits;
		int mval = (int) Math.pow(2, bits);
		int[] result = new int[m];
		int next;
		for (int i = m; i > 0; i--) {
			next = a;
			a = a >> bits;
			result[i - 1] = next - a * mval;
		}
		return result;
	}

	public static int[] unpackLong(long a, int bits) {
		int m = 64 / bits;
		int mval = (int) Math.pow(2, bits);
		int[] result = new int[m];
		long next;
		for (int i = m; i > 0; i--) {
			next = a;
			a = a >> bits;
			result[i - 1] = (int) (next - a * mval);
		}
		return result;
	}

	public static float[] parseCommaSeparatedFloatArray(String s) {
		if (s.isEmpty())
			return new float[] {};
		String[] tmp = s.replaceAll(",", ";").split(";");
		float[] ret = new float[tmp.length];
		for (int i = 0; i < tmp.length; i++)
			ret[i] = Float.parseFloat(tmp[i]);
		return ret;
	}

	public static int[] parseCommaSeparatedIntegerArray(String s) {
		if (s.isEmpty())
			return new int[] {};
		String[] tmp = s.replaceAll(",", ";").split(";");
		int[] ret = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++)
			ret[i] = Integer.parseInt(tmp[i]);
		return ret;
	}

	public static boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Добавляет элемент o в конец массива arr либо создает новый массив, если
	 * arr == null. Не работает с примитивами.
	 * 
	 * @param arr
	 *            — массив для расширения, может быть null (будет создан новый
	 *            массив из одного элемента)
	 * @param o
	 *            — объект для добавления, может быть null (массив просто будет
	 *            расширен)
	 * @param c
	 *            — класс массива, указывается явно
	 * @return новый массив, состоящий из копии входящего и добавленного
	 *         элемента
	 */
	public static Object[] addElementToArray(Object[] arr, Object o, Class<?> c) {
		if (arr == null) {
			arr = (Object[]) Array.newInstance(c, 1);
			arr[0] = o;
			return arr;
		}
		int len = arr.length;
		Object[] tmp = (Object[]) Array.newInstance(c, len + 1);
		System.arraycopy(arr, 0, tmp, 0, len);
		tmp[len] = o;
		return tmp;
	}

	public static String dumpObject(Object o, boolean simpleTypes,
			boolean parentFields, boolean ignoreStatics) {
		Class<?> cls = o.getClass();
		String val, type, result = "["
				+ (simpleTypes ? cls.getSimpleName() : cls.getName()) + "\n";
		Object fldObj;
		GArray<Field> fields = new GArray<Field>();
		while (cls != null) {
			for (Field fld : cls.getDeclaredFields())
				if (!fields.contains(fld)) {
					if (ignoreStatics && Modifier.isStatic(fld.getModifiers()))
						continue;
					fields.add(fld);
				}
			cls = cls.getSuperclass();
			if (!parentFields)
				break;
		}

		for (Field fld : fields) {
			fld.setAccessible(true);
			try {
				fldObj = fld.get(o);
				if (fldObj == null)
					val = "NULL";
				else
					val = fldObj.toString();
			} catch (Throwable e) {
				e.printStackTrace();
				val = "<ERROR>";
			}
			type = simpleTypes ? fld.getType().getSimpleName() : fld.getType()
					.toString();

			result += String.format("\t%s [%s] = %s;\n", fld.getName(), type,
					val);
		}

		result += "]\n";
		return result;
	}

	public static void waitForFreePorts(String host, int... ports) {
		boolean binded = false;
		while (!binded)
			for (int port : ports)
				try {
					ServerSocket ss = host.equalsIgnoreCase("*") ? new ServerSocket(
							port) : new ServerSocket(port, 50,
							InetAddress.getByName(host));
					_log.fine("Port " + port + " binded successfully.");
					ss.close();
					binded = true;
				} catch (Exception e) {
					_log.warning("\nPort "
							+ port
							+ " is allready binded. Please free it and restart server.");
					binded = false;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e2) {
					}
				}
	}
}