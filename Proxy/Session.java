package Proxy;
import java.io.*;
import java.net.*;

import Communication.Proxy2Remote;
public class Session extends Thread{
	private Socket socketManager;
	private DataInputStream client2ProxInput;
	private DataOutputStream client2ProxOutput;
	public Session(Socket socketManager)
	{
		
		try {
			this.socketManager = socketManager;
			System.out.println("Client Connected: " + this.socketManager.getRemoteSocketAddress());
			client2ProxInput = new DataInputStream(this.socketManager.getInputStream());
			client2ProxOutput = new DataOutputStream(this.socketManager.getOutputStream());
		} catch (Exception e) {
			System.out.println("Socket[ListenProxy] - > " + e.toString());
		}
		
	}
	public void run()
	{
		try {
							byte[] b = new byte[this.client2ProxInput.available()];
							if(b.length > 0) {
								for(int i =0; i < b.length; i++)
								{
									b[i] = (byte)this.client2ProxInput.read();
								}
								String data = new String(b);
								String[] split = data.split(" ", 3);
								String[] host = split[1].split(":");
								if(split[0].equals("CONNECT")) {
									System.out.println("HTTPS CONNECT Request");
									establishConnection(host[0], Integer.parseInt(host[1]));
								}
								
							}
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	private void getRequest(String host) {
		try {
			System.out.println(host);
			URL uri = new URL("http://" + host + "/");
			HttpURLConnection url = (HttpURLConnection)uri.openConnection();
			DataInputStream inputStream = new DataInputStream(url.getInputStream());
			byte[] buffer = new byte[inputStream.available()];
			if(buffer.length > 0) {
				for(int i =0; i < buffer.length; i++) {
					buffer[i] = (byte)inputStream.read();
				}
				String data = new String(buffer);
				System.out.println(data);
			}
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	private void establishConnection(String host, int port)
	{
		try {
			Socket proxy2Remote = new Socket(host, port);
			System.out.println("Proxy connected to remote server: " + host + ":" + port);
			String response = "HTTP/1.1 200 Connection Established\r\n"  + "Connection: close\r\n\r\n";
			this.client2ProxOutput.write(response.getBytes());
			new Proxy2Remote(proxy2Remote, this).start();
			Thread t = new Thread() {
				public void run()
				{
					try{
						while(true) {
							byte[] b = new byte[client2ProxInput.available()];
							if(b.length > 0) 
							{
								for(int i =0; i < b.length; i++)
								{
									b[i] = (byte)client2ProxInput.read();
								}
								//Envia la peticion get al servidor remoto
								Proxy2Remote.sendProxy2Server(b);
								System.out.println("Succesfully sent proxy to remote server");
							}
							Thread.sleep(200);
						}
					}
					catch (Exception e) {
						System.out.println("Read[client2proxinput] - > " + e.toString());
					}
				}
			};
			t.start();
			
			}catch (Exception e) {
				System.out.println(e.toString());
			}
	}
	public void Proxy2Client(byte[] b)
	{
		try {
			this.client2ProxOutput.write(b);
		}catch (Exception e) {
			System.out.println("Proxy to Client: " + e.toString());
		}
	}
}
