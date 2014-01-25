package fw.game.model.ai;

import java.util.ArrayList;

public class L2FishingInfo {	
	public int fishType = 4,time=0,fish_HP=0;
	public boolean isNightLure = false,HPstop=false,GoodUse=false,started = false;
	public ArrayList<L2FishingListener> _listeners = new ArrayList<L2FishingListener>();
	
	
	public void setStarted(boolean b) {	
		started = b;
		for(L2FishingListener _l:_listeners)
			_l.FishingStartEnd();
	}
	
	public void setHPRegen(int b) {	
		fish_HP = b;
		for(L2FishingListener _l:_listeners)
			_l.FishingHpRegen();
	}
	
	
	
	
}
