package fw.loginserver.serverpackets;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Format: dd b dddd s d: session id d: protocol revision b: 0x90 bytes : 0x80
 * bytes for the scrambled RSA public key 0x10 bytes at 0x00 d: unknow d: unknow
 * d: unknow d: unknow s: blowfish key
 * 
 */
public final class Init extends ServerBasePacket {
	private int _sessionId;
	private int _protocolVersion;
	private byte[] _rawRSApublicKey;
	private RSAPublicKey _RSApublicKey;
	private int[] _unkGG = new int[4];
	private BigInteger Modulus;
	private byte[] _blowfishKey;

	public Init(byte data[]) {
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
		// readH(); //read message lenght
		readC(); // (1) message type
		_sessionId = readD();
		_protocolVersion = readD();
		_rawRSApublicKey = readB(128);
		_unkGG[0] = readD();
		_unkGG[1] = readD();
		_unkGG[2] = readD();
		_unkGG[3] = readD();
		_blowfishKey = readB(16);
		readC();
	}

	public void runImpl() {
		Modulus = descrambleModulus(_rawRSApublicKey);
		RSAPublicKeySpec keygen = new RSAPublicKeySpec(Modulus, RSAKeyGenParameterSpec.F4);
		
		KeyFactory _kf;
		try {
			_kf = KeyFactory.getInstance("RSA");
			_RSApublicKey = (RSAPublicKey) _kf.generatePublic(keygen);
		} catch (NoSuchAlgorithmException e) {
			System.out.print("No Such Algorithm: "+e.getMessage());
		} catch (InvalidKeySpecException e) {
			System.out.print("Invalid KeySpec: "+e.getMessage());
		}
		
	}

	/**
	 * @return the _sessionId
	 */
	public int get_sessionId() {
		return _sessionId;
	}

	/**
	 * @return the _protocolVersion
	 */
	public int get_protocolVersion() {
		return _protocolVersion;
	}

	/**
	 * @return the _RSApublicKey
	 */
	public RSAPublicKey get_RSApublicKey() {
		return _RSApublicKey;
	}

	/**
	 * @return the _unkGG (GameGuard)
	 */
	public int[] get_unkGG() {
		return _unkGG;
	}

	/**
	 * @return the _blowfishKey
	 */
	public byte[] get_blowfishKey() {
		return _blowfishKey;
	}

	protected void write() {
		writeC(0x00); // init packet id
		writeD(_sessionId); // session id
		writeD(_protocolVersion); // protocol revision
		writeB(_rawRSApublicKey); // RSA Public Key
		// unk GG related?
		writeD(_unkGG[0]);
		writeD(_unkGG[1]);
		writeD(_unkGG[2]);
		writeD(_unkGG[3]);
		writeB(_blowfishKey); // BlowFish key
		writeC(0x00); // null termination ;)
	}

	private static BigInteger descrambleModulus(byte[] scrambledMod) {
		if (scrambledMod.length != 0x80)
			return null;
		// step 1 : xor last $40 bytes with first $40 bytes
		for (int i = 0; i < 0x40; i++) {
			scrambledMod[0x40 + i] ^= scrambledMod[i];
		}
		// step 2 : xor bytes $0d-$10 with bytes $34-$38
		for (int i = 0; i <= 3; i++) {
			scrambledMod[0x0d + i] ^= scrambledMod[0x34 + i];
		}
		// step 3 : xor first $40 bytes with last $40 bytes
		for (int i = 0; i < 0x40; i++) {
			scrambledMod[i] ^= scrambledMod[0x40 + i];
		}
		// step 4 : $4d-$50 <-> $00-$04
		byte tmp = 0;
		for (int i = 0; i <= 3; i++) {
			tmp = scrambledMod[0x00 + i];
			scrambledMod[0x00 + i] = scrambledMod[0x4d + i];
			scrambledMod[0x4d + i] = tmp;
		}
		byte[] result = new byte[129];
		System.arraycopy(scrambledMod, 0, result, 1, 128);
		BigInteger _modulus = new BigInteger(result);
		return _modulus;
	}
}
