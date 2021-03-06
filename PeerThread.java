import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;


public class PeerThread extends Thread{
	private BufferedReader bufferedReader;
	private String ipStr;
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	private node_info node_storage;
	private int my_port;
	private BlockChain chain;
	
	
	public PeerThread(Socket socket,String ipStr, int portnum, BlockChain chain, node_info node_storage) throws IOException{
		//bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		oin = new ObjectInputStream(socket.getInputStream());
		oout = new ObjectOutputStream(socket.getOutputStream());
		this.ipStr = ipStr;
		this.my_port = portnum;
		this.chain = chain;
	}
	
	public void run() {
		boolean flag = true;
		System.out.println("PeerThread Start..." + ipStr);
		NetworkEndPoint i = null;
		try {
			i = new NetworkEndPoint();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(flag) {
			/*System.out.println("iteration");
			try {
				String line;
				while((line = bufferedReader.readLine())!= null) {
					System.out.println("[" + ipStr + "] : " + line);
				}
				
				
			}*/
			try {
				Object data = oin.readObject();
				if(data instanceof Transaction) {
					Transaction tr = (Transaction) data;
					Block block = chain.queue.get(chain.queue.size() - 1);
					if(block.add_transaction_num == 4) {
						chain.queue.add(new Block(chain.getPreviousHash()));
						chain.queue.get(chain.queue.size() - 1).addTransaction(tr);
					}else {
						block.addTransaction(tr);
					}
					System.out.println("Transaction Added in Block!");
				}else if(data instanceof TransactionInfo) {
					String target = ((TransactionInfo)data).getRcvId();
					MemberInfo targetMem = null;
					if(node_storage.select(target) != null) {
						oout.writeObject(node_storage.idToMember.get(target).getWallet().publicKey);
					}
				}
			}
			catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
