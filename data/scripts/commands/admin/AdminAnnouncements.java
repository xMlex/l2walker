package commands.admin;

import jext.scripts.ScriptFile;
import jext.scripts.handler.AdminCommandHandler;
import jext.scripts.handler.IAdminCommandHandler;



/**
 * This class handles following admin commands: - announce text = announces text
 * to all players - list_announcements = show menu - reload_announcements =
 * reloads announcements from txt file - announce_announcements = announce all
 * stored announcements to all players - add_announcement text = adds text to
 * startup announcements - del_announcement id = deletes announcement with
 * respective id
 */
public class AdminAnnouncements implements IAdminCommandHandler, ScriptFile
{
	private static enum Commands
	{
		admin_list_announcements,
		admin_announce_announcements,
		admin_add_announcement,
		admin_del_announcement,
		admin_announce,
		admin_a,
		admin_announce_menu,
		admin_crit_announce,
		admin_c,
		admin_toscreen,
		admin_s,
		admin_reload_announcements
	}

	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString)
	{
		Commands command = (Commands) comm;

		System.out.println("Excecute script command - ok: "+this.getClass().getName());
		//Announcements a = Announcements.getInstance();

		/*switch(command)
		{
			case admin_list_announcements:
				a.listAnnouncements(activeChar);
				break;
			case admin_announce_menu:
				a.handleAnnounce(fullString, 20);
				a.listAnnouncements(activeChar);
				break;
			case admin_announce_announcements:
				for(L2Player player : L2ObjectsStorage.getAllPlayersForIterate())
					a.showAnnouncements(player);
				a.listAnnouncements(activeChar);
				break;
			case admin_add_announcement:
				if(wordList.length < 2)
					return false;
				try
				{
					String val = fullString.substring(23);
					a.addAnnouncement(val);
					a.listAnnouncements(activeChar);
				}
				catch(StringIndexOutOfBoundsException e)
				{}
				break;
			case admin_del_announcement:
				if(wordList.length < 2)
					return false;
				try
				{
					int val = new Integer(fullString.substring(23));
					a.delAnnouncement(val);
					a.listAnnouncements(activeChar);
				}
				catch(StringIndexOutOfBoundsException e)
				{}
				break;
			case admin_announce:
				a.handleAnnounce(fullString, 15);
				break;
			case admin_a:
				a.handleAnnounce(fullString, 8);
				break;
			case admin_crit_announce:
			case admin_c:
				if(wordList.length < 2)
					return false;
				Announcements.getInstance().announceToAll(activeChar.getName() + ": " + fullString.replaceFirst("admin_crit_announce ", "").replaceFirst("admin_c ", ""), Say2C.CRITICAL_ANNOUNCEMENT);
				break;
			case admin_toscreen:
			case admin_s:
				if(wordList.length < 2)
					return false;
				String text = activeChar.getName() + ": " + fullString.replaceFirst("admin_toscreen ", "").replaceFirst("admin_s ", "");
				int time = 3000 + text.length() * 100; // 3 секунды + 100мс на символ
				ExShowScreenMessage sm = new ExShowScreenMessage(text, time, ScreenMessageAlign.TOP_CENTER, text.length() < 64);
				for(L2Player player : L2ObjectsStorage.getAllPlayersForIterate())
					player.sendPacket(sm);
				break;
			case admin_reload_announcements:
				Announcements.getInstance().loadAnnouncements();
				Announcements.getInstance().listAnnouncements(activeChar);
				activeChar.sendMessage("Announcements reloaded.");
				break;
		}*/

		return true;
	}

	public Enum[] getAdminCommandEnum()
	{
		return Commands.values();
	}

	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	public void onReload()
	{}

	public void onShutdown()
	{}
}