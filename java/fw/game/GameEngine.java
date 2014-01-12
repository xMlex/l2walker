package fw.game;

import fw.game.clientpackets.*;
import fw.game.model.L2MoveTask;
import fw.game.model.L2Object;
import fw.game.model.L2Player;

import java.util.logging.Logger;

import fw.common.ThreadPoolManager;
import javolution.util.FastMap;
import fw.connection.GameConnection;
import fw.connection.LoginConnection;
import fw.connection.LoginResult;
import fw.connection.game.IGameConnectionLitener;
import fw.connection.game.clientpackets.LogoutRequest;
import fw.connection.login.clientpackets.ILoginConnectionListener;
import fw.dbClasses.DbObjects;

public class GameEngine implements IGameConnectionLitener,ILoginConnectionListener,Runnable {
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
	
	private DbObjects dbObjects = null;

	private GameVisualInterface visualInterface = null;
	private boolean logout = false;
	private boolean isInLogedState = false;
	
	// NETWORK
	private LoginConnection loginConnection = null;
	private GameConnection gameConnection = null;
	private LoginResult _loginResult = null;
	private String _login,_password,_loginHost;
	private int _loginPort,_loginServerId,_protocolVersion,_charNum;
	
	// GAME
	private boolean _enabled = false;
	private L2World _world = new L2World();
	private L2Player _selfChar;
	private final CombatEngine _combatengine;
	private final L2MoveTask _moveTask;
	
	public GameEngine(GameVisualInterface visualInterface) {
		_combatengine = new CombatEngine(this);
		_moveTask = new L2MoveTask(this);
		this.visualInterface = visualInterface;
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, 1000, 1000);
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(_moveTask, 1000, 500);
	}

	public void setLogout() {
		if (isInLogedState) {
			logout = true;
		}
	}

	public void doLogin(final String login, final String password,
			final String host, final int port, int protocol,
			final int serverNum, int charNum) {
		_login = login;
		_password = password;
		_loginHost = host;
		_loginPort = port;
		_protocolVersion = protocol;
		_charNum = charNum;
		_loginServerId = serverNum;
		
		loginConnection = new LoginConnection(this);

		visualInterface.putMessage("LOGIN HOST " + host + " PORT " + port,
				MSG_SYSTEM_ATENTION, true);

		loginConnection.start();		
	}

	private void logoutEvent() {
		loginConnection = null;
		gameConnection = null;
		setSelfChar(null);
		_world.clear();
		visualInterface.procLogoutEvent();
	}

	private void connectToGameHost(LoginResult loginResult) {

		visualInterface.putMessage("GAME HOST [" + _loginServerId + "] "
				+ loginResult.host.Addr + " PORT " + loginResult.host.port,
				MSG_SYSTEM_ATENTION, true);
		visualInterface.putMessage("GAME PROTOCOL " + _protocolVersion,
				MSG_SYSTEM_ATENTION, true);
		visualInterface.putMessage("GAME CHAR [" + _charNum + "]",
				MSG_SYSTEM_ATENTION, true);

		gameConnection = new GameConnection(this);
		gameConnection.start();
	}

	public L2Object getCurrentTarget() {
		return getSelfChar().getTarget();
	}

	public GameConnection getGameConnection() {
		return gameConnection;
	}

	public LoginConnection getLoginConnection() {
		return loginConnection;
	}

	public GameVisualInterface getVisualInterface() {
		return visualInterface;
	}

	public void sendPacket(byte data[]) {
		if (gameConnection != null)
			try {
				//gameConnection.sendPacket(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}


	public void logOut() {
		logout = true;
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


	public void removeCurrentTarget() {
		//this.currentTarget = null;
		visualInterface.procMyTargetUnselected();
	}

	public void deleteL2char(L2Char l2Char) {
		visualInterface.procDeleteL2char(l2Char);
	}

	public void deleteItem(L2Item item) {
		visualInterface.procDeleteItem(item);
	}

	public void setCurrentTarget(L2Char currentTarget) {
		//this.currentTarget = currentTarget;
		visualInterface.procMyTargetSelected(currentTarget);
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

	public void GameConnectionOnConnect() {
		visualInterface.putMessage("Connected to game server",MSG_SYSTEM_SUCESS, true);		
	}

	public void GameConnectionOnDisconnect() {
		logoutEvent();
		visualInterface.putMessage("Disconnected from game server",MSG_SYSTEM_NORMAL, true);
		getWorld().clear();
	}

	public void GameConnectionMessage(String msg) {
		visualInterface.putMessage("[G] "+msg,MSG_SYSTEM_NORMAL, false);		
	}

	public void GameConnectionError(String msg) {
		visualInterface.putMessage("[G] "+msg,MSG_SYSTEM_FAIL, true);			
	}

	public int GameConnectionGetCharNum() {
		return _charNum;
	}

	public int GameConnectionGetProtocolVersion() {
		return _protocolVersion;
	}

	public LoginResult GetLoginResult() {
		return _loginResult;
	}

	public String getLoginHost() {
		return _loginHost;
	}

	public int getLoginPort() {
		return _loginPort;
	}

	public String getLogin() {
		return _login;
	}

	public String getPassword() {
		return _password;
	}

	public int getLoginServerId() {
		return _loginServerId;
	}

	public void setLoginResult(LoginResult res) {		
		_loginResult = res;		
		if(res.ok){
			connectToGameHost(_loginResult);
		}else{
			LoginConnectionMessage(res.motivo);
			logoutEvent();
		}
	}

	public void LoginConnectionMessage(String msg) {	
		visualInterface.putMessage("[L] "+msg,MSG_SYSTEM_NORMAL, false);
	}

	public void LoginConnectionOnConnected() {	
		visualInterface.putMessage("[L] Connected",MSG_SYSTEM_SUCESS, true);
	}

	public void LoginConnectionOnDisconnect() {		
		visualInterface.putMessage("[L] Disconnected",MSG_SYSTEM_ATENTION, false);		
	}

	public void run() {
		if(logout){
			if(loginConnection != null)
				loginConnection.getSocket().disconnect();
			if(gameConnection != null)
				gameConnection.sendPacket(new LogoutRequest());
			logout = false;
		}		
	}
	
	public int getCharNum() {
		return _charNum;
	}

	public GameEngine getGameEngine() {
		return this;
	}

	public L2World getWorld() {
		return _world;
	}

	public L2Player getSelfChar() {
		return _selfChar;
	}

	public void setSelfChar(L2Player _selfChar) {
		this._selfChar = _selfChar;
	}

	public DbObjects getDbObjects()
	{
		return dbObjects;
	}

	public void setDbObjects(DbObjects dbObjects)
	{
		this.dbObjects = dbObjects;
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean _enabled) {
		this._enabled = _enabled;
	}
}
