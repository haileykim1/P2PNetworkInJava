import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	ObjectOutputStream oout;
	static String consortiumName = "";
	int cnt = 0;
	
	public void run(){
		System.out.println("Broker Node start...");
		
		
		
		try {

			
			
			
			while(true) {
				serverSocket = new ServerSocket(my_port);
				socket = serverSocket.accept();
				oin = new ObjectInputStream(socket.getInputStream());
				oout = new ObjectOutputStream(socket.getOutputStream());
				
				//Message format
				//enrol : MemberInfo
				//enrolConsortium : ConsotriumInfo
				//Transaction : type#sender_node_id#receiver_node_id#price
				//BLOCK : Block
				//DELETE : type#sender -> String
				
				
				//object타입 소켓 통신
				Object data = oin.readObject();
				
				if(data instanceof MemberInfo) {
					System.out.println("Object : Member Enrollment");
					boolean flag = node_storage.enrol((MemberInfo)data);
					oout.writeObject(flag);
				}
				else if(data instanceof Block) {
					System.out.println("Object : Block");
					//Blockchain에 등록
					
					oout.writeObject(chain.addBlock((Block)data));
				}
				else if(data instanceof ConsortiumInfo) {
					System.out.println("Object : Consortium");
					
				}
				
				else if(data instanceof Transaction) {
					System.out.println("Object : Transaction");
					NetworkEndPoint i = new NetworkEndPoint();
					Transaction tr = (Transaction) data;
					String receive_node = node_storage.select(tr.recipient_id);
					//받은 Transaction 검증하는 부분
					if(tr.processTransaction() == false) {
						System.err.println("Broker Node : Error : transaction can not verify");
					}
					else {
						
						//보내는 부분(나에게 해당 member가 있던 없던 다 보내어 block에 기록하여야하므로 보냄
						//Consortium 노드에 구현
							String[] consortium_ip = {i.HeeEul, i.YeIn, i.SoYang};
							String MyWanIP = find_MyWanIP();
							for(int k = 0; k < 3; k++) {
								if(MyWanIP == consortium_ip[k]) {
									continue;
								}
								i.set_IP(consortium_ip[k]);
								i.object_send(tr, my_port-1);
							}
						//receipent 측에서 자신에게 보내진 transaction인지 아닌지 확인한 후, 맞다면 지갑에 반영한 후 block에 기록, 아니라면 그냥 블록에 기록
						//이건 membernode 쪽에 message 받기 해주어야 함.
						Iterator<String> keys = node_storage.node_list.keySet().iterator();
						while(keys.hasNext()) {
							String key = keys.next();
							i.set_IP(node_storage.select(key));
							i.object_send(tr, my_port);		
						}
					
						
					}
				}
				else if(data instanceof String) {
					String str = (String)data;
					if(data.equals("HASH")) {
						System.out.println("Membernode needs prevHash value");
						oout.writeObject(chain.getPreviousHash());
					}else if(data.equals("ID")) {
						System.out.println("Membernode needs ID value");
						oout.writeObject((String)(consortiumName + (++cnt)));
					}
					else {

						System.out.println("Object : String -> DELETE");
						node_storage.delete((String)data);
					}
				}
				
				else {
					System.out.println("Received Object Unknown");
				}
				
					
				
				
				oout.close();
				oin.close();
				socket.close();
				serverSocket.close();	
			}
			
			
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//broker_node를 생성했을 때부터 local message 대기
	broker_node(node_info node_storage, int my_port, BlockChain chain, String name) {
		this.node_storage = node_storage;
		this.my_port = my_port;
		this.chain = chain;
		this.consortiumName = name;
		this.start();
	}
	
	@Override
	protected void finalize() throws Throwable{
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
