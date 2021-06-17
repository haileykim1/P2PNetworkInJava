import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class Consortium_node{
	static node_info node_storage;

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.out.println("Consortium Node Start...");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));		
		//broker_node bk_node = new broker_node();		
		node_storage = new node_info();		
		//BlockChain chain = new BlockChain();
		
		System.out.println(">> Enter your Port Number");
		int myPortNum = Integer.parseInt(bufferedReader.readLine());
		
		ServerThread serverThread = new ServerThread(myPortNum);
		
		serverThread.start();
		
		new Consortium_node().pullFromPeers(bufferedReader, serverThread);
	}
	
	public void pullFromPeers(BufferedReader bufferedReader, ServerThread serverThread) throws Exception{
		System.out.println("pull From Peers");
		//peer ip주소 배열
		String[] peers = new String[3];
		int[] peerPorts = new int[3];
		
		peers[0] = "175.208.245.129";
		peers[1] = "221.167.222.167";
		peers[0] = "59.13.228.230";
		
		for(int i = 0; i < peers.length; ++i) {
			Socket socket = null;
			try {
				socket = new Socket(peers[i], 30002);
				new PeerThread(socket, peers[i]).start();
			} catch(Exception e) {
				if(socket != null)
					socket.close();
				else
					System.out.println("Invalid Input");
				//e.printStackTrace();
				//connection exception timeout
			}
		}
		communicate(bufferedReader, serverThread);

	}
	public void communicate(BufferedReader bufferedReader, ServerThread serverThread) {
		//msg snd interface
		System.out.println("communicate");
		try {
			System.out.println(">> Send Message");
			boolean flag = true;
			while(flag) {
				System.out.print(">>");
				String msg = bufferedReader.readLine();
				if(msg.equals("exit")){
					flag = false;
					System.out.println("Consortium Node Exit...");
					break;
				}else {
					serverThread.sendMessage(msg);
				}
			}
			System.exit(0);
			
		}catch(Exception e) {
			
		}
	}
}
