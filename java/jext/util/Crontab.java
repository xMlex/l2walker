package jext.util;

import java.util.Calendar;

/**
 * Клас для парсинга и анализа даты и времени, представленного в формате Crontab
 * http://ru.wikipedia.org/wiki/Cron
 * 
 * * * * * * выполняемая команда - - - - - | | | | | | | | | ----- День недели
 * (0 - 7) (Воскресенье =0 или =7) | | | ------- Месяц (1 - 12) | | ---------
 * День (1 - 31) | ----------- Час (0 - 23) ------------- Минута (0 - 59)
 */
public class Crontab {
	private static final int MINUTESPERHOUR = 59;
	private static final int HOURESPERDAY = 23;
	private static final int DAYSPERWEEK = 7;
	private static final int MONTHSPERYEAR = 12;
	private static final int DAYSPERMONTH = 31;

	private static final int MINUTESMIN = 0;
	private static final int HOURESMIN = 0;
	private static final int DAYSMIN = 1;
	private static final int MONTHSMIN = 1;
	private static final int DAYSPERWEEKMIN = 0;

	private GArray<Integer> _Minutes = new GArray<Integer>();
	private GArray<Integer> _Hours = new GArray<Integer>();
	private GArray<Integer> _DaysInMonth = new GArray<Integer>();
	private GArray<Integer> _Month = new GArray<Integer>();
	private GArray<Integer> _DaysInWeek = new GArray<Integer>();
	private String _configLine = "";

	public Crontab(String line) {
		_configLine = line;
		String[] params = _configLine.split(" ");
		_Minutes = parseRangeParam(params[0], MINUTESPERHOUR, MINUTESMIN);
		_Hours = parseRangeParam(params[1], HOURESPERDAY, HOURESMIN);
		_DaysInMonth = parseRangeParam(params[2], DAYSPERMONTH, DAYSMIN);
		_Month = parseRangeParam(params[3], MONTHSPERYEAR, MONTHSMIN); // 1 =
																		// jan
		_DaysInWeek = parseRangeParam(params[4], DAYSPERWEEK, DAYSPERWEEKMIN);
	}

	/**
	 * @param param
	 *            a range
	 * @param timelength
	 *            MINUTESPERHOUR, HOURESPERDAY, DAYSPERWEEK, MONTHSPERYEAR
	 * @return
	 */
	private static GArray<Integer> parseRangeParam(String param,
			int timelength, int minlength) {
		String[] paramarray;
		if (param.indexOf(",") != -1)
			paramarray = param.split(","); // разделитель ","
		else
			paramarray = new String[] { param };

		StringBuffer rangeitems = new StringBuffer();
		for (int i = 0; i < paramarray.length; i++)
			// you may mix */# syntax with other range syntax
			if (paramarray[i].indexOf("/") != -1) {
				// handle */# syntax
				for (int a = 1; a <= timelength; a++)
					if (a
							% Integer.parseInt(paramarray[i]
									.substring(paramarray[i].indexOf("/") + 1)) == 0)
						if (a == timelength)
							rangeitems.append(minlength + ",");
						else
							rangeitems.append(a + ",");
			} else if (paramarray[i].equals("*"))
				rangeitems.append(fillRange(minlength + "-" + timelength));
			else
				rangeitems.append(fillRange(paramarray[i]));

		String[] values = rangeitems.toString().split(",");
		GArray<Integer> result = new GArray<Integer>();
		for (int i = 0; i < values.length; i++)
			result.add(Integer.parseInt(values[i]));

		return result;
	}

	private static String fillRange(String range) {
		if (range.indexOf("-") == -1) // разделитель "-"
			return range + ",";

		String[] rangearray = range.split("-");
		StringBuffer result = new StringBuffer();
		for (int i = Integer.parseInt(rangearray[0]); i <= Integer
				.parseInt(rangearray[1]); i++)
			result.append(i + ",");
		return result.toString();
	}

	public boolean canRunAt(Calendar cal) {
		int month = cal.get(Calendar.MONTH) + 1; // 0=Jan, 1=Feb, ...
		int day = cal.get(Calendar.DAY_OF_MONTH); // 1...
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 0,7=Sunday, 1=Monday,
														// ...
		int hour = cal.get(Calendar.HOUR_OF_DAY); // 0-23
		int minute = cal.get(Calendar.MINUTE); // 0-59

		if (_Minutes.contains(minute) && _Hours.contains(hour)
				&& _DaysInMonth.contains(day) && _Month.contains(month)
				&& _DaysInWeek.contains(dayOfWeek))
			return true;

		return false;
	}

	public boolean canRunNow() {
		return canRunAt(Calendar.getInstance());
	}

	/**
	 * Возвращает время в unixtime следующего возможного вызова
	 * 
	 * @param time
	 *            время, относительно которого выполняется расчет
	 * @return время в мс
	 */
	public long timeNextUsage(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// System.out.println("**************");
		// System.out.println("Cron pattern: " + _configLine);
		// System.out.println("Source date: " + cal.getTime().toString() +
		// " day of week:" + cal.get(Calendar.DAY_OF_WEEK));

		// System.out.println("Minutes[]: " + _Minutes.toString());
		cal.set(Calendar.MINUTE,
				brute(_Minutes, cal.get(Calendar.MINUTE), MINUTESMIN,
						MINUTESPERHOUR));
		// System.out.println("Minutes: " + cal.get(Calendar.MINUTE));
		// System.out.println("Modified date: " + cal.getTime().toString());

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.HOUR, 1);
		// System.out.println(" - added 1h: " + cal.getTime().toString());

		// System.out.println("Hours[]: " + _Hours.toString());
		cal.set(Calendar.HOUR_OF_DAY,
				brute(_Hours, cal.get(Calendar.HOUR_OF_DAY), HOURESMIN,
						HOURESPERDAY));
		// System.out.println("Hours: " + cal.get(Calendar.HOUR_OF_DAY));
		// System.out.println("Modified date: " + cal.getTime().toString());

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.HOUR, 24);
		// System.out.println(" - added 24h: " + cal.getTime().toString());

		// System.out.println("DaysInMonth[]: " + _DaysInMonth.toString());
		cal.set(Calendar.DAY_OF_MONTH,
				brute(_DaysInMonth, cal.get(Calendar.DAY_OF_MONTH), DAYSMIN,
						DAYSPERMONTH));
		// System.out.println("DaysInMonth: " + cal.get(Calendar.DAY_OF_MONTH));
		// System.out.println("Modified date: " + cal.getTime().toString());

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.DAY_OF_YEAR, 31);
		// System.out.println(" - added 31d: " + cal.getTime().toString());

		// System.out.println("Month[]: " + _Month.toString());
		cal.set(Calendar.MONTH,
				brute(_Month, cal.get(Calendar.MONTH) + 1, MONTHSMIN,
						MONTHSPERYEAR) - 1);
		// System.out.println("Month: " + cal.get(Calendar.MONTH));
		// System.out.println("Modified date: " + cal.getTime().toString());

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.DAY_OF_YEAR, 31);
		// System.out.println(" - added 31d: " + cal.getTime().toString());

		// System.out.println("DaysInWeek[]: " + _DaysInWeek.toString());
		cal.set(Calendar.DAY_OF_WEEK,
				brute(_DaysInWeek, cal.get(Calendar.DAY_OF_WEEK),
						DAYSPERWEEKMIN, DAYSPERWEEK));
		// System.out.println("DaysInWeek: " + cal.get(Calendar.DAY_OF_WEEK));

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.DAY_OF_YEAR, 7);
		// System.out.println(" - added 1w: " + cal.getTime().toString());

		// System.out.println("1Modified date: " + cal.getTime().toString());
		// System.out.println("1Source unixtime date  : " + time);
		// System.out.println("1Modified unixtime date: " +
		// cal.getTimeInMillis());

		if (cal.getTimeInMillis() < time)
			cal.add(Calendar.HOUR, 24);

		// System.out.println("2Modified date: " + cal.getTime().toString());
		// System.out.println("2Source unixtime date  : " + time);
		// System.out.println("2Modified unixtime date: " +
		// cal.getTimeInMillis());
		return cal.getTimeInMillis();
	}

	private int brute(GArray<Integer> arr, int cur, int min, int max) {
		boolean antiloop = true;
		int start_value = cur;
		int next = cur;
		while (antiloop) {
			if (arr.contains(next))
				return next;

			if (next < max)
				next++;
			else
				next = min;

			if (start_value == next)
				antiloop = false;
		}

		return -1;
	}
}