package xmlex.ext.client;

public class TaskConnect implements Runnable{

	private L2BaseClient _client;
	
	public TaskConnect(L2BaseClient client){
		System.out.println("ok1");
		_client = client;
	}
	
	public void run() {		
		System.out.println("ok");
		_client.connect();
	}

}
