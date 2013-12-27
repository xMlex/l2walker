package fw.game;

import fw.connection.game.serverpackets.CharSelectionInfo.Heroes;




public interface GameVisualInterface
{
	public void procSetUserChar(final UserChar userChar);
	public void putMessage(final String msg, final int msg_type, final boolean bold);
	public void procLogoutEvent();
	public void requestPartyDialog(final String requestor,final String partyType);
	public void requestHtmlDialog(final String htmlMessage);
	public void requestHeroesListDialog(final Heroes[] heroesList, int size);
	public void procPlayerChar(final PlayerChar playerChar);
	public void procNpcChar(final NpcChar npcChar);
	public void procDeleteL2char(final L2Char l2Char);
	public void procDeleteItem(final L2Item item);
	public void procPlayerCharClanInfo(final PlayerChar playerChar);
	public void procMyTargetSelected(final L2Char targetChar);
	public void procUpdateTargetcharStatus(final L2Char targetChar);
	public void procUpdateUsercharStatus(final UserChar userChar);
	public void procMyTargetUnselected();
	public void procAddSkill(final L2Skill l2Skill);
	public void procSelfSkillUse(final L2Skill l2Skill,final L2SkillUse l2SkillUse);
	public void procRemoveSkill(final L2Skill l2Skill);
	public void procAddPartyChars(final L2PartyChar  l2PartyChars[]);
	public void procUpdatePartyChar(final L2PartyChar l2PartyChar);
	public void procDeletePartyChar(final L2PartyChar l2PartyChar);
	public void procDeleteAllPartyChars( );

	public void procAddItems(final L2Item  items[]);
	public void procAddEnvItem(final L2Item  item);
	public void procUpdateItems(final L2Item  items[]);
	public void procDeletItems(final L2Item  items[]);

	public void procTeleportClear();

	public void requestTrade(final L2Char tradeChar);
	public void requestTradeDialog(final L2Item items[]);
	public void procSendTradeDone();
	public void procConfirmedTrade();
	public void procTradeAddOwnItem(L2Item item);
	public void procTradeAddOtherItem(L2Item item);
	
	public boolean checkTradeDialog();

}
