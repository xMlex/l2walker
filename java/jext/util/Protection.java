package jext.util;

public final class Protection {
	public static int initSession(byte[] key) {
		return 0; // болванка
	}

	public static boolean check(byte[] raw, final int offset, final int size,
			int sessionKey) {
		return true; // болванка
	}

	public static boolean encrypt(byte[] raw, final int offset, final int size,
			byte[] _outKey, int _outCount) {
		return false; // болванка
	}

	public static boolean decrypt(byte[] raw, final int offset, final int size,
			byte[] _inKey, int _inCount) {
		return false; // болванка
	}

	public static String ExtractHWID(byte[] _data) {
		return ""; // болванка
	}

	public static boolean BF_Init(int[] P, int[] S0, int[] S1, int[] S2,
			int[] S3) {
		return false; // болванка
	}
}