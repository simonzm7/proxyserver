package Proxy;

import java.io.IOException;
import java.net.*;
public class ProxyListen extends Thread{

	private int port;
	private ServerSocket serverSocket;
	public ProxyListen(int port)
	{
		this.port = port;
		this.Initialize();
	}
	private void Initialize()
	{
		try {
			serverSocket = new ServerSocket();
			InetSocketAddress endpoint = new InetSocketAddress(this.port);
			serverSocket.bind(endpoint);
			System.out.println("Proxy Server Listening on port 2001");

		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	private void startSession()
	{
		try {
			new Session(serverSocket.accept()).start();
		}catch (Exception e) {
			
		}
		finally {
			startSession();
		}
	}
	public void run()
	{
		startSession();
	}
}
