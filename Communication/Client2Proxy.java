package Communication;

import java.io.*;

public class Client2Proxy extends Thread{

		private DataInputStream client2ProxInput;
		private DataOutputStream remoteOutput;
		public Client2Proxy(DataInputStream client2ProxInput, DataOutputStream remoteOutput)
		{
			this.client2ProxInput = client2ProxInput;
			this.remoteOutput = remoteOutput;
		}
		public void run() {
			try {
				while(true) {
					byte[] buffer = new byte[client2ProxInput.available()];
					if(buffer.length > 0) {
						for(int i = 0; i < buffer.length; i++) {
							buffer[i] = (byte)client2ProxInput.read();
						}
						remoteOutput.write(buffer);
						System.out.println("[Client2Prox] -> " + buffer.length);
					}
					Thread.sleep(1);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
}
