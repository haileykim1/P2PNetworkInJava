import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Iterator;

public class TransThread extends Thread{
	private BufferedReader bufferedReader;
	private String ipStr;
	private ObjectInputStream oin;
	private MemberInfo my_info;
	private int my_port;
	private Block block;
	public TransThread(Socket socket, MemberInfo my_info, Block block, int portnum) throws IOException{
		oin = new ObjectInputStream(socket.getInputStream());
		this.ipStr = ipStr;
		this.my_info = my_info;
		this.my_port = portnum;
		this.block = block;
	}
	
	public void run() {
		boolean flag = true;
		System.out.println("TransThread Start..." + ipStr);
		NetworkEndPoint i = null;
		try {
			i = new NetworkEndPoint();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(flag) {
			
			try {
				Object data = i.object_receive(my_port);
				if(data instanceof Transaction) {
					Transaction tr = (Transaction) data;
					
					block.addTransaction(tr);
					
				}
			}
			catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
