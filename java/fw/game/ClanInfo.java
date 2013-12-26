package fw.game;

import javolution.util.FastList;

public class ClanInfo
{
	public boolean initialized=false;
	public int clanId=0;
	public String clanName="";
	public String allyName="";
	
	public FastList<PlayerChar> listWaitForClanInfo=new FastList<PlayerChar>();	
}
