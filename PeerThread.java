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
		while(flag) {
			try {
				StringBuilder sb = new StringBuilder();
				String line;
				while((line = bufferedReader.readLine())!= null) {
					sb.append(line);
				}
				
				System.out.println("[" + ipStr + "] : " + sb.toString());
				
			}catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
