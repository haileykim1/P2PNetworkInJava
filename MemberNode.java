public class MemberNode {
	private Block block;
	private Wallet wallet;
	private NetworkEndPoint networkendpoint;
	//여기다 정보 기록하고 brokernode통해 enroll시 이거 넘겨줌.
	//memberinfo에서 wallet 상태도 접근 가능 
	private MemberInfo memberInfo;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
	
	
}