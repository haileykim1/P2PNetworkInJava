import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Iterator;


public class PeerThread extends Thread{
	private BufferedReader bufferedReader;
	private String ipStr;
	private ObjectInputStream oin;
	private node_info node_storage;
	private int my_port;
	public PeerThread(Socket socket,String ipStr, node_info storage, int portnum) throws IOException{
		//bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		oin = new ObjectInputStream(socket.getInputStream());
		this.ipStr = ipStr;
		this.node_storage = storage;
		this.my_port = portnum;
		
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
					Iterator<String> keys = node_storage.node_list.keySet().iterator();
					while(keys.hasNext()) {
						String key = keys.next();
						i.set_IP(node_storage.select(key));
						i.object_send(tr, my_port);		
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
