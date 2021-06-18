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
				
				
				else if(data instanceof TransactionInfo) {
					System.out.println("Object : TransactionInfo");
					
					//inner
					//public key를 가져오면 Transaction을 생성하고
					TransactionInfo info = (TransactionInfo) data;
					
					//트랜잭션 info 보내기
					serverThread.sendMessage(info);
					
					//node_info에 있는 Membernode의 값이 null이 아니라면, 즉, 나의 컨소시움에 속해 있는 MemberNode에 Send를 하는 경우
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
					//받은 Transaction 검증하는 부분
					if(tr.processTransaction() == false) {
						System.err.println("Broker Node : Error : transaction can not verify");
						oout.writeObject(false);
					}
					else {

						
						//보내는 부분(나에게 해당 member가 있던 없던 다 보내어 block에 기록하여야하므로 보냄
						//Consortium 노드에 구현
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
						//receipent 측에서 자신에게 보내진 transaction인지 아닌지 확인한 후, 맞다면 지갑에 반영한 후 block에 기록, 아니라면 그냥 블록에 기록
						//이건 membernode 쪽에 message 받기 해주어야 함.
					
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
<<<<<<< HEAD
						//블록이 완성되면 조건 넣기
						Block block = new Block(chain.getPreviousHash());
						if(block.nonce == 4) {
							oout.writeObject(block);
=======
						//����� �ϼ��Ǹ� ���� �ֱ�
						
						if(chain.queue.get(chain.queuePos).add_transaction_num == 4) {
							oout.writeObject(chain.queue.get(chain.queuePos));
							chain.queuePos++;
>>>>>>> bb610437cce899f0d5001d285346e95c4d65565a
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
	

	
	
	
}
