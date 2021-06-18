import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class broker_node extends Thread{
	
	static node_info node_storage;
	static int my_port;
	static BlockChain chain;
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream oin;
	
	public void run(){
		System.out.println("Broker Node start...");
		
		
		
		try {

			serverSocket = new ServerSocket(my_port);
			socket = serverSocket.accept();
			oin = new ObjectInputStream(socket.getInputStream());
			
			
			while(true) {
				
				//Message format
				//enrol : MemberInfo
				//enrolConsortium : ConsotriumInfo
				//Transaction : type#sender_node_id#receiver_node_id#price
				//BLOCK : Block
				//DELETE : type#sender -> String
				
				
				//objectŸ�� ���� ���
				Object data = oin.readObject();
				
				if(data instanceof MemberInfo) {
					System.out.println("Object : Member Enrollment");
					node_storage.enrol((MemberInfo)data);
				}
				else if(data instanceof Block) {
					System.out.println("Object : Block");
					//Blockchain�� ���
					chain.addBlock((Block)data);
				}
				else if(data instanceof ConsortiumInfo) {
					System.out.println("Object : Consortium");
					
				}
				
				else if(data instanceof Transaction) {
					System.out.println("Object : Transaction");
					NetworkEndPoint i = new NetworkEndPoint();
					Transaction tr = (Transaction) data;
					String receive_node = node_storage.select(tr.recipient_id);
					//���� Transaction �����ϴ� �κ�
					if(tr.processTransaction() == false) {
						System.err.println("Broker Node : Error : transaction can not verify");
					}
					else {
						
						//������ �κ�(������ �ش� member�� �ִ� ���� �� ������ block�� ����Ͽ����ϹǷ� ����
						//Consortium ��忡 ����
							String[] consortium_ip = {i.HeeEul, i.YeIn, i.SoYang};
							String MyWanIP = find_MyWanIP();
							for(int k = 0; k < 3; k++) {
								if(MyWanIP == consortium_ip[k]) {
									continue;
								}
								i.set_IP(consortium_ip[k]);
								i.object_send(tr, my_port-1);
							}
						//receipent ������ �ڽſ��� ������ transaction���� �ƴ��� Ȯ���� ��, �´ٸ� ������ �ݿ��� �� block�� ���, �ƴ϶�� �׳� ��Ͽ� ���
						//�̰� membernode �ʿ� message �ޱ� ���־�� ��.
						Iterator<String> keys = node_storage.node_list.keySet().iterator();
						while(keys.hasNext()) {
							String key = keys.next();
							i.set_IP(node_storage.select(key));
							i.object_send(tr, my_port);		
						}
					
						
					}
				}
				else if(data instanceof String) {
					System.out.println("Object : String -> DELETE");
					node_storage.delete((String)data);
				}
				
				else {
					System.out.println("Received Object Unknown");
				}
				
					
				/*
				
				
				case "TRANSACTION":
					//�ش� string �м��ؼ� �´� ���ҽÿ� �����ֱ�
					//�ش� ���ҽÿ����� �����ְ� Consortium node���� �޾Ƽ� ã�� �ݾ� �ݿ�
					//WAN_TRANSACTION :type#Myip#sender_node_id#price
					String sender_node = msg[1];
					node_id = msg[2];
					String price = msg[3];
					String receive_node = node_storage.select(node_id);
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
				default :
					System.out.println("brokernode : Error : String Error");
				}*/	
				
					
			}
			
			
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//broker_node�� �������� ������ local message ���
	broker_node(node_info node_storage, int my_port, BlockChain chain) {
		this.node_storage = node_storage;
		this.my_port = my_port;
		this.chain = chain;
		this.start();
	}
	
	@Override
	protected void finalize() throws Throwable{
		oin.close();
		socket.close();
		serverSocket.close();
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
