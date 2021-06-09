
public class Consortium_node extends Thread {
	
	public void run() {
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
				//msg_receive(i);
				//msg_send(i);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Consortium_node th = new Consortium_node();
		
		broker_node bk_node = new broker_node();
		
		node_info node_list = new node_info();
		
		BlockChain chain = new BlockChain();
		
		th.start();
	}
	
	private void msg_receive(NetworkEndPoint i) {
		
		try {
			
			while(true) {

				System.out.println("--Receive");
				String s = i.wan_receive(7070);
				System.out.println(s);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	private void msg_send(NetworkEndPoint i) {
		
		try {
			System.out.println("--Sender");
			i.wan_send("nice to meet you",7070);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
