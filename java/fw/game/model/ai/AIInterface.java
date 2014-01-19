package fw.game.model.ai;

import fw.game.model.L2Player;
import fw.game.model.L2PlayerEventListener;

public abstract class AIInterface implements Runnable,L2PlayerEventListener  {
	
	protected final L2Player _self;
	
	public AIInterface(L2Player self){
		_self = self;
	}
	
	public L2Player getSelf(){
		return _self;
	}
	
}
