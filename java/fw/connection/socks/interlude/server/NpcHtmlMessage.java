package fw.connection.socks.interlude.server;

import fw.connection.socks.interlude.L2GameSocksServerPacket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maxim on 01.08.14.
 */
public class NpcHtmlMessage extends L2GameSocksServerPacket {

    int npcObjId = 0;
    String html = "";

    @Override
    public void read() {
        npcObjId = readD();
        html = readS();
    }

    @Override
    public boolean excecute() {

        //_log.info("HTML: "+html);
        getClient().bypass_list.clear();
        Pattern p = Pattern.compile("\"bypass -h (.*?)\"");
        Matcher m = p.matcher(html);
        while(m.find()) {
            getClient().bypass_list.add(m.group(1));
        }
        return true;
    }
}
