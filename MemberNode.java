public class MemberNode extends Thread{
	private Block block;
	private Wallet wallet;
	private NetworkEndPoint networkendpoint;
	
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
				//WAN ��� ���
				System.out.println("Starting MEMBER NODE...");
				
				//msg_receive(i);
				//msg_send(i);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MemberNode th = new MemberNode();
			
		
		th.start();
	}
}