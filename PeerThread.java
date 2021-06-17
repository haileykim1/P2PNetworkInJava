import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class PeerThread extends Thread{
	private BufferedReader bufferedReader;
	private String ipStr;
	
	public PeerThread(Socket socket,String ipStr) throws IOException{
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.ipStr = ipStr;
	}
	
	public void run() {
		boolean flag = true;
		System.out.println("PeerThread Start..." + ipStr);
		while(flag) {
			System.out.println("iteration");
			try {
				String line;
				while((line = bufferedReader.readLine())!= null) {
					System.out.println("[" + ipStr + "] : " + line);
				}
				
				
			}catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
