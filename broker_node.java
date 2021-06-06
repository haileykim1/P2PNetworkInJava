
public class broker_node extends Thread {
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
				String s = i.wan_receive(7070);
				System.out.println("--Receive");
				System.out.println(s);
				//문자열 parsing하기
				switch(s) {
				case "ENROL":
					//CONSORTIUM 등록
					break;
				case "TRANSACTION":
					//해당 string 분석해서 맞는 컨소시움에 보내주기
				case "BLOCK":
					//block chain에 등록
					break;
				default :
					System.out.println("Error : String Error");
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//broker_node를 생성했을 때부터 local message 대기
	broker_node() {
		broker_node th = new broker_node();
		th.start();
	}
	
	public void member_node_enrol(/*Memeber_Node node*/) {
		
	}
	
}
