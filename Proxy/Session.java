package Proxy;
import java.io.*;
import java.net.*;

import Communication.Client2Proxy;
import Communication.Proxy2Remote;
public class Session extends Thread{
	public Socket socketManager;
	private DataInputStream client2ProxInput;
	public DataOutputStream client2ProxOutput;
	public Session(Socket socketManager)
	{
		
		try {
			this.socketManager = socketManager;
			this.socketManager.setSoTimeout(2000);
			System.out.println("Client Connected: " + this.socketManager.getRemoteSocketAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void run()
	{
		try {
			client2ProxInput = new DataInputStream(this.socketManager.getInputStream());
			client2ProxOutput = new DataOutputStream(this.socketManager.getOutputStream());
				byte[] b = new byte[this.client2ProxInput.available()];
				if(b.length > 0) {
					for(int i =0; i < b.length; i++)
					{
						b[i] = (byte)this.client2ProxInput.read();
					}
					String data = new String(b);
					//System.out.println(data);
					String[] split = data.split(" ", 2);
					String[] host = split[1].split(":");
					if(split[0].equals("CONNECT")) {
						System.out.println("HTTPS CONNECT Request");
						CONNECTRequest(host[0], 443);
					}
					else if(split[0].equals("GET")) {
						System.out.println("GET Request");
						//System.out.println("GET ->" + split[1]);
					}
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void CONNECTRequest(String host, int port)
	{
		try {
			Socket proxy2Remote = new Socket(host, port);
			System.out.println("Proxy connected to remote server: " + host + ":" + port);
			String response = "HTTP/1.1 200 Connection Established\r\n"  + "Connection: close\r\n\r\n";
			this.client2ProxOutput.write(response.getBytes());
			DataOutputStream remoteOutput = new DataOutputStream(proxy2Remote.getOutputStream());
			new Client2Proxy(this.client2ProxInput, remoteOutput).start();
			new Proxy2Remote(proxy2Remote, this).start();

			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void Proxy2Client(byte[] b)
	{
		try {
			this.client2ProxOutput.write(b);
			this.client2ProxOutput.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
