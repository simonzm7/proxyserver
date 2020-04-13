package Communication;

import java.io.*;
import java.net.Socket;

import Proxy.Session;

public class Proxy2Remote extends Thread {

	private static Socket remoteSocket;
	private static DataInputStream inputStream;
	private static DataOutputStream outputStream;
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
			outputStream = new DataOutputStream(remoteSocket.getOutputStream());
			while(true) {
				byte[] b = new byte[inputStream.available()];
				if(b.length > 0) {
					for(int i =0; i < b.length; i++)
					{
						b[i] = (byte)inputStream.read();
					}
					String data = new String(b);
					//Recibe el resultado de la peticion del servidor remoto
					sessionManager.Proxy2Client(b);
					System.out.println("RESPUESTA ENVIADA AL CLIENTE");
				}
				Thread.sleep(200);
			}
		}catch (Exception e) {
			System.out.println("Read[client2proxinput] - > " + e.toString());
		}
	}
	public static void sendProxy2Server(byte[] b)
	{
		try {
			outputStream.write(b);
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
