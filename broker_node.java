
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
				//���ڿ� parsing�ϱ�
				switch(s) {
				case "ENROL":
					//CONSORTIUM ���
					break;
				case "TRANSACTION":
					//�ش� string �м��ؼ� �´� ���ҽÿ� �����ֱ�
				case "BLOCK":
					//block chain�� ���
					break;
				default :
					System.out.println("Error : String Error");
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//broker_node�� �������� ������ local message ���
	broker_node() {
		broker_node th = new broker_node();
		th.start();
	}
	
	public void member_node_enrol(/*Memeber_Node node*/) {
		
	}
	
}
