package jext.scripts;

/**
 * @Author: Death
 * @Date: 23/6/2007
 * @Time: 9:22:07
 * 
 *        Просто интерфейс с методами которые обязательно должны использоваться
 *        в скриптах.
 */
public interface ScriptFile {
	/**
	 * Вызывается при загрузке классов скриптов
	 */
	public void onLoad();

	/**
	 * Вызывается при перезагрузке После перезагрузки onLoad() вызывается
	 * автоматически
	 */
	public void onReload();

	/**
	 * Вызывается при выключении сервера
	 */
	public void onShutdown();
}
