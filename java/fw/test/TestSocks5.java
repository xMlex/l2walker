package fw.test;

public class TestSocks5 {
	
	private  static Socks5Proxy _proxy;
	
	public static void main(String[] args) {
		_proxy = new Socks5Proxy();
		_proxy.start();
	}
	
}
