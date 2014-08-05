package fw.connection.socks_login;

import fw.util.Rnd;
import xmlex.ext.crypt.NewCrypt;

import java.io.IOException;

/**
 * Created by Maxim on 04.08.14.
 */
public class LoginClientCrypt {
    private static final byte[] STATIC_BLOWFISH_KEY =
            {
                    (byte) 0x6b,
                    (byte) 0x60,
                    (byte) 0xcb,
                    (byte) 0x5b,
                    (byte) 0x82,
                    (byte) 0xce,
                    (byte) 0x90,
                    (byte) 0xb1,
                    (byte) 0xcc,
                    (byte) 0x2b,
                    (byte) 0x6c,
                    (byte) 0x55,
                    (byte) 0x6c,
                    (byte) 0x6c,
                    (byte) 0x6c,
                    (byte) 0x6c
            };

    private NewCrypt _staticCrypt = new NewCrypt(true);
    private NewCrypt _crypt;
    public boolean _static = true;

    public void setKey(byte[] key)
    {
        _crypt = new NewCrypt(key);
    }

    public NewCrypt getCrypt()
    {
        return _crypt;
    }

    public boolean decrypt(byte[] raw, final int offset, final int size) throws IOException
    {
        if(_static){
            _staticCrypt.decrypt(raw, offset, size);
            NewCrypt.decXORPass(raw, offset, size);
        }else
            _crypt.decrypt(raw, offset, size);
        return NewCrypt.verifyChecksum(raw, offset, size);
    }

    public int encrypt(byte[] raw, final int offset, int size) throws IOException
    {
        // reserve checksum
        size += 4;

        if(_static)
        {
            // reserve for XOR "key"
            size += 4;
            // padding
            size += 8 - size % 8;
            NewCrypt.encXORPass(raw, offset, size, Rnd.nextInt());
            _staticCrypt.crypt(raw, offset, size);
            _static = false;
        }
        else
        {
            // padding
            size += 8 - size % 8;
            NewCrypt.appendChecksum(raw, offset, size);
            _crypt.crypt(raw, offset, size);
        }
        //_static = false;
        return size;
    }
}
