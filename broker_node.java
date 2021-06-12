import java.net.InetAddress;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class broker_node extends Consortium_node{
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
				
				//Message format
				//enrol : type#Membernode_id#Membernode_ip
				//Transaction : type#sender_node_id#receiver_node_id#price
				//BLOCK : type#
				//DELETE : type#sender
				String s = i.wan_receive(7070);
				System.out.println("--Receive");
				System.out.println(s);
				//���ڿ� parsing�ϱ�
				String[] msg = s.split("#");
				switch(msg[0]) {
				case "ENROL":
					//CONSORTIUM ���
					String node_id = msg[1];
					String node_ip = msg[2];
					super.node_storage.enrol(node_id, node_ip);
					break;
					
				case "TRANSACTION":
					//�ش� string �м��ؼ� �´� ���ҽÿ� �����ֱ�
					//�ش� ���ҽÿ����� �����ְ� Consortium node���� �޾Ƽ� ã�� �ݾ� �ݿ�
					//WAN_TRANSACTION :type#Myip#sender_node_id#price
					String sender_node = msg[1];
					node_id = msg[2];
					String price = msg[3];
					String receive_node = super.node_storage.select(node_id);
					if(receive_node == null) {
						String[] consortium_ip = {i.HeeEul, i.YeIn, i.SoYang};
						String MyWanIP = find_MyWanIP();
						for(int k = 0; k < 3; k++) {
							if(MyWanIP == consortium_ip[k]) {
								continue;
							}
							i.set_IP(consortium_ip[k]);
							i.wan_send("WAN_TRANSACTION#" + MyWanIP + "#" + sender_node + "#price", 7070);
						}
					}
					else {
						//�̰� membernode �ʿ� message �ޱ� ���־�� ��.
						i.set_IP(receive_node);
						i.wan_send("TRANSCATION#" + sender_node + "#price", 7070);
					}
					
					break;
				case "BLOCK":
					//block chain�� ���
					//super.chain.add_block();
					break;
				case "DELETE":
					node_id = msg[1];
					super.node_storage.delete(node_id);
					
				default :
					System.out.println("brokernode : Error : String Error");
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
	
	public String find_MyWanIP() {
		String MyWanIP = null;
		try {
			
            String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=%EB%82%B4+%EA%B3%B5%EC%9D%B8+ip&oquery=%EB%82%B4+ip&tqi=h7VLTlp0J14ssdCf%2BZ4ssssss4l-262799";
            		
            Connection conn = Jsoup.connect(url);
            Document html = conn.get();
            Elements fileblocks = html.getElementsByClass("ip_chk_box"); //ip_chk_wrap
            for(Element fileblock : fileblocks) {
            	String text = fileblock.text();
            	MyWanIP = text;
            }
            
            
            System.out.println("MyWanIP : " + MyWanIP);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return MyWanIP;
	}
	
}
