package fw.game.engines;

import java.util.logging.Logger;

import fw.game.GameEngine;

public class IEngine {
	protected static final Logger _log = Logger.getLogger(IEngine.class
			.getName());
	protected final GameEngine _ge;
	public IEngine(GameEngine ge){
		_ge = ge;		
	}
}
