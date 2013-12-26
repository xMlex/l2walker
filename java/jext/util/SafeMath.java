package jext.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Безопасная от переполнения математика
 * 
 * @author DRiN
 */

public class SafeMath {
	/*
	 * long, 64bit Long.MIN_VALUE = –9223372036854775808 Long.MAX_VALUE =
	 * 9223372036854775807
	 */

	public static long safeMulLong(long a, long b) throws ArithmeticException {
		BigInteger ret = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
		if (isOverflowLong(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%s)", a, b, ret.longValue(),
					ret.toString()));
		return ret.longValue();
	}

	public static long safeMulLong(long a, double b) throws ArithmeticException {
		BigDecimal ret = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
		if (isOverflowLong(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%s)", a, b, ret.longValue(),
					ret.toString()));
		return ret.longValue();
	}

	public static long safeMulLong(double a, double b)
			throws ArithmeticException {
		BigDecimal ret = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
		if (isOverflowLong(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%s)", a, b, ret.longValue(),
					ret.toString()));
		return ret.longValue();
	}

	public static long safeMulLongOrMax(long a, long b) {
		BigInteger ret = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
		return isOverflowLong(ret) ? Long.MAX_VALUE : ret.longValue();
	}

	public static long safeDivLong(long a, long b) throws ArithmeticException {
		if (a == Long.MIN_VALUE && b == -1)
			throw new ArithmeticException(String.format("Overflow: %d/%d=%d",
					a, b, a / b));
		return a / b;
	}

	public static long safeAddLong(long a, long b) throws ArithmeticException {
		BigInteger ret = BigInteger.valueOf(a).add(BigInteger.valueOf(b));
		if (isOverflowLong(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d+%d=%d (%s)", a, b, ret.longValue(),
					ret.toString()));
		return ret.longValue();
	}

	public static long safeAddLongOrMax(long a, long b) {
		BigInteger ret = BigInteger.valueOf(a).add(BigInteger.valueOf(b));
		return isOverflowLong(ret) ? Long.MAX_VALUE : ret.longValue();
	}

	public static long safeSubLong(long a, long b) throws ArithmeticException {
		BigInteger ret = BigInteger.valueOf(a).subtract(BigInteger.valueOf(b));
		if (isOverflowLong(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d-%d=%d (%s)", a, b, ret.longValue(),
					ret.toString()));
		return ret.longValue();
	}

	public static long safeIncLong(long a, long b) throws ArithmeticException {
		return safeAddLong(a, 1);
	}

	public static long safeDecLong(long a, long b) throws ArithmeticException {
		return safeSubLong(a, 1);
	}

	public static boolean isOverflowLong(double val) {
		return isOverflowLong(BigDecimal.valueOf(val));
	}

	static final BigInteger BigInteger_Long_MAX_VALUE = BigInteger
			.valueOf(Long.MAX_VALUE);
	static final BigInteger BigInteger_Long_MIN_VALUE = BigInteger
			.valueOf(Long.MIN_VALUE);

	public static boolean isOverflowLong(BigInteger val) {
		return val.compareTo(BigInteger_Long_MAX_VALUE) == 1
				|| val.compareTo(BigInteger_Long_MIN_VALUE) == -1;
	}

	static final BigDecimal BigDecimal_Long_MAX_VALUE = BigDecimal
			.valueOf(Long.MAX_VALUE);
	static final BigDecimal BigDecimal_Long_MIN_VALUE = BigDecimal
			.valueOf(Long.MIN_VALUE);

	public static boolean isOverflowLong(BigDecimal val) {
		return val.compareTo(BigDecimal_Long_MAX_VALUE) == 1
				|| val.compareTo(BigDecimal_Long_MIN_VALUE) == -1;
	}

	/*
	 * int, 32bit Integer.MIN_VALUE = –2147483648 Integer.MAX_VALUE = 2147483647
	 */

	public static int safeMulInt(int a, int b) throws ArithmeticException {
		long ret = 1L * a * b;
		if (isOverflowInt(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%d)", a, b, (int) ret, ret));
		return (int) ret;
	}

	public static int safeDivInt(int a, int b) throws ArithmeticException {
		if (a == Integer.MIN_VALUE && b == -1)
			throw new ArithmeticException(String.format("Overflow: %d/%d=%d",
					a, b, a / b));
		return a / b;
	}

	public static int safeAddInt(int a, long b) throws ArithmeticException {
		long ret = a + b;
		if (isOverflowInt(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d+%d=%d (%d)", a, b, (int) ret, ret));
		return (int) ret;
	}

	public static int safeAddInt(int a, int b) throws ArithmeticException {
		return safeAddInt(a, (long) b);
	}

	public static int safeSubInt(int a, int b) throws ArithmeticException {
		long ret = 0L + a - b;
		if (isOverflowInt(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d-%d=%d (%d)", a, b, (int) ret, ret));
		return (int) ret;
	}

	public static int safeIncInt(int a, int b) throws ArithmeticException {
		return safeAddInt(a, 1);
	}

	public static int safeDecInt(int a, int b) throws ArithmeticException {
		return safeSubInt(a, 1);
	}

	public static boolean isOverflowInt(long val) {
		return val > Integer.MAX_VALUE || val < Integer.MIN_VALUE;
	}

	/*
	 * short, 16bit Short.MIN_VALUE = –32768 Short.MAX_VALUE = 32767
	 */

	public static short safeMulShort(int a, int b) throws ArithmeticException {
		int ret = a * b;
		if (isOverflowShort(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%d)", a, b, (short) ret, ret));
		return (short) ret;
	}

	public static int safeDivShort(int a, int b) throws ArithmeticException {
		if (a == Short.MIN_VALUE && b == -1)
			throw new ArithmeticException(String.format("Overflow: %d/%d=%d",
					a, b, a / b));
		return a / b;
	}

	public static short safeAddShort(int a, int b) throws ArithmeticException {
		int ret = a + b;
		if (isOverflowShort(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d+%d=%d (%d)", a, b, (short) ret, ret));
		return (short) ret;
	}

	public static short safeSubShort(int a, int b) throws ArithmeticException {
		int ret = a - b;
		if (isOverflowShort(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d-%d=%d (%d)", a, b, (short) ret, ret));
		return (short) ret;
	}

	public static short safeIncShort(int a, int b) throws ArithmeticException {
		return safeAddShort(a, 1);
	}

	public static short safeDecShort(int a, int b) throws ArithmeticException {
		return safeSubShort(a, 1);
	}

	public static boolean isOverflowShort(int val) {
		return val > Short.MAX_VALUE || val < Short.MIN_VALUE;
	}

	/*
	 * byte, 8bit Byte.MIN_VALUE = –128 Byte.MAX_VALUE = 127
	 */

	public static byte safeMulByte(int a, int b) throws ArithmeticException {
		int ret = a * b;
		if (isOverflowByte(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d*%d=%d (%d)", a, b, (byte) ret, ret));
		return (byte) ret;
	}

	public static int safeDivByte(int a, int b) throws ArithmeticException {
		if (a == Byte.MIN_VALUE && b == -1)
			throw new ArithmeticException(String.format("Overflow: %d/%d=%d",
					a, b, a / b));
		return a / b;
	}

	public static byte safeAddByte(int a, int b) throws ArithmeticException {
		int ret = a + b;
		if (isOverflowByte(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d+%d=%d (%d)", a, b, (byte) ret, ret));
		return (byte) ret;
	}

	public static byte safeSubByte(int a, int b) throws ArithmeticException {
		int ret = a - b;
		if (isOverflowByte(ret))
			throw new ArithmeticException(String.format(
					"Overflow: %d-%d=%d (%d)", a, b, (byte) ret, ret));
		return (byte) ret;
	}

	public static byte safeIncByte(int a, int b) throws ArithmeticException {
		return safeAddByte(a, 1);
	}

	public static byte safeDecByte(int a, int b) throws ArithmeticException {
		return safeSubByte(a, 1);
	}

	public static boolean isOverflowByte(int val) {
		return val > Byte.MAX_VALUE || val < Byte.MIN_VALUE;
	}
}