package fw.game.model;

public interface L2PlayerEventListener {

	public void onPlayerEvent(L2PlayerEvent evt);
	public void onPlayerEvent(L2PlayerEvent evt,int objId);
	public void onPlayerEvent(L2PlayerEvent evt,L2Object objId);
}
