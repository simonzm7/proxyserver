package Main;

import Proxy.ProxyListen;

public class Program {

	public static void main(String[] args) {
		new ProxyListen(2001).start();
	}

}
