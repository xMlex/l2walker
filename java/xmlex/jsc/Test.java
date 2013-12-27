package xmlex.jsc;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		
		
		ArrayList<TestClient> _clients = new ArrayList<TestClient>();
		
		for (int i = 0; i < 1; i++) {
			_clients.add(new TestClient());
			_clients.get(i).connect();
		}
		
		Thread.sleep(10000);
		
		for (int i = 0; i < 1; i++) {
			_clients.get(i).disconnect();
			//_clients.remove(i);
		}
		
		/*Thread.sleep(2000);
		
		for (int i = 0; i < 1; i++) {
			_clients.get(i).connect();
		}
		
		Thread.sleep(5000);
		
		for (int i = 0; i < 1; i++) {
			_clients.get(i).disconnect();
			_clients.remove(i);
		}*/
		
		
		/*scManager manager = new scManager();
		
		ArrayList<scClient> _list = new ArrayList<scClient>();
		for (int i = 0; i < 20; i++) {
			_list.add(new scClient());
			manager.addClient(_list.get(i));
			Thread.sleep(500);
		}
		
		Thread.sleep(2000);
		
		for (int i = 0; i < 20; i++) {
			manager.disconnectClient(_list.get(i));
		}*/
		
		//scClient client = new scClient();
		
		//manager.addClient(client);	
		
		/*for (int i = 0; i < 200; i++) {
			Thread.sleep(20);		
			manager.addClient(new scClient());
		}*/
		
		
		//while(true){ Thread.sleep(200); }

	}

}
