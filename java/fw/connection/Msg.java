package fw.connection;

public class Msg
{
	public enum MSG_TYPE
	{
		ATENTION, SUCESS, FAIL
	}

	public final MSG_TYPE type;
	public final String msg;

	public Msg(MSG_TYPE type, String msg)
	{
		this.type = type;
		this.msg = msg;
	}

}
