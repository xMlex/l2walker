package fw.connection.login.clientpackets;

import fw.connection.LoginResult;

public interface ILoginConnectionListener {
	public String getLoginHost();
	public int getLoginPort();
	public String getLogin();	
	public String getPassword();
	public int getLoginServerId();
	public void setLoginResult(LoginResult res);
	public void LoginConnectionMessage(String msg);
	public void LoginConnectionOnConnected();
	public void LoginConnectionOnDisconnect();
}
