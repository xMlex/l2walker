package jext.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.4.1 $ $Date: 2005/03/27 15:30:08 $
 */

public class FileLogFormatter extends Formatter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	private static final String CRLF = "\r\n";
	private static final String _ = "\t";

	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		output.append(record.getMillis());
		output.append(_);
		output.append(record.getLevel().getName());
		output.append(_);
		output.append(record.getThreadID());
		output.append(_);
		output.append(record.getLoggerName());
		output.append(_);
		output.append(record.getMessage());
		output.append(CRLF);
		return output.toString();
	}
}
