package fw.extensions.util;


public class Stats
{
	public static int getOnline()
	{
		return 0;//L2ObjectsStorage.getAllPlayersCount();
	}

	public static int getOnline(boolean includeFake)
	{
		return 0;//getOnline() + (includeFake ? FakePlayersTable.getFakePlayersCount() : 0);
	}
}