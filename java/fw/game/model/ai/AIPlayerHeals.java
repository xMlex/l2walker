package fw.game.model.ai;

import fw.connection.game.clientpackets.RequestActionUse;
import fw.game.model.L2Object;
import fw.game.model.L2Player;
import fw.game.model.L2PlayerEvent;

public class AIPlayerHeals extends AIInterface {

	private boolean _useSkillRelax = false;
	
	public AIPlayerHeals(L2Player self) {
		super(self);
	}

	public void run() {
		if(getSelf().getCurrentHpPercents() < 80 && !getSelf().isSitting()){
			//getSelf().setCurAction(_curAction);
			getSelf().sendPacket(new RequestActionUse(RequestActionUse.SIT_STAND));
		}
		
		if(getSelf().getCurrentHpPercents() > 80 && getSelf().isSitting()){
			getSelf().sendPacket(new RequestActionUse(RequestActionUse.SIT_STAND));
		}
	}

	public enum HealStateType{
		HP,MP,CP,WEIGHT
	}
	
	class AIBaseHealRule{
		public boolean enabled = false;
		
	}

	public void onPlayerEvent(L2PlayerEvent evt) {
		if(L2PlayerEvent.StatusUpdate == evt)
			run();		
	}

	public void onPlayerEvent(L2PlayerEvent evt, int objId)
	{
		
	}

	public void onPlayerEvent(L2PlayerEvent evt, L2Object objId) 
	{
		
	}
	
}
