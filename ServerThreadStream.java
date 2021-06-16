import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadStream extends Thread{
	
	private ServerThread serverThread;
	private Socket socket;
	private PrintWriter printWriter;
	
	public ServerThreadStream(Socket socket, ServerThread serverThread) {
		this.serverThread = serverThread;
		this.socket = socket;
		
	}
	
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		
		
		}catch(Exception e) {
			serverThread.getServerThreadStreams().remove(this);
		}
	}
	
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

}
