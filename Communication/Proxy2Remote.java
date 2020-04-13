package Communication;

import java.io.*;
import java.net.Socket;

import Proxy.Session;

public class Proxy2Remote extends Thread {

	private static Socket remoteSocket;
	private static InputStream inputStream;
	private static Session sessionManager;
	public Proxy2Remote(Socket entrada, Session session)
	{
		remoteSocket = entrada;
		sessionManager = session;
	}
	public void run()
	{
		try {
			inputStream = new DataInputStream(remoteSocket.getInputStream());
			while(true) {
				byte[] buffer = new byte[inputStream.available()];
				if(buffer.length > 0) {
					for(int i =0; i < buffer.length; i++) {
						buffer[i] = (byte)inputStream.read();
					}
					sessionManager.client2ProxOutput.write(buffer);
					System.out.println("[Proxy2Remote] -> " + buffer.length);
				}
				Thread.sleep(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
