package fw.connection.login.serverpackets;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

import fw.connection.login.clientpackets.AuthGameGuard;

public class Init extends LoginServerPacket{

	private int _sessionId;
	@SuppressWarnings("unused")
	private int _protocolVersion;
	private byte[] _rawRSApublicKey = new byte[128];
	private RSAPublicKey _RSApublicKey;
	private int[] _unkGG = new int[4];
	private BigInteger Modulus;
	private byte[] _blowfishKey = new byte[16];
	
	@Override
	public void excecute() {
		//_log.info("excecute");
		getClient().setNewCryptKey(_blowfishKey);
		getClient().setSessionId(_sessionId);
		Modulus = descrambleModulus(_rawRSApublicKey);
		RSAPublicKeySpec keygen = new RSAPublicKeySpec(Modulus, RSAKeyGenParameterSpec.F4);
		
		KeyFactory _kf;
		try {
			_kf = KeyFactory.getInstance("RSA");
			_RSApublicKey = (RSAPublicKey) _kf.generatePublic(keygen);
			getClient().setRSAKey(_RSApublicKey);
			getClient().sendPacket(new AuthGameGuard(_sessionId, _unkGG[0], _unkGG[1], _unkGG[2], _unkGG[3]));
		} catch (NoSuchAlgorithmException e) {
			_log.warning("No Such Algorithm: "+e.getMessage());
		} catch (InvalidKeySpecException e) {
			_log.warning("Invalid KeySpec: "+e.getMessage());
		}
	}

	@Override
	public void read() {
		//a_log.info("read");
		_sessionId = readD();
		_protocolVersion = readD();
		readB(_rawRSApublicKey); // 128
		_unkGG[0] = readD();
		_unkGG[1] = readD();
		_unkGG[2] = readD();
		_unkGG[3] = readD();
		readB(_blowfishKey); //16
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
