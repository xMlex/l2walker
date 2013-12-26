package fw.loginserver.clientpackets;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;


/**
 * Format: x
 * 0 (a leading null)
 * x: the rsa encrypted block with the login an password
 */
public class RequestAuthLogin extends ClientBasePacket
{

	private byte[] _raw = new byte[128];
	private String _user;
	private String _password;
	private int _responce = 0;
	byte[] encrypted = null;

	public RequestAuthLogin(RSAPublicKey _RSA, String user, String pass,int response) throws Exception{
		byte byteLogin[] = user.getBytes();
		byte bytePassword[] = pass.getBytes();
		_responce = response;
		
		int t = 98;
		int pendolum = 14 - byteLogin.length;
		for (int i = 0; i < byteLogin.length; i++) {
			_raw[t] = byteLogin[i];
			t++;
		}
		t = t + pendolum;
		//pendolum = 16 - bytePassword.length;
		for (int i = 0; i < bytePassword.length; i++) {
			_raw[t] = bytePassword[i];
			t++;
		}
		
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
		rsaCipher.init(Cipher.ENCRYPT_MODE, _RSA);
		encrypted = rsaCipher.doFinal(_raw, 0x00, 0x80 );
		//System.out.println("Size of RSA data: "+encrypted.length);
	}
	
	public void readImpl()
	{

	}

	public void run()
	{
		byte[] decrypted = null;
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
//			rsaCipher.init(Cipher.DECRYPT_MODE, getClient().getRSAPrivateKey());
			decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80 );
		}
		catch (GeneralSecurityException e)
		{
			e.printStackTrace();
			return;
		}

		_user = new String(decrypted, 0x5E, 14 ).trim();
		_user = _user.toLowerCase();
		set_password(new String(decrypted, 0x6C, 16).trim());
		/*_ncotp = decrypted[0x7c];
		_ncotp |= decrypted[0x7d] << 8;
		_ncotp |= decrypted[0x7e] << 16;
		_ncotp |= decrypted[0x7f] << 24;*/
	}

	@Override
	protected void writeImpl() {
		writeC(0x00);
		writeB(encrypted);
		writeD(_responce);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		//это как-то связано с GG
		writeD(8); //const = 8
		writeH(0);
		writeC(0);
		
	}

	public void set_password(String _password) {
		this._password = _password;
	}
	public void set_login(String login) {
		this._user = login;
	}

	public String get_password() {
		return _password;
	}
	
}
