import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Consortium_node{
	
	/*public void run() {
		NetworkEndPoint i = null;
		try {
			i = new NetworkEndPoint();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			while(true) {
				//Thread.sleep(10);
				//WAN 통신 대기
				System.out.println("Starting CONSORTIUM NODE...");
				
				//msg_receive(i);
				//msg_send(i);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}*/
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		broker_node bk_node = new broker_node();
		
		node_info node_list = new node_info();
		
		BlockChain chain = new BlockChain();
		
		ServerThread serverThread = new ServerThread(30001);
		
		serverThread.start();
		
		new Consortium_node().pullFromPeers(bufferedReader, serverThread);
	}
	
	public void pullFromPeers(BufferedReader bufferedReader, ServerThread serverThread) throws Exception{

		//peer ip주소 배열
		String[] peers = new String[2];
		peers[0] = "175.208.245.129";
		peers[1] = "221.167.222.167";
		
		for(int i = 0; i < peers.length; ++i) {
			Socket socket = null;
			try {
				socket = new Socket(peers[i], 30001);
				new PeerThread(socket, peers[i]).start();
			} catch(Exception e) {
				if(socket != null)
					socket.close();
				else
					System.out.println("Invalid Input");
			}
		}
		communicate(bufferedReader, serverThread);
	}

	
	public void communicate(BufferedReader bufferedReader, ServerThread serverThread) {
		//msg snd interface
	}
}
