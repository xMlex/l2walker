package fw.connection.game;

import fw.connection.LoginResult;
import fw.game.GameEngine;
import fw.game.GameVisualInterface;

public interface IGameConnectionLitener {
	public void GameConnectionOnConnect();

	public void GameConnectionOnDisconnect();

	public void GameConnectionMessage(String msg);

	public void GameConnectionError(String msg);

	public int GameConnectionGetCharNum();

	public int GameConnectionGetProtocolVersion();
	public GameVisualInterface getVisualInterface();
	public GameEngine getGameEngine();
	public LoginResult GetLoginResult();
}
