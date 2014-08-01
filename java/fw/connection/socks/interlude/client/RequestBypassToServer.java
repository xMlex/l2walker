package fw.connection.socks.interlude.client;

import fw.connection.socks.interlude.L2GameSocksClientPacket;

/**
 * Created by Maxim on 01.08.14.
 */
public class RequestBypassToServer extends L2GameSocksClientPacket {
    @Override
    public void excecute()
    {
        String bp = "",r_br = readS();

        for (int i = 0; i < getClient().bypass_list.size(); i++)
            if(getClient().bypass_list.get(i).equalsIgnoreCase(r_br))
                bp = getClient().bypass_list.get(i);
        if(!bp.isEmpty()){
            writeC(0x21);
            writeS(bp);
            //_log.info("RequestBypassToServer: "+bp);
        }else{
            _log.info("!RequestBypassToServer: "+bp+" Orig: "+r_br);
        }

    }
}
