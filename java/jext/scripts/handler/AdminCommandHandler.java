package jext.scripts.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdminCommandHandler {
	private static AdminCommandHandler _instance;
	private Map<String, IAdminCommandHandler> _datatable;

	public static AdminCommandHandler getInstance() {
		if (_instance == null)
			_instance = new AdminCommandHandler();
		return _instance;
	}

	private AdminCommandHandler() {
		_datatable = new HashMap<String, IAdminCommandHandler>();
	}

	public void registerAdminCommandHandler(IAdminCommandHandler handler) {
		for (Enum<?> e : handler.getAdminCommandEnum())
			_datatable.put(e.toString().toLowerCase(), handler);
	}

	public IAdminCommandHandler getAdminCommandHandler(String adminCommand) {
		String command = adminCommand;
		if (adminCommand.indexOf(" ") != -1)
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		return _datatable.get(command);
	}

	public void useAdminCommandHandler(String adminCommand) {

		String[] wordList = adminCommand.split(" ");
		IAdminCommandHandler handler = _datatable.get(wordList[0]);
		if (handler != null) {
			int command_success = 0;
			try {
				for (Enum<?> e : handler.getAdminCommandEnum())
					if (e.toString().equalsIgnoreCase(wordList[0])) {
						command_success = handler.useAdminCommand(e, wordList,
								adminCommand) ? 1 : 0;
						break;
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Log.LogCommand(activeChar, Log.CMD_ADMH, adminCommand,
			// command_success);
		}
	}

	/**
	 * @return размер комманд
	 */
	public int size() {
		return _datatable.size();
	}

	public void clear() {
		_datatable.clear();
	}

	/**
	 * Получение списка зарегистрированных админ команд
	 * 
	 * @return список команд
	 */
	public Set<String> getAllCommands() {
		return _datatable.keySet();
	}
}