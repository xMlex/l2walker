package fw.connection.login.clientpackets;

import javax.crypto.Cipher;

public class RequestAuthLogin extends LoginClientPacket {

	private int _responce;
	private byte[] _raw = new byte[128],encrypted;
	
	public RequestAuthLogin(int responce) {
		_responce = responce;
	}

	@Override
	public void excecute() {
		
		byte byteLogin[] = getClient().getLogin().getBytes();
		byte bytePassword[] = getClient().getPassword().getBytes();
		
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
		
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, getClient().getRSAKey());
			encrypted = rsaCipher.doFinal(_raw, 0x00, 0x80 );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

}
