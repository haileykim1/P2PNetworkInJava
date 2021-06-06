
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
				String s = i.wan_receive(7071);
				System.out.println("--Receive");
				System.out.println(s);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Consortium_node th = new Consortium_node();
		th.start();
		
		broker_node bk_node = new broker_node();
		
		node_info node_list = new node_info();
		
		blockchain chain = new blockchain();
	}

}
