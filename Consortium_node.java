
public class Consortium_node extends Thread {
	node_info node_storage;
	broker_node bk_node;
	BlockChain chain;
	NetworkEndPoint i;
	
	Consortium_node() {
		node_storage = new node_info();
		bk_node = new broker_node();
		chain = new BlockChain();
		Consortium_node th = new Consortium_node();
		
		th.start();
	}
	public void run() {
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
				
				msg_receive();
				//msg_send(i);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Consortium_node node = new Consortium_node();
	}
	
	private String msg_receive() {
		
		try {

				System.out.println("--Receive");
				String s = i.wan_receive(7070);
				System.out.println(s);
				return s;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	
	private void msg_send(String s) {
		
		try {
			System.out.println("--Sender");
			i.wan_send(s,7070);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
