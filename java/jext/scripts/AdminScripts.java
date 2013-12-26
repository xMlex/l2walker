package jext.scripts;

import jext.scripts.handler.IAdminCommandHandler;

public class AdminScripts implements IAdminCommandHandler {
	private static enum Commands {
		admin_scripts_reload, admin_sreload, admin_sqreload
	}

	public boolean useAdminCommand(Enum comm, String[] wordList,
			String fullString) {
		Commands command = (Commands) comm;

		switch (command) {
		case admin_scripts_reload:
		case admin_sreload:
			if (wordList.length < 2)
				return false;
			String param = wordList[1];
			if (param.equalsIgnoreCase("all")) {

				System.out.println("Scripts reload starting...");
				if (Scripts.getInstance().reload())
					System.out.println("Scripts reloaded with errors. Loaded "
							+ Scripts.getInstance().getClasses().size()
							+ " classes.");
				else
					System.out.println("Scripts successfully reloaded. Loaded "
							+ Scripts.getInstance().getClasses().size()
							+ " classes.");
			} else if (Scripts.getInstance().reloadClass(param))
				System.out.println("Scripts reloaded with errors. Loaded "
						+ Scripts.getInstance().getClasses().size()
						+ " classes.");
			else
				System.out.println("Scripts successfully reloaded. Loaded "
						+ Scripts.getInstance().getClasses().size()
						+ " classes.");
			break;
		case admin_sqreload:
			if (wordList.length < 2)
				return false;
			String quest = wordList[1];
			if (Scripts.getInstance().reloadQuest(quest))
				System.out.println("Quest '" + quest
						+ "' reloaded with errors.");
			else
				System.out.println("Quest '" + quest
						+ "' successfully reloaded.");
			// reloadQuestStates();
			break;
		}
		return true;
	}

	private void reloadQuestStates() {
		/*
		 * for(QuestState qs : p.getAllQuestsStates())
		 * p.delQuestState(qs.getQuest().getName());
		 * 
		 * Quest.playerEnter(p);
		 */
	}

	public Enum[] getAdminCommandEnum() {
		return Commands.values();
	}
}