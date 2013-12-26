package jext.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleLogFormatter extends Formatter {
	private static final String CRLF = "\r\n";
	private static final SimpleDateFormat tsformat = new SimpleDateFormat("HH:mm:ss.SSS  ");
	private Date ts = new Date();

	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		ts.setTime(record.getMillis());
		output.append(tsformat.format(ts));
		output.append(record.getMessage());
		output.append(CRLF);
		if (record.getThrown() != null)
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				output.append(sw.toString());
				output.append(CRLF);
			} catch (Exception ex) {
			}

		return output.toString();
	}
}