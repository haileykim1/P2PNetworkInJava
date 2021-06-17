import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{

	private ServerSocket serverSocket;
	private Set<ServerThreadStream> serverThreadStreams = new HashSet<ServerThreadStream>();
			
	public ServerThread(int portNum) throws Exception{
		serverSocket = new ServerSocket(portNum);
	}
	
	public void run() {
		System.out.println("ServerThread Start...");
		try {
			while(true) {
				ServerThreadStream serverThreadStream = new ServerThreadStream(serverSocket.accept(), this);
				serverThreadStreams.add(serverThreadStream);
				serverThreadStream.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	void sendMessage(String message) {
		try {
			for(ServerThreadStream s: serverThreadStreams) {
				s.getPrintWriter().println(message);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Set<ServerThreadStream> getServerThreadStreams(){
		return serverThreadStreams;
	}
}
