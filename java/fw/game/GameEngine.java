package fw.game;

import fw.game.clientpackets.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import fw.common.ThreadPoolManager;

//import fw.util.Packets;
//import fw.util.Printer;
import javolution.util.FastMap;
import fw.connection.ConnectionEventReceiver;
import fw.connection.ENUM_CONECTION_EVENT;
import fw.connection.GameConnection;
import fw.connection.GamePackageEventReceiver;
import fw.connection.LoginConnection;
import fw.connection.LoginResult;
import fw.connection.Msg;
import fw.connection.game.server.*;
import fw.dbClasses.DbObjects;

public class GameEngine implements ConnectionEventReceiver,
		GamePackageEventReceiver {
	private static final Logger _log = Logger.getLogger(GameEngine.class
			.getName());
	public final static int MSG_SYSTEM_NORMAL = 1;
	public final static int MSG_SYSTEM_FAIL = 2;
	public final static int MSG_SYSTEM_SUCESS = 3;
	public final static int MSG_SYSTEM_ATENTION = 4;
	public final static int MSG_CHAT_NORMAL = 10;
	public final static int MSG_CHAT_ALL = 11;
	public final static int MSG_ANUNCEMENT = 20;
	public final static int MSG_TELL = 30;
	public final static int MSG_TRADE = 40;
	public final static int MSG_PARTY = 50;
	public final static int MSG_CLAN = 60;
	public final static int MSG_ALLY = 70;
	public final static int MSG_HERO = 80;
	public final static int MSG_COMBAT = 90;

	private LoginConnection loginConnection = null;
	private GameConnection gameConnection = null;
	private DbObjects dbObjects = null;
	private UserChar userChar = null;
	private L2Char currentTarget = null;
	private Maps maps = new Maps();
	private FastMap<String, L2Skill> userCharSkills = new FastMap<String, L2Skill>();
	private FastMap<Integer, L2Item> userCharItems = new FastMap<Integer, L2Item>();
	private FastMap<Integer, FastMap<Integer, L2Item>> userCharItemsFromId = new FastMap<Integer, FastMap<Integer, L2Item>>();

	private GameVisualInterface visualInterface = null;
	private boolean logout = false;
	private boolean isInLogedState = false;
	private int charNum;
	private int protocol = 152;
	private int serverNum;

	public GameEngine(GameVisualInterface visualInterface) {
		this.visualInterface = visualInterface;
	}

	public void setLogout() {
		if (isInLogedState) {
			logout = true;
		}

	}

	public void doLogin(final String login, final String password,
			final String host, final int port, int protocol,
			final int serverNum, int charNum) {
		this.protocol = protocol;
		this.serverNum = serverNum;
		this.charNum = charNum;
		loginConnection = new LoginConnection(this, host, port, serverNum,
				login, password);

		visualInterface.putMessage("LOGIN HOST " + host + " PORT " + port,
				MSG_SYSTEM_ATENTION, true);

		new Thread() {
			@Override
			public void run() {
				try {
					logout = false;
					isInLogedState = true;

					LoginResult loginResult = null;
					loginConnection.fireLogin();
					loginConnection.start();

					while ((loginResult = loginConnection.getLoginResult()) == null) {
						if (logout)
							break;

						try {
							sleep(200);
						} catch (InterruptedException e) {
							// NADA
						}
					}

					loginConnection.setTerminate();

					if (!logout) {
						if (!loginResult.ok) {
							visualInterface.putMessage(loginResult.motivo,
									MSG_SYSTEM_FAIL, true);
						} else {
							visualInterface.putMessage(loginResult.motivo,
									MSG_SYSTEM_SUCESS, true);
							connectToGameHost(loginResult);
						}
					}

				} catch (IOException e) {
					visualInterface.putMessage("CONNECTION ERROR",
							MSG_SYSTEM_FAIL, true);
				} catch (Exception e) {
					visualInterface.putMessage("UNKNOW ERROR", MSG_SYSTEM_FAIL,
							true);
					e.printStackTrace();
				}

				logoutEvent();
				isInLogedState = false;
			}
		}.start();
	}

	private void logoutEvent() {
		loginConnection = null;
		gameConnection = null;
		userChar = null;
		currentTarget = null;
		userCharSkills.clear();
		maps.removeAll();
		visualInterface.procLogoutEvent();
	}

	@SuppressWarnings("static-access")
	private void connectToGameHost(LoginResult loginResult) throws IOException {

		visualInterface.putMessage("GAME HOST [" + serverNum + "] "
				+ loginResult.host.Addr + " PORT " + loginResult.host.port,
				MSG_SYSTEM_ATENTION, true);
		visualInterface.putMessage("GAME PROTOCOL " + protocol,
				MSG_SYSTEM_ATENTION, true);
		visualInterface.putMessage("GAME CHAR [" + charNum + "]",
				MSG_SYSTEM_ATENTION, true);

		gameConnection = new GameConnection(this, this, loginResult, protocol,
				charNum);
		gameConnection.fireGame();
		gameConnection.start();

		while (gameConnection.isConnected()) {
			if (logout) {
				gameConnection.setTerminate();
				return;
			}

			try {
				Thread.currentThread().sleep(200);
			} catch (InterruptedException e) {
				// NADA
			}
		}

		gameConnection.setTerminate();
	}

	public void procConnectionEvent(Object data, ENUM_CONECTION_EVENT event) {
		if (event == ENUM_CONECTION_EVENT.EVT_MSG) {
			if (data instanceof String) {
				visualInterface.putMessage((String) data, MSG_SYSTEM_NORMAL,
						false);
			} else if (data instanceof Msg) {
				Msg msg = (Msg) data;
				int msg_type = MSG_SYSTEM_NORMAL;

				if (msg.type == Msg.MSG_TYPE.ATENTION)
					msg_type = MSG_SYSTEM_ATENTION;
				else if (msg.type == Msg.MSG_TYPE.SUCESS)
					msg_type = MSG_SYSTEM_SUCESS;
				else if (msg.type == Msg.MSG_TYPE.FAIL)
					msg_type = MSG_SYSTEM_FAIL;

				visualInterface.putMessage(msg.msg, msg_type, true);
			}
		}
	}

	public L2Char getCurrentTarget() {
		return currentTarget;
	}

	public DbObjects getDbObjects() {
		return dbObjects;
	}

	public GameConnection getGameConnection() {
		return gameConnection;
	}

	public LoginConnection getLoginConnection() {
		return loginConnection;
	}

	public Maps getMaps() {
		return maps;
	}

	public UserChar getUserChar() {
		if(userChar == null) userChar = new UserChar();
		return userChar;
	}

	public GameVisualInterface getVisualInterface() {
		return visualInterface;
	}

	public void sendPacket(byte data[]) {
		if (gameConnection != null)
			try {
				gameConnection.sendPacket(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void procGamePackage(byte[] data) {

		byte id = data[0];
		_log.fine("PKT id: " + Integer.toHexString(id));
		/*
		 * System.out .println(
		 * "\nINI====================================================================="
		 * ); System.out.println(Packets.getServerMessageType(pktType, 0));
		 * System.out.print(Printer.printData(data, data.length)); System.out
		 * .println(
		 * "\nEND====================================================================="
		 * );
		 */
		GameServerPacket packet = null;
		switch (id) {
		case 0x0A:
			getVisualInterface().putMessage("LoginFail",0, true);
			break;	
		case 0x09:
			packet = new CharSelectionInfo();
			break;
		default:
			_log.warning("Unknow id: "+Integer.toHexString(id));
			break;
		}
		if(packet != null){
			packet.setClient(this);
			packet.setByteBuffer(ByteBuffer.wrap(data));			
			ThreadPoolManager.getInstance().executeLSGSPacket(packet);
		}

	}

	public void setDbObjects(DbObjects dbObjects) {
		this.dbObjects = dbObjects;
		maps.setDbObjects(dbObjects);
	}

	public void sendMessage(String type, String text, String target) {
		if (gameConnection == null)
			return;

		Say mySay = new Say(this, type, text, target);
		mySay.runImpl();

	}

	public void sendLeaveParty() {
		if (gameConnection == null)
			return;
		RequestWithDrawalParty myRequestWithDrawalParty = new RequestWithDrawalParty(
				this);
		myRequestWithDrawalParty.runImpl();
	}

	public void logOut() {
		if (gameConnection == null)
			return;
		Logout myLogout = new Logout(this);
		myLogout.runImpl();
	}

	public void sendRequestSkillList() {
		if (gameConnection == null)
			return;

		RequestSkillList myRequestSkillList = new RequestSkillList(this);
		myRequestSkillList.runImpl();

		RequestSkillCooltime myRequestSkillCooltime = new RequestSkillCooltime(
				this);
		myRequestSkillCooltime.runImpl();
	}

	public void sendRequestUseSkill(int skill_id, int ctrlPressed,
			int shiftPressed) {
		if (gameConnection == null)
			return;
		RequestSkillUse myRequestSkillUse = new RequestSkillUse(this, skill_id,
				ctrlPressed, shiftPressed);
		myRequestSkillUse.runImpl();
	}

	public void sendAnswerJoinParty(int response) {
		if (gameConnection == null)
			return;
		RequestAnswerJoinParty myRequestAnswerJoinParty = new RequestAnswerJoinParty(
				this, response);
		myRequestAnswerJoinParty.runImpl();
	}

	public void sendHtmlRequest(String command) {
		if (gameConnection == null)
			return;
		RequestBypassToServer myRequestBypassToServer = new RequestBypassToServer(
				this, command);
		myRequestBypassToServer.runImpl();
		// System.out.println(command);
	}

	public void sendAction(int objectIdTarget) {
		if (gameConnection == null)
			return;
		Action myAction = new Action(this, userChar, objectIdTarget, 0x00);
		myAction.runImpl();
	}

	public void sendAddTradeItem(L2Item tradeItem, int count) {
		if (gameConnection == null)
			return;
		AddTradeItem myAddTradeItem = new AddTradeItem(this, tradeItem, count);
		myAddTradeItem.runImpl();
	}

	public void requestTrade(int objectIdTarget) {
		if (gameConnection == null)
			return;
		TradeRequest myTrade = new TradeRequest(this, objectIdTarget);
		myTrade.runImpl();
	}

	public void sendUseItem(int objectId) {
		if (gameConnection == null)
			return;
		RequestUseItem myRequestUseItem = new RequestUseItem(this, objectId);
		myRequestUseItem.runImpl();
	}

	public void sendDropItem(int objectId, int count, int x, int y, int z) {
		if (gameConnection == null)
			return;
		RequestDropItem myDropItem = new RequestDropItem(this, objectId, count,
				x, y, z);
		myDropItem.runImpl();
	}

	public void sendDeleteItem(int objectId, int count) {
		if (gameConnection == null)
			return;
		RequestDestroyItem myDeleteItem = new RequestDestroyItem(this,
				objectId, count);
		myDeleteItem.runImpl();
	}

	public void sendDeleteItem(int objectId) {
		if (gameConnection == null)
			return;
		RequestUseItem myRequestUseItem = new RequestUseItem(this, objectId);
		myRequestUseItem.runImpl();
	}

	public void sendCancelTarget() {
		if (gameConnection == null)
			return;
		RequestTargetCanceld myRequestTargetCanceld = new RequestTargetCanceld(
				this);
		myRequestTargetCanceld.runImpl();
	}

	public void sendTradeDone(int result) {
		if (gameConnection == null)
			return;
		TradeDone myTradeDone = new TradeDone(this, result);
		myTradeDone.runImpl();
	}

	public void sendPartyInvite(String charName, int partyType) {
		if (gameConnection == null)
			return;
		RequestJoinParty myRequestJoinParty = new RequestJoinParty(this,
				charName, partyType);
		myRequestJoinParty.runImpl();
	}

	public void sendAnswerTradeRequest(int response) {
		if (gameConnection == null)
			return;
		AnswerTradeRequest myAnswerTradeRequest = new AnswerTradeRequest(this,
				response);
		myAnswerTradeRequest.runImpl();
	}

	public void sendEnterWorld() {
		if (gameConnection == null)
			return;

		EnterWorld myEnterWorld = new EnterWorld(this);
		myEnterWorld.runImpl();
	}

	public void removeCurrentTarget() {
		this.currentTarget = null;
		visualInterface.procMyTargetUnselected();
	}

	public void deleteL2char(L2Char l2Char) {
		visualInterface.procDeleteL2char(l2Char);
	}

	public void deleteItem(L2Item item) {
		visualInterface.procDeleteItem(item);
	}

	public void setCurrentTarget(L2Char currentTarget) {
		this.currentTarget = currentTarget;
		visualInterface.procMyTargetSelected(currentTarget);
	}

	public FastMap<String, L2Skill> getUserCharSkills() {
		return userCharSkills;
	}

	public void addPartyChars(final L2PartyChar l2PartyChars[]) {
		visualInterface.procAddPartyChars(l2PartyChars);
	}

	public void updatePartyChar(final L2PartyChar l2PartyChar) {
		visualInterface.procUpdatePartyChar(l2PartyChar);
	}

	public void deletePartyChar(final L2PartyChar l2PartyChar) {
		visualInterface.procDeletePartyChar(l2PartyChar);
	}

	public void deleteAllPartyChars() {
		visualInterface.procDeleteAllPartyChars();
	}

	public FastMap<Integer, L2Item> getUserCharItems() {
		return userCharItems;
	}

	public FastMap<Integer, FastMap<Integer, L2Item>> getUserCharItemsFromId() {
		return userCharItemsFromId;
	}

}
