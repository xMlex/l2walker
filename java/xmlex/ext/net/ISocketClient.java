package xmlex.ext.net;

//import java.io.IOException;
import java.net.*;

public class ISocketClient implements Runnable{
	
	private String host="localhost";
	private int port=2106,timeout = 10;
	private Socket socket;
	//private ISocketClientListener _listener = null;
	private Thread _worker = new Thread(this);
	
	public boolean connect(){
		
		// create a socket with a timeout
	    try
	    {
	      InetAddress inteAddress = InetAddress.getByName(host);
	      SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);
	  
	      // create a socket
	      socket = new Socket();
	  
	      // this method will block no more than timeout ms.	      
	      socket.connect(socketAddress, timeout*1000);
	      //if(socket.isConnected())
	      _worker.start();
	      return socket.isConnected();
	    } 
	    catch (Exception e) 
	    {
	      System.err.println("Timed out waiting for the socket. Error: "+e.getMessage());
	      e.printStackTrace();	     
	    }
		
		return false;
	}

	public void run() {
		
		try {
			
			Thread.sleep(2);
		} catch (Exception e) {}
		
		if(socket == null) return;
		//if(socket.isClosed()) return;
		
		
		//socket.getInputStream().
	}

}
