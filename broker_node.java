import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;


public class broker_node extends Thread implements Serializable{
	
	static node_info node_storage;
	static int my_port;
	static BlockChain chain;
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream oin;
	ObjectOutputStream oout;
	static String consortiumName = "";
	static ServerThread serverThread;
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
				
				
				//object���엯 �냼耳� �넻�떊
				Object data = oin.readObject();
				
				if(data instanceof MemberInfo) {
					System.out.println("Object : Member Enrollment");
					boolean flag = node_storage.enrol((MemberInfo)data);
					oout.writeObject(flag);
				}
				else if(data instanceof Block) {
					System.out.println("Object : Block");
					//Blockchain�뿉 �벑濡�
					
					oout.writeObject(chain.addBlock((Block)data));
				}
				
				
				else if(data instanceof TransactionInfo) {
					System.out.println("Object : TransactionInfo");
					
					//inner
					//public key瑜� 媛��졇�삤硫� Transaction�쓣 �깮�꽦�븯怨�
					TransactionInfo info = (TransactionInfo) data;
					
					//�듃�옖�옲�뀡 info 蹂대궡湲�
					serverThread.sendMessage(info);
					
					//node_info�뿉 �엳�뒗 Membernode�쓽 媛믪씠 null�씠 �븘�땲�씪硫�, 利�, �굹�쓽 而⑥냼�떆���뿉 �냽�빐 �엳�뒗 MemberNode�뿉 Send瑜� �븯�뒗 寃쎌슦
					MemberInfo receiver;
					if((node_storage.select(info.getRcvId())) != null) {
						receiver = node_storage.node_list.get(info.getRcvId());
						oout.writeObject(receiver.getWallet().publicKey);
					}
						
				}
				
				else if(data instanceof Transaction) {
					System.out.println("Object : Transaction");
					NetworkEndPoint i = new NetworkEndPoint();
					Transaction tr = (Transaction) data;
					String receive_node = node_storage.select(tr.recipient_id);
					//諛쏆� Transaction 寃�利앺븯�뒗 遺�遺�
					if(tr.processTransaction() == false) {
						System.err.println("Broker Node : Error : transaction can not verify");
						oout.writeObject(false);
					}
					else {

						
						//蹂대궡�뒗 遺�遺�(�굹�뿉寃� �빐�떦 member媛� �엳�뜕 �뾾�뜕 �떎 蹂대궡�뼱 block�뿉 湲곕줉�븯�뿬�빞�븯誘�濡� 蹂대깂
						//Consortium �끂�뱶�뿉 援ы쁽
						//Outer
							//String[] consortium_ip = {i.HeeEul, i.YeIn, i.SoYang};
							//String MyWanIP = find_MyWanIP();
							//String MyWanIP = "59.13.228.230";
							/*for(int k = 0; k < 3; k++) {
								if(MyWanIP == consortium_ip[k]) {
									continue;
								}
								i.set_IP(consortium_ip[k]);
								i.object_send(tr, my_port-1);
							}*/
							
							serverThread.sendMessage(tr);
						//receipent 痢≪뿉�꽌 �옄�떊�뿉寃� 蹂대궡吏� transaction�씤吏� �븘�땶吏� �솗�씤�븳 �썑, 留욌떎硫� 吏�媛묒뿉 諛섏쁺�븳 �썑 block�뿉 湲곕줉, �븘�땲�씪硫� 洹몃깷 釉붾줉�뿉 湲곕줉
						//�씠嫄� membernode 履쎌뿉 message 諛쏄린 �빐二쇱뼱�빞 �븿.
					
						oout.writeObject(true);
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
					}else if(data.equals("BLOCK")) {
						//占쏙옙占쏙옙占� 占싹쇽옙占실몌옙 占쏙옙占쏙옙 占쌍깍옙
						
						if(chain.queue.get(chain.queuePos).add_transaction_num == 4) {
							oout.writeObject(chain.queue.get(chain.queuePos));
							chain.queuePos++;
						} else {
							oout.writeObject(false);
						}
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
	
	//broker_node瑜� �깮�꽦�뻽�쓣 �븣遺��꽣 local message ��湲�
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
	

	
	
	
}
