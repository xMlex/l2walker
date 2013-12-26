package jext.scripts.handler;

public interface IAdminCommandHandler {
	/**
	 * this is the worker method that is called when someone uses an admin
	 * command.
	 * 
	 * @param fullString
	 *            TODO
	 * @param command
	 * @return command success
	 */
	@SuppressWarnings("rawtypes")
	public boolean useAdminCommand(Enum comm, String[] wordList,
			String fullString);

	/**
	 * this method is called at initialization to register all the item ids
	 * automatically
	 * 
	 * @return all known itemIds
	 */
	@SuppressWarnings("rawtypes")
	public Enum[] getAdminCommandEnum();
}