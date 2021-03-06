import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;


public class Consortium_node{
	static node_info node_storage;
	//???????????? ???? ->????? ?????????Id ????????? ex)soyang1 soyang2 soyang3...
	static String consortiumName = "";
	static int myPortNum;
	static BlockChain chain;
	static broker_node bk_node;
	static ServerThread serverThread;


	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.out.println("Consortium Node Start...");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		node_storage = new node_info();
		chain = new BlockChain();

		System.out.println(">> Enter your Port Number");
		myPortNum = Integer.parseInt(bufferedReader.readLine());

		//broker node??? port ??????? my_port + 1
		bk_node = new broker_node(node_storage, myPortNum + 1, chain, "soyang");
		serverThread = new ServerThread(myPortNum);
		bk_node.serverThread = serverThread;

		serverThread.start();

		new Consortium_node().pullFromPeers(bufferedReader, serverThread);
	}

	public void pullFromPeers(BufferedReader bufferedReader, ServerThread serverThread) throws Exception{
		System.out.println("pull From Peers");
		//peer ip???? ???
		String[] peers = new String[2];
		int[] peerPorts = new int[2];

		peers[0] = "175.208.245.129";
		peers[1] = "221.167.222.167";
		//peers[2] = "59.13.228.230";
		peerPorts[0] = 30001;
		peerPorts[1] = 30001;
		//peerPorts[2] = 30006;
		

		for(int i = 0; i < peers.length; ++i) {
			Socket socket = null;
			try {
				socket = new Socket(peers[i], peerPorts[i]);
				new PeerThread(socket, peers[i],myPortNum, chain, node_storage).start();
				
			} catch(Exception e) {
				if(socket != null) {
					System.out.println("socketnull");
					socket.close();
				}
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
				String msg = bufferedReader.readLine();
				if(msg.equals("exit")){
					flag = false;
					System.out.println("Consortium Node Exit...");
					serverThread.closeSocket();
					break;
				}else {
					serverThread.sendMessage(msg);
				}
			}
			System.exit(0);

		}catch(Exception e) {

		}
	}

	public String getConsortiumName() {
		return consortiumName;
	}

	public void P2PTransaction(Transaction transaction) {
		Date date = new Date();
		//0 : ??, 1 : ??, 2 : ??
		int myNum = 0;
		int consortiumNum = 3;

		while(true) {
			int second = (int) ((date.getTime() / 10) % consortiumNum);
			Block block = chain.queue.get(chain.queue.size() - 1);
			if(second == myNum) {
				//Peer?? transaction ??
				serverThread.sendMessage(transaction);
				//? ??? transaction ??
				if(block.add_transaction_num == 4) {
					chain.queue.add(new Block());
					chain.queue.get(chain.queue.size() - 1).addTransaction(transaction);
				}else {
					block.addTransaction(transaction);
				}
				System.out.println("Transaction Added in Block!");
				break;
			}
		}


	}

}